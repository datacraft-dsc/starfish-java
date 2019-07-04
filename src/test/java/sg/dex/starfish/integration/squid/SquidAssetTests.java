package sg.dex.starfish.integration.squid;

import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.models.DDO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.squid.SquidAgent;
import sg.dex.starfish.impl.squid.SquidAsset;
import sg.dex.starfish.impl.url.ResourceAsset;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;
import sg.dex.starfish.util.DID;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class SquidAssetTests {
    private Ocean ocean;
    private SquidAgent squidAgent;

    @Before
    public void setup() {

        ocean = SquidHelperTest.getOcean();
        // create random DID
        DID did = DID.createRandom();

        // initialize squidAgent
        squidAgent = SquidAgent.create(SquidHelperTest.getPropertiesMap(), ocean, did);
    }


    @Test
    public void testRegisterAssetTest_01() throws Exception {

        byte[] data = {1, 2, 3, 4};
        MemoryAsset asset = MemoryAsset.create(data);
        // register created asset
        SquidAsset squidAsset_1 = squidAgent.registerAsset(asset);
        // any sf asset

        com.oceanprotocol.squid.models.DID did = squidAsset_1.getSquidDDO().getDid();

        DDO resolvedDDO = ocean.getOceanAPI().getAssetsAPI().resolve(did);

        assertEquals(did.did, resolvedDDO.id);

    }


    @Test
    public void testRequestTokens() throws EthereumException {

        BigInteger token = ocean.requestTokens(BigInteger.valueOf(10));
        assertEquals(BigInteger.valueOf(10), token);

    }

    @Test
    public void testDemo_01() throws IOException {


        Map<String, Object> metaMAp = new HashMap<>();
        metaMAp.put("type", "dataset");

        String METADATA_JSON_CONTENT = new String(Files.readAllBytes(Paths.get("src/test/resources/examples/sg_cart_mart/SJR8961K_metadata.json")));

        Asset surferAsset = ResourceAsset.create(METADATA_JSON_CONTENT,"examples/sg_cart_mart/SJR8961K_content.json");

        RemoteAgent surfer = RemoteAgentConfig.getRemoteAgent();

        //register to surfer
        surfer.uploadAsset(surferAsset);

//        // register the BlockChain
//        SquidAsset squidAsset = squidAgent.registerAsset(surferAsset);
//
//        // getting the registered from squid agent using asset DID
//        SquidAsset squidAsset_FromChain = squidAgent.getAsset(squidAsset.getAssetDID());
//
        System.out.println("Asset Content on Surfer : "+ surferAsset.getAssetID());
//        System.out.println("Asset Metatdata on Network : "+ squidAsset.getSquidDDO().getDid());
//
//        assertNotNull(squidAsset_FromChain.getSquidDDO());

    }




}
