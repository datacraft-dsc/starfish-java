package developerTC;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static sg.dex.starfish.constant.Constant.*;

/**
 * As a developer working with Ocean,
 * I need a way to request metadata for an arbitrary asset
 */
public class MetaDataAccess_07 {


    private RemoteAgent remoteAgent;
    private static String METADATA_JSON_CONTENT;

    @Before
    public void setup() throws IOException {

        remoteAgent = AgentService.getRemoteAgent();
        String METADATA_JSON_SAMPLE = "src/integration-test/resources/assets/test_metadata.json";
        METADATA_JSON_CONTENT = new String(Files.readAllBytes(Paths.get(METADATA_JSON_SAMPLE)));
        Assume.assumeNotNull(remoteAgent);

    }

    @Test
    public void testMEmoryAgentMetaData()  {
        byte[] data = {2, 5, 7};
        MemoryAsset asset = MemoryAsset.create(data);
        DataAsset remoteAsset = remoteAgent.registerAsset(asset);

        assertEquals(remoteAsset.getMetadata().get(DATE_CREATED).toString(), asset.getMetadata().get(DATE_CREATED).toString());
        assertEquals(remoteAsset.getMetadata().get(TYPE).toString(), asset.getMetadata().get(TYPE));
        assertEquals(remoteAsset.getMetadata().get(SIZE).toString(), asset.getMetadata().get(SIZE));
        assertEquals(remoteAsset.getMetadata().get(CONTENT_TYPE), asset.getMetadata().get(CONTENT_TYPE));

    }

    @Test
    public void testRemoteAssetMetaDataAsset() throws IOException {


        byte[] data = {2, 5, 7};
        MemoryAsset asset = MemoryAsset.create(data, METADATA_JSON_CONTENT);

        DataAsset remoteAsset = remoteAgent.registerAsset(asset);
        assertEquals(remoteAsset.getMetadata().get("name").toString(), "This is to verify the asset registration on Network ");
        assertEquals(remoteAsset.getMetadata().get("description").toString(), "This is to verify the asset registration on Network");

    }




    @After
    public void clear() {
        remoteAgent = null;
    }
}
