package sg.dex.starfish.developer_usecase;

import org.junit.ClassRule;
import org.junit.Test;
import sg.dex.starfish.connection_check.AssumingConnection;
import sg.dex.starfish.connection_check.ConnectionChecker;

/**
 * As a developer working with Ocean,
 * I need to obtain service endpoints for an arbitrary Agent in the Ocean ecosystem
 */
public class AgentEndpointQuery_06 {
    @ClassRule
    public static AssumingConnection assumingConnection =
            new AssumingConnection(new ConnectionChecker(RemoteAgentConfig.getSurferUrl()));
    @Test
    public  void test(){


    }
}
