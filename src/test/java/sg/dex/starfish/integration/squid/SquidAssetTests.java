package sg.dex.starfish.integration.squid;

import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.models.Account;
import com.oceanprotocol.squid.models.Balance;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.ARemoteAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.squid.SquidAgent;
import sg.dex.starfish.impl.squid.SquidAsset;
import sg.dex.starfish.impl.url.ResourceAsset;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * To run the testcase you need to take latest pull of
 * https://github.com/oceanprotocol/barge
 * cd barge
 * ./start_ocean.sh
 */
@RunWith(JUnit4.class)
@Ignore
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
    public void testCreateSquidAsset()  {

        byte[] data = {1, 2, 3, 4};

        MemoryAsset asset = MemoryAsset.create(data);
        // register created asset
        // any sf asset
        SquidAsset squidAsset_1 = squidAgent.registerAsset(asset);
        assertEquals(asset.getAssetID(),squidAsset_1.getAssetID());



    }

    @Test
    public void testQueryDDOSquidAsset() {
        byte[] data = {1, 2, 3, 4};

        MemoryAsset asset = MemoryAsset.create(data);
        Assume.assumeNotNull(asset);

        // register the Starfish asset created above into OCN
        squidAsset = squidAgent.registerAsset(asset);

        // getting the registered from squid agent using asset DID
        SquidAsset squidAsset_1 = squidAgent.getAsset(squidAsset.getAssetDID());


        assertEquals(squidAsset.getAssetID(), squidAsset_1.getAssetID());

    }

    @Test
    public void testRequestTokens() throws EthereumException {

        BigInteger token = ocean.requestTokens(BigInteger.valueOf(10));
        assertEquals(BigInteger.valueOf(10), token);

    }

    @Test
    public void purchaseAndBalanceAsset() throws EthereumException {

        String receiverAddress = "0x064789569D09b4d40b54383d84A25A840E5D67aD";
        String receiverPasswd = "ocean_secret";

        Account receiverAccount = new Account(receiverAddress, receiverPasswd);


        //eg: 5.9E-17
        Balance balanceBefore = ocean.getBalance(receiverAccount);


        // getting the price from asset metadata
        //eg: 10
        BigInteger assetPrice = BigInteger.valueOf(1);

        ocean.transfer(receiverAddress, assetPrice);

        //eg:  //eg: 6.9E-17
        Balance balanceAfter = ocean.getBalance(receiverAccount);

        assertEquals(1, balanceBefore.getEth().compareTo(balanceAfter.getEth()));


    }
    @Test
    public void testRegisterOnSurferAndChain() throws IOException, EthereumException {

        // read metadata
        String asset_metaData = new String(Files.readAllBytes(Paths.get("src/test/resources/assets/test_metadata.json")));

        // create asset using metadata and given content
        ResourceAsset memory_asset= ResourceAsset.create("assets/test_content.json", JSON.toMap(asset_metaData));

        // create surfer agent
        RemoteAgent surfer = RemoteAgentConfig.getRemoteAgent();

        //register and upload the asset to surfer
        surfer.uploadAsset(memory_asset);

        // register the asset on Ocean Network
        SquidAsset squidAsset = squidAgent.registerAsset(memory_asset);


        // getting the registered from squid agent using asset DID
        SquidAsset squidAsset_FromChain = squidAgent.getAsset(squidAsset.getAssetDID());


        // verifying registration
        ARemoteAsset aRemoteAsset =surfer.getAsset(memory_asset.getAssetID());


       // validating name
        assertEquals(squidAsset_FromChain.getSquidDDO().metadata.base.name,memory_asset.getMetadata().get("name"));
        assertEquals(squidAsset_FromChain.getSquidDDO().metadata.base.name,aRemoteAsset.getMetadata().get("name"));

        // validating author
        assertEquals(squidAsset_FromChain.getSquidDDO().metadata.base.author,memory_asset.getMetadata().get("author"));
        assertEquals(squidAsset_FromChain.getSquidDDO().metadata.base.author,aRemoteAsset.getMetadata().get("author"));


        // validating content
        assertEquals(aRemoteAsset.getContent().length ,memory_asset.getContent().length);


        //*****Consume Asset ************

        // get the consumer account details
//        String receiverAddress = SquidHelperTest.getPropertiesMap().get("receiver.address");
//        String receiverPasswd = SquidHelperTest.getPropertiesMap().get("receiver.password");
//
//        Account consumerAccount = new Account(receiverAddress, receiverPasswd);
//
//        Balance balanceBefore = ocean.getBalance(consumerAccount);
//
//        // get price of asset
//        String price =squidAsset_FromChain.getSquidDDO().metadata.base.price ;
//        BigInteger priceOfAsset=new BigInteger(price);
//
//        // validate price
//        if(-1== priceOfAsset.compareTo(balanceBefore.getEth())){
//            ocean.transfer(receiverAddress, priceOfAsset);
//            String content=Utils.stringFromStream(aRemoteAsset.getContentStream());
//            assertNotNull(content);
//
//        }

    }


//    @Test
//    public void query() throws Exception {
//
//        byte[] data = {1, 2, 3, 4};
//
//        MemoryAsset asset = MemoryAsset.create(data);
//        assertNotNull(asset);
//
//        // register the Starfish asset created above into OCN
//        squidAsset = squidAgent.registerAsset(asset);
//
//
//        Map<String, Object> params = new HashMap<>();
//        // this is the default value for license
//        params.put("type", "dataset");
//
//        List<DDO> results = ocean.getAssetsAPI().query(params).getResults();
//        assertTrue(results.contains(squidAsset.getSquidDDO()));
//
//    }

    @Test
    public void testGetTransaction() throws URISyntaxException {

        String url = SquidHelperTest.getPropertiesMap().get("submarine.url");
        String account = SquidHelperTest.getPropertiesMap().get("test.account");

        Map<String,Object> transactionMap =ocean.getTransaction(url,account);

        String actual=transactionMap.get("message").toString();

        assertEquals("OK",actual);


    }


}
