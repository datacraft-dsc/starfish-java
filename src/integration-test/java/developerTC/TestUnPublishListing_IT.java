package developerTC;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.ARemoteAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * As a developer working with an Ocean marketplace,
 * I need a way to un publish my asset (i.e. remove relevant listings) from a marketplace
 */
@Disabled
public class TestUnPublishListing_IT {


    private RemoteAgent remoteAgent;

    @BeforeEach
    public void setUp() {
        remoteAgent = AgentService.getRemoteAgent();
        assumeTrue(null != remoteAgent);
    }


    /**
     * test to create listing
     */
    @Test
    public void testCreateListing() {
        // creating a memory Asset
        Asset memoryAsset = MemoryAsset.createFromString("Test Publish of an Asset");
        // registering the Asset
        ARemoteAsset remoteAsset = remoteAgent.registerAsset(memoryAsset);


        // meta data creation
        Map<String, Object> data2 = new HashMap<>();
        data2.put("assetid", remoteAsset.getAssetID());

        // to create listing Asset must be registered and map must have an asset id against which the listing will be created
        Listing listing = remoteAgent.createListing(data2);

        // by default for the first time the listing will be unpublished
        assertEquals(listing.getMetaData().get("status"), "unpublished");
        assertEquals(listing.getMetaData().get("id"), listing.getId());

        // comparing both asset ID
        assertEquals(listing.getAssetID(), remoteAsset.getAssetID());


    }

    /**
     * Test to update the listing status
     */
    @Test
    public void testUpdateListing() {

        // creating a memory Asset
        Asset memoryAsset = MemoryAsset.createFromString("Test Publish of an Asset");
        // registering the Asset
        ARemoteAsset remoteAsset = remoteAgent.registerAsset(memoryAsset);


        // meta data creation
        Map<String, Object> data2 = new HashMap<>();
        data2.put("assetid", remoteAsset.getAssetID());

        // to create listing Asset must be registered and map must have an asset id against which the listing will be created
        Listing listing = remoteAgent.createListing(data2);

        // updating the listing meta data map
        data2.put("id", listing.getMetaData().get("id"));
        data2.put("status", "published");

        // updating the listing with new values
        remoteAgent.updateListing(data2);

        // verifying the updated value
        assertEquals(listing.getMetaData().get("status"), "published");


    }

    @Test
    public void testCreateWithoutAssetId() {

        // this map dont have the assetid so validation will fail
        Map<String, Object> listingMetaData = new HashMap<>();

        // to create listing Asset must be registered and map must have an asset id against which the listing will be created

        assertThrows(StarfishValidationException.class, () -> {
            remoteAgent.createListing(listingMetaData);

        });
    }

    @Test
    public void testUpdateWithoutCreate() {
        Map<String, Object> listingMetaData = new HashMap<>();

        assertThrows(StarfishValidationException.class, () -> {
            remoteAgent.updateListing(listingMetaData);
        });
    }

    @Test
    public void testUpdateForNull() {

        assertThrows(StarfishValidationException.class, () -> {
            remoteAgent.updateListing(null);
        });
    }

    @Test
    public void testUpdateReturnNewListing() {
        // creating a memory Asset
        Asset memoryAsset = MemoryAsset.createFromString("Test Publish of an Asset");
        // registering the Asset
        ARemoteAsset remoteAsset = remoteAgent.registerAsset(memoryAsset);


        // meta data creation
        Map<String, Object> data2 = new HashMap<>();
        data2.put("assetid", remoteAsset.getAssetID());

        // to create listing Asset must be registered and map must have an asset id against which the listing will be created
        Listing listing = remoteAgent.createListing(data2);

        // updating the listing meta data map
        Map<String, Object> metaDataToUpdate = new HashMap<>();
        metaDataToUpdate.put("id", listing.getMetaData().get("id"));

        data2.put("status", "published");
        Listing updatedListing = remoteAgent.updateListing(metaDataToUpdate);
        assertNotNull(updatedListing);
        // update should create a new instance listing
        assertFalse(updatedListing.equals(listing));


    }

    @Test
    public void testGetListing() {
// creating a memory Asset
        Asset memoryAsset = MemoryAsset.createFromString("Test Publish of an Asset");
        // registering the Asset
        ARemoteAsset remoteAsset = remoteAgent.registerAsset(memoryAsset);


        // meta data creation
        Map<String, Object> data2 = new HashMap<>();
        data2.put("assetid", remoteAsset.getAssetID());
        // to create listing Asset must be registered and map must have an asset id against which the listing will be created
        Listing listing = remoteAgent.createListing(data2);
        Listing listing1 = remoteAgent.getListing(listing.getId());
        // to maintain the immutability
        assertFalse(listing1.equals(listing));


    }


}
