package sg.dex.starfish.impl.memory;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sg.dex.starfish.Listing;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Hex;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMemoryPurchase {

    private static final byte[] BYTE_DATA = Hex.toBytes("0123456789");

    /**
     * Test create Purchse with Valid data
     */
    @Test
    public void testPurchaseValidData() {

        // create memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();
        // create asset
        MemoryAsset a = MemoryAsset.create(BYTE_DATA);
        memoryAgent.uploadAsset(a);

        Map<String, Object> metaDataForListing = new HashMap<>();
        //data.put( "status", "unpublished");
        metaDataForListing.put("assetid", a.getAssetID());


        Listing listing = memoryAgent.createListing(getResponseMetaDataPurchase(metaDataForListing));

        Map<String, Object> metaDataForPurchase = getResponseMetaDataPurchase(metaDataForListing);

        metaDataForPurchase.put("listingid", listing.getMetaData().get("id"));

        MemoryPurchase memoryPurchase = memoryAgent.createPurchase(metaDataForPurchase);


        Assert.assertNotNull(memoryPurchase);
        Assert.assertNotNull(memoryPurchase.getListingId());
        Assert.assertNotNull(memoryPurchase.getId());
    }

    /**
     * Listing id is mandatory, if it not there it will throw exception
     */
    @Test(expected = Exception.class)
    public void testPurchaseMissingData() {

        // create memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();
        // create asset
        MemoryAsset a = MemoryAsset.create(BYTE_DATA);
        memoryAgent.uploadAsset(a);

        Map<String, Object> metaDataForListing = new HashMap<>();
        Map<String, Object> metaDataForPurchase = getResponseMetaDataPurchase(metaDataForListing);


        MemoryPurchase memoryPurchase = memoryAgent.createPurchase(metaDataForPurchase);


        Assert.assertNotNull(memoryPurchase);
        Assert.assertNotNull(memoryPurchase.getListingId());
        Assert.assertNotNull(memoryPurchase.getId());
    }
    /**
     * API to create a response similar to Remote Agents responses.
     *
     * @param purchaseData
     * @return Map<String, Object> responseMetaDataPurchase
     */
    private Map<String, Object> getResponseMetaDataPurchase(Map<String, Object> purchaseData) {


        Map<String, Object> responseMetadata = new HashMap<>();
        responseMetadata.putAll(purchaseData);
        responseMetadata.put("status", "wishlist");
        responseMetadata.put("id", DID.createRandomString());

        responseMetadata.put("userid", purchaseData.get("userid") == null ? 1234 : purchaseData.get("userid"));
        responseMetadata.put("info", purchaseData.get("info") == null ? null : purchaseData.get("info"));
        responseMetadata.put("agreement", purchaseData.get("agreement") == null ? Instant.now() : purchaseData.get("agreement"));
        responseMetadata.put("ctime", purchaseData.get("ctime") == null ? Instant.now() : purchaseData.get("agreement"));
        responseMetadata.put("utime", purchaseData.get("utime") == null ? Instant.now() : purchaseData.get("agreement"));

        return responseMetadata;

    }

}
