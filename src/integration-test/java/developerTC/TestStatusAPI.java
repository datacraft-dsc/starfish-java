package developerTC;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteDataAsset;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class TestStatusAPI {
    private RemoteAgent remoteAgent;

    @BeforeEach
    public void setup() {
        remoteAgent = AgentService.getRemoteAgent();
        assumeTrue(null != remoteAgent);

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
