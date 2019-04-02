package sg.dex.starfish.developer_usecase;

import sg.dex.starfish.Ocean;
import sg.dex.starfish.util.DID;

import static org.junit.Assert.assertNotNull;

/**
 * As a developer working with Ocean,
 * I need a way to connect to the Ocean Network
 */
public class ConnectToOcean_01 {
    public static void main(String args[]) {

        //
        Ocean ocean = Ocean.connect();
        DID surferDID = DID.createRandom();
        ocean.registerLocalDID(surferDID, "test");
        assertNotNull(ocean);//,surferDID);

    }
}
