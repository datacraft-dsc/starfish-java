package sg.dex.starfish.developer_usecase;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.connection_check.AssumingConnection;
import sg.dex.starfish.connection_check.ConnectionChecker;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * As a developer working with Ocean,
 * I need a stable identifier (Agent ID) for an arbitrary Agent in the Ocean ecosystem
 */
@RunWith(JUnit4.class)
public class AgentIdentity_03 {

    private RemoteAgent remoteAgent;

    @ClassRule
    public static AssumingConnection assumingConnection =
            new AssumingConnection(new ConnectionChecker(RemoteAgentConfig.getSurferUrl()));
    @Before
    public void setup() {

        remoteAgent = RemoteAgentConfig.getRemoteAgent();
    }

    @Test
    public void testDid() {
        DID did = remoteAgent.getDID();
       // System.out.println(did);
        assertNotNull(did.getID());
        assertEquals(did.getMethod(), "ocn");
        assertEquals(did.getScheme(), "did");
        assertNotNull(did);

    }
    @After
    public void clear(){
        remoteAgent =null;
    }
}
