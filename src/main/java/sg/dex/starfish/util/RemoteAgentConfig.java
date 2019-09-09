package sg.dex.starfish.util;

import sg.dex.starfish.Ocean;
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

    public static RemoteAgent getRemoteAgent(String ddoString,DID did,String userName,String password){
        // getting the default Ocean instance
        Ocean ocean = Ocean.connect();
        // creating unique DID

        did = (did== null) ? DID.createRandom():did;

        // registering the DID and DDO
        ocean.installLocalDDO(did, ddoString);

        //Creating remote Account
        Map<String,Object> credentialMap = new HashMap<>();
        credentialMap.put("username",userName);
        credentialMap.put("password",password);

        RemoteAccount account = RemoteAccount.create(Utils.createRandomHexString(32), credentialMap);
        // creating a Remote agent instance for given Ocean and DID
        return RemoteAgent.create(ocean, did,account);
    }

}
