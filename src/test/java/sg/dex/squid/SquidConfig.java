package sg.dex.squid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.SquidAgent;
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

public class SquidConfig {

	static RemoteAgent getSquid(Ocean ocean) throws Exception {
		Map<String,Object> ddo=new HashMap<>();
		List<Map<String,Object>> services=new ArrayList<>();
		// services.add(Utils.mapOf(
		// 		"type","Ocean.Meta.v1",
		// 		"serviceEndpoint",host+"/api/v1/meta"));
		// services.add(Utils.mapOf(
		// 		"type","Ocean.Storage.v1",
		// 		"serviceEndpoint",host+"/api/v1/assets"));
		// services.add(Utils.mapOf(
		// 		"type","Ocean.Storage.v1",
		// 		"serviceEndpoint",host+"/api/v1/invoke"));
		ddo.put("service",services);
		String ddoString=JSON.toPrettyString(ddo);
		Map<String,Object> squidDDO=JSON.toMap(ddoString);

		DID squidDID=DID.createRandom();
		ocean.registerLocalDID(squidDID,ddoString);

		Config config = ConfigFactory.load();

		OceanAPI oceanAPI = OceanAPI.getInstance(config);
		assertNotNull(oceanAPI.getMainAccount());
		assertEquals(config.getString("account.main.address"), oceanAPI.getMainAccount().address);
		assertNotNull(oceanAPI.getAssetsAPI());
		assertNotNull(oceanAPI.getAccountsAPI());
		assertNotNull(oceanAPI.getSecretStoreAPI());

		RemoteAgent squid=SquidAgent.create(oceanAPI,ocean,squidDID);
		assertEquals(squidDID,squid.getDID());
		assertEquals(squidDDO,squid.getDDO());
		return squid;
	}

}
