package sg.dex.starfish.samples.squid;

import com.oceanprotocol.squid.api.AccountsAPI;
import com.oceanprotocol.squid.api.OceanAPI;
import com.oceanprotocol.squid.api.config.OceanConfig;
import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.exceptions.InitializationException;
import com.oceanprotocol.squid.exceptions.InvalidConfiguration;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.squid.SquidAccount;
import sg.dex.starfish.impl.squid.SquidAgent;
import sg.dex.starfish.impl.squid.SquidAsset;
import sg.dex.starfish.impl.url.RemoteHttpAsset;
import sg.dex.starfish.integration.squid.SquidBuilder;
import sg.dex.starfish.util.DID;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Test class for working with a Ocean Commons Marketplace asset
 *
 * @author Mike
 */
@SuppressWarnings("javadoc")
public class CommonsAssetTest {

    public static OceanAPI buildOceanAPI() throws InitializationException, InvalidConfiguration {
        Properties properties = new Properties();

        properties.put(OceanConfig.KEEPER_URL, "http://localhost:8545");
        properties.put(OceanConfig.KEEPER_URL, "http://localhost:8545");
        properties.put(OceanConfig.KEEPER_GAS_LIMIT, "4712388");
        properties.put(OceanConfig.KEEPER_GAS_PRICE, "100000000000");

        properties.put(OceanConfig.AQUARIUS_URL, "http://localhost:5000");
        properties.put(OceanConfig.SECRETSTORE_URL, "http://localhost:12001");

        properties.put(OceanConfig.CONSUME_BASE_PATH, "tmp");

        // config from squid-java
        properties.put(OceanConfig.MAIN_ACCOUNT_ADDRESS, "0x00Bd138aBD70e2F00903268F3Db08f2D25677C9e");
        properties.put(OceanConfig.MAIN_ACCOUNT_PASSWORD, "node0");
        properties.put(OceanConfig.MAIN_ACCOUNT_CREDENTIALS_FILE, "src/test/resources/accounts/parity/00bd138abd70e2f00903268f3db08f2d25677c9e.json.testaccount");
        properties.put(OceanConfig.DID_REGISTRY_ADDRESS, "0xc354ba9AD5dF1023C2640b14A09E61a500F21546");
        properties.put(OceanConfig.AGREEMENT_STORE_MANAGER_ADDRESS, "0x62f84700b1A0ea6Bfb505aDC3c0286B7944D247C");
        properties.put(OceanConfig.LOCKREWARD_CONDITIONS_ADDRESS, "0xE30FC30c678437e0e8F78C52dE9db8E2752781a0");
        properties.put(OceanConfig.ESCROWREWARD_CONDITIONS_ADDRESS, "0xeD4Ef53376C6f103d2d7029D7E702e082767C6ff");
        properties.put(OceanConfig.ESCROW_ACCESS_SS_CONDITIONS_ADDRESS, "0xfA16d26e9F4fffC6e40963B281a0bB08C31ed40C");
        properties.put(OceanConfig.ACCESS_SS_CONDITIONS_ADDRESS, "0x45DE141F8Efc355F1451a102FB6225F1EDd2921d");
        properties.put(OceanConfig.CONDITION_STORE_MANAGER_ADDRESS, "0x9768c8ae44f1dc81cAA98F48792aA5730cAd2F73");
        properties.put(OceanConfig.TEMPLATE_STORE_MANAGER_ADDRESS, "0x9768c8ae44f1dc81cAA98F48792aA5730cAd2F73");
        properties.put(OceanConfig.TOKEN_ADDRESS, "0x9861Da395d7da984D5E8C712c2EDE44b41F777Ad");
        properties.put(OceanConfig.DISPENSER_ADDRESS, "0x865396b7ddc58C693db7FCAD1168E3BD95Fe3368");
//		properties.put(OceanConfig.PROVIDER_ADDRESS, "0x413c9ba0a05b8a600899b41b0c62dd661e689354");
        properties.put(OceanConfig.PROVIDER_ADDRESS, "0x00Bd138aBD70e2F00903268F3Db08f2D25677C9e");

        OceanAPI oceanAPIFromProperties = OceanAPI.getInstance(properties);
        return oceanAPIFromProperties;
    }


    static Asset getSurferAsset() {
        Map<String, Object> additionaldataMap = new HashMap<>();
        additionaldataMap.put(Constant.DATE_CREATED, "2012-10-10T17:00:000Z");
        additionaldataMap.put(Constant.TYPE, Constant.DATA_SET);
        additionaldataMap.put(Constant.NAME, "Test Starfish Asset registration");
        additionaldataMap.put("license", "NA");
        additionaldataMap.put("author", "Test user _01 ");
        additionaldataMap.put("price", 10);


        // creating starfish Asset
        RemoteHttpAsset remoteHttpAsset = RemoteHttpAsset.create("https://oceanprotocol.com/tech-whitepaper.pdf", additionaldataMap);
        return remoteHttpAsset;
    }

