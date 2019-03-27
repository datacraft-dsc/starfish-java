package sg.dex.starfish.impl;

import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.memory.MemoryListing;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class TestListing {

    public static void main(String a[]){
        byte[] data=new byte[] {1,2,3};
        MemoryAsset m =MemoryAsset.create(data);
        MemoryListing memoryListing =MemoryListing.create(m.getAssetID());
        assertNotNull(memoryListing);
        assertNotNull(memoryListing.getId());
        assertNotNull(memoryListing.getStatus());
        assertNotNull(memoryListing.getTrust_level());
        assertEquals(memoryListing.getAssetId(),m.getAssetID());
    }
}
