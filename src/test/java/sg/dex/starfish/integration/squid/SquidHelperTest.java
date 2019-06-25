package sg.dex.starfish.integration.squid;

import com.oceanprotocol.squid.api.OceanAPI;
import com.oceanprotocol.squid.api.config.OceanConfig;
import com.oceanprotocol.squid.exceptions.InitializationException;
import com.oceanprotocol.squid.exceptions.InvalidConfiguration;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SquidHelperTest {

    private static Ocean ocean;
    private static OceanAPI oceanAPI;
    private static Map<String, String> propertiesMap = new HashMap<>();
    private static Properties properties = new Properties();
    private static Properties properties_01 = getProperties();

    static {

        initializeSquidConfig();
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



    private static void initializeSquidConfig() {
        properties.put(OceanConfig.KEEPER_URL, properties_01.getProperty("keeper.url"));
//        properties.put(OceanConfig.KEEPER_URL, "http://localhost:8545");
        properties.put(OceanConfig.KEEPER_GAS_LIMIT, properties_01.getProperty("keeper.gasLimit"));
        properties.put(OceanConfig.KEEPER_GAS_PRICE, properties_01.getProperty("keeper.gasPrice"));

        properties.put(OceanConfig.AQUARIUS_URL, properties_01.getProperty("aquarius.url"));
        properties.put(OceanConfig.SECRETSTORE_URL, properties_01.getProperty("secretstore.url"));

        properties.put(OceanConfig.CONSUME_BASE_PATH, properties_01.getProperty("consume.basePath"));

        // config from squid-java
        properties.put(OceanConfig.MAIN_ACCOUNT_ADDRESS, properties_01.getProperty("account.main.address"));
        properties.put(OceanConfig.MAIN_ACCOUNT_PASSWORD, properties_01.getProperty("account.main.password"));
        properties.put(OceanConfig.MAIN_ACCOUNT_CREDENTIALS_FILE, properties_01.getProperty("account.main.credentialsFile"));
        properties.put(OceanConfig.DID_REGISTRY_ADDRESS, properties_01.getProperty("contract.DIDRegistry.address"));
        properties.put(OceanConfig.AGREEMENT_STORE_MANAGER_ADDRESS, properties_01.getProperty("contract.AgreementStoreManager.address"));
        properties.put(OceanConfig.LOCKREWARD_CONDITIONS_ADDRESS, properties_01.getProperty("contract.LockRewardCondition.address"));
        properties.put(OceanConfig.ESCROWREWARD_CONDITIONS_ADDRESS, properties_01.getProperty("contract.EscrowReward.address"));
        properties.put(OceanConfig.ESCROW_ACCESS_SS_CONDITIONS_ADDRESS, properties_01.getProperty("contract.EscrowAccessSecretStoreTemplate.address"));
        properties.put(OceanConfig.ACCESS_SS_CONDITIONS_ADDRESS, properties_01.getProperty("contract.AccessSecretStoreCondition.address"));
        properties.put(OceanConfig.CONDITION_STORE_MANAGER_ADDRESS, properties_01.getProperty("contract.ConditionStoreManager.address"));
        properties.put(OceanConfig.TEMPLATE_STORE_MANAGER_ADDRESS, properties_01.getProperty("contract.TemplateStoreManager.address"));
        properties.put(OceanConfig.TOKEN_ADDRESS, properties_01.getProperty("contract.OceanToken.address"));
        properties.put(OceanConfig.DISPENSER_ADDRESS, properties_01.getProperty("contract.Dispenser.address"));
        properties.put(OceanConfig.PROVIDER_ADDRESS, properties_01.getProperty("provider.address"));
    }


    public static Ocean getOcean() {

        return ocean;
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        try {
            try (InputStream is = RemoteAgentConfig.class.getClassLoader()
                    .getResourceAsStream("application_test.properties")) {
                properties.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static Map<String, String> getPropertiesMap() {
        return propertiesMap;
    }


}
