package sg.dex.starfish;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	private DID(String method, String id, String path, String fragment) {
		this.method=method;
		this.id=id;
		this.path=path;
		this.fragment=fragment;
	}
	
	public static boolean isValidDID(String did) {
		Matcher m = DID_PATTERN.matcher(did);
		return m.matches();
	}
	
	public static DID parse(String did) {
		Matcher m = DID_PATTERN.matcher(did);
		if (m.matches()) {
			String method=m.group(1);
			String id=m.group(2);
			String path=m.group(3);
			String fragment=m.group(4);
			return new DID(method,id,path,fragment);
		} else {
			throw new IllegalArgumentException("Parse failure on invalid DID ["+did+"]");
		}
	}
	
	public String getScheme() {
		return "did";
	}

	public String getMethod() {
		return method;
	}

	public String getID() {
		return id;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getFragment() {
		return fragment;
	}

	@Override
	public String toString() {
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

}
