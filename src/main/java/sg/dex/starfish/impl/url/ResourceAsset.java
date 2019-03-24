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
 * Class exposing a Java classpath resource as an Ocean asset.
 * 
 * Mainly useful for testing, or for applications that wish to embed some default assets such as 
 * scripts as fixed resources.
 *
 * @author Mike
 */
public class ResourceAsset extends ADataAsset {
	private final String resourceName;

	protected ResourceAsset(String meta, String resourceName) {
		super(meta);
		this.resourceName=resourceName;
	}

	public static ResourceAsset create(String meta, String resourcePath) {
		return new ResourceAsset(meta,resourcePath);
	}
	
	public static ResourceAsset create(String resourceName) {
		return create(buildMetaData(resourceName,null),resourceName);
	}
	
	private static String buildMetaData(String resourcePath,Map<String,Object> meta) {
		String hash=Hex.toString(Hash.keccak256(resourcePath));

		Map<String,Object> ob=new HashMap<>();
		ob.put("name", resourcePath);
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
	public InputStream getContentStream() {
		InputStream istream= Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
		if (istream==null) throw new IllegalStateException("Resource does not exist on classpath: "+resourceName);
		return istream;
	}

	@Override
	public long getContentSize() {
		throw new TODOException();
	}

	public String getName() {
		return resourceName;
	}





}
