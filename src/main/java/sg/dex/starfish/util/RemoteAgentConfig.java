package sg.dex.starfish.util;

import sg.dex.starfish.Resolver;
import sg.dex.starfish.impl.memory.LocalResolverImpl;
import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to get the Remote Agent based on the host.
 * Currently it is written to connect with Surfer
 * It will connect with default OCEAN (a placeholder for real OCEAN instance)
 */
public final class RemoteAgentConfig {

    public static RemoteAgent getRemoteAgent(String ddoString, DID did, String userName, String password) {
        // creating unique DID

        did = (did == null) ? DID.createRandom() : did;

        Resolver resolver=new LocalResolverImpl();
        // registering the DID and DDO
        resolver.registerDID(did, ddoString);

        //Creating remote Account
        Map<String, Object> credentialMap = new HashMap<>();
        credentialMap.put("username", userName);
        credentialMap.put("password", password);

        RemoteAccount account = RemoteAccount.create(Utils.createRandomHexString(32), credentialMap);
        // creating a Remote agent instance for given Ocean and DID
        return RemoteAgent.create(resolver, did, account);
    }
    
    public static RemoteAgent getRemoteAgent(String ddoString, DID did, RemoteAccount remoteAccount) {
        // creating unique DID

        did = (did == null) ? DID.createRandom() : did;

        Resolver resolver=new LocalResolverImpl();
        // registering the DID and DDO
        resolver.registerDID(did, ddoString);

        // creating a Remote agent instance for given Ocean and DID
        return RemoteAgent.create(resolver, did, remoteAccount);
    }

    public static RemoteAgent getRemoteAgent(DID did, Resolver resolver, RemoteAccount remoteAccount) {
        // creating a Remote agent instance for given Ocean and DID
        return RemoteAgent.create(resolver, did, remoteAccount);
    }

}
