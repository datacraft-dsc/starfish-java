package sg.dex.starfish.impl.squid;

import com.oceanprotocol.common.web3.KeeperService;
import com.oceanprotocol.squid.api.config.OceanConfig;
import com.oceanprotocol.squid.api.config.OceanConfigFactory;
import com.oceanprotocol.squid.api.helper.OceanInitializationHelper;
import com.oceanprotocol.squid.exceptions.InitializationException;
import com.oceanprotocol.squid.exceptions.InvalidConfiguration;
import com.oceanprotocol.squid.external.AquariusService;
import com.oceanprotocol.squid.manager.OceanManager;
import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class SquidService {
    private static Properties properties;
    private static OceanConfig oceanConfig;
    private static OceanInitializationHelper oceanInitializationHelper;
    private static SquidService squidService = new SquidService();

    private SquidService() {
        properties = getProperties();
        oceanConfig = OceanConfigFactory.getOceanConfig(properties);
        oceanInitializationHelper = new OceanInitializationHelper(oceanConfig);
    }

    public static SquidService getSquidService() {
        return squidService;
    }

    public static OceanManager getResolverManager() throws IOException, CipherException, InvalidConfiguration, InitializationException {

        OceanManager oceanManager = OceanManager.getInstance(getKeeperService(properties), getAquariusService());
        oceanManager.setDidRegistryContract(oceanInitializationHelper.loadDIDRegistryContract(getKeeperService(properties)));
        return oceanManager;




    }

    public static KeeperService getKeeperService(Properties properties) throws IOException, CipherException {

        OceanConfig oceanConfig = OceanConfigFactory.getOceanConfig(properties);
        KeeperService keeper = KeeperService.getInstance(
                oceanConfig.getKeeperUrl(),
                oceanConfig.getMainAccountAddress(),
                oceanConfig.getMainAccountPassword(),
                oceanConfig.getMainAccountCredentialsFile(),
                oceanConfig.getKeeperTxAttempts(),
                oceanConfig.getKeeperTxSleepDuration()
        );

        keeper.setGasLimit(oceanConfig.getKeeperGasLimit())
                .setGasPrice(oceanConfig.getKeeperGasPrice());
        return keeper;

    }

    public static AquariusService getAquariusService() {
        return oceanInitializationHelper.getAquarius();

    }

    public static KeeperService getKeeperService() throws IOException, CipherException {
        return oceanInitializationHelper.getKeeper();

    }

    public static String getProvider() throws IOException, CipherException {
        return oceanConfig.getMainAccountAddress();

    }
    private Properties getProperties() {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {

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
