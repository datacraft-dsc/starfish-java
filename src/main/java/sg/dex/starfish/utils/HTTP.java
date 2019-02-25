package sg.dex.starfish.utils;

import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;

public class HTTP {

	/**
	 * Creates a HTTP entity with the specified String as UTF_8 bytes
	 * @param s
	 * @return
	 */
	public static HttpEntity textEntity(String s) {
		return new ByteArrayEntity(s.getBytes(StandardCharsets.UTF_8));
	}

}
