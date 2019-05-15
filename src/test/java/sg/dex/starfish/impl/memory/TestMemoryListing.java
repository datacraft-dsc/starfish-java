package sg.dex.starfish.impl.memory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Hex;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMemoryListing {

    private static final byte[] BYTE_DATA = Hex.toBytes("0123456789");
    private static String METADATA_JSON_SAMPLE = "src/test/resources/example/asset_metadata.json";

    /**
     * API to test the create listing with extra info data
     */
    @Test
    public void testCreateListing() {

        // create memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();
        // create asset
        MemoryAsset a = MemoryAsset.create(BYTE_DATA);

        Asset uploadedAsset = memoryAgent.uploadAsset(a);

        Map<String, Object> metaData = new HashMap<>();
        try {
            String METADATA_JSON_CONTENT = new String(Files.readAllBytes(Paths.get(METADATA_JSON_SAMPLE)));
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap json = objectMapper.readValue(METADATA_JSON_CONTENT, HashMap.class);
            metaData.put("info", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        metaData.put("assetid", uploadedAsset.getAssetID());
        Listing listing = memoryAgent.createListing(getResponseMetaDataListing(metaData));
        Assert.assertNotNull(listing);
        Assert.assertNotNull(listing.getAsset());
        Assert.assertNotNull(listing.getMetaData());
    }

    /**
     * Test create listing with info data is null
     */

    @Test
    public void testCreateListingWithoutInfo() {

        // create memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();
        // create asset
        MemoryAsset a = MemoryAsset.create(BYTE_DATA);
        Asset uploadedAsset = memoryAgent.uploadAsset(a);
        Map<String, Object> metaData = new HashMap<>();
        //data.put( "status", "unpublished");
        metaData.put("assetid", uploadedAsset.getAssetID());


        Listing listing = memoryAgent.createListing(getResponseMetaDataListing(metaData));
        Assert.assertNotNull(listing);
        Assert.assertNotNull(listing.getAsset());
        Assert.assertNull(listing.getInfo());
        Assert.assertNotNull(listing.getMetaData());
    }

    /**
     *Testing listing API with all (agreement,info,..)valid data
     */
    @Test
    public void testWithAllData() {
        // create memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();
        // create asset
        MemoryAsset a = MemoryAsset.create(BYTE_DATA);
        memoryAgent.uploadAsset(a);

        // Agreement map
        Map<String, Object> agreement = new HashMap<>();
        agreement.put("test", "test");
        Map<String, Object> metaData = new HashMap<>();

        //data.put( "status", "unpublished");
        metaData.put("assetid", a.getAssetID());
        metaData.put("userid", "789e3f52da1020b56981e1cb3ee40c4df72103452f0986569711b64bdbdb4ca6");
        metaData.put("agreement", agreement);
        metaData.put("ctime", Instant.now());
        metaData.put("title", "test listing ");
        metaData.put("description", "this is a test listing");
        metaData.put("trust_level", 0);

        Listing listing = memoryAgent.createListing(getResponseMetaDataListing(metaData));
        Assert.assertNotNull(listing);
        Assert.assertNotNull(listing.getAsset());
        Assert.assertNull(listing.getInfo());
        Assert.assertNotNull(listing.getMetaData());
        assertNotNull(listing.getAgreement());


    }

    /**
     * Create listing with asset ID.IT will throw exception as asset id is mandatory for creating
     * listing.
     */
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


        Listing listing = memoryAgent.createListing(getResponseMetaDataListing(metaData));
        Assert.assertNotNull(listing);
        Assert.assertNotNull(listing.getAsset());
        Assert.assertNotNull(listing.getMetaData());
    }

    /**
     * Prepare the listing meta data required for creating listing
     *
     * @param meta
     * @return
     */
    private Map<String, Object> getResponseMetaDataListing(Map<String, Object> meta) {
        Map<String, Object> responseMetadata = new HashMap<>();

        responseMetadata.putAll(meta);
        // default status
        responseMetadata.put("status", "unpublished");

        responseMetadata.put("id", DID.createRandom());

        responseMetadata.put("trust_level", meta.get("trust_level") == null ? 0 : meta.get("trust_level"));
        responseMetadata.put("userid", meta.get("userid") == null ? 1234 : meta.get("userid"));
        responseMetadata.put("agreement", meta.get("agreement") == null ? 0 : meta.get("agreement"));
        responseMetadata.put("info", meta.get("info"));
        responseMetadata.put("utime", meta.get("utime") == null ? Instant.now() : meta.get("utime"));
        responseMetadata.put("ctime", meta.get("ctime") == null ? Instant.now() : meta.get("ctime"));

        return responseMetadata;


    }
}
