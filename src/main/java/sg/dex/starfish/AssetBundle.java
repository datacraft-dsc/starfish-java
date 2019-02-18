package sg.dex.starfish;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

/**
 * Class representing an asset bundle
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

	@SuppressWarnings("unchecked")
	public List<String> getContentIDs() {
		JSONObject meta=getMetadata();
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
	 * @param path
	 * @return Asset
	 */
	public Asset getContent(String path) {
		throw new UnsupportedOperationException();
	}
}
