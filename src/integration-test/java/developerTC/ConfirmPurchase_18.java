package developerTC;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.Purchase;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.ARemoteAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.ProvUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static sg.dex.starfish.constant.Constant.*;


/**
 * "As a developer building a service provider Agent for Ocean,
 * I need a way to confirm if an Asset has been successfully purchased so that
 * I can determine whether to serve the asset to a given requestor
 * "
 */
public class ConfirmPurchase_18 {

    private ARemoteAsset remoteAsset;
    private RemoteAgent remoteAgent;
    private Listing listing;

    @BeforeEach
    public void setUp() {
        // create remote Agent
        remoteAgent = AgentService.getRemoteAgent();
        Asset a = MemoryAsset.createFromString("test Purchase");

        // create remote Asset
        remoteAsset = remoteAgent.registerAsset(a);
        // remoteAgent.registerAsset(remoteAsset);
        Map<String, Object> data2 = new HashMap<>();
        //data.put( "status", "unpublished");
        data2.put("assetid", remoteAsset.getAssetID());
        listing = remoteAgent.createListing(data2);
        data2.put(ID, listing.getMetaData().get(ID));
        data2.put(STATUS, PUBLISHED);
        listing = remoteAgent.updateListing(data2);


    }


    @Test
    public void testOrderedStatusForPurchaseAsset() {

        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put(LISTING_ID, listing.getMetaData().get(ID));
        purchaseData.put(STATUS, ORDERED);
        Purchase purchase = remoteAgent.createPurchase(purchaseData);
        String status = purchase.status();
        assertEquals(ORDERED, status);
        assertEquals(purchase.getListing().getId(), listing.getMetaData().get(ID));

    }

    @Test
    public void testWishlistStatusForPurchaseAsset() {

        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put(LISTING_ID, listing.getMetaData().get(ID));
        purchaseData.put(STATUS, WISHLIST);
        Purchase purchase = remoteAgent.createPurchase(purchaseData);
        assertEquals(purchase.getListing().getId(), listing.getMetaData().get(ID));
        String status = purchase.status();
        assertEquals(WISHLIST, status);

    }

    @Test
    public void testDeliveredStatusForPurchaseAsset() {

        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put(LISTING_ID, listing.getMetaData().get(ID));
        purchaseData.put(STATUS, DELIVERED);
        Purchase purchase = remoteAgent.createPurchase(purchaseData);
        assertEquals(purchase.getListing().getId(), listing.getMetaData().get(ID));
        String status = purchase.status();
        assertEquals(DELIVERED, status);

    }

    @Test
    public void testGetListing() {
        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put(LISTING_ID, listing.getMetaData().get(ID));
        purchaseData.put(STATUS, DELIVERED);
        Purchase purchase = remoteAgent.createPurchase(purchaseData);
        Listing l = purchase.getListing();
        assertEquals(l.getId(), listing.getId());

    }

    @Test
    public void testStatus() {
        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put(LISTING_ID, listing.getMetaData().get(ID));
        purchaseData.put(STATUS, DELIVERED);
        Purchase purchase = remoteAgent.createPurchase(purchaseData);
        String status = purchase.status();
        assertEquals(status, DELIVERED);

    }

    @Test
    public void testMetaData() {
        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put(LISTING_ID, listing.getMetaData().get("id"));
        purchaseData.put(STATUS, DELIVERED);
        Purchase purchase = remoteAgent.createPurchase(purchaseData);
        Map<String, Object> meta = purchase.getMetaData();
        assertEquals(meta.get(STATUS), DELIVERED);

    }

    @Test
    public void testNullData() {

        assertThrows(StarfishValidationException.class, () -> {
            Purchase purchase = remoteAgent.createPurchase(null);
            purchase.getMetaData();
        });

    }


    @Test
    public void testMetaDataWithProv() {
        // create Asset
        byte[] data = {3, 4, 5, 6};
        // adding metadata
        String actId = UUID.randomUUID().toString();
        String agentId = UUID.randomUUID().toString();
        Map<String, Object> provmetadata = ProvUtil.createPublishProvenance(actId, agentId);
        Map<String, Object> metaDataAsset = new HashMap<>();
        metaDataAsset.put("provenance", provmetadata);


        MemoryAsset memoryAsset = MemoryAsset.create(data, metaDataAsset);
        remoteAsset = remoteAgent.registerAsset(memoryAsset);

        // create listing
        Map<String, Object> data2 = new HashMap<>();
        //data.put( "status", "unpublished");
        data2.put("assetid", remoteAsset.getAssetID());
        Listing listing = remoteAgent.createListing(data2);

        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put(LISTING_ID, listing.getMetaData().get("id"));
        purchaseData.put(STATUS, DELIVERED);

        Purchase purchase = remoteAgent.createPurchase(purchaseData);
        Map<String, Object> meta = purchase.getMetaData();

        assertEquals(meta.get(STATUS), DELIVERED);

    }

}
