package sg.dex.starfish.integration.developerTC;

import org.junit.Before;
import org.junit.Test;
import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.Purchase;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.ARemoteAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static sg.dex.starfish.constant.Constant.*;

/**
 * "As a developer working with Ocean,
 * I need a way to purchase an Asset for which
 * I have reviewed and agreed to the service agreement offered in a relevant Asset Listing
 * "
 */
public class PurchaseAsset_17 {

    ARemoteAsset remoteAsset;
    RemoteAgent remoteAgent;
    Listing listing;

    @Before
    public void setUp() {
        // create remote Agent
        remoteAgent = RemoteAgentConfig.getRemoteAgent();
        Asset a = MemoryAsset.create("Test Asset purchase");

        // create remote Asset
        remoteAsset = remoteAgent.registerAsset(a);
        remoteAgent.registerAsset(remoteAsset);
        Map<String, Object> data2 = new HashMap<>();
        //data.put( "status", "unpublished");
        data2.put("assetid", remoteAsset.getAssetID());
         listing = remoteAgent.createListing(data2);
        data2.put("id", listing.getMetaData().get("id"));
        data2.put("status", "published");
        remoteAgent.updateListing(data2);
    }


    @Test
    public  void testPurchaseAsset(){

        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put("listingid", listing.getMetaData().get("id"));

        Purchase purchase = remoteAgent.createPurchase(purchaseData);
        assertNotNull(purchase);
        assertNotNull(purchase);

    }
    private  String buildMetaData(byte[] data, Map<String, Object> meta) {
        String hash = Hex.toString(Hash.keccak256(data));

        Map<String, Object> ob = new HashMap<>();
        ob.put(DATE_CREATED, Instant.now().toString());
        ob.put(CONTENT_HASH, hash);
        ob.put(TYPE, DATA_SET);
        ob.put(SIZE, Integer.toString(data.length));
        ob.put(CONTENT_TYPE, "application/octet-stream");

        if (meta != null) {
            for (Map.Entry<String, Object> me : meta.entrySet()) {
                ob.put(me.getKey(), me.getValue());
            }
        }

        return JSON.toString(ob);
    }


}
