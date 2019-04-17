package sg.dex.starfish.impl.memory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import sg.dex.starfish.Listing;
import sg.dex.starfish.util.Hex;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class TestMemoryListing {

    private static final byte[] BYTE_DATA = Hex.toBytes("0123456789");
    private static String METADATA_JSON_SAMPLE = "src/test/resources/example/test_asset.json";

    @Test
    public void testCreateListing() {

        // create memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();
        // create asset
        MemoryAsset a = MemoryAsset.create(BYTE_DATA);

        memoryAgent.uploadAsset(a);

        Map<String, Object> metaData = new HashMap<>();
        try {
            String METADATA_JSON_CONTENT = new String(Files.readAllBytes(Paths.get(METADATA_JSON_SAMPLE)));
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap json = objectMapper.readValue(METADATA_JSON_CONTENT, HashMap.class);
            metaData.put("info", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        metaData.put("assetid",a.getAssetID());
        Listing listing = memoryAgent.createListing(metaData);
        Assert.assertNotNull(listing);
        Assert.assertNotNull(listing.getAsset());
        Assert.assertNotNull(listing.getMetaData());
    }


    @Test
    public void testCreateListingWithoutInfo() {

        // create memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();
        // create asset
        MemoryAsset a = MemoryAsset.create(BYTE_DATA);
        memoryAgent.uploadAsset(a);
        Map<String, Object> metaData = new HashMap<>();
        //data.put( "status", "unpublished");
        metaData.put("assetid", a.getAssetID());


        Listing listing = memoryAgent.createListing(metaData);
        Assert.assertNotNull(listing);
        Assert.assertNotNull(listing.getAsset());
        Assert.assertNull(listing.getInfo());
        Assert.assertNotNull(listing.getMetaData());
    }

    @Test
    public void testWithAllData() {
        // create memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();
        // create asset
        MemoryAsset a = MemoryAsset.create(BYTE_DATA);
        memoryAgent.uploadAsset(a);
        Map<String, Object> metaData = new HashMap<>();
        //data.put( "status", "unpublished");
        metaData.put("assetid", a.getAssetID());
        metaData.put("userid", "789e3f52da1020b56981e1cb3ee40c4df72103452f0986569711b64bdbdb4ca6");
        metaData.put("agreement", null);
        metaData.put("ctime", Instant.now());
        metaData.put("title", "test listing ");
        metaData.put("description", "this is a test listing");
        metaData.put("trust_level", 0);

        Listing listing = memoryAgent.createListing(metaData);
        Assert.assertNotNull(listing);
        Assert.assertNotNull(listing.getAsset());
        Assert.assertNull(listing.getInfo());
        Assert.assertNotNull(listing.getMetaData());


    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateListingWithoutMandatoryData() {

        // create memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();
        // create asset
        MemoryAsset a = MemoryAsset.create(BYTE_DATA);
        memoryAgent.uploadAsset(a);

        Map<String, Object> metaData = new HashMap<>();

        try {
            String METADATA_JSON_CONTENT = new String(Files.readAllBytes(Paths.get(METADATA_JSON_SAMPLE)));
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap json = objectMapper.readValue(METADATA_JSON_CONTENT, HashMap.class);
            metaData.put("info", json);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Listing listing = memoryAgent.createListing(metaData);
        Assert.assertNotNull(listing);
        Assert.assertNotNull(listing.getAsset());
        Assert.assertNotNull(listing.getMetaData());
    }
}
