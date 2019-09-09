package sg.dex.starfish.util;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.dex.starfish.constant.Constant;

/**
 * Class representing a valid W3C DID
 * See: https://w3c-ccg.github.io/did-spec/
 *
 * @author Mike
 *
 */
public class DID {
	private static final String METHOD_REGEX="([a-z]+)";
	private static final String ID_REGEX="([a-zA-z0-9\\\\d._]+)?";
	private static final String PATH_REGEX="(?:/([a-z0-9\\-._~%!$&'()*+,;=:@/]*))?";
	private static final String FRAGMENT_REGEX="(?:#(.*))?";
	private static final Pattern DID_PATTERN=Pattern.compile("did:"+METHOD_REGEX+":"+ID_REGEX+PATH_REGEX+FRAGMENT_REGEX+"$");

	private final String method;
	private final String id;
	private final String path;
	private final String fragment;
	private String fullString=null;

	/**
	 * Creates a DID
	 *
	 * @param method METHOD portion of DID
	 * @param id ID portion of DID
	 * @param path PATH portion of DID (optional)
	 * @param fragment FRAGMENT portion of DID  (optional)
	 * @throws IllegalArgumentException if method or id are null
	 */
	private DID(String method, String id, String path, String fragment) {
		if (method==null) throw new IllegalArgumentException("DID method cannot be null");
		if (id==null) throw new IllegalArgumentException("DID id cannot be null");
		this.method=method;
		this.id=id;
		this.path=path;
		this.fragment=fragment;
	}

	/**
	 * Creates a DID with the specified method, id, path and fragment
	 * @param method The DID method, e.g. "op"
	 * @param id The DID idstring
	 * @param path The DID path
	 * @param fragment The DID fragment
	 * @return DID The newly created DID
	 */
	public static DID create(String method, String id, String path, String fragment) {
		return new DID(method,id,path,fragment);
	}

	/**
	 * Checks if the provided String is a valid DID
	 *
	 * @param did Any String to test as a DID
	 * @return boolean true if the String is parseable as a valid DID, false otherwise
	 */
	public static boolean isValidDID(String did) {
		Matcher m = DID_PATTERN.matcher(did);
		return m.matches();
	}

	/**
	 * Attempts to parse the given string as a DID
	 *
	 * @param did The String to parse as a DID
	 * @throws IllegalArgumentException on DID parse error
	 * @return DID The newly created DID
	 */
	public static DID parse(String did) {
		Matcher m = DID_PATTERN.matcher(did);
		if (m.matches()) {
			String method=m.group(1);
			String id=m.group(2);
			String path=m.group(3);
			String fragment=m.group(4);
			DID newDID= new DID(method,id,path,fragment);
			newDID.fullString=did;
			return newDID;
		} else {
			throw new IllegalArgumentException("Parse failure on invalid DID ["+did+"]");
		}
	}

	/**
	 * Gets the DID scheme for this DID
	 * @return String The DID scheme, always defined as "did"
	 */
	public String getScheme() {
		return "did";
	}

	/**
	 * Gets the DID method for this DID
	 * @return String The DID method, e.g. "op"
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Gets the DID idstring for this DID
	 * @return String The DID idstring, e.g. "2ee8d7d4dd764bc96e8f1c762b1ca4ff54688b22aeb851378aa9a543290bcbd9"
	 */
	public String getID() {
		return id;
	}

	/**
	 * Gets the DID path for this DID
	 * @return String The DID path, or null if there is no path specified
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Gets the DID fragment for this DID
	 * @return String The DID fragment, or null if there is no fragment specified
	 */
	public String getFragment() {
		return fragment;
	}

	private String createString() {
		StringBuffer sb=new StringBuffer();
		sb.append("did");
		sb.append(':');
		sb.append(method);
		sb.append(':');
		sb.append(id);
		if (path!=null) {
			sb.append('/');
			sb.append(path);
		}
		if (fragment!=null) {
			sb.append('/');
			sb.append(fragment);
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		if (fullString==null) fullString=createString();
		return fullString;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof DID)) return false;
		return equals((DID)o);
	}

	/**
	 * Tests if this DID is equal to another DID
	 * @param d The DID to compare for equality
	 * @return boolean true if this DID equals the DID argument, false otherwise
	 */
	public boolean equals(DID d) {
		if (!method.equals(d.method)) return false;
		if (!id.equals(d.id)) return false;
		if (!Utils.equals(path,d.path)) return false;
		if (!Utils.equals(fragment,d.fragment)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int h=0;
		h+=method.hashCode();
		h+=13*id.hashCode();
		h+=53*Utils.hashCode(path);
		h+=101*Utils.hashCode(path);
		return h;
	}

	/**
	 * Creates a new DID from this DID with an updated path
	 * @param path The new path to add to this DID (may be null)
	 * @return DID The updated DID with the specified path argument
	 */
	public DID withPath(String path) {
		return DID.create(method,id,path,fragment);
	}

	/**
	 * Creates a random Ocean-compliant DID as a string, of the format:
	 *
	 *   "did:op:a1019172af9ae4d6cb32b52193cae1e3d61c0bcf36f0ba1cd30bf82d6e446563"
	 *
	 * @return A valid Ocean DID represented as a string
	 */
	public static String createRandomString() {
		SecureRandom sr=new SecureRandom();
		byte[] bytes=new byte[Utils.DID_LENGTH];
		sr.nextBytes(bytes);
		return "did:"+Constant.DID_METHOD+":"+Hex.toString(bytes);
	}

	/**
	 * Creates a random Ocean-compliant DID
	 * @return The created DID
	 */
	public static DID createRandom() {
		return parse(DID.createRandomString());
	}

	/**
	 * Removes the path and fragment from a DID, creating a simple DID that uniquely identifies the DID Subject.
	 * @return A DID without path and fragment portions.
	 */
	public DID withoutPath() {
		if ((path==null)&&(fragment==null)) return this;
		return new DID(method,id,null,null);
	}


}
