package sg.dex.starfish.integration.developerTC;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteDataAsset;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

/**
 * Class to test the Remote listing functionality
 */
@RunWith(JUnit4.class)
public class ViewAssetListing_14 {

    private RemoteAgent remoteAgent;

    @Before
    public void setUp() {
        // create remote Agent
        remoteAgent = RemoteAgentConfig.getRemoteAgent();

    }

    @Test
    public void testSearchListingById() {
        // creating a memory asset
        Asset asset = MemoryAsset.createFromString("Test listing searching by listing id");

        RemoteDataAsset remoteAsset = remoteAgent.registerAsset(asset);

        // creating metadata of listing
        Map<String, Object> data2 = new HashMap<>();
        data2.put("assetid", remoteAsset.getAssetID());

        // creating the listing
        Listing listing = remoteAgent.createListing(data2);

        // getting the listing id of created listing
        String listingId = listing.getMetaData().get("id").toString();

        // checking the listing id is present in remote
        Listing listing1FromRemote = remoteAgent.getListing(listingId);
        // comparing both lisitng id ,it should eb same
        assertEquals(listing1FromRemote.getMetaData().get("id"), listingId);


    }


}
