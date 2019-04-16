package sg.dex.starfish.developer_usecase;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.connection_check.AssumingConnection;
import sg.dex.starfish.connection_check.ConnectionChecker;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static sg.dex.starfish.constant.Constant.*;

/**
 * As a developer working with Ocean,
 * I need a way to request metadata for an arbitrary asset
 */
@RunWith(JUnit4.class)
public class MetaDataAccess_07 {
    @ClassRule
    public static AssumingConnection assumingConnection =
            new AssumingConnection(new ConnectionChecker(RemoteAgentConfig.getSurferUrl()));
    private Asset asset;
    private RemoteAgent remoteAgent;

    @Before
    public void seup() {
        byte data[] = {2, 5, 7};
        asset = MemoryAsset.create(data);
        remoteAgent = RemoteAgentConfig.getRemoteAgent();

    }

    @Test
    public void testMEmoryAgentMetaData() {

        assertNotNull(asset.getMetadata());
        assertNotNull(asset.getMetadata().get(DATE_CREATED));
        assertNotNull(asset.getMetadata().get(CONTENT_HASH));
        assertNotNull(asset.getMetadata().get(TYPE));
        assertNotNull(asset.getMetadata().get(SIZE));
        assertNotNull(asset.getMetadata().get(CONTENT_TYPE));

    }

    @Test
    public void testRemoteAssetMetaDataAsset() {

        asset = RemoteAsset.create(remoteAgent, "This is remote data");
        assertNotNull(asset.getAssetID());

    }

    @Test(expected = Error.class)
    public void testRemoteAssetMetaDataAssetInvalidJson() {

        asset = RemoteAsset.create(remoteAgent, "This is remote data");
        assertNotNull(asset.getMetadata().get(CONTENT_HASH));
//
    }

    @Test
    public void validJsonData() {
        String data = "Valid Json Data";
        String hash = Hex.toString(Hash.keccak256(data));
        Map<String, Object> ob = new HashMap<>();
        ob.put(DATE_CREATED, Instant.now().toString());
        ob.put(CONTENT_HASH, hash);
        ob.put(TYPE, "dataset");
        ob.put(SIZE, Integer.toString(data.length()));
        ob.put(CONTENT_TYPE, "application/octet-stream");
        asset = RemoteAsset.create(remoteAgent, JSON.toPrettyString(ob));
        System.out.println(asset);
        System.out.println(asset.getMetadataString());
        assertNotNull(asset.getMetadataString());
        assertNotNull(asset.getMetadata().get(CONTENT_HASH));
        assertNotNull(asset.getMetadata().get(TYPE));
        assertNotNull(asset.getMetadata().get(SIZE));
        assertNotNull(asset.getMetadata().get(CONTENT_TYPE));


    }

    @After
    public void clear() {
        asset = null;
        remoteAgent = null;
    }
}
