package sg.dex.starfish.util;

/**
 * Utility class for hexadecimal strings
 *
 * @author Mike
 *
 */
public class Hex {
	/**
	 * Converts an int value in the range 0..15 to a lowercase hexadecimal character
	 * @param value Integer value to convert
	 * @return The lowercase hex character
	 */
	public static char toChar(int value) {
		if (value>=0) {
			if (value<=9) return (char)(value+48);
			if (value<=15) return (char)(value+87);
		}
		throw new IllegalArgumentException("Invalid value for hex char: "+value);
	}

	/**
	 * Converts a hex string to a byte array.
	 * The hex string must have an even number of digits.
	 *
	 * @param hex String containing Hex digits
	 * @return The representation of the hex string as a byte array
	 */
	public static byte[] toBytes(String hex) {
		int length=hex.length();
		int n=length/2;
		if (n*2!=length) throw new Error("Hex string must have even length: "+length);
		byte[] result=new byte[n];

		for (int i=0; i<n; i++) {
			result[i]=(byte)(Hex.val(hex.charAt(2*i))*16+Hex.val(hex.charAt(2*i+1)));
		}

		return result;
	}

	/**
	 * Gets the value of a single hex car e.g. val('c') =&gt; 12
	 * @param c Character to convert
	 * @return The int value of the hex char, in the range 0-15 inclusive
	 */
	public static int val(char c) {
		int v=(int)c;
		if (v<=102) {
			if (v>=97) return v-87; // lowercase
			if ((v>=65)&&(v<=70)) return v-55; // uppercase
			if ((v>=48)&&(v<=57)) return v-48; // digit
		}
		throw new Error("Invalid hex char ["+c+"] = "+(int)c);
	}

	/**
	 * Converts a byte array of length N to a hex string of length 2N
	 * @param data Array of bytes
	 * @return The lowercase hex string generated
	 */
	public static String toString(byte[] data) {
		return toString(data,0,data.length);
	}

	/**
	 * Converts a byte array of length N to a hex string of length 2N
	 * @param data Array of bytes
	 * @param offset Offset into the byte array to start
	 * @param length Number of bytes from the byte array to convert to hex
	 * @return The lowercase hex string generated
	 */
	public static String toString(byte[] data, int offset, int length) {
		char[] hex=new char[length*2];
		for (int i=0; i<length; i++) {
			int v=((int)data[i+offset])&0xFF;
			hex[i*2]=toChar(v>>>4);
			hex[i*2+1]=toChar(v&0xF);
		}
		return new String(hex);
	}

	/**
	 * Converts an int to its 8-character hex representation
	 * @param identityHashCode
	 * @return
	 */
	public static String toString(int value) {
		char[] hex=new char[8];
		for (int i=0; i<8; i++) {
			hex[i]=toChar(0x0F&(value>>(28-4*i)));
		}
		return new String(hex);
	}
}
