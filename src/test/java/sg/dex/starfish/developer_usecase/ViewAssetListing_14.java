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
 * "As a developer working with Ocean,
 * I need a way to view an asset available for puchase and any associated terms / service agreements that may be purchased
 * "
 */
@RunWith(JUnit4.class)
public class ViewAssetListing_14 {
    RemoteAsset remoteAsset;
    RemoteAgent remoteAgent;

    @Before
    public void setUp() {
        // create remote Agent
        remoteAgent = RemoteAgentConfig.getRemoteAgent();
        // create remote Asset
        remoteAsset = RemoteAsset.create(remoteAgent, "Test Asset publish");
        // register Remote asset
        remoteAgent.registerAsset(remoteAsset);


    }

    @Test
    public void testSearchListingById() {

        Map<String, Object> data2 = new HashMap<>();
        data2.put("assetid", remoteAsset.getAssetID());
        Listing listing = remoteAgent.createListing(data2);
        String listingId = listing.getMetaData().get("id").toString();
        assertNotNull(remoteAgent.getListing(listingId));

    }

    @Test
    public void testSearchAllListing() {

        List<Listing> listingLst = remoteAgent.getAllListing();
        for(Listing listing: listingLst){
            String listingId = listing.getMetaData().get("id").toString();
            assertNotNull(remoteAgent.getListing(listingId));
        }

    }

}
