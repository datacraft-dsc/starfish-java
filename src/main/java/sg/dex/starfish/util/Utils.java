package sg.dex.starfish.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for StarFish
 *
 * @author Mike
 *
 */
public class Utils {

	/**
	 * Length of a DID identifier in bytes
	 */
	public static final int DID_LENGTH = 32;

	/**
	 * Creates a random hex string of the specified length
	 *
	 * @param length Number of bytes of hex data to create
	 * @return A lowercase hex string of the specified length
	 */
	public static String createRandomHexString(int length) {
		SecureRandom sr = new SecureRandom();
		byte[] bytes = new byte[length];
		sr.nextBytes(bytes);
		return Hex.toString(bytes);
	}

	/**
	 * Compares two objects for equality. null is considered equal to null.
	 *
	 * @param a First object
	 * @param b Second object
	 * @return boolean true if the arguments are equal, false otherwise
	 */
	public static boolean equals(Object a, Object b) {
		if (a == null) return b == null;
		return a.equals(b);
	}

	/**
	 * Computes the hashcode for an Object. returns zero for null.
	 *
	 * @param o Object for which to compute the hashcode
	 * @return The computed hashcode
	 */
	public static int hashCode(Object o) {
		if (o == null) return 0;
		return o.hashCode();
	}

	/**
	 * Gets the class of an object, or null if the argument is null
	 *
	 * @param o Any Object
	 * @param <T> The class of the Object
	 * @return The Class of the argument provided
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(T o) {
		if (o == null) return null;
		return (Class<T>) o.getClass();
	}

	/**
	 * Coerces the argument to a boolean value, where: - null is considered false -
	 * Strings "false" and "true" are interpreted appropriately - Boolean values are
	 * retained as-is
	 *
	 * @throws IllegalArgumentException if the object cannot be successfully
	 *             converted to a boolean
	 * @param o The object to attempt to coerce to a boolean value
	 * @return The boolean value of this object if coercion is successful
	 */
	public static boolean coerceBoolean(Object o) {
		if (o == null) return false;
		if (o instanceof Boolean) {
			return (Boolean) o;
		}
		if (o instanceof String) {
			String s = (String) o;
			if (s.equals("true")) return true;
			if (s.equals("false")) return false;
		}
		throw new IllegalArgumentException("Can't coerce to boolean: " + o);
	}

	/**
	 * Coerces an object to an int value.
	 *
	 * @throws IllegalArgumentException if the object cannot be successfully
	 *             converted to an int
	 * @param o An object to be converted to an int
	 * @return The coerced int value of the object
	 */
	public static int coerceInt(Object o) {
		if (o instanceof Number) {
			if (o instanceof Integer) return (Integer) o;
			Number n = (Number) o;
			int value = (int) n.longValue();
			if (value != n.doubleValue()) throw new IllegalArgumentException("Cannot coerce to int without loss:");
			return value;
		}
		if (o instanceof String) {
			return Integer.parseInt((String) o);
		}
		throw new IllegalArgumentException("Can't coerce to int: " + o);
	}

	/**
	 * Consumes an InputStream to a String
	 *
	 * @param inputStream the InputStream
	 * @throws RuntimeException if inputStream unreadable
	 * @return The String value of inputStream
	 */
	public static String stringFromStream(InputStream inputStream) {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int length;
		try {
			while ((length = inputStream.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Can't read input stream", e);
		}
		return new String(result.toByteArray(), StandardCharsets.UTF_8);
	}

	/**
	 * Creates a map using the given arguments as keys and values
	 *
	 * @param params A sequence of (key,value) objects
	 * @throws IllegalArgumentException if mapOf has odd number of arguments
	 * @return A map containing the key keys and values
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> mapOf(Object... params) {
		int len = params.length;
		if ((len & 1) != 0)
			throw new IllegalArgumentException("mapOf requires a even number of arguments but got: " + len);
		Map<K, V> result = new HashMap<>(len >> 1);
		for (int i = 0; i < len; i += 2) {
			K key = (K) params[i];
			V value = (V) params[i + 1];
			result.put(key, value);
		}
		return result;
	}

	/**
	 * Checks if a resource is available
	 *
	 * @param fileName Name of resource file to query
	 * @return true if the resource exists
	 */
	public static boolean resourceExists(String fileName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url = classLoader.getResource(fileName);
		return (url != null);
	}

	/**
	 * Checks HTTP URL and returns <code>true</code> a connection can be established
	 * to the corresponding host and port
	 *
	 * @param urlString The HTTP URL to be checked.
	 * @return boolean if endpoint is up within the timeout
	 */
	public static boolean checkURL(String urlString) {
		try {
			URL url = new URL(urlString);
			Socket socket = new Socket(url.getHost(), url.getPort());
			socket.close();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
}
