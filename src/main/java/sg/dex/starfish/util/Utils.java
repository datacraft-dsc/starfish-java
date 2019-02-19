package sg.dex.starfish.util;

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

	/**
	 * Creates a random Ocean-compliant DID as a string
	 * @return String
	 */
	public static String createRandomDIDString() {
		SecureRandom sr=new SecureRandom();
		byte[] bytes=new byte[Utils.DID_LENGTH];
		sr.nextBytes(bytes);
		return "did:ocn:"+Hex.toString(bytes);
	}

	/**
	 * Compares to objects for equality. null is considered equal to null.
	 * @param a First object
	 * @param b Second object
	 * @return boolean
	 */
	public static boolean equals(Object a, Object b) {
		if (a==null) return b==null;
		return a.equals(b);
	}

	/**
	 * Computes the hashcode for an Object. returns zero for null.
	 * @param o Object for which to compute the hashcode
	 * @return int
	 */
	public static int hashCode(Object o) {
		if (o==null) return 0;
		return o.hashCode();
	}

}
