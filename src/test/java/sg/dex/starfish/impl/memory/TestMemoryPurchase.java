package sg.dex.starfish.impl.memory;

import org.junit.Assert;
import org.junit.Test;
import sg.dex.starfish.Listing;
import sg.dex.starfish.util.Hex;

import java.util.HashMap;
import java.util.Map;

public class TestMemoryPurchase {

    private static final byte[] BYTE_DATA = Hex.toBytes("0123456789");


    @Test
    public void testCreateListingWithoutInfo() {

        // create memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();
        // create asset
        MemoryAsset a = MemoryAsset.create(BYTE_DATA);
        memoryAgent.uploadAsset(a);
        Map<String, Object> metaDataForListing = new HashMap<>();
        //data.put( "status", "unpublished");
        metaDataForListing.put("assetid", a.getAssetID());

        Listing listing = memoryAgent.createListing(metaDataForListing);

        Map<String, Object> metaDataForPurchase = new HashMap<>();
        metaDataForPurchase.put("listingid", listing.getMetaData().get("id"));

        MemoryPurchase memoryPurchase = memoryAgent.createPurchase(metaDataForPurchase);


        Assert.assertNotNull(memoryPurchase);
        Assert.assertNotNull(memoryPurchase.getListingId());
        Assert.assertNotNull(memoryPurchase.getId());
    }

    @Test(expected = RuntimeException.class)
    public void testPurchaseWithoutMandatoryData() {

        // create memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();
        // create asset
        MemoryAsset a = MemoryAsset.create(BYTE_DATA);
        memoryAgent.uploadAsset(a);
        Map<String, Object> metaDataForListing = new HashMap<>();
        //data.put( "status", "unpublished");
        metaDataForListing.put("assetid", a.getAssetID());

        Listing listing = memoryAgent.createListing(metaDataForListing);

        Map<String, Object> metaDataForPurchase = new HashMap<>();
       // metaDataForPurchase.put("listingid", listing.getMetaData().get("id"));

        MemoryPurchase memoryPurchase = memoryAgent.createPurchase(metaDataForPurchase);


        Assert.assertNotNull(memoryPurchase);
        Assert.assertNotNull(memoryPurchase.getListingId());
        Assert.assertNotNull(memoryPurchase.getId());
    }


}
