package sg.dex.starfish.developer_usecase;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.connection_check.AssumingConnection;
import sg.dex.starfish.connection_check.ConnectionChecker;

/**
 * "As a developer working with Ocean,
 * I need a way to create a valid service agreement that I can offer to others for puchase
 * "
 */

@RunWith(JUnit4.class)
public class ConstructServiceAgreemnt_09 {
    @ClassRule
    public static AssumingConnection assumingConnection =
            new AssumingConnection(new ConnectionChecker(RemoteAgentConfig.getSurferUrl()));

    @Test
    public void test() {

    }
}
