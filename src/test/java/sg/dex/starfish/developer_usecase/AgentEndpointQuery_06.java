package sg.dex.starfish.developer_usecase;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import sg.dex.starfish.connection_check.AssumingConnection;
import sg.dex.starfish.connection_check.ConnectionChecker;
import sg.dex.starfish.impl.remote.RemoteAgent;

import static junit.framework.TestCase.assertNotNull;


/**
 * As a developer working with Ocean,
 * I need to obtain service endpoints for an arbitrary Agent in the Ocean ecosystem
 */
public class AgentEndpointQuery_06 {
    @ClassRule
    public static AssumingConnection assumingConnection =
            new AssumingConnection(new ConnectionChecker(RemoteAgentConfig.getSurferUrl()));
    private RemoteAgent remoteAgent ;

    @Before
    public void setup(){
        remoteAgent = RemoteAgentConfig.getRemoteAgent();
    }
    @Test
    public  void testServiceEndPoint(){
        // getting the URL for storage service
        String storage =remoteAgent.getStorageEndpoint();

        // getting the URL for invoke service
        String invoke =remoteAgent.getInvokeEndpoint();

        // getting the URL for metaData service
        String meta =remoteAgent.getMetaEndpoint();

        // getting the URL for market service
        String market =remoteAgent.getMarketEndpoint();


        assertNotNull(market);
        assertNotNull(meta);
        assertNotNull(invoke);
        assertNotNull(storage);

    }
}
