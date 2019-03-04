package sg.dex.squid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.squid.SquidAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import com.oceanprotocol.squid.api.OceanAPI;
import com.oceanprotocol.squid.api.AssetsAPI;
import com.oceanprotocol.squid.api.AccountsAPI;
import com.oceanprotocol.squid.api.SecretStoreAPI;
import com.oceanprotocol.squid.models.Account;

public class SquidBuilder {

	static SquidAgent create(Ocean ocean) throws Exception {
		Map<String,Object> ddo=new HashMap<>();
		List<Map<String,Object>> services=new ArrayList<>();
		ddo.put("service",services);
		String ddoString=JSON.toPrettyString(ddo);
		Map<String,Object> squidDDO=JSON.toMap(ddoString);

		DID squidDID=DID.createRandom();
		ocean.registerLocalDID(squidDID,ddoString);

		// TODO: try/catch
		// TODO: static default config
		// TODO: map to override
		Config config = ConfigFactory.load();

		OceanAPI oceanAPI = OceanAPI.getInstance(config);
		assertNotNull(oceanAPI.getMainAccount());
		assertEquals(config.getString("account.main.address"), oceanAPI.getMainAccount().address);
		assertNotNull(oceanAPI.getAssetsAPI());
		assertNotNull(oceanAPI.getAccountsAPI());
		assertNotNull(oceanAPI.getSecretStoreAPI());

		SquidAgent squid=SquidAgent.create(oceanAPI,ocean,squidDID);
		assertEquals(squidDID,squid.getDID());
		assertEquals(squidDDO,squid.getDDO());
		return squid;
	}

}
