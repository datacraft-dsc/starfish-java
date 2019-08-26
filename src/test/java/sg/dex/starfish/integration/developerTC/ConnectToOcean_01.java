package sg.dex.starfish.integration.developerTC;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * As a developer working with Ocean,
 * I need a way to connect to the Ocean Network
 */
@RunWith(JUnit4.class)
public class ConnectToOcean_01 {

    // instance of Ocean class use to represent the Ocean Network
    private Ocean ocean;

    @Before
    public void setup() {

        ocean = Ocean.connect();
    }

    @Test
    public void testOceanConnect() {

        //
        Ocean ocean = Ocean.connect();
        DID surferDID = DID.createRandom();
        Map<String, Object> ddo = new HashMap<>();
        ddo.put("test", "1234");
        ocean.registerLocalDID(surferDID, JSON.toPrettyString(ddo));
        Assume.assumeNotNull(ocean);
        assertEquals(ocean.getDDO(surferDID).get("test").toString(), "1234");


    }

    @Test
    public void testOceanConnectDoubleRegistration() {

        DID surferDID = DID.createRandom();
        Map<String, Object> ddo = new HashMap<>();
        ddo.put("test", "test");
        ocean.registerLocalDID(surferDID, JSON.toPrettyString(ddo));
        Assume.assumeNotNull((ocean));
        assertEquals(ocean.getDDO(surferDID).get("test").toString(), "test");

        ddo.put("test", "testAgain");
        ocean.registerLocalDID(surferDID, JSON.toPrettyString(ddo));
        assertEquals(ocean.getDDO(surferDID).get("test").toString(), "testAgain");


    }

    @Test
    public void testnullDID(){
        Map<String, Object> ddo = new HashMap<>();
        DID surferDID = null;
        ddo.put("test", "test");
        ocean.registerLocalDID(surferDID, JSON.toPrettyString(ddo));
        assertEquals(ocean.getDDO(surferDID).get("test").toString(), "test");

    }

}
