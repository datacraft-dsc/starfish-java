package developerTC;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.Purchase;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteDataAsset;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Class to test the Purchase by listing functionality
 */
@Ignore
public class PurchaseByListing_24 {

    private RemoteAgent remoteAgent;

    @Before
    public void setUp() {
        remoteAgent = AgentService.getRemoteAgent();
    }

    @Test
    public void testPurchaseAssetByListing() {
        Asset asset = MemoryAsset.createFromString("Test purchase by listing");
        RemoteDataAsset remoteAsset = remoteAgent.registerAsset(asset);

        HashMap<String, Object> data = new HashMap<>();
        data.put("assetid", remoteAsset.getAssetID());
        data.put("price", "10");
        data.put("publisher", "0x068Ed00cF0441e4829D9784fCBe7b9e26D4BD8d0");

        Listing listing = remoteAgent.createListing(data);

        Purchase purchase = listing.purchase();

        assertNotNull(purchase);
        assertEquals(purchase.status() , "delivered");
        Asset purchasedAsset = listing.getAsset();
        assertNotNull(asset);
        assertEquals(asset.getAssetID(), purchasedAsset.getAssetID());
        assertNotNull(purchasedAsset.getMetadata());
    }
}
