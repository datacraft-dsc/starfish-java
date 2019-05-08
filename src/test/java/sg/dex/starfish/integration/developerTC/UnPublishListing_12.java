//package sg.dex.starfish.integration.developerTC;
//
//import org.junit.Before;
//import org.junit.Test;
//import sg.dex.starfish.Asset;
//import sg.dex.starfish.Listing;
//import sg.dex.starfish.impl.memory.MemoryAsset;
//import sg.dex.starfish.impl.remote.RemoteAgent;
//import sg.dex.starfish.impl.remote.RemoteAsset;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static junit.framework.TestCase.assertEquals;
//
///**
// * As a developer working with an Ocean marketplace,
// * I need a way to unpublish my asset (i.e. remove relevant listings) from a marketplace
// */
//public class UnPublishListing_12 {
//
//
//    private RemoteAgent remoteAgent;
//
//    @Before
//    public void setUp() {
//        remoteAgent = RemoteAgentConfig.getRemoteAgent();
//    }
//
//
//    @Test
//    public void testUnPublishAsset() {
//
//        // creating a memory Asset
//        Asset memoryAsset = MemoryAsset.create("Test Publish of an Asset");
//        // registering the Asset
//        RemoteAsset remoteAsset = remoteAgent.registerAsset(memoryAsset);
//
//
//        // meta data creation
//        Map<String, Object> data2 = new HashMap<>();
//        data2.put("assetid", remoteAsset.getAssetID());
//        // to create listing Asset must be registered and map must have an asset id against which the listing will be created
//        Listing listing = remoteAgent.createListing(data2);
//
//        // by default for the first time the listing will be unpublished
//        assertEquals(listing.getMetaData().get("status"), "unpublished");
//
//        // updating the listing meta data map
//        data2.put("id", listing.getMetaData().get("id"));
//        data2.put("status", "published");
//
//        // updating the listing with new values
//        remoteAgent.updateListing(data2);
//
//        // verifying the updated value
//        assertEquals(listing.getMetaData().get("status"), "published");
//
//        // again trying to un publish
//
//        data2.put("status", "unpublished");
//
//        // updating the listing with new values
//        remoteAgent.updateListing(data2);
//
//        // verifying the updated value
//        assertEquals(listing.getMetaData().get("status"), "unpublished");
//
//
//
//    }
//
//
//}
