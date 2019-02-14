package sg.dex.starfish;

import java.security.SecureRandom;

import sg.dex.crypto.Hex;

/**
 * Utility class for StarFish
 * @author Mike
 *
 */
public class Utils {

	/**
	 * Length of a DID identifier in bytes
	 */
	public static final int DID_LENGTH = 32;

	public static String createRandomDID() {
		SecureRandom sr=new SecureRandom();
		byte[] bytes=new byte[Utils.DID_LENGTH];
		sr.nextBytes(bytes);
		return Hex.toString(bytes);
	}

}
