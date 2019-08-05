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

import static junit.framework.TestCase.assertNotNull;

@RunWith(JUnit4.class)
//@Ignore
public class SquidAgentTest {


    private SquidAgent squidAgent;
    private Ocean ocean = SquidHelperTest.getOcean();
    private Map<String, String> configMap = SquidHelperTest.getPropertiesMap();

    @Before
    public void setup() {
        // create random DID
        DID did = DID.createRandom();
        squidAgent = SquidAgent.create(configMap, ocean, did);

        byte[] data = {1, 2, 3, 4};

        MemoryAsset asset = MemoryAsset.create(data);
        assertNotNull(asset);
    }


    @Test(expected = UnsupportedOperationException.class)
    public void testUploadAsset() {
        byte[] data = {1, 2, 3, 4};
        MemoryAsset asset = MemoryAsset.create(data);
        squidAgent.uploadAsset(asset);
    }

    @Test
    public void testGetAssetByAssetID() {
        byte[] data = {1, 2, 3, 4};
        MemoryAsset asset = MemoryAsset.create(data);
        SquidAsset squidAsset = squidAgent.registerAsset(asset);
        // getting the registered from squid agent using asset DID
        SquidAsset squidAsset_1 = squidAgent.getAsset(squidAsset.getAssetDID());

        // verifying the asset ddo , not it will not be null
        assertNotNull(squidAsset_1.getSquidDDO());
    }

    @Test
    public void testSearchAsset() throws DDOException {
        byte[] data = {1, 2, 3, 4};
        MemoryAsset asset = MemoryAsset.create(data);
        SquidAsset squidAsset = squidAgent.registerAsset(asset);
        List<SquidAsset> allSquidAsset = squidAgent.searchAsset(squidAsset.getSquidDDO().metadata.base.name);
        assertNotNull(allSquidAsset);


    }

    @Test
    public void testQueryAsset() throws Exception {

        byte[] data = {1, 2, 3, 4};
        MemoryAsset asset = MemoryAsset.create(data);

        SquidAsset squidAsset = squidAgent.registerAsset(asset);

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("license", "Test_license");
        List<SquidAsset> allSquidAsset = squidAgent.queryAsset(queryMap);
        assertNotNull(allSquidAsset);


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
        assertNotNull(squidAsset1);


    }
}
