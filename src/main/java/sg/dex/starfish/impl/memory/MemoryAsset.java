/**
  * Represents an Asset on the Ocean Network.
  *
  * Assets are defined by JSON metadata, and the Asset ID is the keccak256 hash of the metadata
  * as encoded in UTF-8.
  */
package sg.dex.starfish.impl.memory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.json.simple.JSONObject;

import sg.dex.crypto.Hash;
import sg.dex.starfish.ADataAsset;
import sg.dex.starfish.Asset;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.util.Hex;

/**
 * Class representing a local in-memory asset.
 *
 * Intended for use in testing or local development situations.
 *
 * @author Mike
 *
 */
public class MemoryAsset extends ADataAsset {

	private byte[] data;

	private MemoryAsset(String meta, byte[] data){
		super(meta);
		this.data=data;
	}
	
	/**
	 * Gets a MemoryAsset using the content and metadata from the provided asset
	 * @param memoryAgent
	 * @param a
	 * @return
	 */
	public static MemoryAsset create(Asset a) {
		if (a instanceof MemoryAsset) {
			return (MemoryAsset)a;
		} else if (a.isDataAsset()) {
			byte[] data=((DataAsset)a).getBytes();
			return new MemoryAsset(a.getMetadataString(),data);
		} else {
			throw new IllegalArgumentException("Asset must be a data asset");
		}
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
	 * @param data Asset data
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
	public boolean isDataAsset() {
		return true;
	}

	@Override
	public InputStream getInputStream() {
		if (data==null) throw new Error("MemoryAsset has not been initialised with data");
		return new ByteArrayInputStream(data);
	}
	
	@Override 
	public byte[] getBytes() {
		// we take a copy of data to protected immutability of MemoryAsset instance
		return data.clone();
	}

	@Override
	public long getSize() {
		return data.length;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getParamValue() {
		JSONObject o=new JSONObject();
		// pass the asset ID, i.e. hash of content
		o.put("id", getAssetID());
		return o;
	}

}
