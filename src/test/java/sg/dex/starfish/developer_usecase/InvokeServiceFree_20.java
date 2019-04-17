package sg.dex.starfish.developer_usecase;

import org.junit.ClassRule;
import org.junit.Test;
import sg.dex.starfish.connection_check.AssumingConnection;
import sg.dex.starfish.connection_check.ConnectionChecker;

/**
 * As a developer working with Ocean,
 * I wish to invoke a free service available on the Ocean ecosystem and obtain the results as a new Ocean Asset
 */
public class InvokeServiceFree_20 {
    @ClassRule
    public static AssumingConnection assumingConnection =
            new AssumingConnection(new ConnectionChecker(RemoteAgentConfig.getSurferUrl()));

    @Test
    public void test() {

    }
}
