package squid;

import com.oceanprotocol.squid.api.OceanAPI;
import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.models.Account;
import com.oceanprotocol.squid.models.Balance;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import sg.dex.starfish.Resolver;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.resource.ResourceAsset;
import sg.dex.starfish.impl.squid.SquidAgent;
import sg.dex.starfish.impl.squid.SquidAsset;
import sg.dex.starfish.impl.squid.SquidResolverImpl;
import sg.dex.starfish.impl.squid.SquidService;
import sg.dex.starfish.util.DID;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * To run the testcase you need to take latest pull of
 * https://github.com/oceanprotocol/barge
 * cd barge
 * ./start_ocean.sh
 */
@RunWith(JUnit4.class)
//@Ignore
public class SquidAssetTests {
    private SquidAgent squidAgent;
    private Resolver resolver;
    private OceanAPI oceanAPI = SquidService.getOceanAPI();

    @Before
    public void setup() {

        resolver = new SquidResolverImpl();
        // create random DID
        DID did = DID.createRandom();

        // initialize squidAgent
        squidAgent = SquidAgent.create(resolver, did);
    }



    @Test
    public void testRegistration() {
        byte[] data = {1, 2, 3, 4};

        MemoryAsset asset = MemoryAsset.create(data);

        // register the Starfish asset created above into OCN
        SquidAsset squidAsset = squidAgent.registerAsset(asset);

        // getting the registered from squid agent using asset DID
        SquidAsset squidAsset_1 = squidAgent.getAsset(squidAsset.getDID());


        assertEquals(squidAsset_1.getDID(), squidAsset.getDID());

    }

    @Test
    public void testRequestTokens() throws EthereumException {

        TransactionReceipt transactionReceipt = oceanAPI.getAccountsAPI().requestTokens(BigInteger.valueOf(10));
        assertTrue(transactionReceipt.isStatusOK());

    }

    @Test
    public void purchaseAndBalanceAsset() throws EthereumException {

        String receiverAddress = "0x064789569D09b4d40b54383d84A25A840E5D67aD";
        String receiverPasswd = "ocean_secret";
        int transferAmount =2;

        Account receiverAccount = new Account(receiverAddress, receiverPasswd);

        Balance balanceBefore = oceanAPI.getAccountsAPI().balance(receiverAccount);

        BigInteger assetPrice = BigInteger.valueOf(transferAmount);

        oceanAPI.getTokensAPI().transfer(receiverAddress, assetPrice);

        Balance balanceAfter = oceanAPI.getAccountsAPI().balance(receiverAccount);

        assertEquals(transferAmount, balanceAfter.getDrops().subtract(balanceBefore.getDrops()).intValue());


    }


//    @Test
//    public void search() throws Exception {
//
//        byte[] data = {1, 2, 3, 4};
//        Map<String,Object> metaMap = new HashMap<>();
//        metaMap.put("name","Test search Asset");
//
//        MemoryAsset asset = MemoryAsset.create(data,metaMap);
//
//        String searchText = "Test search Asset";
//        SquidAsset squidAsset =squidAgent.registerAsset(asset);
//
//        List<DDO> results = oceanAPI.getAssetsAPI().search(searchText).getResults();
//        assertNotNull(results);
//
//    }
//
//    @Test
//    public void query() throws Exception {
//
//        oceanAPI.getAssetsAPI().create(metadataBase, providerConfig);
//        log.debug("DDO registered!");
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("license", "CC-BY");
//
//        List<DDO> results = oceanAPI.getAssetsAPI().query(params).getResults();
//        assertNotNull(results);
//
//    }

//    @Test
//    public void testGetTransaction() throws URISyntaxException {
//
//        String url = SquidService.getProperties().get("submarine.url").toString();
//        String account = SquidService.getProperties().get("test.account").toString();
//
//        Map<String, Object> transactionMap = oceanAPI.getTokensAPI().getTransaction(url, account);
//
//        String actual = transactionMap.get("message").toString();
//
//        assertEquals("OK", actual);
//
//
//    }

    /**
     * This testcase if for register the meta data to Ocean Network
     * this can be verified by login to the testnet and check for the
     * registered asset
     * testnet url: https://commons.nile.dev-ocean.com/
     *
     * @throws IOException
     */
    @Test
    public void testRegisterMetadataOnNetwork() throws IOException {

        // read metadata
        String asset_metaData = new String(Files.readAllBytes(Paths.get("src/integration-test/resources/assets/test_metadata.json")));

        // create asset using metadata and given content
        // ResourceAsset memory_asset = ResourceAsset.create("assets/test_content.json", JSON.toMap(asset_metaData));
        ResourceAsset memory_asset = ResourceAsset.create(null, asset_metaData);

        // register the asset on Ocean Network
        SquidAsset squidAsset = squidAgent.registerAsset(memory_asset);


        // getting the registered from squid agent using asset DID
        SquidAsset squidAsset_FromChain = squidAgent.getAsset(squidAsset.getDID());

        assertEquals(squidAsset.getDID(), squidAsset_FromChain.getDID());

    }
}
