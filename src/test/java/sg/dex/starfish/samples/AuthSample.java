package sg.dex.starfish.samples;

import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;
import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;

public class AuthSample {


    public static void main(String... args) {

        // getting the Surfer Instance
        RemoteAgent surfer = RemoteAgentConfig.getRemoteAgent();
        // credential
        Map<String, Object> credentialMap = new HashMap<>();
        credentialMap.put("username", RemoteAgentConfig.getSurferUser());
        credentialMap.put("password", RemoteAgentConfig.getSurferPassword());

        // creating remote Account
        RemoteAccount surferAccount = RemoteAccount.create("9671e2c4dabf1b0ea4f4db909b9df3814ca481e3d110072e0e7d776774a68e0d",
                credentialMap);

        // registering acc to Surfer
        surfer = surfer.connect(surferAccount);


        String newToken = surfer.createToken(surferAccount);
        assertNotNull(newToken);

        // get ALL Tokens
        String firstToken = surfer.getToken();
        assertNotNull(firstToken);


        String token = surferAccount.getUserDataMap().get("token").toString();
        assertNotNull(token);

    }

}
