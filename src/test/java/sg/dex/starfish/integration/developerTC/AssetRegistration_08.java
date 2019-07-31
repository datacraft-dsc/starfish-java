package sg.dex.starfish.integration.developerTC;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.impl.file.FileAsset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.url.ResourceAsset;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.ProvUtil;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;


/**
 * As a developer working with Ocean, I need a way to register a new asset with Ocean
 */
@RunWith(JUnit4.class)
public class AssetRegistration_08 {

    private RemoteAgent remoteAgent;

    @Before
    public void setup() {
        remoteAgent = RemoteAgentConfig.getRemoteAgent();

    }

    @Test
    public void testRegister() {
        String data = "Simple memory Asset";
        Asset asset = MemoryAsset.create(data);

        Asset remoteAsset = remoteAgent.registerAsset(asset);

        assertEquals(asset.getAssetID(), remoteAsset.getAssetID());

        // get registered Asset by ID
        assertEquals(remoteAsset.isDataAsset(), remoteAsset.isDataAsset());
        assertEquals(remoteAsset.getMetadataString(), remoteAsset.getMetadataString());
    }

    @Test
    public void testRegisterWithTwoAssetSameContent() {
        String data = "Simple Test two  Asset with same content";

        Asset remoteAsset = remoteAgent.registerAsset(MemoryAsset.create(data));
        Asset remoteAsset1 = remoteAgent.registerAsset(MemoryAsset.create(data));

        assertNotEquals(remoteAsset1.getAssetID(), remoteAsset.getAssetID());
    }

    @Test
    public void testRegisterWithProv() {
        byte[] data = {1, 2, 3, 4};

        String actId = UUID.randomUUID().toString();
        String agentId = UUID.randomUUID().toString();

        Map<String, Object> provmetadata = ProvUtil.createPublishProvenance(actId, agentId);
        Map<String, Object> metaDataAsset = new HashMap<>();
        metaDataAsset.put("provenance", provmetadata);
        Asset asset = MemoryAsset.create(data, metaDataAsset);

        Asset remoteAsset = remoteAgent.registerAsset(asset);

        assertEquals(asset.getAssetID(), remoteAsset.getAssetID());

        // get registered Asset by ID
        assertEquals(remoteAsset.isDataAsset(), remoteAsset.isDataAsset());
        assertEquals(remoteAsset.getMetadataString(), remoteAsset.getMetadataString());
        assertNotNull(asset.getMetadata().get("provenance"));
    }

    @Test
    public void testHashForResourceAsset() throws IOException {

        // read metadata
        String asset_metaData = new String(Files.readAllBytes(Paths.get("src/test/resources/assets/SJR8961K_metadata.json")));

        // create asset using metadata and given content
        ResourceAsset resourceAsset = ResourceAsset.create(asset_metaData, "assets/SJR8961K_content.json");
        String content = Utils.stringFromStream(resourceAsset.getContentStream());
        String expected = Hex.toString(Hash.keccak256(content));
        String actual = resourceAsset.getMetadata().get(Constant.CONTENT_HASH).toString();

        assertEquals(expected, actual);
    }
    @Test
    public void testHashForResourceAssetWithoutMetadata() throws IOException {

        // create asset using metadata and given content
        ResourceAsset resourceAsset = ResourceAsset.create( "assets/SJR8961K_content.json");
        String content = Utils.stringFromStream(resourceAsset.getContentStream());
        String expected = Hex.toString(Hash.keccak256(content));
        String actual = resourceAsset.getMetadata().get(Constant.CONTENT_HASH).toString();

        assertEquals(expected, actual);
    }

    @Test(expected = StarfishValidationException.class)
    public void testBadResourceFile() throws IOException {

        // create asset using metadata and given content
        ResourceAsset resourceAsset = ResourceAsset.create( "assets/SJR8961K_content_NA.json");
        String content = Utils.stringFromStream(resourceAsset.getContentStream());
        String expected = Hex.toString(Hash.keccak256(content));
        String actual = resourceAsset.getMetadata().get(Constant.CONTENT_HASH).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testHashForFileAsset() throws IOException {

        // read metadata
        String asset_metaData = new String(Files.readAllBytes(Paths.get("src/test/resources/assets/SJR8961K_metadata.json")));

        Path path = Paths.get("src/test/resources/assets/SJR8961K_content.json");
        // create asset using metadata and given content

        FileAsset fileAsset = FileAsset.create(path.toFile(), asset_metaData);

        String content = Utils.stringFromStream(fileAsset.getContentStream());
        String expected = Hex.toString(Hash.keccak256(content));

        String actual = fileAsset.getMetadata().get(Constant.CONTENT_HASH).toString();

        assertEquals(expected, actual);
    }
    @Test
    public void testHashForFileAssetWithDefaultMetadata()  {

        // read metadata

        Path path = Paths.get("src/test/resources/assets/SJR8961K_content.json");
        // create asset using metadata and given content
        FileAsset fileAsset = FileAsset.create(path.toFile());
        String content = Utils.stringFromStream(fileAsset.getContentStream());
        String expected = Hex.toString(Hash.keccak256(content));
        String actual = fileAsset.getMetadata().get(Constant.CONTENT_HASH).toString();

        assertEquals(expected, actual);
    }

    @Test(expected = StarfishValidationException.class)
    public void testfileNotExist()  {

        // read metadata

        Path path = Paths.get("src/test/resources/assets/SJR8961K_content_NA.json");
        // create asset using metadata and given content
        FileAsset fileAsset = FileAsset.create(path.toFile());
        String content = Utils.stringFromStream(fileAsset.getContentStream());
        String expected = Hex.toString(Hash.keccak256(content));
        String actual = fileAsset.getMetadata().get(Constant.CONTENT_HASH).toString();

        assertEquals(expected, actual);
    }
}
