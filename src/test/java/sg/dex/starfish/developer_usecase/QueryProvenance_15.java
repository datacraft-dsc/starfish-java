package sg.dex.starfish.developer_usecase;

import org.junit.ClassRule;
import org.junit.Test;
import sg.dex.starfish.connection_check.AssumingConnection;
import sg.dex.starfish.connection_check.ConnectionChecker;

/**
 * "As a developer or data scientist working with Ocean,
 * I need a way to view the provenance for an asset
 * "
 */
public class QueryProvenance_15 {
    @ClassRule
    public static AssumingConnection assumingConnection =
            new AssumingConnection(new ConnectionChecker(RemoteAgentConfig.getSurferUrl()));

    @Test
    public void test() {

    }
}
