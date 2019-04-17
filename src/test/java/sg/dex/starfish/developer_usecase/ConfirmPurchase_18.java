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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * "As a developer building a service provider Agent for Ocean,
 * I need a way to confirm if an Asset has been sucessfully puchased so that
 * I can determine whether to serve the asset to a given requestor
 * "
 */
public class ConfirmPurchase_18 {
    @ClassRule
    public static AssumingConnection assumingConnection =
            new AssumingConnection(new ConnectionChecker(RemoteAgentConfig.getSurferUrl()));
    private RemoteAsset remoteAsset;
    private RemoteAgent remoteAgent;
    private Listing listing;

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
    public void testOrderedStatusForPurchaseAsset() {

        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put("listingid", listing.getMetaData().get("id"));
        purchaseData.put("status", "ordered");
        Purchase purchase = remoteAgent.createPurchase(purchaseData);
        assertNotNull(purchase);
        String status = purchase.status();
        assertEquals("ordered", status);

    }

    @Test
    public void testWishlistStatusForPurchaseAsset() {

        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put("listingid", listing.getMetaData().get("id"));
        purchaseData.put("status", "wishlist");
        Purchase purchase = remoteAgent.createPurchase(purchaseData);
        assertNotNull(purchase);
        String status = purchase.status();
        assertEquals("wishlist", status);

    }

    @Test
    public void testDeliveredStatusForPurchaseAsset() {

        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put("listingid", listing.getMetaData().get("id"));
        purchaseData.put("status", "delivered");
        Purchase purchase = remoteAgent.createPurchase(purchaseData);
        assertNotNull(purchase);
        String status = purchase.status();
        assertEquals("delivered", status);

    }
}
