package sg.dex.starfish.impl.url;

import java.io.InputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import sg.dex.crypto.Hash;
import sg.dex.starfish.impl.ADataAsset;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.exception.TODOException;

/**
 * Class exposing a Java resource referenced by a URL as an Ocean asset
 *
 * @author Mike
 *
 */
public class ResourceAsset extends ADataAsset {
	private final String name;

	protected ResourceAsset(String meta, String name) {
		super(meta);
		this.name=name;
	}

	public static ResourceAsset create(String meta, String resourcePath) {
		return new ResourceAsset(meta,resourcePath);
	}
	
	public static ResourceAsset create(String resourcePath) {
		return create(buildMetaData(resourcePath,null),resourcePath);
	}
	
	private static String buildMetaData(String resourcePath,Map<String,Object> meta) {
		String hash=Hex.toString(Hash.keccak256(resourcePath));

		Map<String,Object> ob=new HashMap<>();
		ob.put("dateCreated", Instant.now().toString());
		ob.put("contentHash", hash);
		ob.put("type", "dataset");
		ob.put("contentType","application/octet-stream");

		if (meta!=null) {
			for (Map.Entry<String,Object> me:meta.entrySet()) {
				ob.put(me.getKey(), me.getValue());
			}
		}

		return JSON.toString(ob);
	}

	/**
	 * Gets InputStream corresponding to this Asset
	 *
	 * @throws AuthorizationException if requestor does not have access permission
	 * @throws StorageException if unable to load the Asset
	 * @return An input stream allowing consumption of the asset data
	 */
	@Override
	public InputStream getInputStream() {
		InputStream istream= Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
		if (istream==null) throw new IllegalStateException("Resource does not exist on classpath: "+name);
		return istream;
	}

	@Override
	public long getContentSize() {
		throw new TODOException();
	}

	public String getName() {
		return name;
	}





}
