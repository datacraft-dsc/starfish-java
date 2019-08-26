package sg.dex.starfish.integration.squid;

import com.oceanprotocol.squid.exceptions.DDOException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.squid.SquidAgent;
import sg.dex.starfish.impl.squid.SquidAsset;
import sg.dex.starfish.util.DID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(JUnit4.class)
@Ignore
public class SquidAgentTest {


    private SquidAgent squidAgent;
    private Ocean ocean = SquidHelperTest.getOcean();
    private Map<String, String> configMap = SquidHelperTest.getPropertiesMap();

    @Before
    public void setup() {
        // create random DID
        DID did = DID.createRandom();
        squidAgent = SquidAgent.create(configMap, ocean, did);

    }


    @Test(expected = UnsupportedOperationException.class)
    public void testUploadAsset() {
        byte[] data = {1, 2, 3, 4};
        MemoryAsset asset = MemoryAsset.create(data);
        SquidAsset squidAsset = squidAgent.uploadAsset(asset);
        assertEquals(asset.getAssetID(), squidAsset.getAssetID());
    }

    @Test
    public void testGetAssetByAssetID() {
        byte[] data = {1, 2, 3, 4};
        MemoryAsset asset = MemoryAsset.create(data);
        SquidAsset squidAsset = squidAgent.registerAsset(asset);
        // getting the registered from squid agent using asset DID
        SquidAsset squidAsset_1 = squidAgent.getAsset(squidAsset.getAssetDID());
        assertEquals(asset.getAssetID(), squidAsset_1.getAssetID());

    }

    @Test
    public void testSearchAsset() throws DDOException {
        byte[] data = {1, 2, 3, 4};
        MemoryAsset asset = MemoryAsset.create(data);
        SquidAsset squidAsset = squidAgent.registerAsset(asset);
        List<SquidAsset> allSquidAsset = squidAgent.searchAsset(squidAsset.getSquidDDO().metadata.base.name);
        assertTrue(allSquidAsset.contains(squidAsset));


    }

    @Test
    public void testQueryAsset() throws Exception {

        byte[] data = {1, 2, 3, 4};
        MemoryAsset asset = MemoryAsset.create(data);

        SquidAsset squidAsset = squidAgent.registerAsset(asset);

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("license", "Test_license");
        List<SquidAsset> allSquidAsset = squidAgent.queryAsset(queryMap);
        assertTrue(allSquidAsset.contains(squidAsset));


    }


    @Test
    public void testResolveSquidDID() {

        byte[] data = {1, 2, 3, 4};
        MemoryAsset asset = MemoryAsset.create(data);

        SquidAsset squidAsset = squidAgent.registerAsset(asset);
        System.out.println("Asset ID" + squidAsset.getAssetID());
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("license", "Test_license");
        SquidAsset squidAsset1 = squidAgent.resolveSquidDID(squidAsset.getAssetDID());
        assertEquals(squidAsset.getAssetID(), squidAsset1.getAssetID());


    }
}
