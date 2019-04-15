package sg.dex.starfish.impl.memory;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;
import sg.dex.starfish.Listing;
import sg.dex.starfish.util.Hex;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class TestMemoryListing {

    private static final byte[] BYTE_DATA = Hex.toBytes("0123456789");

    @Test
    public void testCreateListing() {

        // create memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();
        // create asset
        MemoryAsset a = MemoryAsset.create(BYTE_DATA);

        memoryAgent.uploadAsset(a);

        Map<String, Object> metaData = new HashMap<>();
        //data.put( "status", "unpublished");
        metaData.put("assetid", a.getAssetID());
        String info = "{\n" +
                "    \"title\": \"Test\",\n" +
                "    \"description\": \"Listing test\"\n" +
                "  }";
        JSONObject json = null;
        JSONParser parser = new JSONParser();
        try {
            json = (JSONObject) parser.parse(info);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        metaData.put("info", json);

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
    public void testWithAllData(){
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

    @Test(expected = RuntimeException.class)
    public void testCreateListingWithoutMandatoryData() {

        // create memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();
        // create asset
        MemoryAsset a = MemoryAsset.create(BYTE_DATA);

        memoryAgent.uploadAsset(a);

        Map<String, Object> metaData = new HashMap<>();
        //data.put( "status", "unpublished");
        //metaData.put("assetID", a.getAssetID());
        String info = "{\n" +
                "    \"title\": \"Test\",\n" +
                "    \"description\": \"Listing test\"\n" +
                "  }";
        JSONObject json = null;
        JSONParser parser = new JSONParser();
        try {
            json = (JSONObject) parser.parse(info);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        metaData.put("info", json);

        Listing listing = memoryAgent.createListing(metaData);
        Assert.assertNotNull(listing);
        Assert.assertNotNull(listing.getAsset());
        Assert.assertNotNull(listing.getMetaData());
    }
}
