//package sg.dex.starfish.impl.squid;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.oceanprotocol.squid.exceptions.DDOException;
//import com.oceanprotocol.squid.exceptions.DIDFormatException;
//import com.oceanprotocol.squid.exceptions.EthereumException;
//import com.oceanprotocol.squid.models.DDO;
//import com.oceanprotocol.squid.models.aquarius.SearchResult;
//import com.oceanprotocol.squid.models.asset.AssetMetadata;
//import org.web3j.crypto.CipherException;
//import sg.dex.starfish.Asset;
//import sg.dex.starfish.Resolver;
//import sg.dex.starfish.constant.Constant;
//import sg.dex.starfish.exception.AuthorizationException;
//import sg.dex.starfish.exception.GenericException;
//import sg.dex.starfish.exception.StorageException;
//import sg.dex.starfish.impl.AAgent;
//import sg.dex.starfish.util.DID;
//import sg.dex.starfish.util.JSON;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Class implementing a Squid Agent
// *
// * @author Ayush
// */
//public class SquidAgent extends AAgent {
//
//    private SquidResolverImpl squidResolver;
//    private SquidService squidService;
//
//    /**
//     * Creates a SquidAgent with the specified OceanAPI, Ocean connection and
//     * DID
//     *
//     * @param resolver Resolver instance used to resolve / register DIDs and
//     *                 DDO.
//     * @param did      DID for this agent
//     */
//    protected SquidAgent(
//            Resolver resolver, DID did, SquidService squidService) {
//        super(resolver, did);
//        squidResolver = (SquidResolverImpl) resolver;
//        this.squidService = squidService;
//    }
//
//
//    /**
//     * Creates a RemoteAgent with the specified Resolver and DID
//     *
//     * @param resolver Resolver instance used to resolve / register DIDs and DDO
//     * @param did      DID for this agent
//     * @return RemoteAgent return instance of remote Agent
//     */
//    public static SquidAgent create(Resolver resolver, DID did, SquidService squidService) {
//
//        return new SquidAgent(resolver, did, squidService);
//    }
//
//
//    /**
//     * Registers an asset with this agent.
//     * The agent must support metadata storage.
//     *
//     * @param asset The asset to register
//     * @return SquidAsset
//     * @throws AuthorizationException        if requestor does not have register
//     *                                       permission
//     * @throws StorageException              if unable to register the
//     *                                       SquidAsset
//     * @throws UnsupportedOperationException if the agent does not support
//     *                                       metadata storage
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    public <R extends Asset> R registerAsset(Asset asset) {
//
//        try {
//            return (R) createSquidAssetInNetwork(getMetaData(asset), squidService);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (DDOException e) {
//            e.printStackTrace();
//        }
//        //Todo FIX IT, may throw exception
//        throw new GenericException("Exception while registering Asset into OCN");
//    }
//
//    @Override
//    public <R extends Asset> R registerAsset(String metaString) {
//        try {
//            return (R) createSquidAssetInNetwork(JSON.toMap(metaString), squidService);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (DDOException e) {
//            e.printStackTrace();
//        }
//        //Todo FIX IT, may throw exception
//        throw new GenericException("Exception while registering Asset into OCN");
//    }
//
//    /**
//     * Methods to get the Squid Asset after registering to Ocean Network
//     *
//     * @param metaData metaData
//     * @return Squid Asset
//     * @throws IOException        IOException will be thrown
//     * @throws DDOException       DDOException will be thrown
//     * @throws DIDFormatException DIDFormatException will be thrown
//     */
//    private SquidAsset createSquidAssetInNetwork(Map<String, Object> metaData, SquidService squidService) throws IOException, DDOException {
//
//        // get metadata required registration on OCN
//        AssetMetadata metadataBase = getMetadataForSquidFromAsset(metaData);
//
//        DDO squidDDO = squidService.getAssetAPI().create(metadataBase, squidService.getProvideConfig());
//        return SquidAsset.create(squidDDO.metadata.toString(), squidDDO.getDid());
//
//    }
//
//    private AssetMetadata getMetadataForSquidFromAsset(Map<String, Object> metaData) throws IOException {
//
//        // todo map mandatory attribute
//        Map<String, Object> squidMetaDAta = new HashMap<>();
//        // modify get metadata to squid..metadata
//        squidMetaDAta.put("base", metaData);
//
//        return DDO.fromJSON(new TypeReference<AssetMetadata>() {
//        }, JSON.toString(squidMetaDAta));
//    }
//
//    private Map<String, Object> getMetaData(Asset asset) {
//        Map<String, Object> meta = new HashMap<>();
//        // getting asset meta data
//        Map<String, Object> assetMetadata = JSON.toMap(asset.getMetadataString());//.getMetadata();
//        // initialize with default value
//
//        meta.put(Constant.TYPE, Constant.DATA_SET);
//        meta.put(Constant.NAME, Constant.DEFAULT_NAME);
//        meta.put("license", Constant.DEFAULT_LICENSE);
//        meta.put("author", Constant.DEFAULT_AUTHOR);
//        meta.put("price", Constant.DEFAULT_PRICE);
//
//        if (assetMetadata != null) {
//            for (Map.Entry<String, Object> me : meta.entrySet()) {
//                if (assetMetadata.get(me.getKey()) != null) {
//                    meta.put(me.getKey(), assetMetadata.get(me.getKey()));
//                }
//            }
//        }
//        meta.put(Constant.DATE_CREATED, Constant.DEFAULT_DATE_CREATED);
//        return meta;
//    }
//
//    /**
//     * Gets an asset for the given asset ID from this agent.
//     * Returns null if the asset ID does not exist.
//     *
//     * @param id The ID of the asset to get from this agent
//     * @return SquidAsset The asset found
//     * @throws AuthorizationException if requestor does not have access
//     *                                permission
//     * @throws StorageException       if there is an error in retrieving the
//     *                                SquidAsset
//     */
//    @Override
//    public SquidAsset getAsset(String id) {
//        DID did = DID.create("op", id, null, null);
//        return getAsset(did);
//    }
//
//    @Override
//    public SquidAsset getAsset(DID did) {
//
//
//        try {
//            DDO squidDDO = squidResolver.getSquidDDO(did);
//            SquidAsset.create(squidDDO.metadata.toString(), squidDDO.getDid());
//        } catch (EthereumException e) {
//            e.printStackTrace();
//        } catch (DDOException e) {
//            e.printStackTrace();
//        } catch (DIDFormatException e) {
//            e.printStackTrace();
//        } catch (CipherException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return SquidAsset.create(did, squidService);
//
//    }
//
//    /**
//     * API to uploads an squid asset to this agent. Registers the asset with the
//     * agent if required.
//     * <p>
//     * Throws an exception if upload is not possible, with the following likely
//     * causes:
//     * - The agent does not support uploads of this asset type / size
//     * - The data for the asset cannot be accessed by the agent
//     *
//     * @param a SquidAsset to upload
//     * @return SquidAsset An asset stored on the agent if the upload is
//     *         successful
//     * @throws AuthorizationException if requestor does not have upload
//     *                                permission
//     * @throws StorageException       if there is an error in uploading the
//     *                                SquidAsset
//     */
//    @Override
//    public SquidAsset uploadAsset(Asset a) {
//        throw new UnsupportedOperationException("Upload not supported by squid agent");
//    }
//
//    /**
//     * Method to search an asset
//     *
//     * @param text asset to be searched
//     * @return List of Squid Asset that matches
//     * @throws DDOException exception
//     */
//
//    public List<SquidAsset> searchAsset(String text) throws DDOException {
//        SearchResult searchResult = squidService.getOceanAPI().getAssetsAPI().search(text);
//        List<DDO> listDDO = searchResult.results;
//        List<SquidAsset> squidAssetLst = new ArrayList<>();
//        for (DDO ddo : listDDO) {
//            DID did = DID.parse(ddo.getDid().toString());
//            SquidAsset asset = getAsset(did);
//            squidAssetLst.add(asset);
//        }
//        return squidAssetLst;
//
//    }
//
//    /**
//     * API to return Squid DDO based on Squid DID
//     *
//     * @param did DID
//     * @return DDO ddo
//     * @throws UnsupportedOperationException exception
//     */
//    public SquidAsset resolveSquidDID(DID did) {
//
//        if (null == did) {
//            throw new UnsupportedOperationException("DID cannot be null");
//        }
//        SquidAsset squidAsset = getAsset(did);
//        return squidAsset;
//
//    }
//
//    /**
//     * API to Query Asset based on map argument passed
//     *
//     * @param queryMapData map data
//     * @return list of Squid Asset
//     * @throws Exception any exception occur while calling squid API
//     */
//    public List<SquidAsset> queryAsset(Map<String, Object> queryMapData) throws Exception {
//
//        List<DDO> listDDO = squidService.getAssetAPI().query(queryMapData).getResults();
//        List<SquidAsset> squidAssetLst = new ArrayList<>();
//        for (DDO ddo : listDDO) {
//            DID did = DID.parse(ddo.getDid().toString());
//            SquidAsset asset = getAsset(did);
//            squidAssetLst.add(asset);
//        }
//        return squidAssetLst;
//    }
//
//
//}
