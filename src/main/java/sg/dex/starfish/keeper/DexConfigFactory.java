package sg.dex.starfish.keeper;

import org.web3j.tx.TransactionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.util.Properties;

/**
 * Factory to get instances of DexConfig
 */
public class DexConfigFactory {


    private static final String DEFAULT_KEEPER_URL = "http://localhost:8545";
    private static final BigInteger DEFAULT_KEEPER_GAS_LIMIT = BigInteger.valueOf(4712388l);
    private static final BigInteger DEFAULT_KEEPER_GAS_PRICE = BigInteger.valueOf(100000000000l);
    private static final String DEFAULT_AQUARIUS_URL = "http://localhost:5000";
    private static final String DEFAULT_SECRET_STORE_URL = "http://localhost:12001";
    private static final String DEFAULT_CONSUME_PATH = "/tmp";


    /**
     * Creates an DexConfig object from a set of properties
     *
     * @param properties configuration values
     * @return an DexConfig value with all the values set
     */
    public static DexConfig getDexConfig(String fileName) {

        Properties properties = getProperties(fileName);
        DexConfig dexConfig = new DexConfig();

        properties.getOrDefault(DexConfig.CONSUME_BASE_PATH, DEFAULT_CONSUME_PATH);

        dexConfig.setKeeperUrl((String) properties.getOrDefault(DexConfig.KEEPER_URL, DEFAULT_KEEPER_URL));
        dexConfig.setKeeperGasLimit(new BigInteger((String) properties.getOrDefault(dexConfig.KEEPER_GAS_LIMIT, DEFAULT_KEEPER_GAS_LIMIT.toString())));
        dexConfig.setKeeperGasPrice(new BigInteger((String) properties.getOrDefault(dexConfig.KEEPER_GAS_PRICE, DEFAULT_KEEPER_GAS_PRICE.toString())));
        dexConfig.setKeeperTxAttempts(Integer.parseInt(
                (String) properties.getOrDefault(
                        DexConfig.KEEPER_TX_ATTEMPTS, String.valueOf(TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH))
        ));
        dexConfig.setKeeperTxSleepDuration(
                Long.parseLong(
                        (String) properties.getOrDefault(DexConfig.KEEPER_TX_SLEEPDURATION, String.valueOf(TransactionManager.DEFAULT_POLLING_FREQUENCY))
                ));

        dexConfig.setAquariusUrl((String) properties.getOrDefault(DexConfig.AQUARIUS_URL, DEFAULT_AQUARIUS_URL));
        dexConfig.setSecretStoreUrl((String) properties.getOrDefault(dexConfig.SECRETSTORE_URL, DEFAULT_SECRET_STORE_URL));
        dexConfig.setProviderAddress((String) properties.getOrDefault(dexConfig.PROVIDER_ADDRESS, ""));
        dexConfig.setDidRegistryAddress((String) properties.getOrDefault(dexConfig.DID_REGISTRY_ADDRESS, ""));
        dexConfig.setEscrowRewardConditionsAddress((String) properties.getOrDefault(dexConfig.ESCROWREWARD_CONDITIONS_ADDRESS, ""));
        dexConfig.setEscrowAccessSecretStoreTemplateAddress((String) properties.getOrDefault(dexConfig.ESCROW_ACCESS_SS_CONDITIONS_ADDRESS, ""));
        dexConfig.setLockrewardConditionsAddress((String) properties.getOrDefault(dexConfig.LOCKREWARD_CONDITIONS_ADDRESS, ""));
        dexConfig.setAccessSsConditionsAddress((String) properties.getOrDefault(dexConfig.ACCESS_SS_CONDITIONS_ADDRESS, ""));
        dexConfig.setAgreementStoreManagerAddress((String) properties.getOrDefault(dexConfig.AGREEMENT_STORE_MANAGER_ADDRESS, ""));
        dexConfig.setConditionStoreManagerAddress((String) properties.getOrDefault(dexConfig.CONDITION_STORE_MANAGER_ADDRESS, ""));
        dexConfig.setTokenAddress((String) properties.getOrDefault(dexConfig.TOKEN_ADDRESS, ""));
        dexConfig.setTemplateStoreManagerAddress((String) properties.getOrDefault(dexConfig.TEMPLATE_STORE_MANAGER_ADDRESS, ""));
        dexConfig.setDispenserAddress((String) properties.getOrDefault(dexConfig.DISPENSER_ADDRESS, ""));
        dexConfig.setConsumeBasePath((String) properties.getOrDefault(dexConfig.CONSUME_BASE_PATH, DEFAULT_CONSUME_PATH));
        dexConfig.setMainAccountAddress((String) properties.getOrDefault(dexConfig.MAIN_ACCOUNT_ADDRESS, ""));
        dexConfig.setMainAccountPassword((String) properties.getOrDefault(dexConfig.MAIN_ACCOUNT_PASSWORD, ""));
        dexConfig.setMainAccountCredentialsFile((String) properties.getOrDefault(dexConfig.MAIN_ACCOUNT_CREDENTIALS_FILE, ""));

        return dexConfig;

    }

    private static Properties getProperties(String fileName) {
        File file;

        URL resource = DexConfigFactory.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            file =  new File(fileName);
        } else {
            file = new File(resource.getFile());
        }

        Properties prop = new Properties();

        try (InputStream input = new FileInputStream(file.getPath())) {

            if (input == null) {
                throw new IOException("properties files is missing");
            }

            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
