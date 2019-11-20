package developerTC;

import org.junit.jupiter.api.Test;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.RemoteAgentConfig;
import sg.dex.starfish.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RemoteAgentTest_26 {


    private static String getDDO() {
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();
        services.add(Utils.mapOf(
                "type", "Ocean.Meta.v1",
                "serviceEndpoint", AgentService.getSurferUrl() + "/api/v1/meta"));
        services.add(Utils.mapOf(
                "type", "Ocean.Auth.v1",
                "serviceEndpoint", AgentService.getSurferUrl() + "/api/v1/auth"));
        ddo.put("service", services);
        String ddoString = JSON.toPrettyString(ddo);

        return ddoString;

    }

    @Test
    public void testCreatRemoteAgent_WithCredential() {
        DID did = DID.createRandom();


        // create remote acc with credential
        Map<String, Object> credentialMap = new HashMap<>();
        credentialMap.put("username", "Aladdin");
        credentialMap.put("password", "OpenSesame");

        RemoteAccount remoteAccount = RemoteAccount.create(Utils.createRandomHexString(32), credentialMap);
        RemoteAgent remoteAgent = RemoteAgentConfig.getRemoteAgent(getDDO(), did, remoteAccount);
        try {
            remoteAgent.getAsset("1234");
        } catch (RemoteException e) {
            assertTrue(e.getMessage().contains("Asset ID not found "));
        }

    }

    @Test
    public void testCreatRemoteAgent_WithToken() {
        DID did = DID.createRandom();


        // create remote acc with credential
        Map<String, Object> credentialMap = new HashMap<>();
        credentialMap.put("username", "Aladdin");
        credentialMap.put("password", "OpenSesame");

        RemoteAccount remoteAccount = RemoteAccount.create(Utils.createRandomHexString(32), credentialMap);
        RemoteAgent remoteAgent = RemoteAgentConfig.getRemoteAgent(getDDO(), did, remoteAccount);
        remoteAgent.createToken(remoteAccount);

        // getting the token
        String token = remoteAccount.getUserDataMap().get("token").toString();

        // creating remote Acc based on token
        RemoteAccount remoteAccount1 = RemoteAccount.create(Utils.createRandomHexString(32), token);
        // creating Agent 2
        RemoteAgent remoteAgent1 = RemoteAgentConfig.getRemoteAgent(getDDO(), did, remoteAccount1);
        try {
            remoteAgent1.getAsset("1234");
        } catch (RemoteException e) {
            assertTrue(e.getMessage().contains("Asset ID not found "));
        }

    }


}
