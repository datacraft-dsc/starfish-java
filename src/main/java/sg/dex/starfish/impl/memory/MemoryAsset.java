/**
  * Represents an Asset on the Ocean Network.
  *
  * Assets are defined by JSON metadata, and the Asset ID is the keccak256 hash of the metadata
  * as encoded in UTF-8.
  */
package sg.dex.starfish.impl.memory;

import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.impl.ADataAsset;
import sg.dex.starfish.util.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static sg.dex.starfish.constant.Constant.*;

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
			byte[] data=asset.getContent();
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
		if(data == null){
			throw new IllegalArgumentException("Missing data");
		}
		return create(buildMetaData(data,null),data);
	}


	/**
	 * Creates a MemoryAsset with the provided string data, encoded in UTF_8
	 * Default metadata will be generated.
	 *
	 * @param string String containing the data for this asset
	 * @return The newly created in-memory asset
	 */
	public static Asset create(String string) {
		byte[] bytes=string.getBytes(StandardCharsets.UTF_8);
		return create(Utils.mapOf(CONTENT_TYPE,"text/plain; charset=utf-8"),bytes);
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
	 * Build default metadata for an in-memory asset
	 * @param data Asset data
	 * @return The default metadata as a String
	 */
	private static String buildMetaData(byte[] data,Map<String,Object> meta) {
		String hash=Hex.toString(Hash.keccak256(data));

		Map<String,Object> ob=new HashMap<>();
		ob.put(DATE_CREATED, Instant.now().toString());
		ob.put(CONTENT_HASH, hash);
		ob.put(TYPE, "dataset");
		ob.put(SIZE, Integer.toString(data.length));
		ob.put(CONTENT_TYPE,"application/octet-stream");

		if (meta!=null) {
			for (Map.Entry<String,Object> me:meta.entrySet()) {
				ob.put(me.getKey(), me.getValue());
			}
		}

		return JSON.toString(ob);
	}

	@Override
	public boolean isDataAsset() {
		return true;
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
		if (data==null) throw new Error("MemoryAsset has not been initialised with data");
		return new ByteArrayInputStream(data);
	}

	/**
	 * Gets raw data corresponding to this Asset
	 *
	 * @throws AuthorizationException if requestor does not have access permission
	 * @throws StorageException if unable to load the Asset
	 * @return An input stream allowing consumption of the asset data
	 */
	@Override
	public byte[] getContent() {
		// we take a copy of data to protected immutability of MemoryAsset instance
		return data.clone();
	}

	@Override
	public long getContentSize() {
		return data.length;
	}

	@Override
	public Map<String,Object> getParamValue() {
		Map<String,Object> o=new HashMap<>();
		// pass the asset ID, i.e. hash of content
		o.put(ID, getAssetID());
		return o;
	}


}
