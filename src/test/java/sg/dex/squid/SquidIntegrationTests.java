package sg.dex.squid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oceanprotocol.squid.api.OceanAPI;
import com.oceanprotocol.squid.api.AssetsAPI;
import com.oceanprotocol.squid.api.AccountsAPI;
import com.oceanprotocol.squid.api.SecretStoreAPI;
import com.oceanprotocol.squid.models.Account;

public class SquidIntegrationTests {

	private static final Logger log = LogManager.getLogger(SquidIntegrationTests.class);

    @Test public void buildAPIFromConfig() throws Exception {

        Config config = ConfigFactory.load();

        OceanAPI oceanAPI = OceanAPI.getInstance(config);
        assertNotNull(oceanAPI.getMainAccount());
        assertEquals(config.getString("account.main.address"), oceanAPI.getMainAccount().address);
        assertNotNull(oceanAPI.getAssetsAPI());
        assertNotNull(oceanAPI.getAccountsAPI());
        assertNotNull(oceanAPI.getSecretStoreAPI());
    }
}
