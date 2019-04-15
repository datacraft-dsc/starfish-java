package sg.dex.starfish.developer_usecase;

import org.junit.Before;
import org.junit.Test;
import sg.dex.starfish.Listing;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

/**
 * As a developer working with an Ocean marketplace,
 * I need a way to unpublish my asset (i.e. remove relevant listings) from a marketplace
 */
public class UnPublishListing_12 {
    RemoteAsset remoteAsset;
    RemoteAgent remoteAgent;

    @Before
    public void setUp() {
        // create remote Agent
        remoteAgent = RemoteAgentConfig.getRemoteAgent();
        // create remote Asset
        remoteAsset = RemoteAsset.create(remoteAgent, "Test Asset publish");


    }

    @Test
    public void testPublishAsset() {
    	if (remoteAgent==null) return;

    	// register Remote asset
        remoteAgent.registerAsset(remoteAsset);
        Map<String, Object> data2 = new HashMap<>();
        //data.put( "status", "unpublished");
        data2.put("assetid", remoteAsset.getAssetID());
        Listing listing = remoteAgent.createListing(data2);
        assertEquals(listing.getMetaData().get("status"), "unpublished");
        data2.put("id", listing.getMetaData().get("id"));
        data2.put("status", "published");
        remoteAgent.updateListing(data2);
        assertEquals(listing.getMetaData().get("status"), "published");
        data2.put("id", listing.getMetaData().get("id"));
        data2.put("status", "unpublished");
        remoteAgent.updateListing(data2);

        assertEquals(listing.getMetaData().get("status"), "unpublished");

    }


}
