package sg.dex.starfish.impl.squid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oceanprotocol.squid.exceptions.DDOException;
import com.oceanprotocol.squid.exceptions.DIDFormatException;
import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.models.DDO;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a SquidAsset on the Ocean Network.
 *
 * @author Tom
 */
public class SquidAsset extends AAsset {


    private final Ocean ocean;
    private final DID did;
    private DDO ddo;


    private SquidAsset(String meta, DID did, DDO ddo, Ocean ocean) {
        super(meta);
        this.did = did;
        this.ddo = ddo;
        this.ocean = ocean;
    }

    private SquidAsset(String meta, DID did, Ocean ocean) {
        super(meta);
        this.did = did;
        this.ocean = ocean;
    }

    public static SquidAsset create(String meta, DID did, DDO ddo, Ocean ocean) {
        return new SquidAsset(meta, did, ddo, ocean);
    }


    public static Asset create(Ocean ocean, DID did) {
        com.oceanprotocol.squid.models.DID squidDID;
        try {
            squidDID = com.oceanprotocol.squid.models.DID.builder().setDid(did.toString());
            DDO ddo = ocean.getAssetsAPI().resolve(squidDID);

            String metaString = wrapDDOMeta(ddo);
            return create(metaString, did, ddo, ocean);
        } catch (DIDFormatException e) {
            throw new Error(e);
        } catch (EthereumException e) {
            throw new Error(e);
        } catch (DDOException e) {
            throw new Error(e);
        }
    }

    /**
     * API to create Squid Asset using metaData DID and Ocean instance
     * @param metadata
     * @param did
     * @param ocean
     * @return
     */
    public static SquidAsset create(String metadata, DID did, Ocean ocean) {

        return new SquidAsset(buildMetaData(metadata), did, ocean);

    }

    /**
     * API to build metadata with default value
     * @param metadata
     * @return
     */
    private static String buildMetaData(String metadata) {


        // create default metadata
        Map<String, Object> defaultMetadataMap = new HashMap<>();
        defaultMetadataMap.put(Constant.DATE_CREATED, "2012-10-10T17:00:000Z");
        defaultMetadataMap.put(Constant.TYPE, Constant.DATA_SET);
        defaultMetadataMap.put(Constant.NAME, "NA");
        defaultMetadataMap.put("license", "NA");
        defaultMetadataMap.put("author", "NA");
        defaultMetadataMap.put("price", 1);

        if (metadata != null) {
            Map<String, Object> metaMap = JSON.toMap(metadata);
            for (Map.Entry<String, Object> me : metaMap.entrySet()) {
                defaultMetadataMap.put(me.getKey(), me.getValue());
            }
        }
        return JSON.toPrettyString(defaultMetadataMap);

    }

    private static String wrapDDOMeta(DDO ddo) {
        HashMap<String, Object> info = new HashMap<>();
        try {
            info.put("ddoString", JSON.toMap(ddo.toJson()));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

        HashMap<String, Object> meta = new HashMap<>();
        meta.put("type", "dataset");
        meta.put("additionalInformation", info);

        String result = JSON.toPrettyString(meta);
        return result;
    }

    public Ocean getOcean() {
        return ocean;
    }

    public DDO getDdo() {
        return ddo;
    }

    /**
     * Gets the DID for this SquidAsset
     *
     * @return DID
     */
    @Override
    public DID getAssetDID() {
        return did;
    }

    @Override
    public boolean isDataAsset() {
        return true;
    }

    /**
     * Returns true if this asset is an operation, i.e. can be invoked on an
     * appropriate agent
     *
     * @return true if this asset is an operation, false otherwise
     */
    @Override
    public boolean isOperation() {
        return false;
    }

}