    static SquidAsset testSquidAgent(OceanAPI oceanAPI) throws Exception {
        // crete Ocean
        Ocean ocean = Ocean.connect(oceanAPI);

        // create Squid Agent
        SquidAgent squidAgent = SquidBuilder.create(ocean);

        // register Asset with Squid agent
        SquidAsset squidAsset = squidAgent.registerAsset(getSurferAsset());
        System.out.println("**** Asset registered successfully ****");
        System.out.println(squidAsset.getAssetDID());
        return squidAsset;


    }

    public static void main(String... args) throws Exception {
        OceanAPI oceanAPI = buildOceanAPI();
        Ocean ocean = Ocean.connect(oceanAPI);
        SquidAsset squidAsset = testSquidAgent(oceanAPI);

        verifyAssetDID(oceanAPI, ocean, squidAsset.getAssetDID());
//

    }

    private static void verifyAssetDID(OceanAPI oceanAPI, Ocean ocean, DID didSquid) throws EthereumException {
        DID did = DID.parse(didSquid.toString());
        System.out.println(did);

        Asset a = ocean.getAsset(did);

        AccountsAPI accountsAPI = oceanAPI.getAccountsAPI();
        SquidAccount account = SquidAccount.create(ocean, oceanAPI.getMainAccount());

        accountsAPI.requestTokens(BigInteger.valueOf(Long.MAX_VALUE));

        // accountsAPI..balance(account);
        System.out.println(account.getID());
        System.out.println(account.getOceanBalance());
        System.out.println(account.getEthBalance());
    }

    /**
     * Method to create Ocean API with live network configuration
     *
     * @return OcenaAPI
     * @throws
     * @throws
     */
    public static OceanAPI buildOceanAPILive() throws InvalidConfiguration, InitializationException {
        Properties properties = new Properties();

        // config from https://github.com/oceanprotocol/commons/blob/master/client/src/config.ts
        properties.put(OceanConfig.KEEPER_URL, "https://nile.dev-ocean.com");
        properties.put(OceanConfig.KEEPER_GAS_LIMIT, "4712388");
        properties.put(OceanConfig.KEEPER_GAS_PRICE, "100000000000");

        properties.put(OceanConfig.AQUARIUS_URL, "https://nginx-aquarius.dev-ocean.com");
        properties.put(OceanConfig.SECRETSTORE_URL, "https://secret-store.dev-ocean.com");

        properties.put(OceanConfig.CONSUME_BASE_PATH, "tmp");

        // config from squid-java
        properties.put(OceanConfig.MAIN_ACCOUNT_ADDRESS, "0x00Bd138aBD70e2F00903268F3Db08f2D25677C9e");
        properties.put(OceanConfig.MAIN_ACCOUNT_PASSWORD, "node0");
        properties.put(OceanConfig.MAIN_ACCOUNT_CREDENTIALS_FILE, "src/test/resources/accounts/parity/00bd138abd70e2f00903268f3db08f2d25677c9e.json.testaccount");
        properties.put(OceanConfig.DID_REGISTRY_ADDRESS, "0x4A0f7F763B1A7937aED21D63b2A78adc89c5Db23");
        properties.put(OceanConfig.AGREEMENT_STORE_MANAGER_ADDRESS, "0x62f84700b1A0ea6Bfb505aDC3c0286B7944D247C");
        properties.put(OceanConfig.LOCKREWARD_CONDITIONS_ADDRESS, "0xE30FC30c678437e0e8F78C52dE9db8E2752781a0");
        properties.put(OceanConfig.ESCROWREWARD_CONDITIONS_ADDRESS, "0xeD4Ef53376C6f103d2d7029D7E702e082767C6ff");
        properties.put(OceanConfig.ESCROW_ACCESS_SS_CONDITIONS_ADDRESS, "0xfA16d26e9F4fffC6e40963B281a0bB08C31ed40C");
        properties.put(OceanConfig.ACCESS_SS_CONDITIONS_ADDRESS, "0x45DE141F8Efc355F1451a102FB6225F1EDd2921d");
        properties.put(OceanConfig.CONDITION_STORE_MANAGER_ADDRESS, "0x9768c8ae44f1dc81cAA98F48792aA5730cAd2F73");
        properties.put(OceanConfig.TEMPLATE_STORE_MANAGER_ADDRESS, "0x9768c8ae44f1dc81cAA98F48792aA5730cAd2F73");
        properties.put(OceanConfig.TOKEN_ADDRESS, "0x9861Da395d7da984D5E8C712c2EDE44b41F777Ad");
        properties.put(OceanConfig.DISPENSER_ADDRESS, "0x865396b7ddc58C693db7FCAD1168E3BD95Fe3368");
        properties.put(OceanConfig.PROVIDER_ADDRESS, "0x413c9ba0a05b8a600899b41b0c62dd661e689354");

        OceanAPI oceanAPIFromProperties = OceanAPI.getInstance(properties);
        return oceanAPIFromProperties;

    }
}
