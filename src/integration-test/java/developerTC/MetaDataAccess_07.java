package developerTC;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.JSON;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static sg.dex.starfish.constant.Constant.*;

/**
 * As a developer working with Ocean,
 * I need a way to request metadata for an arbitrary asset
 */
@RunWith(JUnit4.class)
public class MetaDataAccess_07 {


    private RemoteAgent remoteAgent;
    private static String METADATA_JSON_SAMPLE = "src/test/resources/assets/asset_metadata.json";

    @Before
    public void setup() {

        remoteAgent = AgentService.getRemoteAgent();

    }

    @Test
    public void testMEmoryAgentMetaData() {
        byte[] data = {2, 5, 7};
        MemoryAsset asset = MemoryAsset.create(data);
        Asset remoteAsset = remoteAgent.registerAsset(asset);

        assertEquals(remoteAsset.getMetadata().get(DATE_CREATED).toString(), asset.getMetadata().get(DATE_CREATED).toString());
        assertEquals(remoteAsset.getMetadata().get(TYPE).toString(), asset.getMetadata().get(TYPE));
        assertEquals(remoteAsset.getMetadata().get(SIZE).toString(), asset.getMetadata().get(SIZE));
        assertEquals(remoteAsset.getMetadata().get(CONTENT_TYPE), asset.getMetadata().get(CONTENT_TYPE));

    }

    @Test
    public void testRemoteAssetMetaDataAsset() {
        byte[] data = {2, 5, 7};
        MemoryAsset asset = MemoryAsset.create(data, getMetaData());

        Asset remoteAsset = remoteAgent.registerAsset(asset);
        assertEquals(remoteAsset.getMetadata().get("title"), "First listing");
        assertEquals(remoteAsset.getMetadata().get("description"), "this is the Memory listing");

    }


    private Map<String, Object> getMetaData() {
        try {
            String METADATA_JSON_CONTENT = new String(Files.readAllBytes(Paths.get(METADATA_JSON_SAMPLE)));
            return JSON.toMap(METADATA_JSON_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @After
    public void clear() {
        remoteAgent = null;
    }
}
