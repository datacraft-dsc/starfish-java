package developerTC;

import org.junit.jupiter.api.*;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteDataAsset;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * As a developer working with an Network marketplace,
 * I need a way to upload my asset with a service agreement
 */
public class TestUploadAsset_IT {


    private RemoteAgent remoteAgent;

    @BeforeAll
    @DisplayName("Check if RemoteAgent is up!!")
    public static void init() {
        Assumptions.assumeTrue(ConnectionStatus.checkAgentStatus(), "Agent :" + AgentService.getSurferUrl() + "is not running. is down");
    }

    @BeforeEach
    public void setUp() {
        // create remote Agent
        remoteAgent = AgentService.getRemoteAgent();

    }

    @Test
    public void testUploadDownloadAsset() {

        Asset asset = MemoryAsset.createFromString("test upload of asset");

        RemoteDataAsset ra = remoteAgent.uploadAsset(asset);

        assertEquals(asset.getAssetID(), ra.getAssetID());
        assertArrayEquals(asset.getContent(), ra.getContent());


    }

}
