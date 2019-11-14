package developerTC;

import org.junit.Before;
import org.junit.Test;
import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.file.FileAsset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteDataAsset;
import sg.dex.starfish.impl.resource.ResourceAsset;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.ProvUtil;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static sg.dex.starfish.constant.Constant.CONTENT_HASH;


/**
 * As a developer working with Ocean, I need a way to register a new asset with Ocean
 */
public class AssetRegistration_08 {

    private RemoteAgent remoteAgent;

    @Before
    public void setup() {
        remoteAgent = AgentService.getRemoteAgent();

    }

    @Test
    public void testRegister() {
        String data = "Simple memory Asset";
        Asset asset = MemoryAsset.createFromString(data);
        Asset remoteAsset = remoteAgent.registerAsset(asset);

        assertEquals(asset.getAssetID(), remoteAsset.getAssetID());

        // get registered Asset by ID
        assertTrue(remoteAsset.isDataAsset());
        assertEquals(remoteAsset.getMetadataString(), asset.getMetadataString());
    }

    @Test
    public void testRegisterWithTwoAssetSameContent() {
        String stringData = "Simple Test two  Asset with same content";

        Asset remoteAsset = remoteAgent.registerAsset(MemoryAsset.createFromString(stringData));
        Asset remoteAsset1 = remoteAgent.registerAsset(MemoryAsset.createFromString(stringData));

        assertNotEquals(remoteAsset1.getAssetID(), remoteAsset.getAssetID());
        // get registered Asset by ID
        assertTrue(remoteAsset.isDataAsset());
        assertTrue(remoteAsset1.isDataAsset());
    }

    @Test
    public void testRegisterWithProvenance() {
        byte[] data = {1, 2, 3, 4};

        String actId = UUID.randomUUID().toString();
        String agentId = UUID.randomUUID().toString();

        Map<String, Object> provmetadata = ProvUtil.createPublishProvenance(actId, agentId);
        Map<String, Object> metaDataAsset = new HashMap<>();
        metaDataAsset.put("provenance", provmetadata);
        Asset asset = MemoryAsset.create(data, metaDataAsset);

        Asset remoteAsset = remoteAgent.registerAsset(asset);

        assertEquals(asset.getAssetID(), remoteAsset.getAssetID());

        Map<String, Object> provData = JSON.toMap(remoteAsset.getMetadata().get("provenance").toString());

        // get registered Asset by ID
        assertEquals(remoteAsset.isDataAsset(), remoteAsset.isDataAsset());
        assertEquals(remoteAsset.getMetadataString(), remoteAsset.getMetadataString());
        assertTrue(provData.get("activity").toString().contains(actId));
        assertTrue(provData.get("wasGeneratedBy").toString().contains(actId));

    }

    @Test
    public void testVerifyContent() throws IOException {

        // read metadata
        String content = new String(Files.readAllBytes(Paths.get("src/integration-test/resources/assets/test_content.json")));

        // create asset using metadata and given content
        Asset memoryAsset = MemoryAsset.create(content.getBytes());

        DataAsset dataAsset = remoteAgent.uploadAsset(memoryAsset);

        assertEquals(Hex.toString(Hash.sha3_256(dataAsset.getContent())), Hex.toString(Hash.sha3_256(content)));

    }

    @Test
    public void testHashForRemoteAsset() throws IOException {


        // read metadata
        String content = new String(Files.readAllBytes(Paths.get("src/integration-test/resources/assets/test_content.json")));

        // create asset using metadata and given content
        Asset memoryAsset = MemoryAsset.create(content.getBytes());

        DataAsset dataAsset = remoteAgent.uploadAsset(memoryAsset);
        DataAsset dataAssetWithHash =dataAsset.includeContentHash();

        assertEquals(Hex.toString(Hash.sha3_256(dataAsset.getContent())), Hex.toString(Hash.sha3_256(content)));
//        assertEquals(Hex.toString(Hash.sha3_256(dataAssetWithHash.getMetadata().get(CONTENT_HASH).toString())), Hex.toString(Hash.sha3_256(content)));

    }



    @Test
    public void testMetadataWithoutHash() {

        // create asset using metadata and given content
        ResourceAsset resourceAsset = ResourceAsset.create("assets/test_content.json");
        assertNull(resourceAsset.getMetadata().get(CONTENT_HASH));
    }

    @Test
    public void testHashForFileAssetWithDefaultMetadata() {

        // read metadata

        Path path = Paths.get("src/test/resources/assets/test_content.json");
        // create asset using metadata and given content
        FileAsset fileAsset = FileAsset.create(path.toFile());
        String content = Utils.stringFromStream(fileAsset.getContentStream());
        String expected = Hex.toString(Hash.sha3_256(content));

        fileAsset = (FileAsset) fileAsset.includeContentHash();
        String actual = fileAsset.getMetadata().get(CONTENT_HASH).toString();

        assertEquals(expected, actual);
    }


}
