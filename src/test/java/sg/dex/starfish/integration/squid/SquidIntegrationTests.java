package sg.dex.starfish.integration.squid;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanprotocol.squid.api.OceanAPI;
import com.oceanprotocol.squid.models.Account;
import com.oceanprotocol.squid.models.Balance;
import com.oceanprotocol.squid.models.DDO;
import com.oceanprotocol.squid.models.asset.AssetMetadata;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.squid.SquidAgent;
import sg.dex.starfish.impl.squid.SquidAsset;
import sg.dex.starfish.impl.url.RemoteHttpAsset;
import sg.dex.starfish.util.JSON;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SquidIntegrationTests {
    private static Ocean ocean; // need to check
    private static SquidAgent squidAgent = null;
    private static OceanAPI oceanAPI;
    private static Config config;

    @BeforeClass
    public static void initialize() throws Exception {

        config = ConfigFactory.load();
        oceanAPI = OceanAPI.getInstance(config);
        ocean = Ocean.connect(oceanAPI);
        squidAgent = SquidBuilder.create(ocean);

    }

    static Asset getSurferAsset() {
        Map<String, Object> additionaldataMap = new HashMap<>();
        additionaldataMap.put(Constant.DATE_CREATED, "2012-10-10T17:00:000Z");
        additionaldataMap.put(Constant.TYPE, Constant.DATA_SET);
        additionaldataMap.put(Constant.NAME, "Test Starfish Asset registration");
        additionaldataMap.put("license", "NA");
        additionaldataMap.put("author", "Test user _01 ");
        additionaldataMap.put("price", 1);

        // creating starfish Asset
        RemoteHttpAsset remoteHttpAsset = RemoteHttpAsset.create("https://oceanprotocol.com/tech-whitepaper.pdf", additionaldataMap);
        return remoteHttpAsset;
    }

    /**
     * Test to register and create asset
     */

    @Test
    public void dCreateAsset() {
        System.out.println("=== dCreateAsset ===");
        if (ocean == null) {
            System.out.println("WARNING: barge not running");
        } else {
            SquidAsset squidAsset = squidAgent.registerAsset(getSurferAsset());
            System.out.println("**** Asset registered successfully ****");
            assertNotNull(squidAsset.getAssetDID());
        }
    }

    @Test
    public void eRegisterAsset() {
        System.out.println("=== eRegisterAsset ===");
        if (ocean == null) {
            System.out.println("WARNING: barge not running");
        } else {
            SquidAsset squidAsset = squidAgent.registerAsset(getSurferAsset());
            System.out.println("**** Asset registered successfully ****");
            assertNotNull(squidAsset.getAssetDID());
            assertNotNull(ocean.getAsset(squidAsset.getAssetDID()));
        }
    }

    @Test
    public void gSearchListings() {
        System.out.println("=== gSearchListings ===");
        if (ocean == null) {
            System.out.println("WARNING: barge not running");
        } else {
//
        }
    }

    @Test
    public void hPurchaseAsset() {
        System.out.println("=== hPurchaseAsset ===");
        if (ocean == null) {
            System.out.println("WARNING: barge not running");
        } else {
        }
    }

    @Test
    public void iDownloadAsset() {
        System.out.println("=== iDownloadAsset ===");
        if (ocean == null) {
            System.out.println("WARNING: barge not running");
        } else {
        }
    }

    @Test
    public void requestTokens() throws Exception {

        BigInteger tokens = BigInteger.ONE;

        Balance balanceBefore = oceanAPI.getAccountsAPI().balance(oceanAPI.getMainAccount());

        TransactionReceipt receipt = oceanAPI.getTokensAPI().request(tokens);

        assertTrue(receipt.isStatusOK());

        Balance balanceAfter = oceanAPI.getAccountsAPI().balance(oceanAPI.getMainAccount());

        BigDecimal before = balanceBefore.getOceanTokens();
        BigDecimal after = balanceAfter.getOceanTokens();
        assertEquals(0, after.compareTo(before.add(BigDecimal.ONE)));
    }

    @Test
    public void transferAmount() throws Exception {

        String receiverAddress = config.getString("account.parity.address");
        String receiverPasswd = config.getString("account.parity.password");

        Account receiverAccount = new Account(receiverAddress, receiverPasswd);

        Balance balanceBefore = oceanAPI.getAccountsAPI().balance(receiverAccount);
        Assert.assertNotNull(balanceBefore);

        // get price from asset
        Asset asset = getSurferAsset();
        Long price = Long.valueOf(asset.getMetadata().get("price").toString());
        BigInteger assetPrice = BigInteger.valueOf(price);
        oceanAPI.getTokensAPI().transfer(receiverAddress, assetPrice);

        Balance balanceAfter = oceanAPI.getAccountsAPI().balance(receiverAccount);

        assertEquals(-1, balanceBefore.getOceanTokens().compareTo(balanceAfter.getOceanTokens()));

    }

    @Test
    public void getAssetContent() throws Exception {

        Map<String, Object> squidMetaDAta = new HashMap<>();
        squidMetaDAta.put("base", getSurferAsset().getMetadata());

        AssetMetadata metadataBase = DDO.fromJSON(new TypeReference<AssetMetadata>() {
        }, JSON.toString(squidMetaDAta));

       DDO ddo = oceanAPI.getAssetsAPI().create(metadataBase, SquidAgent.getProvideConfig());


        com.oceanprotocol.squid.models.DID squidDID = new com.oceanprotocol.squid.models.DID(ddo.id);

        //Flowable<OrderResult> response = oceanAPI.getAssetsAPI().order(squidDID,  Service.DEFAULT_ACCESS_SERVICE_ID);

//        OrderResult orderResult = response.blockingFirst();
//        Assert.assertNotNull(orderResult.getServiceAgreementId());
//        assertEquals(true, orderResult.isAccessGranted());
//       // log.debug("Granted Access Received for the service Agreement " + orderResult.getServiceAgreementId());
//
//        InputStream result = oceanAPI.getAssetsAPI().consumeBinary(
//                orderResult.getServiceAgreementId(),
//                squidDID,
//                Service.DEFAULT_ACCESS_SERVICE_ID,
//                0);

        // Assert.assertNotNull(result);

    }
}
