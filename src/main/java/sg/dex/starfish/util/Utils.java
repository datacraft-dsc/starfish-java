package sg.dex.starfish.util;

import java.security.SecureRandom;

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
	 * Creates a random Ocean-compliant DID
	 * @return String
	 */
	public static DID createRandomDID() {
		return DID.parse(createRandomDIDString());
	}
	
	/**
	 * Creates a random hex string of the specified length
	 * @param length
	 * @return
	 */
	public static String createRandomHexString(int length) {
		SecureRandom sr=new SecureRandom();
		byte[] bytes=new byte[length];
		sr.nextBytes(bytes);
		return Hex.toString(bytes);
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

	/**
	 * Gets the class of an object, or null if the parameter is null
	 * @param o
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(T o) {
		if (o==null) return null;
		return (Class<T>) o.getClass();
	}

	/**
	 * Coerces the argument to a boolean value, where:
	 * - null is considered false
	 * - Strings "false" and "true" are interpreted appropriately
	 * - Boolean values are retained
	 * 
	 * Throws an exception if coercion is not possible.
	 * @param o
	 * @return
	 */
	public static boolean coerceBoolean(Object o) {
		if (o==null) return false;
		if (o instanceof Boolean) {
			return (Boolean)o;
		}
		if (o instanceof String) {
			String s=(String) o;
			if (s.equals("true")) return true;
			if (s.equals("false")) return false;
		}
		throw new IllegalArgumentException("Can't coerce to boolean: "+o);
	}

	
	public static int coerceInt(Object o) {
		if (o instanceof Number) {
			if (o instanceof Integer) return (Integer)o;
			Number n=(Number)o;
			int value=(int)n.longValue();
			if (value!=n.doubleValue()) throw new IllegalArgumentException("Cannot coerce to int without loss:");
			return value;
		}
		if (o instanceof String) {
			return Integer.parseInt((String) o);
		}
		throw new IllegalArgumentException("Can't coerce to int: "+o);
	}



}
