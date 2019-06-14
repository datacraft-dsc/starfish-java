package sg.dex.starfish.integration.squid;

import com.oceanprotocol.squid.api.OceanAPI;
import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.models.Account;
import com.oceanprotocol.squid.models.Balance;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.squid.SquidAgent;
import sg.dex.starfish.impl.squid.SquidAsset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SquidAssetTests {
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

    @Test
    public void createAssetTest() {
        DID did = DID.createRandom();
        String metadata = getMetaData();
        SquidAsset asset = SquidAsset.create(metadata, did, ocean);
        assertNotNull(asset);

    }

    @Test
    public void registerAssetTest() {
        // create squid asset
        DID did = DID.createRandom();
        Asset asset = SquidAsset.create(getMetaData(), did, ocean);
        assertNotNull(asset);
        // register created asset
        SquidAsset squidAsset = squidAgent.registerAsset(asset);
        assertNotNull(squidAsset);
        assertNotNull(squidAsset.getAssetDID());
        assertNotNull(squidAsset.getOcean());
        assertEquals(squidAsset.isDataAsset(), true);
    }

    @Test
    public void searchAsset() {
        // create squid asset
        DID did = DID.createRandom();
        SquidAsset asset = SquidAsset.create(getMetaData(), did, ocean);
        assertNotNull(asset);
        assertNull(asset.getDdo());
        // register squid asset to chain
        SquidAsset squidAsset = squidAgent.registerAsset(asset);
        // verifying the asset
        assertNotNull(squidAsset.getDdo());
        // search asset by DDO and verify its data
        assertEquals(squidAsset.getDdo().metadata.base.name, "Dex_Developer");
        assertEquals(squidAsset.getDdo().metadata.base.license, "DEX_license");
        assertTrue(squidAsset.getDdo().metadata.base.price.equals("10"));

    }

    @Test
    public void requestTokens() {
        try {
            int token = squidAgent.requestTokens(10);
            assertEquals(10, token);
        } catch (EthereumException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void purchaseAndBalanceAsset() {

        DID did = DID.createRandom();
        SquidAsset asset = SquidAsset.create(getMetaData(), did, ocean);
        assertNotNull(asset);
        assertNull(asset.getDdo());
        SquidAsset squidAsset = squidAgent.registerAsset(asset);

        String receiverAddress = config.getString("account.parity.address");
        String receiverPasswd = config.getString("account.parity.password");

        Account receiverAccount = new Account(receiverAddress, receiverPasswd);

        try {
            Balance balanceBefore = squidAgent.balance(receiverAccount);

            Assert.assertNotNull(balanceBefore);

            Long price = Long.valueOf(squidAsset.getMetadata().get("price").toString());
            BigInteger assetPrice = BigInteger.valueOf(price);
            ocean.getOceanAPI().getTokensAPI().transfer(receiverAddress, assetPrice);

            Balance balanceAfter = squidAgent.balance(receiverAccount);

            assertEquals(-1, balanceBefore.getOceanTokens().compareTo(balanceAfter.getOceanTokens()));
        } catch (Exception e) {

        }

    }

    private String getMetaData() {
        Map<String, Object> meta = new HashMap<>();
        meta.put(Constant.DATE_CREATED, "2012-10-10T17:00:000Z");
        meta.put(Constant.TYPE, Constant.DATA_SET);
        meta.put(Constant.NAME, "Dex_Developer");
        meta.put("license", "DEX_license");
        meta.put("author", "Dex_developer");
        meta.put("price", 10);
        return JSON.toString(meta);
    }


}
