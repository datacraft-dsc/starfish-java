/**
  * Represents an Asset on the Ocean Network.
  * 
  * Assets are defined by JSON metadata, and the Asset ID is the keccak256 hash of the metadata
  * as encoded in UTF-8.
  */
package sg.dex.starfish;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import sg.dex.crypto.Hash;
import sg.dex.crypto.Hex;

/**
 * Class representing a local in-memory asset.
 * 
 * Intended for use in testing or local development situations.
 * 
 * @author Mike
 *
 */
public class MemoryAsset extends ADataAsset {
	private final String metadataString;
	private final String id;
	private byte[] data;
	
	private MemoryAsset(String meta, byte[] data){
		this.metadataString=meta;
		this.id=Hex.toString(Hash.keccak256(getMetadataString()));
		this.data=data;
	}
	
	public static MemoryAsset create(byte[] data) {
		return create(buildMetaData(data,null),data);
	}
	
	public static MemoryAsset create(Map<Object,Object> meta, byte[] data) {
		return create(buildMetaData(data,meta),data);
	}
	
	private static MemoryAsset create(String meta, byte[] data) {
		return new MemoryAsset(meta,data);
	}

	/**
	 * Build default metadata for a local asset
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static String buildMetaData(byte[] data,Map<Object,Object> meta) {
		String hash=Hex.toString(Hash.keccak256(data));
		JSONObject ob=new JSONObject();
		if (meta!=null) {
			for (Map.Entry<Object,Object> me:meta.entrySet()) {
				ob.put(me.getKey(), me.getValue());
			}
		}
		
		ob.put("contentHash", hash);
		ob.put("size", Integer.toString(data.length));
		return ob.toJSONString();
	}

	@Override
	public JSONObject getMetadata() {
		JSONParser parser=new JSONParser();
		try {
			return (JSONObject) parser.parse(metadataString);
		} catch (ParseException e) {
			throw new Error("Error in JSON parson",e);
		}
	}

	@Override
	public String getAssetID() {
		return id;
	}

	/**
	 * Gets the metadata for this asset as a String
	 * @return
	 */
	public String getMetadataString() {
		return metadataString;
	}

	@Override
	public boolean isDataAsset() {
		return true;
	}

	@Override
	public InputStream getInputStream() {
		return new ByteArrayInputStream(data);
	}

	@Override
	public long getSize() {
		return data.length;
	}

}