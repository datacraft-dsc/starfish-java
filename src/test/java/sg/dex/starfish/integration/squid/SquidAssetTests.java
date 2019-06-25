package sg.dex.starfish.integration.squid;

import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.models.Account;
import com.oceanprotocol.squid.models.Balance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.ARemoteAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.squid.SquidAgent;
import sg.dex.starfish.impl.squid.SquidAsset;
import sg.dex.starfish.impl.url.ResourceAsset;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;
import sg.dex.starfish.util.DID;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class SquidAssetTests {
    private Ocean ocean;
    private SquidAgent squidAgent;
    private SquidAsset squidAsset;

    @Before
    public void setup() {

        ocean = SquidHelperTest.getOcean();
        // create random DID
        DID did = DID.createRandom();

        // initialize squidAgent
        squidAgent = SquidAgent.create(SquidHelperTest.getPropertiesMap(), ocean, did);
    }


    @Test
    public void registerAssetTest_01() {

        byte[] data = {1, 2, 3, 4};

        MemoryAsset asset = MemoryAsset.create(data);
        assertNotNull(asset);
        // register created asset
        // any sf asset
        SquidAsset squidAsset_1 = squidAgent.registerAsset(asset);
        assertNotNull(squidAsset_1);
        assertNotNull(squidAsset_1.getAssetDID());
        assertNotNull(squidAsset_1.getOcean());
        assertEquals(squidAsset_1.isDataAsset(), true);
        assertEquals(asset.getAssetID(), squidAsset_1.getAssetID());
    }

    @Test
    public void queryDDOSquidAsset() {
        byte[] data = {1, 2, 3, 4};

        MemoryAsset asset = MemoryAsset.create(data);
        assertNotNull(asset);

        // register the Starfish asset created above into OCN
        squidAsset = squidAgent.registerAsset(asset);

        // getting the registered from squid agent using asset DID
        SquidAsset squidAsset_1 = squidAgent.getAsset(squidAsset.getAssetDID());


        // verifying the asset ddo , not it will not be null
        assertNotNull(squidAsset_1.getSquidDDO());
        // look up did , form squid agent ...

        // search asset by DDO and verify respective data
        assertEquals(squidAsset_1.getSquidDDO().metadata.base.name, "Dex_Developer");
        assertEquals(squidAsset_1.getSquidDDO().metadata.base.license, "DEX_license");
        assertTrue(squidAsset_1.getSquidDDO().metadata.base.price.equals("10"));

    }

    @Test
    public void requestTokens() throws EthereumException {

        BigInteger token = ocean.requestTokens(BigInteger.valueOf(10));
        assertEquals(BigInteger.valueOf(10), token);

    }

    //@Test
    public void purchaseAndBalanceAsset() throws EthereumException {

        String receiverAddress = "00bd138abd70e2f00903268f3db08f2d25677c9e";
        String receiverPasswd = "node0";

        Account receiverAccount = new Account(receiverAddress, receiverPasswd);


        //eg: 5.9E-17
        Balance balanceBefore = ocean.getBalance(receiverAccount);

        Assert.assertNotNull(balanceBefore);

        // getting the price from asset metadata
        //eg: 10
        BigInteger assetPrice = BigInteger.valueOf(1);

        ocean.transfer(receiverAddress, assetPrice);

        //eg:  //eg: 6.9E-17
        Balance balanceAfter = ocean.getBalance(receiverAccount);

        assertEquals(-1, balanceBefore.getOceanTokens().compareTo(balanceAfter.getOceanTokens()));


    }
    @Test
    public void registerAssetOnChain() throws IOException {


        Map<String,Object> metaMAp = new HashMap<>();
        metaMAp.put("type","dataset");

        Asset surferAsset= ResourceAsset.create("examples/metadata.json");

        System.out.println("ID is :::"+ surferAsset.getAssetID());


        RemoteAgent surfer = RemoteAgentConfig.getRemoteAgent();

        //register to surfer
        surfer.uploadAsset(surferAsset);


        // register the BlockChain
        SquidAsset squidAsset = squidAgent.registerAsset(surferAsset);

        // getting the registered from squid agent using asset DID
        SquidAsset squidAsset_FromChain = squidAgent.getAsset(squidAsset.getAssetDID());


        ARemoteAsset aRemoteAsset =surfer.getAsset(surferAsset.getAssetID());

        String content_01=new String(aRemoteAsset.getContent(), StandardCharsets.UTF_8);
//        System.out.println("*******************");
//        System.out.println(content_01);
//
//        System.out.println("*******************");
//        System.out.println("SURFER: "+aRemoteAsset.getAssetDID());
//        System.out.println("SURFER ASsetID: "+aRemoteAsset.getAssetID());
//        System.out.println("SQUID: "+squidAsset_FromChain.getAssetDID());
//        System.out.println("SQUID AssetID: "+squidAsset_FromChain.getAssetID());
//        System.out.println("SF AssetID: "+surferAsset.getAssetID());

        // verifying the asset ddo , not it will not be null
        assertNotNull(squidAsset_FromChain.getSquidDDO());
        // look up did , form squid agent ...

    }

}
