package sg.dex.starfish.developer_usecase;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.connection_check.AssumingConnection;
import sg.dex.starfish.connection_check.ConnectionChecker;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * As a developer working with Ocean,
 * I need a way to connect to the Ocean Network
 */
@RunWith(JUnit4.class)
public class ConnectToOcean_01 {

    @ClassRule
    public static AssumingConnection assumingConnection =
            new AssumingConnection(new ConnectionChecker(RemoteAgentConfig.getSurferUrl()));
    Ocean ocean;

    @Before
    public void setup() {
        ocean = new Ocean();
    }

    @Test
    public void testOceanConnect() {

        //
        Ocean ocean = Ocean.connect();
        DID surferDID = DID.createRandom();
        Map<String, Object> ddo = new HashMap<>();
        ddo.put("test", "test");
        ocean.registerLocalDID(surferDID, JSON.toPrettyString(ddo));
        assertNotNull(ocean);
        assertEquals(ocean.getDDO(surferDID).get("test").toString(), "test");


    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOceanConnectException() {

        //
        Ocean ocean = Ocean.connect();
        DID surferDID = DID.createRandom();
        Map<String, Object> ddo = new HashMap<>();
        ddo.put("test", "test");
        ocean.registerLocalDID(surferDID, null);
        assertNotNull(ocean);
        assertEquals(ocean.getDDO(surferDID), null);

    }

    @Test
    public void testOceanConnectDoubleRegistration() {

        //

        DID surferDID = DID.createRandom();
        Map<String, Object> ddo = new HashMap<>();
        ddo.put("test", "test");
        ocean.registerLocalDID(surferDID, JSON.toPrettyString(ddo));
        assertNotNull(ocean);
        assertEquals(ocean.getDDO(surferDID).get("test").toString(), "test");

        ddo.put("test", "testAgain");
        ocean.registerLocalDID(surferDID, JSON.toPrettyString(ddo));
        assertEquals(ocean.getDDO(surferDID).get("test").toString(), "testAgain");


    }

    @Test
    public void testregisterDDO() {
        Map<String, Object> ddo = new HashMap<>();
        ddo.put("test", "test");
        DID did = ocean.registerDDO(JSON.toPrettyString(ddo));
        ddo.put("test", "testNewValue");
        assertEquals(ocean.getDDO(did).get("test").toString(), "test");
    }

}
