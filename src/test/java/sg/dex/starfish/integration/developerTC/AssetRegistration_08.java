package sg.dex.starfish.integration.developerTC;

import org.junit.Assume;
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
import sg.dex.starfish.impl.remote.RemoteDataAsset;
import sg.dex.starfish.impl.url.ResourceAsset;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;
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
import static org.junit.Assert.*;


/**
 * As a developer working with Ocean, I need a way to register a new asset with Ocean
 */
@RunWith(JUnit4.class)
public class AssetRegistration_08 {

    private RemoteAgent remoteAgent;

    @Before
    public void setup() {
        remoteAgent = RemoteAgentConfig.getRemoteAgent();
        Assume.assumeNotNull(remoteAgent);

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
        String asset_metaData = new String(Files.readAllBytes(Paths.get("src/test/resources/assets/test_metadata.json")));

        // create asset using metadata and given content
        ResourceAsset resourceAsset = ResourceAsset.create("assets/test_content.json", JSON.toMap(asset_metaData));

        remoteAgent.uploadAsset(resourceAsset);
        remoteAgent.getContentStream(resourceAsset.getAssetID());

        RemoteDataAsset remoteAsset =(RemoteDataAsset) remoteAgent.getAsset(resourceAsset.getAssetID());

        resourceAsset =(ResourceAsset) resourceAsset.includeContentHash();
        assertNotNull(resourceAsset.getMetadata().get(Constant.CONTENT_HASH));


    }

    @Test(expected = StarfishValidationException.class)
    public void testHashForRemoteAsset() throws IOException {

        // read metadata
        String asset_metaData = new String(Files.readAllBytes(Paths.get("src/test/resources/assets/test_metadata.json")));

        // create asset using metadata and given content
        ResourceAsset resourceAsset = ResourceAsset.create("assets/test_content.json", JSON.toMap(asset_metaData));

        remoteAgent.uploadAsset(resourceAsset);
        remoteAgent.getContentStream(resourceAsset.getAssetID());

        RemoteDataAsset remoteAsset =(RemoteDataAsset) remoteAgent.getAsset(resourceAsset.getAssetID());

        remoteAsset =(RemoteDataAsset) remoteAsset.includeContentHash();
        assertNotNull(remoteAsset.getMetadata().get(Constant.CONTENT_HASH));


    }

    @Test
    public void testMetadataWithHash()  {

        // create asset using metadata and given content
        ResourceAsset resourceAsset = ResourceAsset.create("assets/test_content.json");
        assertNull(resourceAsset.getMetadata().get(Constant.CONTENT_HASH));
        resourceAsset.includeContentHash();
        // calculate content hash
       String expected= Hex.toString(Hash.sha3_256(resourceAsset.getContent()));

//        assertNotNull(resourceAsset.getMetadata().get(Constant.CONTENT_HASH));
        assertEquals(expected,resourceAsset.getMetadata().get(Constant.CONTENT_HASH).toString());
        resourceAsset.validateContentHash();
    }
    @Test
    public void testMetadataWithoutHash() {

        // create asset using metadata and given content
        ResourceAsset resourceAsset = ResourceAsset.create("assets/test_content.json");
        assertNull(resourceAsset.getMetadata().get(Constant.CONTENT_HASH));
    }

    @Test
    public void testHashForFileAssetWithDefaultMetadata()  {

        // read metadata

        Path path = Paths.get("src/test/resources/assets/test_content.json");
        // create asset using metadata and given content
        FileAsset fileAsset = FileAsset.create(path.toFile());
        String content = Utils.stringFromStream(fileAsset.getContentStream());
        String expected = Hex.toString(Hash.sha3_256(content));

        fileAsset =(FileAsset)fileAsset.includeContentHash();
        String actual = fileAsset.getMetadata().get(Constant.CONTENT_HASH).toString();

        assertEquals(expected, actual);
    }

    public void testfileNotExist()  {

        // read metadata

        Path path = Paths.get("src/test/resources/assets/test_content.json");
        // create asset using metadata and given content
        FileAsset fileAsset = FileAsset.create(path.toFile());
        String content = Utils.stringFromStream(fileAsset.getContentStream());



        assertNull(fileAsset.getMetadata().get(Constant.CONTENT_HASH));
        fileAsset =(FileAsset)fileAsset.includeContentHash();
        assertNotNull(fileAsset.getMetadata().get(Constant.CONTENT_HASH));
        fileAsset.validateContentHash();


        String expected = Hex.toString(Hash.sha3_256(content));
        String actual = fileAsset.getMetadata().get(Constant.CONTENT_HASH).toString();
        assertEquals(expected, actual);
    }
}
