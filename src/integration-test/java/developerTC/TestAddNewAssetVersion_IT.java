package developerTC;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import sg.dex.starfish.impl.remote.RemoteAgent;

/**
 * As a publisher working for Data Supply Lines,
 * I want to publish a new asset (as a new version of an existing asset), so that consumers can purchase the latest version
 */
@Disabled
public class TestAddNewAssetVersion_IT {
    private RemoteAgent remoteAgent;


    @BeforeEach
    public void setUp() {
        // create remote Agent
        remoteAgent = AgentService.getRemoteAgent();
        // create remote Asset


    }


}
