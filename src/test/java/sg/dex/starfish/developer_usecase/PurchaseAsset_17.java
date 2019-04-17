package sg.dex.starfish.developer_usecase;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import sg.dex.starfish.Listing;
import sg.dex.starfish.Purchase;
import sg.dex.starfish.connection_check.AssumingConnection;
import sg.dex.starfish.connection_check.ConnectionChecker;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * "As a developer working with Ocean,
 * I need a way to purchase an Asset for which
 * I have reviewed and agreed to the service agreement offered in a relevant Asset Listing
 * "
 */
public class PurchaseAsset_17 {
    @ClassRule
    public static AssumingConnection assumingConnection =
            new AssumingConnection(new ConnectionChecker(RemoteAgentConfig.getSurferUrl()));
    RemoteAsset remoteAsset;
    RemoteAgent remoteAgent;
    Listing listing;

    @Before
    public void setUp() {
        // create remote Agent
        remoteAgent = RemoteAgentConfig.getRemoteAgent();

        // create remote Asset
        remoteAsset = RemoteAsset.create(remoteAgent, "Test Asset purchase");
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


}
