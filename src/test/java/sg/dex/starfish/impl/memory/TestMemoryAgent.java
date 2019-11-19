package sg.dex.starfish.impl.memory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.operations.ReverseByte_AssetI_AssetO;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Hex;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@SuppressWarnings("javadoc")
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
        Asset aReg = agent1.registerAsset(a);

        assertEquals(id, aReg.getDID().getPath());
        assertEquals(id, agent1.getAsset(id).getAssetID());

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

        Asset registeredAsset = agent1.registerAsset(asset);
        Asset assetFromAgent = agent1.getAsset(asset.getAssetID());
        assertEquals(registeredAsset.getMetadataString(), assetFromAgent.getMetadataString());
        assertEquals(registeredAsset.getDID(), assetFromAgent.getDID());

    }

    /**
     * This test is to test the Asset input Asset output Async
     */
    @Test
    public void testInvokeAsync() {
        byte[] data = new byte[]{1, 2, 3};
        DID did = DID.parse(DID.createRandomString());
        MemoryAgent memoryAgent = MemoryAgent.create(did);

        Operation memoryOperation = ReverseByte_AssetI_AssetO.create(getMetaDataForAssetI_AssetO(), memoryAgent);
        assertTrue(memoryOperation.isOperation());

        // should not have a content hash
        assertFalse(memoryOperation.getMetadata().containsKey(Constant.CONTENT_HASH));

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);


        Job job = memoryAgent.invokeAsync(memoryOperation, test);

        Map<String, Object> res = job.getResult(10000);
        Asset resultAsset = (Asset) res.get("reverse_result");
        assertArrayEquals(new byte[]{3, 2, 1}, resultAsset.getContent());
    }

    /**
     * This test is to test the Asset input Asset output Async
     */
    @Test
    public void testInvoke() {
        byte[] data = new byte[]{1, 2, 3};
        DID did = DID.parse(DID.createRandomString());
        MemoryAgent memoryAgent = MemoryAgent.create(did);

        Operation memoryOperation = ReverseByte_AssetI_AssetO.create(getMetaDataForAssetI_AssetO(), memoryAgent);
        assertTrue(memoryOperation.isOperation());

        // should not have a content hash
        assertFalse(memoryOperation.getMetadata().containsKey(Constant.CONTENT_HASH));

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);


        Job job = memoryAgent.invoke(memoryOperation, test);

        Map<String, Object> res = job.getResult(10000);
        Asset resultAsset = (Asset) res.get("reverse_result");
        assertArrayEquals(new byte[]{3, 2, 1}, resultAsset.getContent());
    }

    @Test
    public void testInvokeException() {
        byte[] data = new byte[]{1, 2, 3};
        DID did = DID.parse(DID.createRandomString());
        MemoryAgent memoryAgent = MemoryAgent.create(did);

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);


        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memoryAgent.invoke(null, test);
        });


    }

    @Test
    public void testInvokeAsyncException() {
        byte[] data = new byte[]{1, 2, 3};
        DID did = DID.parse(DID.createRandomString());
        MemoryAgent memoryAgent = MemoryAgent.create(did);


        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);

        //Job job = memoryAgent.invokeAsync(null, test);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memoryAgent.invokeAsync(null, test);
        });


    }


    private String getMetaDataForAssetI_AssetO() {
        String meta = "{\"dateCreated\":\"2019-05-07T08:17:31.521445Z\",\n" +
                "\t\"contentType\":\"application/octet-stream\",\n" +
                "\t\"tags\":[\"Reverse byte\"],\n" +
                "\t\"license\":\"CC-BY\",\n" +
                "\t\"author\":\"Reverse Byte Inc\",\n" +
                "\t\"name\":\"Reverse byte computation operation\",\n" +
                "\t\"description\":\"Reverse the give byte\",\n" +
                "\t\"inLanguage\":\"en\",\n" +
                "\t\"type\":\"operation\",\n" +
                " \"operation\":{\n" +
                "     \"modes\":[\"sync\",\"async\"],\n" +
                "\t\t\"params\":{\"input\":{\"type\":\"asset\"}},\n" +
                "\t\t\"results\":{\"output\":{\"type\":\"asset\"}}}}";
        return meta;
    }
}
