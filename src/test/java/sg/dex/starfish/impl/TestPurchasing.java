package sg.dex.starfish.impl;

import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.memory.MemoryListing;
import sg.dex.starfish.impl.memory.MemoryPurchase;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class TestPurchasing {

    public static void main(String[] a) {
        byte[] data = new byte[]{1, 2, 3};
        MemoryAsset m = MemoryAsset.create(data);
        MemoryListing memoryListing = MemoryListing.create(m.getAssetID());

        MemoryPurchase memoryPurchase = MemoryPurchase.create(memoryListing.getAssetId());
        assertNotNull(memoryPurchase);
        assertNotNull(memoryPurchase.getId());
        assertNotNull(memoryPurchase.getStatus());
        assertEquals(memoryPurchase.getListingId(), memoryListing.getId());
    }
}
