/**
  * Represents an Asset on the Ocean Network.
  *
  * Assets are defined by JSON metadata, and the Asset ID is the keccak256 hash of the metadata
  * as encoded in UTF-8.
  */
package sg.dex.starfish.impl.memory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import sg.dex.crypto.Hash;
import sg.dex.starfish.ADataAsset;
import sg.dex.starfish.Asset;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;

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
	 * @param asset The asset to use to construct this MemoryAsset
	 * @return A new MemoryAsset containing the data from the passed asset argument
	 */
	public static MemoryAsset create(Asset asset) {
		if (asset instanceof MemoryAsset) {
			return (MemoryAsset)asset;
		} else if (asset.isDataAsset()) {
			byte[] data=((DataAsset)asset).getBytes();
			return new MemoryAsset(asset.getMetadataString(),data);
		} else {
			throw new IllegalArgumentException("Asset must be a data asset");
		}
	}
	
	/**
	 * Creates a MemoryAsset with the provided data. Default metadata will be generated.
	 * 
	 * @param data Byte array containing the data for this asset
	 * @return The newly created in-memory asset
	 */
	public static MemoryAsset create(byte[] data) {
		return create(buildMetaData(data,null),data);
	}

	/**
	 * Creates a MemoryAsset with the provided metadata an content
	 * @param meta A map containing the metadata for this asset
	 * @param data Byte array containing the data for this asset
	 * @return The newly created in-memory asset
	 */
	public static MemoryAsset create(Map<String,Object> meta, byte[] data) {
		return create(buildMetaData(data,meta),data);
	}

	private static MemoryAsset create(String meta, byte[] data) {
		return new MemoryAsset(meta,data);
	}

	/**
	 * Build default metadata for a local asset
	 * @param data Asset data
	 * @return The default metadata as a String
	 */
	private static String buildMetaData(byte[] data,Map<String,Object> meta) {
		String hash=Hex.toString(Hash.keccak256(data));
		Map<String,Object> ob=new HashMap<>();
		if (meta!=null) {
			for (Map.Entry<String,Object> me:meta.entrySet()) {
				ob.put(me.getKey(), me.getValue());
			}
		}

		ob.put("contentHash", hash);
		ob.put("size", Integer.toString(data.length));
		return JSON.toString(ob);
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

	@Override
	public Map<String,Object> getParamValue() {
		Map<String,Object> o=new HashMap<>();
		// pass the asset ID, i.e. hash of content
		o.put("id", getAssetID());
		return o;
	}

}
