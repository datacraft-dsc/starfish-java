package sg.dex.starfish.impl.squid;

import com.oceanprotocol.common.web3.KeeperService;
import com.oceanprotocol.squid.api.AssetsAPI;
import com.oceanprotocol.squid.api.OceanAPI;
import com.oceanprotocol.squid.api.config.OceanConfig;
import com.oceanprotocol.squid.api.config.OceanConfigFactory;
import com.oceanprotocol.squid.api.helper.OceanInitializationHelper;
import com.oceanprotocol.squid.exceptions.InitializationException;
import com.oceanprotocol.squid.exceptions.InvalidConfiguration;
import com.oceanprotocol.squid.external.AquariusService;
import com.oceanprotocol.squid.manager.OceanManager;
import com.oceanprotocol.squid.models.service.ProviderConfig;
import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class SquidService {
    private static Properties properties;
    private static OceanConfig oceanConfig;
    private static OceanInitializationHelper oceanInitializationHelper;

    static {
        properties = getProperties();
        oceanConfig = OceanConfigFactory.getOceanConfig(properties);
        oceanInitializationHelper = new OceanInitializationHelper(oceanConfig);
    }


    public static OceanManager getResolverManager() throws IOException, CipherException {

        OceanManager oceanManager = OceanManager.getInstance(getKeeperService(properties), getAquariusService());
        oceanManager.setDidRegistryContract(oceanInitializationHelper.loadDIDRegistryContract(getKeeperService(properties)));
        return oceanManager;


    }

    public static KeeperService getKeeperService(OceanConfig oceanConfig) throws IOException, CipherException {

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

    public static KeeperService getKeeperService(Properties properties) throws IOException, CipherException {

        OceanConfig oceanConfig = OceanConfigFactory.getOceanConfig(properties);
        return getKeeperService(oceanConfig);

    }

    public static AquariusService getAquariusService() {
        return oceanInitializationHelper.getAquarius();

    }

    private static KeeperService getKeeperService() throws IOException, CipherException {
        return oceanInitializationHelper.getKeeper();

    }

    public static String getProvider() {
        return oceanConfig.getMainAccountAddress();

    }

    public static Properties getProperties() {
        Properties prop = new Properties();
        try (InputStream input = SquidService.class.getClassLoader().getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new IOException("properties files is missing");
            }

            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;

    }

    public static OceanAPI getOceanAPI() {
        OceanAPI oceanAPI = null;
        try {
            oceanAPI = OceanAPI.getInstance(properties);
        } catch (InitializationException e) {
            e.printStackTrace();
        } catch (InvalidConfiguration invalidConfiguration) {
            invalidConfiguration.printStackTrace();
        }
        return oceanAPI;
        // ocean = Ocean.connect(oceanAPI);
    }

    public static ProviderConfig getProvideConfig() {

        String metadataUrl = properties.get(OceanConfig.AQUARIUS_URL) + "/api/v1/aquarius/assets/ddo/{did}";
        String consumeUrl = properties.get("brizo.url") + "/api/v1/brizo/services/consume";
        String purchaseEndpoint = properties.get("brizo.url") + "/api/v1/brizo/services/access/initialize";
        String secretStoreEndpoint = properties.get(OceanConfig.SECRETSTORE_URL).toString();
        String providerAddress = properties.get(OceanConfig.PROVIDER_ADDRESS).toString();

        return new ProviderConfig(consumeUrl, purchaseEndpoint, metadataUrl, secretStoreEndpoint, providerAddress);
    }
    public static AssetsAPI getAssetAPI() {
        return getOceanAPI().getAssetsAPI();
    }
}
