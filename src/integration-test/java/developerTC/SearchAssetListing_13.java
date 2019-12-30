package developerTC;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteDataAsset;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * As a developer building client code to interact with an Ocean marketplace,
 * I need a way to search available asset listings
 */
@Disabled
public class SearchAssetListing_13 {

    private RemoteAgent remoteAgent;

    @BeforeEach
    public void setUp() {
        // create remote Agent
        remoteAgent = AgentService.getRemoteAgent();
        assumeTrue(null!= remoteAgent);

    }

    @Test
    public void testSearchListingById() {

        // create memory Asset
        Asset asset = MemoryAsset.createFromString("Test Searching of listing");
        RemoteDataAsset remoteAsset = remoteAgent.registerAsset(asset);

        Map<String, Object> data2 = new HashMap<>();
        data2.put("assetid", remoteAsset.getAssetID());
        // creating listing
        Listing listing = remoteAgent.createListing(data2);

        String listingId = listing.getMetaData().get("id").toString();
        // verifying if the listing has been created
        Listing listingFromAgent =remoteAgent.getListing(listingId);
        assertEquals(listingId,listingFromAgent.getId());
        assertEquals(listing.getAssetID(),listingFromAgent.getAssetID());

    }


    @Test
    public void testSearchListingByInvalidId() {

        Asset asset = MemoryAsset.createFromString("Test Searching of listing");
        RemoteDataAsset remoteAsset = remoteAgent.registerAsset(asset);

        Map<String, Object> data2 = new HashMap<>();
        // adding some invalid asset-id in the map and try to create the listing
        data2.put("assetid", remoteAsset.getAssetID() + "Invalid");

        assertThrows(RemoteException.class, () -> {
            remoteAgent.createListing(data2);
        });

    }

}
