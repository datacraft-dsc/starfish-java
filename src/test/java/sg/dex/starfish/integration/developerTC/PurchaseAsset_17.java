package sg.dex.starfish.integration.developerTC;

import org.junit.Before;
import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.Purchase;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.ARemoteAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeNotNull;
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
        remoteAgent = AgentService.getRemoteAgent();
        Asset a = MemoryAsset.createFromString("Test Asset purchase");

        // create remote Asset
        remoteAsset = remoteAgent.registerAsset(a);
        remoteAgent.registerAsset(remoteAsset);
        Map<String, Object> data2 = new HashMap<>();
        //data.put( "status", "unpublished");
        data2.put("assetid", remoteAsset.getAssetID());
        listing = remoteAgent.createListing(data2);
        data2.put("id", listing.getMetaData().get("id"));
        data2.put("status", "published");
        listing = remoteAgent.updateListing(data2);
    }


    @Test
    public void testPurchaseAsset() {

        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put("listingid", listing.getMetaData().get("id"));

        Purchase purchase = remoteAgent.createPurchase(purchaseData);
        assertEquals(purchase.getListing().getId(), listing.getMetaData().get("id"));

    }

    @Test
    public void testPurchaseWithUnpublishedListing() {
        Asset a = MemoryAsset.createFromString("Test Asset purchase");

        // create remote Asset
        remoteAsset = remoteAgent.registerAsset(a);
        ARemoteAsset aRemoteAsset = remoteAgent.registerAsset(remoteAsset);
        Map<String, Object> data2 = new HashMap<>();
        data2.put("assetid", remoteAsset.getAssetID());
        Listing listing = remoteAgent.createListing(data2);
        data2.put("id", listing.getMetaData().get("id"));
        data2.put(STATUS, UNPUBLISHED);
        listing = remoteAgent.updateListing(data2);
        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put(LISTING_ID, listing.getMetaData().get(ID));

        Purchase purchase = remoteAgent.createPurchase(purchaseData);

        assertEquals(purchase.getMetaData().get(STATUS), WISHLIST);
    }

    @Test(expected = StarfishValidationException.class)
    public void testPurchaseWithNull() {
        assumeNotNull(remoteAgent);
        remoteAgent.createPurchase(null);
    }


}
