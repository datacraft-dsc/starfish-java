package sg.dex.starfish;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sg.dex.starfish.util.AuthorizationException;
import sg.dex.starfish.util.StorageException;

/**
 * Class representing an asset bundle. The contents of the bundle are defined in the asset
 * metadata.
 *
 * @author Mike
 *
 */
public class AssetBundle extends AAsset {

	protected AssetBundle(String meta) {
		super(meta);
	}

	@Override
	public boolean isDataAsset() {
		return false;
	}

	/**
	 * Gets content IDs for each asset
	 *
	 * @throws Error if metadata contents are not a Map
	 * @return List of content ID
	 */
	@SuppressWarnings("unchecked")
	public List<String> getContentIDs() {
		Map<String,Object> meta=getMetadata();
		Object contentValue=meta.get("contents");
		if (!(contentValue instanceof Map)) {
			throw new Error("Expected a map of contents in bundle, found: "+contentValue);
		}
		Map<String,Object> contents = (Map<String,Object>)contentValue;
		ArrayList<String> ids=new ArrayList<String>(contents.size());
		for (Object o: contents.values()) {
			Map<String,Object> content=(Map<String,Object>)o;
			String id=(String)content.get("assetID");
			ids.add(id);
		}
		return ids;
	}

	/**
	 * Gets an asset contained within this bundle
	 * @param path   Path to the asset
	 * @throws AuthorizationException if requestor does not have access permission
	 * @throws StorageException if there is an error in retreiving the Asset
	 * @return The asset at the specified path
	 */
	public Asset getContent(String path) {
		throw new StorageException("getContent Error",
					   new Exception("cannot getContent() for an abstract class"));
	}

	@Override
	public boolean isOperation() {
		return false;
	}
}
