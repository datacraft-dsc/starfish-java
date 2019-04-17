package sg.dex.starfish.developer_usecase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * As a developer working with Ocean,
 * I need a stable identifier (Agent ID) for an arbitrary Agent in the Ocean ecosystem
 * This test class with validate the DID and DDO format and and their data
 */
@RunWith(JUnit4.class)
public class AgentIdentity_03 {

    private RemoteAgent remoteAgent;

    @Before
    public void setup() {

        remoteAgent = RemoteAgentConfig.getRemoteAgent();
    }

    @Test
    public void testDidFormat() {
        // getting the did of the agent
        DID did = remoteAgent.getDID();
        assertNotNull(did.getID());
        assertEquals(did.getMethod(), "ocn");
        assertEquals(did.getScheme(), "did");
        assertNotNull(did);

    }

    @Test
    public void testDDOFormat() {
        // getting the DDo of the Agent
        Map<String,Object> ddo =remoteAgent.getDDO();

        List<Map<String, Object>> services = (List<Map<String, Object>>)ddo.get("service");

        // for each services (Meta,Auth,Storage,Market)  will verify their respective data
        services.forEach(map -> {
            map.keySet().stream().forEach(m ->{
                assertNotNull(map.get(m));
            });

        });


        ddo.get("Ocean.Meta.v1");

    }
    @After
    public void clear(){
        remoteAgent =null;
    }
}
