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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public final class SquidService {
    public Properties getProperties() {
        return properties;
    }

    private  final Properties properties;
    private final OceanConfig oceanConfig;
    private final OceanInitializationHelper oceanInitializationHelper;


    private SquidService(String fileName){
        properties = getProperties(fileName);
        oceanConfig = OceanConfigFactory.getOceanConfig(properties);
        oceanInitializationHelper = new OceanInitializationHelper(oceanConfig);

    }

    public static  SquidService create(String fileName){
        return new SquidService(fileName);

    }


    public  OceanManager getResolverManager() throws IOException, CipherException {

        OceanManager oceanManager = OceanManager.getInstance(getKeeperService(), getAquariusService());
        oceanManager.setDidRegistryContract(oceanInitializationHelper.loadDIDRegistryContract(getKeeperService()));
        return oceanManager;


    }

    public  KeeperService getKeeperService() throws IOException, CipherException {

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

    public  AquariusService getAquariusService() {
        return oceanInitializationHelper.getAquarius();

    }


    public  String getProvider() {
        return oceanConfig.getMainAccountAddress();

    }

    private   Properties getProperties(String fileName) {
        File file = getFileFromResources(fileName);
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
    // get file from classpath, resources folder
    private File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            return new File(fileName);
        } else {
            return new File(resource.getFile());
        }

    }

    public  OceanAPI getOceanAPI() {
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

    public  ProviderConfig getProvideConfig() {

        String metadataUrl = properties.get(OceanConfig.AQUARIUS_URL) + "/api/v1/aquarius/assets/ddo/{did}";
        String consumeUrl = properties.get("brizo.url") + "/api/v1/brizo/services/consume";
        String purchaseEndpoint = properties.get("brizo.url") + "/api/v1/brizo/services/access/initialize";
        String secretStoreEndpoint = properties.get(OceanConfig.SECRETSTORE_URL).toString();
        String providerAddress = properties.get(OceanConfig.PROVIDER_ADDRESS).toString();

        return new ProviderConfig(consumeUrl, purchaseEndpoint, metadataUrl, secretStoreEndpoint, providerAddress);
    }
    public  AssetsAPI getAssetAPI() {
        return getOceanAPI().getAssetsAPI();
    }
}
