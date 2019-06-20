package sg.dex.starfish.integration.squid;

import com.oceanprotocol.squid.api.OceanAPI;
import com.oceanprotocol.squid.api.config.OceanConfig;
import com.oceanprotocol.squid.exceptions.InitializationException;
import com.oceanprotocol.squid.exceptions.InvalidConfiguration;
import sg.dex.starfish.Ocean;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SquidHelperTest {

    private static Ocean ocean;
    private static OceanAPI oceanAPI;
    private static Map<String, String> propertiesMap = new HashMap<>();
    private static Properties properties = new Properties();

    static {

        initializeSquidConfi();
        initializeOcean();
        initializeConfigMap();
    }

    private static void initializeConfigMap() {
        for (final String name : properties.stringPropertyNames()) {
            propertiesMap.put(name, properties.getProperty(name));
        }
    }

    private static void initializeOcean() {
        try {
            oceanAPI = OceanAPI.getInstance(properties);
        } catch (InitializationException e) {
            e.printStackTrace();
        } catch (InvalidConfiguration invalidConfiguration) {
            invalidConfiguration.printStackTrace();
        }
        ocean = Ocean.connect(oceanAPI);
    }

    private static void initializeSquidConfi() {
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
        properties.put(OceanConfig.PROVIDER_ADDRESS, "0x00Bd138aBD70e2F00903268F3Db08f2D25677C9e");
    }


    public static Ocean getOcean() {

        return ocean;
    }

    public static Map<String, String> getPropertiesMap() {
        return propertiesMap;
    }


}
