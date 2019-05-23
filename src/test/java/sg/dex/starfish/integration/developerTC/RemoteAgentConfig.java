package sg.dex.starfish.integration.developerTC;

import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * This class is used to get the Remote Agent based on the host.
 * Currently it is written to connect with Surfer
 * It will connect with default OCEAN (a placeholder for real OCEAN instance)
 */
public class RemoteAgentConfig {

    private static RemoteAgent surfer;
    private static RemoteAgent invokeAgent;
    private static String surferUrl;
    private static String bargeUrl;



    private static String invokeUrl;
    private static String socketTimeout;
    private static String username;
    private static String password;


    static {
        Properties properties = getProperties();
        String ip = properties.getProperty("surfer.host");
        String port = properties.getProperty("surfer.port");
        surferUrl = ip + ":" + port;
        socketTimeout = properties.getProperty("socket.timeout");

        //username and password
        username = properties.getProperty("surfer.username");
        password = properties.getProperty("surfer.password");

        surfer = getSurfer(surferUrl);

        String ip_invoke = properties.getProperty("koi.host");
        String port_invoke = properties.getProperty("koi.port");

        invokeUrl=ip_invoke+":"+port_invoke;
        invokeAgent =getInvokeAgent(invokeUrl);
        // setting barge URL
        String barge_ip = properties.getProperty("barge.host");
        String barge_port = properties.getProperty("barge.port");
        bargeUrl = barge_ip + ":" + barge_port;



    }

    private static RemoteAgent getSurfer(String host) {
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();
        services.add(Utils.mapOf(
                "type", "Ocean.Meta.v1",
                "serviceEndpoint", host + "/api/v1/meta"));
        services.add(Utils.mapOf(
                "type", "Ocean.Storage.v1",
                "serviceEndpoint", host + "/api/v1/assets"));
        services.add(Utils.mapOf(
                "type", "Ocean.Invoke.v1",
                "serviceEndpoint", host ));
        services.add(Utils.mapOf(
                "type", "Ocean.Auth.v1",
                "serviceEndpoint", host + "/api/v1/auth"));
        services.add(Utils.mapOf(
                "type", "Ocean.Market.v1",
                "serviceEndpoint", host + "/api/v1/market"));
        ddo.put("service", services);
        String ddoString = JSON.toPrettyString(ddo);

        // getting the default Ocean instance
        Ocean ocean = Ocean.connect();
        // creating unique DID
        DID surferDID = DID.createRandom();
        // registering the DID and DDO
        ocean.registerLocalDID(surferDID, ddoString);



        //Creating remote Account
        Map<String,Object> credentialMap = new HashMap<>();
        credentialMap.put("username",username);
        credentialMap.put("password",password);

        RemoteAccount account = RemoteAccount.create(Utils.createRandomHexString(32), credentialMap);
        // creating a Remote agent instance for given Ocean and DID
        return RemoteAgent.create(ocean, surferDID,account);
    }

    public static String getSurferUrl() {
        return surferUrl;
    }
    public static String getInvokeUrl() {
        return invokeUrl;
    }
    public static String getBargeUrl() {
        return bargeUrl;
    }

//    public static String getSurferUser() {
//        return username;
//    }
//
//    public static String getSurferPassword() {
//        return password;
//    }

    public static int getSocketTimeout() {
        return Integer.parseInt(socketTimeout);
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



    private static RemoteAgent getInvokeAgent(String host) {
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();

        services.add(Utils.mapOf(
                "type", "Ocean.Invoke.v1",
                "serviceEndpoint", host ));
        services.add(Utils.mapOf(
                "type", "Ocean.Meta.v1",
                "serviceEndpoint", host + "/api/v1/meta"));
        services.add(Utils.mapOf(
                "type", "Ocean.Storage.v1",
                "serviceEndpoint", host + "/api/v1/assets"));
        services.add(Utils.mapOf(
                "type", "Ocean.Auth.v1",
                "serviceEndpoint", host + "/api/v1/auth"));
        ddo.put("service", services);
        String ddoString = JSON.toPrettyString(ddo);

        // getting the default Ocean instance
        Ocean ocean = Ocean.connect();
        // creating unique DID
        DID invokeDID = DID.createRandom();
        // registering the DID and DDO
        ocean.registerLocalDID(invokeDID, ddoString);



        //Creating remote Account
        Map<String,Object> credentialMap = new HashMap<>();
        credentialMap.put("username",username);
        credentialMap.put("password",password);

        RemoteAccount account = RemoteAccount.create(Utils.createRandomHexString(32), credentialMap);
        // creating a Remote agent instance for given Ocean and DID
        return RemoteAgent.create(ocean, invokeDID,account);
    }

    /**
     * Gets the surfer remote agent for testing purposes
     *
     * @return The RemoteAgent, or null if not up
     */
    public static RemoteAgent getRemoteAgent() {
        return surfer;

    }

    /**
     * Gets the koi remote agent for testing purposes
     *
     * @return The RemoteAgent, or null if not up
     */
    public static RemoteAgent getInvoke() {
        return invokeAgent;

    }


    public static String getDataAsStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        String result = "";

        try {

            // is = System.in;
            br = new BufferedReader(new InputStreamReader(is));

            String line = null;

            while ((line = br.readLine()) != null) {

                result = result + line;
            }

        } catch (IOException ioe) {
//            System.out.println("Exception while reading input " + ioe);
        } finally {
            // close the streams using close method
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }

        }

        return result;
    }

}
