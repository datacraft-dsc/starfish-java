package developerTC;


import org.json.simple.JSONArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.JSON;

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

        assertEquals("Surfer",result.get("name"));
        assertTrue(result.get("description").toString().contains("Data Ecosystem Agent"));
        System.out.println(result.get("api-versions"));

    }

    @Test
    public void testDDO() {
        Map<String, Object> result = remoteAgent.getAgentDDO();
        String id = result.get("id").toString();
        Map<String, Object> cre= JSON.parse(result.get("credentials").toString());

        assertTrue(id.contains("did:"));
        assertTrue(cre.get("username").toString().equals("Aladdin"));


    }
}
