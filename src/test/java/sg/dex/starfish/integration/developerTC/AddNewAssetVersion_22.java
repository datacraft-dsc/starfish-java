package sg.dex.starfish.integration.developerTC;

import org.junit.Before;
import sg.dex.starfish.impl.remote.RemoteAgent;

/**
 * As a publisher working for Ocean,
 * I want to publish a new asset (as a new version of an existing asset), so that consumers can purchase the latest version
 */
public class AddNewAssetVersion_22 {
    private RemoteAgent remoteAgent;


    @Before
    public void setUp() {
        // create remote Agent
        remoteAgent = HelperTestService.getRemoteAgent();
        // create remote Asset


    }


}
