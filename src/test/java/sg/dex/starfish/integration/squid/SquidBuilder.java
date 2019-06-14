package sg.dex.starfish.integration.squid;

import com.oceanprotocol.squid.api.OceanAPI;
import com.oceanprotocol.squid.models.service.ProviderConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.squid.SquidAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SquidBuilder {

    public static Map<String, String> configToHashMap(Config config) {
        Map<String, String> options = new HashMap<>();
        for (Map.Entry<String, ConfigValue> entry : config.entrySet()) {
            String value;
            try {
                value = config.getString(entry.getKey());
            } catch (Exception e) {
                value = "<not convertable to a String>";
            }
            options.put(entry.getKey(), value);
        }
        return options;
    }

    public static SquidAgent create(Ocean ocean) throws Exception {
        SquidAgent squid = null;

        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();
        ddo.put("service", services);
        String ddoString = JSON.toPrettyString(ddo);
        Map<String, Object> squidDDO = JSON.toMap(ddoString);

        DID squidDID = DID.createRandom();
        ocean.registerLocalDID(squidDID, ddoString);

        Config config = null;
        String squidConf = "application.conf";
        String squidConfDefault = "squid.conf";
        if (Utils.resourceExists(squidConf)) {
            config = ConfigFactory.load(squidConf);
        } else {
            config = ConfigFactory.load(squidConfDefault);
        }

        // This map is an example of overidding config file defaults
        // with specfic values
        Map<String, String> cliOptions = new HashMap<String, String>();
        cliOptions.put("starfish.java.testing", "true");
        if (cliOptions.size() > 0) {
            Config cliConfig = ConfigFactory.parseMap(cliOptions);
            config = cliConfig.withFallback(config);
        }

        try {
            OceanAPI oceanAPI = OceanAPI.getInstance(config);
            assertNotNull(oceanAPI.getMainAccount());
            assertEquals(config.getString("account.main.address"), oceanAPI.getMainAccount().address);
            assertNotNull(oceanAPI.getAssetsAPI());
            assertNotNull(oceanAPI.getAccountsAPI());
            assertNotNull(oceanAPI.getSecretStoreAPI());

            squid = SquidAgent.create(configToHashMap(config),
                    ocean, squidDID, getProvideConfig(config));

            assertEquals(squidDID, squid.getDID());
            assertEquals(squidDDO, squid.getDDO());
        } catch (Exception e) {
            fail("unable to create squid oceanAPI: " + e);
        }
        return squid;
    }

    static ProviderConfig getProvideConfig(Config config) {


        String metadataUrl = config.getString("aquarius-internal.url") + "/api/v1/aquarius/assets/ddo/{did}";
        String consumeUrl = config.getString("brizo.url") + "/api/v1/brizo/services/consume";
        String purchaseEndpoint = config.getString("brizo.url") + "/api/v1/brizo/services/access/initialize";
        String secretStoreEndpoint = config.getString("secretstore.url");
        String providerAddress = config.getString("provider.address");

        return new ProviderConfig(consumeUrl, purchaseEndpoint, metadataUrl, secretStoreEndpoint, providerAddress);
    }

}
