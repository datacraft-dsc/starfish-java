package sg.dex.crypto;

import java.nio.charset.StandardCharsets;

import org.bouncycastle.jcajce.provider.digest.Keccak;

/**
 * Utility class for hash functions
 * @author Mike
 *
 */
public class Hash {
	/**
	 * Compute Keccak256 hash of byte array segment
	 * @param data
	 * @param offset
	 * @param length
	 * @return byte[]
	 */
    public static byte[] keccak256(byte[] data, int offset, int length) {
        Keccak.DigestKeccak keccak = new Keccak.Digest256();
        keccak.update(data, offset, length);
        return keccak.digest();
    }

	/**
	 * Compute Keccak256 hash of byte array
	 * @param data
	 * @return byte[]
	 */
	public static byte[] keccak256(byte[] data) {
		return keccak256(data,0,data.length);
	}

	/**
	 * Compute Keccak256 hash of string, with UTF-8 encoding
	 * @param string
	 * @return byte[]
	 */
	public static byte[] keccak256(String string) {
		return keccak256(string.getBytes(StandardCharsets.UTF_8));
	}

	public static String keccak256String(String string) {
		return Hex.toString(keccak256(string));
	}

	public static String keccak256String(byte[] data) {
		return Hex.toString(keccak256(data));
	}

}
