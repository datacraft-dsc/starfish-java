package sg.dex.starfish.developer_usecase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Listing;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;

/**
 * As a developer building client code to interact with an Ocean marketplace,
 * I need a way to search available asset listings
 */
@RunWith(JUnit4.class)
public class SearchAssetListing_13 {

    RemoteAsset remoteAsset;
    RemoteAgent remoteAgent;

    @Before
    public void setUp() {
        // create remote Agent
        remoteAgent = RemoteAgentConfig.getRemoteAgent();
    	if (remoteAgent==null) return;

        // create remote Asset
        remoteAsset = RemoteAsset.create(remoteAgent, "Test Asset publish");
        // register Remote asset
        remoteAgent.registerAsset(remoteAsset);


    }

    @Test
    public void testSearchListingById() {
    	if (remoteAgent==null) return;

        Map<String, Object> data2 = new HashMap<>();
        data2.put("assetid", remoteAsset.getAssetID());
        Listing listing = remoteAgent.createListing(data2);
        String listingId = listing.getMetaData().get("id").toString();
        assertNotNull(remoteAgent.getListing(listingId));

    }

    @Test
    public void testSearchAllListing() {
    	if (remoteAgent==null) return;

        List<Listing> listingLst = remoteAgent.getAllListing();
        for(Listing listing: listingLst){
            String listingId = listing.getMetaData().get("id").toString();
            assertNotNull(remoteAgent.getListing(listingId));
        }

    }
    @Test
    public void testSearchListingByInvalidId() {
    	if (remoteAgent==null) return;

        Map<String, Object> data2 = new HashMap<>();
        data2.put("assetid", remoteAsset.getAssetID());
        Listing listing = remoteAgent.createListing(data2);
        String listingId = listing.getMetaData().get("id").toString();
        listingId =listingId+"invalid";
        assertNotNull(remoteAgent.getListing(listingId));

    }

}
