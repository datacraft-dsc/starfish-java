package sg.dex.starfish.samples;

import sg.dex.starfish.Ocean;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;
import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static junit.framework.TestCase.assertNotNull;

public class AuthSample {

    private static String username;
    private static String password;
    private static String surferUrl;
    private static String socketTimeout;


    static {
        Properties properties = getProperties();
        String ip = properties.getProperty("surfer.host");
        String port = properties.getProperty("surfer.port");
        surferUrl = ip + ":" + port;
        socketTimeout = properties.getProperty("socket.timeout");

        //username and password
        username = properties.getProperty("surfer.username");
        password = properties.getProperty("surfer.password");

    }

        private static RemoteAgent getAuthSurfer(String host) {
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();

        services.add(Utils.mapOf(
                "type", "Ocean.Auth.v1",
                "serviceEndpoint", host + "/api/v1/auth"));

        ddo.put("service", services);
        String ddoString = JSON.toPrettyString(ddo);

        // getting the default Ocean instance
        Ocean ocean = Ocean.connect();
        // creating unique DID
        DID surferDID = DID.createRandom();
        // registering the DID and DDO
        ocean.registerLocalDID(surferDID, ddoString);

            return RemoteAgent.create(ocean, surferDID);

    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        try {
            try (InputStream is = RemoteAgentConfig.class.getClassLoader()
                    .getResourceAsStream("application_test.properties")) {
                properties.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void main(String... args) {

        // getting the Surfer Instance
        RemoteAgent authSurfer = getAuthSurfer(surferUrl);

        //Creating remote Account
        Map<String,Object> credentialMap = new HashMap<>();
        credentialMap.put("username",username);
        credentialMap.put("password",password);

        RemoteAccount account = RemoteAccount.create(Utils.createRandomHexString(32), credentialMap);
        // registering acc to Surfer
        authSurfer =authSurfer.connect(account);
        Map<String,Object>  userDetails =authSurfer.getUserDetails();

        assertNotNull(userDetails.get("username"));
        assertNotNull(userDetails.get("status"));
        assertNotNull(userDetails.get("id"));
        assertNotNull(userDetails.get("metadata"));
        assertNotNull(userDetails.get("roles"));
        String token = account.getUserDataMap().get("token").toString();
        assertNotNull(token);

    }

}
