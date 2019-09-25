package sg.dex.starfish.impl.memory;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sg.dex.starfish.Asset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Hex;

import java.util.Arrays;

import static org.junit.Assert.*;

@SuppressWarnings("javadoc")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMemoryAgent {
    private static final byte[] BYTE_DATA = Hex.toBytes("0123456789");

    /**
     * Test DID API and Create API
     */
    @Test
    public void testAgentID() {
        DID did = DID.parse(DID.createRandomString());
        MemoryAgent ma = MemoryAgent.create(did);
        assertEquals(did, ma.getDID());
    }

    /**
     * Test register with 2 agents
     */

    @Test
    public void testRegisterUpload() {
        // it will create Memory agent instance.
        //the instance will be associated with default Ocean and will have unique DID.
        MemoryAgent agent1 = MemoryAgent.create();

        Asset a = MemoryAsset.create(BYTE_DATA);
        String id = a.getAssetID();
        
        // TODO: figure out automatic registration
        // assertNull(agent1.getAsset(id));

        // register
        Asset aReg =agent1.registerAsset(a);

        assertEquals(id,aReg.getDID().getPath());
        assertEquals(id,agent1.getAsset(id).getAssetID());

        // upload will create an Asset and then register it with agent and return the Uploaded asset ref.
        Asset a1 = agent1.uploadAsset(a);

        // verify the content of both asset must be same
        assertTrue(Arrays.equals(BYTE_DATA, a1.getContent()));

        // verify the asset id must be same
        assertEquals(a1.getAssetID(), a.getAssetID());

        // both asset much be equal
        assertEquals(a1, agent1.getAsset(id));


    }

    /**
     * Test upload with agent
     */
    @Test
    public void testUpload() {
        MemoryAgent memoryAgent = MemoryAgent.create();
        MemoryAsset asset = MemoryAsset.create(BYTE_DATA);
        Asset uploadAsset = memoryAgent.uploadAsset(asset);

        assertEquals(asset.getMetadataString(), uploadAsset.getMetadataString());
    }

    /**
     * Test register with Agent
     */
    @Test
    public void testRegister() {
        MemoryAgent agent1 = MemoryAgent.create();
        MemoryAsset asset = MemoryAsset.create(BYTE_DATA);
        String id = asset.getAssetID();
        Asset registeredAsset = agent1.registerAsset(asset);
        assertEquals(asset.getMetadataString(), registeredAsset.getMetadataString());
        assertEquals(id, registeredAsset.getAssetID());
    }

    /**
     * Test GET Asset by asset ID
     */
    @Test
    public void testGetAsset() {
        MemoryAgent agent1 = MemoryAgent.create();
        MemoryAsset asset = MemoryAsset.create(BYTE_DATA);
        String id = asset.getAssetID();
        agent1.registerAsset(asset);
        Asset assetFromAgent = agent1.getAsset(id);
        assertEquals(assetFromAgent, asset);
    }

    /**
     * Test GET Asset by asset DID
     */
    @Test
    public void testGetAssetByDID() {
        DID did = DID.createRandom();
        MemoryAgent agent1 = MemoryAgent.create(did.toString());
        Asset asset = MemoryAsset.create(BYTE_DATA);
        String id = asset.getAssetID();
        assertEquals(64, id.length());

        assertNull(agent1.getAsset(id));
        agent1.registerAsset(asset);
        Asset assetFromAgent = agent1.getAsset(id);
        assertEquals(asset.getMetadataString(), assetFromAgent.getMetadataString());
    }
    /**
     * Test GET Asset by asset DID
     */
    @Test
    public void tesWithDefaultAgent() {

        // create default memory Agent
        MemoryAgent agent1 = MemoryAgent.create();

        Asset asset = MemoryAsset.create(BYTE_DATA);

        Asset registeredAsset =agent1.registerAsset(asset);
        Asset assetFromAgent = agent1.getAsset(asset.getAssetID());
        assertEquals(registeredAsset.getMetadataString(), assetFromAgent.getMetadataString());
        assertEquals(registeredAsset.getDID(), assetFromAgent.getDID());

    }
}
