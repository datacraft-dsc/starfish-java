package sg.dex.starfish.dexchain;

import org.web3j.tx.TransactionManager;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Properties;

/**
 * Factory to get instances of DexConfig
 */
public class DexConfigFactory {


    private static final String DEFAULT_KEEPER_URL = "http://localhost:8545";
    private static final BigInteger DEFAULT_KEEPER_GAS_LIMIT = BigInteger.valueOf(4712388l);
    private static final BigInteger DEFAULT_KEEPER_GAS_PRICE = BigInteger.valueOf(100000000000l);


    /**
     * Creates an DexConfig object from a set of properties
     *
     * @param fileName fileName configuration file
     * @return an DexConfig value with all the values set
     */
    public static DexConfig getDexConfig(String fileName) throws IOException {

        Properties properties = getProperties(fileName);
        DexConfig dexConfig = new DexConfig();

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

        dexConfig.setDidRegistryAddress((String) properties.getOrDefault(dexConfig.DID_REGISTRY_ADDRESS, ""));
        dexConfig.setDirectPurchaseAddress((String) properties.getOrDefault(dexConfig.DIRECT_PURCHASE_ADDRESS, ""));
        dexConfig.setTokenAddress((String) properties.getOrDefault(dexConfig.TOKEN_ADDRESS, ""));
        dexConfig.setMainAccountAddress((String) properties.getOrDefault(dexConfig.MAIN_ACCOUNT_ADDRESS, ""));
        dexConfig.setMainAccountPassword((String) properties.getOrDefault(dexConfig.MAIN_ACCOUNT_PASSWORD, ""));
        dexConfig.setParityAccountAddress((String) properties.getOrDefault(dexConfig.PARITY_ACCOUNT_ADDRESS, ""));
        dexConfig.setParityAccountPassword((String) properties.getOrDefault(dexConfig.PARITY_ACCOUNT_PASSWORD, ""));

        dexConfig.setMainAccountCredentialsFile((String) properties.getOrDefault(dexConfig.MAIN_ACCOUNT_CREDENTIALS_FILE, ""));

        return dexConfig;

    }

    private static Properties getProperties(String fileName) throws IOException {
        Properties prop = new Properties();

            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);

            if (inputStream == null) {

                inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
            } else if (inputStream == null) {
                throw new IOException("Resource file: " + fileName + "not found ");
            }

            prop.load(inputStream);


        return prop;
    }
}
