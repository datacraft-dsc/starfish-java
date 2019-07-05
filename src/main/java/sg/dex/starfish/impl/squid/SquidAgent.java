package sg.dex.starfish.impl.squid;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanprotocol.squid.api.OceanAPI;
import com.oceanprotocol.squid.api.config.OceanConfig;
import com.oceanprotocol.squid.exceptions.DDOException;
import com.oceanprotocol.squid.exceptions.DIDFormatException;
import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.models.DDO;
import com.oceanprotocol.squid.models.aquarius.SearchResult;
import com.oceanprotocol.squid.models.asset.AssetMetadata;
import com.oceanprotocol.squid.models.service.ProviderConfig;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.GenericException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.impl.AAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class implementing a Squid Agent
 *
 * @author Tom
 *
 */
public class SquidAgent extends AAgent {

	private final Ocean ocean;
	private final OceanAPI oceanAPI;
	private final Map<String,String> configMap;


	/**
	 * Creates a SquidAgent with the specified OceanAPI, Ocean connection and DID
	 *
	 * @param ocean Ocean connection to use
	 * @param did DID for this agent
	 */
	protected SquidAgent(Map<String,String> configMap,
			     Ocean ocean, DID did) {
		super(ocean, did);
		this.configMap = configMap;
		this.ocean=ocean;
		this.oceanAPI = ocean.getOceanAPI();
	}


	/**
	 * Creates a RemoteAgent with the specified OceanAPI, Ocean connection and DID
	 *
	 * @param ocean Ocean connection to use
	 * @param did DID for this agent
	 * @return RemoteAgent
	 */
	public static SquidAgent create( Map<String,String> config, Ocean ocean, DID did) {
		return new SquidAgent(config, ocean, did);
	}


	/**
	 * Registers an asset with this agent.
	 * The agent must support metadata storage.
	 *
	 * @param asset The asset to register
	 * @throws AuthorizationException if requestor does not have register permission
	 * @throws StorageException if unable to register the SquidAsset
	 * @throws UnsupportedOperationException if the agent does not support metadata storage
	 * @return SquidAsset
	 */
	@Override
	public SquidAsset registerAsset(Asset asset) {

		try {
			return createSquidAssetInNetwork(asset);

		} catch (DIDFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DDOException e) {
			e.printStackTrace();
		}
		//Todo FIX IT, may throw exception
		throw  new GenericException("Exception while registering Asset into OCN");
	}

	/**
	 * Methods to get the Squid Asset after registering to Ocean Network
	 * @param asset
	 * @return
	 * @throws IOException
	 * @throws DDOException
	 * @throws DIDFormatException
	 */
	private SquidAsset createSquidAssetInNetwork(Asset asset) throws IOException, DDOException, DIDFormatException {

		AssetMetadata metadataBase = createSquidDDO(asset);

		DDO squidDDO = ocean.getAssetsAPI().create(metadataBase, getProvideConfig(configMap));

		DID did = DID.parse(squidDDO.getDid().toString());
		return  SquidAsset.create(asset.getMetadataString(),did,squidDDO,ocean);

	}

	private AssetMetadata createSquidDDO(Asset asset) throws IOException {

		// todo map mandatory attribute
		Map<String, Object> squidMetaDAta = new HashMap<>();
		// modify get metadata to squid..metadata
		squidMetaDAta.put("base", getMetaData(asset));

		return DDO.fromJSON(new TypeReference<AssetMetadata>() {
		}, JSON.toString(squidMetaDAta));
	}

	private Map<String, Object>  getMetaData(Asset asset) {
		Map<String, Object> meta = new HashMap<>();
		// getting asset meta data
		Map<String, Object> assetMetadata=JSON.toMap(asset.getMetadataString());//.getMetadata();
		// initialize with default value

		meta.put(Constant.TYPE, Constant.DATA_SET);
		meta.put(Constant.NAME, Constant.DEFAULT_NAME);
		meta.put("license", Constant.DEFAULT_LICENSE);
		meta.put("author", Constant.DEFAULT_AUTHOR);
		meta.put("price", Constant.DEFAULT_PRICE);

		if (assetMetadata != null) {
			for (Map.Entry<String, Object> me : meta.entrySet()) {
				if( assetMetadata.get(me.getKey()) != null){
					meta.put(me.getKey(),assetMetadata.get(me.getKey()));
				}
			}
		}
        meta.put(Constant.DATE_CREATED, Constant.DEFAULT_DATE_CREATED);
		return meta;
	}

	/**
	 * Gets an asset for the given asset ID from this agent.
	 * Returns null if the asset ID does not exist.
	 *
	 * @param id The ID of the asset to get from this agent
	 * @throws AuthorizationException if requestor does not have access permission
	 * @throws StorageException if there is an error in retrieving the SquidAsset
	 * @return SquidAsset The asset found
	 */
	@Override
	public SquidAsset getAsset(String id) {
		DID did = DID.create("op",id,null,null);
		return getAsset(did);
	}
	
	@Override
	public SquidAsset getAsset(DID did) {
		return (SquidAsset)ocean.getAsset(did);
	}

	/**
	 * API to uploads an squid asset to this agent. Registers the asset with the agent if required.
	 *
	 * Throws an exception if upload is not possible, with the following likely causes:
	 * - The agent does not support uploads of this asset type / size
	 * - The data for the asset cannot be accessed by the agent
	 *
	 * @param a SquidAsset to upload
	 * @throws AuthorizationException if requestor does not have upload permission
	 * @throws StorageException if there is an error in uploading the SquidAsset
	 * @return SquidAsset An asset stored on the agent if the upload is successful
	 */
	@Override
	public SquidAsset uploadAsset(Asset a) {
		throw new UnsupportedOperationException("Upload not supported by squid agent");
	}

	/**
	 * Method to search an asset
	 * @param text asset to be searched
	 * @return List of Squid Asset that matches
     * @throws DDOException exception
	 */

	public List<SquidAsset> searchAsset(String text) throws DDOException {
		SearchResult searchResult = ocean.getOceanAPI().getAssetsAPI().search(text);
		List<DDO> listDDO = searchResult.results;
		List<SquidAsset> squidAssetLst = new ArrayList<>();
		for(DDO ddo: listDDO){
			DID did = DID.parse(ddo.getDid().toString());
			SquidAsset asset =getAsset(did);
			squidAssetLst.add(asset);
		}
		return squidAssetLst;

	}

	private static ProviderConfig getProvideConfig(Map<String,String> configMap) {

		String metadataUrl = configMap.get(OceanConfig.AQUARIUS_URL) + "/api/v1/aquarius/assets/ddo/{did}";
		String consumeUrl = configMap.get("brizo.url") + "/api/v1/brizo/services/consume";
		String purchaseEndpoint = configMap.get("brizo.url") + "/api/v1/brizo/services/access/initialize";
		String secretStoreEndpoint = configMap.get(OceanConfig.SECRETSTORE_URL);
		String providerAddress = configMap.get(OceanConfig.PROVIDER_ADDRESS);

		return new ProviderConfig(consumeUrl, purchaseEndpoint, metadataUrl, secretStoreEndpoint, providerAddress);
	}
    /**
     * API to return Squid DDO based on Squid DID
     * @param did to be resolved
     * @return DDO ddo
     * @throws EthereumException exception
     * @throws DDOException exception
     */
    public DDO resolveSquidDID(com.oceanprotocol.squid.models.DID did) throws EthereumException, DDOException {

        return oceanAPI.getAssetsAPI().resolve(did);
    }


}
