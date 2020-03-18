package developerTC;

import sg.dex.starfish.Resolver;
import sg.dex.starfish.dexchain.DexResolver;
import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * This class is used to get the Remote Agent based on the host.
 * Currently it is written to connect with Surfer
 * It will connect with default OCEAN (a placeholder for real OCEAN instance)
 */
public class AgentService {

    private static RemoteAgent surfer;

    private static String username;
    private static String password;


    private static RemoteAccount remoteAccount;


    static {

        Properties properties = ConnectionStatus.getProperties();
        //username and password
        username = properties.getProperty("surfer.username");
        password = properties.getProperty("surfer.password");

        Resolver resolver = DexResolver.create();
        DID didSurfer = DID.createRandom();

        resolver.registerDID(didSurfer, getDDO(ConnectionStatus.getSurferUrl()));

        remoteAccount = RemoteAccount.create(username, password);

        surfer = RemoteAgent.connect(resolver, didSurfer, remoteAccount);

    }

    private static String getDDO(String host) {
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();
        services.add(Utils.mapOf(
                "type", "DEP.Meta.v1",
                "serviceEndpoint", host + "/api/v1/meta"));
        services.add(Utils.mapOf(
                "type", "DEP.Storage.v1",
                "serviceEndpoint", host + "/api/v1/assets"));
        services.add(Utils.mapOf(
                "type", "DEP.Invoke.v1",
                "serviceEndpoint", host + "/api/v1/invoke"));
        services.add(Utils.mapOf(
                "type", "DEP.Auth.v1",
                "serviceEndpoint", host + "/api/v1/auth"));
        services.add(Utils.mapOf(
                "type", "DEP.Market.v1",
                "serviceEndpoint", host + "/api/v1/market"));
        services.add(Utils.mapOf(
                "type", "DEP.Status",
                "serviceEndpoint", host + "/api/status"));
        services.add(Utils.mapOf(
                "type", "DEP.DDO",
                "serviceEndpoint", host + "/api/ddo"));
        ddo.put("service", services);
        String ddoString = JSON.toPrettyString(ddo);

        return ddoString;

    }

    /**
     * Gets the surfer remote agent for testing purposes
     *
     * @return The RemoteAgent, or null if not up
     */
    public static RemoteAgent getRemoteAgent() {
        return surfer;

    }

    public static String getSurferUrl() {
        return ConnectionStatus.getSurferUrl();

    }

    public static boolean getAgentStatus(String url) {

        int code = 200;
        try {
            URL siteURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.connect();

            code = connection.getResponseCode();
            return code == 200;
        } catch (Exception e) {
            return false;

        }
    }


    public static RemoteAccount getRemoteAccount() {
        return remoteAccount;
    }
}
