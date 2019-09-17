package sg.dex.crypto;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import sg.dex.starfish.util.Hex;

import java.nio.charset.StandardCharsets;

/**
 * Utility class for hash functions
 * 
 * @author Mike
 *
 */
public class Hash {
	public static final byte[] EMPTY_BYTES_SHA3 = sha3_256(new byte[0]);

	/**
	 * Compute the Keccak256 hash of byte array segment
	 * 
	 * @param data Input data
	 * @param offset Start offset in the array
	 * @param length Length of bytes to compute
	 * @return byte[]
	 */
	public static byte[] keccak256(byte[] data, int offset, int length) {
		Keccak.DigestKeccak keccak = new Keccak.Digest256();
		keccak.update(data, offset, length);
		return keccak.digest();
	}
	
	/**
	 * Compute the SHA3-256 hash of byte array segment
	 * 
	 * @param data Input data
	 * @param offset Start offset in the array
	 * @param length Length of bytes to compute
	 * @return byte[]
	 */
	public static byte[] sha3_256(byte[] data, int offset, int length) {
		SHA3.DigestSHA3 md = new SHA3.Digest256();
		md.update(data, offset, length);
		final byte[] result = md.digest();
		return result;
	}

	/**
	 * Compute the Keccak256 hash of a byte array
	 * 
	 * @param data Input data
	 * @return byte[]
	 */
	public static byte[] keccak256(byte[] data) {
		return keccak256(data, 0, data.length);
	}
	
	/**
	 * Compute the SHA3-256 hash of a byte array
	 * 
	 * @param data Input data
	 * @return byte[]
	 */
	public static byte[] sha3_256(byte[] data) {
		return sha3_256(data, 0, data.length);
	}

	/**
	 * Compute the Keccak256 hash of string, with UTF-8 encoding
	 * 
	 * @param string Input string
	 * @return byte[]
	 */
	public static byte[] keccak256(String string) {
		return keccak256(string.getBytes(StandardCharsets.UTF_8));
	}
	
	/**
	 * Compute the SHA3-256 hash of string, with UTF-8 encoding
	 * 
	 * @param string Input string
	 * @return byte[]
	 */
	public static byte[] sha3_256(String string) {
		return sha3_256(string.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Compute the Keccak256 hash of string, with UTF-8 encoding
	 * 
	 * @param string Input string
	 * @return Keccak256 hash as a hex string
	 */
	public static String keccak256String(String string) {
		return Hex.toString(keccak256(string));
	}

	/**
	 * Compute the Keccak256 hash of byte array
	 * 
	 * @param data Input data
	 * @return Keccak256 hash as a hex string
	 */
	public static String keccak256String(byte[] data) {
		return Hex.toString(keccak256(data));
	}

	/**
	 * Compute the sha3_256 hash of byte array
	 *
	 * @param data Input data
	 * @return sha3_256 hash as a hex string
	 */
	public static String sha3_256String(byte[] data) {
		return Hex.toString(sha3_256(data));
	}

    /**
     * Compute the sha3_256 hash of string, with UTF-8 encoding
     *
     * @param string Input string
     * @return sha3_256 hash as a hex string
     */
    public static String sha3_256String(String string) {
        return Hex.toString(sha3_256(string));
    }

}
