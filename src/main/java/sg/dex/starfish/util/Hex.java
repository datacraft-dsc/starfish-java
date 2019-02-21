package sg.dex.starfish.util;

/**
 * Utility class for hexadecimal strings
 *
 * @author Mike
 *
 */
public class Hex {
	/**
	 * Converts an int value in the range 0..15 to a hexadecimal character
	 * @param value Integer value to convert
	 * @return char
	 */
	public static char toChar(int value) {
		if (value>=0) {
			if (value<=9) return (char)(value+48);
			if (value<=15) return (char)(value+87);
		}
		throw new IllegalArgumentException("Invalid value for hex char: "+value);
	}

	/**
	 *
	 * @param hex String containing Hex digits
	 * @return byte[]
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
	 * @return int
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
	 * @return String
	 */
	public static String toString(byte[] data) {
		return toString(data,0,data.length);
	}

	/**
	 * Converts a byte array of length N to a hex string of length 2N
	 * @param data Array of bytes
	 * @param offset offset in data
	 * @param length in bytes of data
	 * @return String
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
}
