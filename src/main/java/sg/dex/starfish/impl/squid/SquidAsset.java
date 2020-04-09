//package sg.dex.starfish.impl.squid;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.oceanprotocol.squid.exceptions.DDOException;
//import com.oceanprotocol.squid.exceptions.DIDFormatException;
//import com.oceanprotocol.squid.exceptions.EthereumException;
//import com.oceanprotocol.squid.models.DDO;
//import sg.dex.starfish.DataAsset;
//import sg.dex.starfish.exception.GenericException;
//import sg.dex.starfish.impl.AAsset;
//import sg.dex.starfish.util.DID;
//import sg.dex.starfish.util.JSON;
//import sg.dex.starfish.util.ProvUtil;
//
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
///**
// * Class representing a SquidAsset on the Ocean Network.
// *
// * @author Tom
// */
//public class SquidAsset extends AAsset implements DataAsset {
//
//    private com.oceanprotocol.squid.models.DID did;
//
//    private SquidAsset(String meta, com.oceanprotocol.squid.models.DID did) {
//        super(meta);
//        this.did = did;
//    }
//
//    public static SquidAsset create(String metaData, com.oceanprotocol.squid.models.DID did) {
//
//        return new SquidAsset(metaData, did);
//    }
//
//
//    public static SquidAsset create(DID did, SquidService squidService) {
//
//        com.oceanprotocol.squid.models.DID squidDID;
//        try {
//            squidDID =
//                    com.oceanprotocol.squid.models.DID.builder().setDid(did.toString());
//            DDO ddo = squidService.getOceanAPI().getAssetsAPI().resolve(squidDID);
//
//            Map<String, Object> metaData = wrapDDOMeta(ddo);
//
//            return new SquidAsset(JSON.toPrettyString(metaData), squidDID);
//        } catch (DIDFormatException e) {
//            throw new Error(e);
//        } catch (EthereumException e) {
//            throw new Error(e);
//        } catch (DDOException e) {
//            throw new Error(e);
//        }
//    }
//
//    /**
//     * Method that wrap the squid DDO data  into starfish metadata as additional
//     * information
//     *
//     * @param ddo squid ddo
//     * @return map of metadata which have squid DDO information
//     */
//    private static HashMap<String, Object> wrapDDOMeta(DDO ddo) {
//        HashMap<String, Object> info = new HashMap<>();
//        try {
//            info.put("ddoString", JSON.toMap(ddo.toJson()));
//        } catch (JsonProcessingException e) {
//            throw new IllegalArgumentException(e);
//        }
//
//        HashMap<String, Object> meta = new HashMap<>();
//        meta.put("type", "dataset");
//        meta.put("additionalInformation", info);
//
//        // adding default provenance
//        String actId = UUID.randomUUID().toString();
//        String agentId = UUID.randomUUID().toString();
//
//        Map<String, Object> provenanceData = ProvUtil.createPublishProvenance(actId, agentId);
//        meta.put("provenance", provenanceData);
//
//        return meta;
//    }
//
//
//    @Override
//    public boolean isDataAsset() {
//        return true;
//    }
//
//    /**
//     * Returns true if this asset is an operation, i.e. can be invoked on an
//     * appropriate agent
//     *
//     * @return true if this asset is an operation, false otherwise
//     */
//    @Override
//    public boolean isOperation() {
//        return false;
//    }
//
//    @Override
//    public InputStream getContentStream() {
//        // todo
//        throw new GenericException("Need to implement");
//    }
//
//    @Override
//    public long getContentSize() {
//        // todo
//        throw new GenericException("Need to implement");
//    }
//
//    @Override
//    public DID getDID() {
//        return DID.parse(this.did.toString());
//    }
//
//
//}
