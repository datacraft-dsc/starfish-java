package developerTC;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteDataAsset;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;

/**
 * As a developer building client code to interact with an Ocean marketplace,
 * I need a way to search available asset listings
 */
@RunWith(JUnit4.class)
public class SearchAssetListing_13 {

    private RemoteAgent remoteAgent;

    @Before
    public void setUp() {
        // create remote Agent
        remoteAgent = AgentService.getRemoteAgent();

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
        assertNotNull(remoteAgent.getListing(listingId));

    }


    @Test(expected = RemoteException.class)
    public void testSearchListingByInvalidId() {

        Asset asset = MemoryAsset.createFromString("Test Searching of listing");
        RemoteDataAsset remoteAsset = remoteAgent.registerAsset(asset);

        Map<String, Object> data2 = new HashMap<>();
        // adding some invalid asset-id in the map and try to create the listing
        data2.put("assetid", remoteAsset.getAssetID() + "Invalid");

        Listing listing = remoteAgent.createListing(data2);


    }

}
