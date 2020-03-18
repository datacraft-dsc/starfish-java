package developerTC;

import org.junit.Assert;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteDataAsset;
import sg.dex.starfish.util.Params;

public class TestParams_IT {


    @BeforeAll
    @DisplayName("Check if RemoteAgent is up!!")
    public static void init() {
        Assumptions.assumeTrue(ConnectionStatus.checkAgentStatus(), "Agent :" + AgentService.getSurferUrl() + "is not running. is down");
    }

    @Test
    public void testAssetByDID() {
        RemoteAgent remoteAgent = AgentService.getRemoteAgent();

        Asset asset = MemoryAsset.create("test".getBytes());
        RemoteDataAsset remoteDataAsset = remoteAgent.uploadAsset(asset);

        Asset asset1 = Params.getAssetByDID(remoteDataAsset.getDID(), remoteAgent.getAccount());
        Assert.assertEquals(asset.getMetadataString(), asset1.getMetadataString());
        Assert.assertEquals(asset.getAssetID(), asset1.getAssetID());
    }
}
