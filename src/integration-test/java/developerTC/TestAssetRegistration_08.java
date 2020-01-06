package developerTC;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Bundle;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.file.FileAsset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.memory.MemoryBundle;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteBundle;
import sg.dex.starfish.impl.remote.RemoteDataAsset;
import sg.dex.starfish.impl.remote.RemoteOperation;
import sg.dex.starfish.impl.resource.ResourceAsset;
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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static sg.dex.starfish.constant.Constant.CONTENT_HASH;


/**
 * As a developer working with Ocean, I need a way to register a new asset with Ocean
 */
public class TestAssetRegistration_08 {

    private RemoteAgent remoteAgent;

    @BeforeEach
    public void setup() {
        remoteAgent = AgentService.getRemoteAgent();
        assumeTrue(null != remoteAgent);

    }

    @Test
    public void testRegister() {
        String data = "Simple memory Asset";
        Asset asset = MemoryAsset.createFromString(data);
        Asset remoteAsset = remoteAgent.registerAsset(asset);

        Assertions.assertEquals(asset.getAssetID(), remoteAsset.getAssetID());

        // get registered Asset by ID
        Assertions.assertTrue(remoteAsset.isDataAsset());
        Assertions.assertEquals(remoteAsset.getMetadataString(), asset.getMetadataString());
    }

    @Test
    public void testRegisterWithTwoAssetSameContent() {
        String stringData = "Simple Test two  Asset with same content";

        Asset remoteAsset = remoteAgent.registerAsset(MemoryAsset.createFromString(stringData));
        Asset remoteAsset1 = remoteAgent.registerAsset(MemoryAsset.createFromString(stringData));

        // get registered Asset by ID
        Assertions.assertTrue(remoteAsset.isDataAsset());
        Assertions.assertTrue(remoteAsset1.isDataAsset());
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

        Assertions.assertEquals(asset.getAssetID(), remoteAsset.getAssetID());

        Map<String, Object> provData = JSON.toMap(remoteAsset.getMetadata().get("provenance").toString());

        // get registered Asset by ID
        Assertions.assertEquals(remoteAsset.isDataAsset(), remoteAsset.isDataAsset());
        Assertions.assertEquals(remoteAsset.getMetadataString(), remoteAsset.getMetadataString());
        Assertions.assertTrue(provData.get("activity").toString().contains(actId));
        Assertions.assertTrue(provData.get("wasGeneratedBy").toString().contains(actId));

    }

    @Test
    public void testVerifyContent() throws IOException {

        // read metadata
        String content = new String(Files.readAllBytes(Paths.get("src/integration-test/resources/assets/test_content.json")));

        // create asset using metadata and given content
        Asset memoryAsset = MemoryAsset.create(content.getBytes());

        DataAsset dataAsset = remoteAgent.uploadAsset(memoryAsset);

        Assertions.assertEquals(Hex.toString(Hash.sha3_256(dataAsset.getContent())), Hex.toString(Hash.sha3_256(content)));

    }

    @Test
    public void testHashForRemoteAsset() throws IOException {


        // read metadata
        String content = new String(Files.readAllBytes(Paths.get("src/integration-test/resources/assets/test_content.json")));

        // create asset using metadata and given content
        Asset memoryAsset = MemoryAsset.create(content.getBytes());

        DataAsset dataAsset = remoteAgent.uploadAsset(memoryAsset);
        // here the byte in asset content is more that 8192
        dataAsset.includeContentHash();

        Assertions.assertEquals(Hash.sha3_256String(dataAsset.getContentStream()), dataAsset.getMetadata().get(Constant.CONTENT_HASH));

    }


    @Test
    public void testMetadataWithoutHash() {

        // create asset using metadata and given content
        ResourceAsset resourceAsset = ResourceAsset.create("assets/test_content.json");
        assertNull(resourceAsset.getMetadata().get(CONTENT_HASH));
    }

    @Test
    public void testHashForFileAssetWithDefaultMetadata() throws IOException {

        // read metadata

        Path path = Paths.get("src/test/resources/assets/test_content.json");
        // create asset using metadata and given content
        FileAsset fileAsset = FileAsset.create(path.toFile());
        // TODO: compute on bytes
        String content = Utils.stringFromStream(fileAsset.getContentStream());
        String expected = Hex.toString(Hash.sha3_256(content));

        fileAsset = (FileAsset) fileAsset.includeContentHash();
        String actual = fileAsset.getMetadata().get(CONTENT_HASH).toString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetDIDAllTypes() throws IOException {
        // RemoteDataAsset
        Asset asset = MemoryAsset.createFromString("Simple memory Asset");
        RemoteDataAsset remoteDataAsset = remoteAgent.registerAsset(asset);

        Assertions.assertEquals(asset.getAssetID(), remoteDataAsset.getDID().getPath());
        Assertions.assertEquals(remoteAgent.getDID().getID(), remoteDataAsset.getDID().getID());

        // RemoteBundle
        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("one", asset);
        Bundle bundle = MemoryBundle.create(assetBundle);
        RemoteBundle remoteBundle = remoteAgent.registerAsset(bundle);

        Assertions.assertEquals(bundle.getAssetID(), remoteBundle.getDID().getPath());
        Assertions.assertEquals(remoteAgent.getDID().getID(), remoteBundle.getDID().getID());

        // RemoteOperation
        String asset_metaData = new String(Files.readAllBytes(Paths.get("src/test/resources/assets/prime_asset_metadata.json")));
        RemoteOperation operationAsset = RemoteOperation.create(remoteAgent, asset_metaData);
        RemoteOperation remoteOperationAsset = remoteAgent.registerAsset(operationAsset);

        Assertions.assertEquals(operationAsset.getAssetID(), remoteOperationAsset.getDID().getPath());
        Assertions.assertEquals(remoteAgent.getDID().getID(), remoteOperationAsset.getDID().getID());
    }

}
