package sg.dex.starfish.developer_usecase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.remote.RemoteAgent;

import static junit.framework.TestCase.assertTrue;

/**
 * As a developer managing a Ocean Agent,
 * I need to be able to update service endpoints for my Agent
 */
@RunWith(JUnit4.class)
public class AgentEndpointUpdate_05 {
    RemoteAgent remoteAgent;

    @Before
    public void setup() {


        // creating a Remote agent instance for given Ocean and DID
        remoteAgent = RemoteAgentConfig.getRemoteAgent();
    }

    @Test
    public void testServiceEndPoint() {

        assertTrue(remoteAgent.getStorageEndpoint().contains( "/api/v1/assets"));
        assertTrue(remoteAgent.getInvokeEndpoint().contains("/api/v1/invoke"));
        assertTrue(remoteAgent.getMetaEndpoint().contains("/api/v1/meta"));
        assertTrue(remoteAgent.getMarketEndpoint().contains("/api/v1/market"));
        assertTrue(remoteAgent.getAuthEndpoint().contains("/api/v1/auth"));
    }

    @Test
    public void testForNull() {
        Ocean ocean = Ocean.connect();
        // Null check validation
        ocean.registerLocalDID(null, null);



    }
}
