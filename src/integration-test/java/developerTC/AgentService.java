package developerTC;

import org.junit.platform.commons.util.StringUtils;
import sg.dex.starfish.Resolver;
import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.squid.DexResolver;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.*;

/**
 * This class is used to get the Remote Agent based on the host.
 * Currently it is written to connect with Surfer
 * It will connect with default OCEAN (a placeholder for real OCEAN instance)
 */
public class AgentService {

    private static RemoteAgent surfer;
    private static RemoteAgent invokeAgent;

    private static String surferUrl;

    private static String invokeUrl;

    private static String socketTimeout;
    private static String username;
    private static String password;



    private static RemoteAccount remoteAccount;


    static {
        Properties properties = getProperties();
        String ip = properties.getProperty("surfer.host");
        String port = properties.getProperty("surfer.port");
        surferUrl = (StringUtils.isBlank(port)) ?ip : (ip + ":" + port) ;
        socketTimeout = properties.getProperty("socket.timeout");

        //username and password
        username = properties.getProperty("surfer.username");
        password = properties.getProperty("surfer.password");




        String ip_invoke = properties.getProperty("koi.host");
        String port_invoke = properties.getProperty("koi.port");

        invokeUrl = ip_invoke + ":" + port_invoke;
        Resolver resolver = null;
        try {
            resolver = DexResolver.create();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DID didSurfer = DID.createRandom();
        DID didInvoke = DID.createRandom();

        resolver.registerDID(didSurfer,getDDO(surferUrl));
        resolver.registerDID(didInvoke,getDDO(invokeUrl));

        remoteAccount =  RemoteAccount.create(username,password);

        surfer = RemoteAgent.connect(resolver, didSurfer, remoteAccount);
        invokeAgent = RemoteAgent.connect(resolver, didInvoke, remoteAccount);

    }

    private static String getDDO(String host) {
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
                "serviceEndpoint", host));
        services.add(Utils.mapOf(
                "type", "Ocean.Auth.v1",
                "serviceEndpoint", host + "/api/v1/auth"));
        services.add(Utils.mapOf(
                "type", "Ocean.Market.v1",
                "serviceEndpoint", host + "/api/v1/market"));
        services.add(Utils.mapOf(
                "type", "Ocean.Status",
                "serviceEndpoint", host + "/api/status"));
        services.add(Utils.mapOf(
                "type", "Ocean.DDO",
                "serviceEndpoint", host + "/api/ddo"));
        ddo.put("service", services);
        String ddoString = JSON.toPrettyString(ddo);

        return ddoString;

    }

    public static String getSurferUrl() {
        return surferUrl;
    }

    public static String getInvokeUrl() {
        return invokeUrl;
    }


    public static int getSocketTimeout() {
        return Integer.parseInt(socketTimeout);
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        try {
            try (InputStream is = AgentService.class.getClassLoader()
                    .getResourceAsStream("application_test.properties")) {
                properties.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }


    private static String getInvokeDDO(String host, Resolver resolver) {
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();

        services.add(Utils.mapOf(
                "type", "Ocean.Invoke.v1",
                "serviceEndpoint", host));
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
        return JSON.toPrettyString(ddo);


    }

    /**
     * Gets the surfer remote agent for testing purposes
     *
     * @return The RemoteAgent, or null if not up
     */
    public static RemoteAgent getRemoteAgent() {
        return surfer;

    }
    public static boolean isServerReachable(String uri) {

        try {
            int timeOut = AgentService.getSocketTimeout();
            URL url = new URL(uri);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(url.getHost(), url.getPort()), timeOut);
            socket.close();
            return true;
        } catch (Exception e) {

            return false;
        }
    }

    /**
     * Gets the koi remote agent for testing purposes
     *
     * @return The RemoteAgent, or null if not up
     */
    public static RemoteAgent getInvoke() {
        return invokeAgent;

    }

    public static boolean checkSurfer() {
        return isServerReachable(surferUrl);
    }

    public static RemoteAccount getRemoteAccount() {
        return remoteAccount;
    }
}
