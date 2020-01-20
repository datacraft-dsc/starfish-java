package developerTC;

import com.sun.tools.javac.comp.Resolve;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.Resolver;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.squid.DexResolver;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRemoteAgentTest_26 {


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
    public void testCreatRemoteAgent_WithCredential() throws IOException {
        DID did = DID.createRandom();

        Resolver resolver = DexResolver.create();
        resolver.registerDID(did,getDDO());

        // create remote acc with credential
        Map<String, Object> credentialMap = new HashMap<>();
        credentialMap.put("username", "Aladdin");
        credentialMap.put("password", "OpenSesame");

        RemoteAccount remoteAccount = RemoteAccount.create(Utils.createRandomHexString(32), credentialMap);
        RemoteAgent remoteAgent = RemoteAgent.connectAgent(resolver, did, remoteAccount);
        try {
            remoteAgent.getAsset("1234");
        } catch (RemoteException e) {
            Assertions.assertTrue(e.getMessage().contains("Asset ID not found "));
        }

    }

    @Test
    public void testCreatRemoteAgent_WithToken() throws IOException {
        DID did = DID.createRandom();

        Resolver resolver = DexResolver.create();
        resolver.registerDID(did,getDDO());

        // create remote acc with credential
        Map<String, Object> credentialMap = new HashMap<>();
        credentialMap.put("username", "Aladdin");
        credentialMap.put("password", "OpenSesame");

        RemoteAccount remoteAccount = RemoteAccount.create(Utils.createRandomHexString(32), credentialMap);
        RemoteAgent remoteAgent = RemoteAgent.connectAgent(resolver, did, remoteAccount);
        remoteAgent.createToken(remoteAccount);

        // getting the token
        String token = remoteAccount.getUserDataMap().get("token").toString();

        // creating remote Acc based on token
        RemoteAccount remoteAccount1 = RemoteAccount.create( token);
        // creating Agent 2
        RemoteAgent remoteAgent1 = RemoteAgent.connectAgent(resolver, did, remoteAccount1);
        try {
            remoteAgent1.getAsset("1234");
        } catch (RemoteException e) {
            assertTrue(e.getMessage().contains("Asset ID not found "));
        }

    }
    @Test
    public void testCreatRemoteAccount_WithUsernamePassword() throws IOException {

        DID did = DID.createRandom();

        Resolver resolver = DexResolver.create();
        resolver.registerDID(did,getDDO());

        RemoteAccount remoteAccount = RemoteAccount.create("Aladdin","OpenSesame");
        RemoteAgent remoteAgent = RemoteAgent.connectAgent(resolver, did, remoteAccount);
        try {
            remoteAgent.getAsset("1234");
        } catch (RemoteException e) {
            // just o validate the auth is successful
            Assertions.assertTrue(e.getMessage().contains("Asset ID not found "));
        }

    }
    @Test
    public void testCreatRemoteAccount_WithWrongUsernamePassword() throws IOException {

        DID did = DID.createRandom();

        Resolver resolver = DexResolver.create();
        resolver.registerDID(did,getDDO());

        RemoteAccount remoteAccount = RemoteAccount.create("WrongUser","WrongPassword");
        RemoteAgent remoteAgent = RemoteAgent.connectAgent(resolver, did, remoteAccount);
        try {
            remoteAgent.getAsset("1234");
        } catch (RemoteException e) {
            // just o validate the auth is successful
            Assertions.assertTrue(e.getMessage().contains("Create token failed for account"));
        }

    }

}