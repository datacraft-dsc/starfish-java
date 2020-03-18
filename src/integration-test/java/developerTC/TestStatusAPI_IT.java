package developerTC;


import org.junit.jupiter.api.*;
import sg.dex.starfish.impl.remote.RemoteAgent;

import java.net.URISyntaxException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestStatusAPI_IT {
    private RemoteAgent remoteAgent;

    @BeforeAll
    @DisplayName("Check if RemoteAgent is up!!")
    public static void init() {
        Assumptions.assumeTrue(ConnectionStatus.checkAgentStatus(), "Agent :" + AgentService.getSurferUrl() + "is not running. is down");
    }

    @BeforeEach
    public void setup() {
        remoteAgent = AgentService.getRemoteAgent();

    }

    @Test
    public void testStatus() {
        Map<String, Object> result = remoteAgent.getStatus();

        assertEquals("Surfer", result.get("name"));
        assertTrue(result.get("description").toString().contains("Data Ecosystem Agent"));

    }

    @Test
    public void testDDO() throws URISyntaxException {
        Map<String, Object> result = remoteAgent.getAgentDDO();
        String id = result.get("id").toString();
        Map<String, Object> cre = remoteAgent.getAccount().getCredentials();

        assertTrue(id.contains("did:"));
        assertTrue(cre.get("username").toString().equals("Aladdin"));


    }

//    @Test
//    public void testCreatAgentByURL() throws URISyntaxException, IOException {
//
//        RemoteAccount remoteAccount = RemoteAccount.create("Aladdin", "OpenSesame");
//        RemoteAgent remoteAgent = RemoteAgent.connect("http://52.230.82.125:3030", remoteAccount);
//        Asset asset = MemoryAsset.create("test".getBytes());
//        RemoteDataAsset remoteDataAsset = remoteAgent.uploadAsset(asset);
//        assertEquals(remoteDataAsset.getAssetID(), asset.getAssetID());
//        assertEquals(Utils.stringFromStream(remoteDataAsset.getContentStream()), "test");
//
//
//    }
}
