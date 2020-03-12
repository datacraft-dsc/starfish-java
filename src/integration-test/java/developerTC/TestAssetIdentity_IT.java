package developerTC;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.ARemoteAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteDataAsset;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.ProvUtil;
import sg.dex.starfish.util.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


/**
 * As a developer working with Data Supply Line,
 * I need a stable identifier (Asset ID) for an arbitrary asset in the Data supply line  ecosystem
 */

public class TestAssetIdentity_IT {

    private RemoteAgent remoteAgent;

    @BeforeClass
    public static void beforeClassMethod() {
        Assume.assumeTrue(AgentService.getAgentStatus(AgentService.getSurferUrl()));
    }

    @BeforeEach
    public void setup() {
        // Initialize Remote Agent
        remoteAgent = AgentService.getRemoteAgent();
        assumeTrue(null != remoteAgent);
    }

    @Test
    public void testByteContent() {

        // create a memory asset
        byte[] data = new byte[]{1, 2, 3};
        Asset asset1 = MemoryAsset.create(data);

        //2. registration : it will just reg the asset and upload its metadata content  and will return a Remote Agent
        ARemoteAsset aRemoteAsset = remoteAgent.registerAsset(asset1);
        RemoteDataAsset remoteAsset = remoteAgent.getAsset(aRemoteAsset.getAssetID());

        // compare both the assetID, It must be equal
        assertEquals(asset1.getAssetID(), remoteAsset.getAssetID());
    }


    // try to register again with same content and check the Hash
    @Test
    public void createAssetWithMetaData() {
        byte[] data = new byte[]{1, 2, 3};
        // update the metadata
        Map<String, Object> metaDataAsset = new HashMap<>();
        metaDataAsset.put("id", "123");
        metaDataAsset.put("name", "Fig");
        metaDataAsset.put("location", "Singapore");

        // creating asset with MetaData
        Asset asset2 = MemoryAsset.create(data, metaDataAsset);


        //2. registration : it will just reg the asset and upload its metadata content  and will return a Remote Agent
        RemoteDataAsset remoteAsset = remoteAgent.registerAsset(asset2);

        // uploading the Asset this remote Agent
        remoteAgent.uploadAsset(asset2);

        // get the Remote asset ID which has been register using remote Agent
        String assetID = remoteAsset.getAssetID();

        //Getting the content of the Asset
        byte[] result = asset2.getContent();

        // compare both the assetID, It must be equal
        assertEquals(assetID, asset2.getAssetID());

        // verifying the Content is same or not
        assertEquals(result.length, data.length);


        // verify the Asset metaDAta must be equal to Registered asset MetaData
        assertEquals(remoteAsset.getMetadata().get("id").toString(), "123");
        assertEquals(remoteAsset.getMetadata().get("name").toString(), "Fig");
        assertEquals(remoteAsset.getMetadata().get("location").toString(), "Singapore");

    }

    @Test
    public void createAssetWithProvMetadata() {
        byte[] data = new byte[]{1, 2, 3};
        // update the metadata
        Map<String, Object> metaDataAsset = new HashMap<>();
        metaDataAsset.put("id", "123");
        metaDataAsset.put("name", "Fig");
        metaDataAsset.put("location", "Singapore");

        String actId = UUID.randomUUID().toString();
        String agentId = remoteAgent.getDID().toString();

        Map<String, Object> provmetadata = ProvUtil.createPublishProvenance(actId, agentId);
        metaDataAsset.put("provenance", provmetadata);
        // creating asset with MetaData
        Asset asset2 = MemoryAsset.create(data, metaDataAsset);
        RemoteDataAsset remoteAsset = remoteAgent.registerAsset(asset2);

        // uploading the Asset this remote Agent
        remoteAgent.uploadAsset(asset2);


        //verify prov info exists
        assertNotNull(remoteAsset.getMetadata().get("provenance"));
        Map<String, Object> provData = JSON.toMap(remoteAsset.getMetadata().get("provenance").toString());

        assertNotNull(provData.get("activity"));
        assertNotNull(provData.get("wasGeneratedBy"));
    }

    @Test
    public void testWithStringContent() {

        // create Asset using String data
        Asset asset3 = MemoryAsset.createFromString("Testing using String");

        //Registering the Asset
        RemoteDataAsset remoteAsset3 = remoteAgent.registerAsset(asset3);
        // uploading the Asset, it will upload the content of an asset
        remoteAgent.uploadAsset(asset3);

        assertEquals(remoteAsset3.getAssetID(), asset3.getAssetID());
        // verify the content
        assertEquals(Utils.stringFromStream(remoteAsset3.getContentStream()),
                "Testing using String");
    }

    @Test
    public void testSameAssetContent() {
        if (remoteAgent == null) return;

        Asset asset3 = MemoryAsset.createFromString("Testing using String");
        RemoteDataAsset remoteAsset3 = remoteAgent.registerAsset(asset3);

        Asset asset4 = MemoryAsset.createFromString("Testing using String");
        RemoteDataAsset remoteAsset4 = remoteAgent.registerAsset(asset4);

        // uploading both the Asset
        remoteAgent.uploadAsset(asset3);
        remoteAgent.uploadAsset(asset4);

        assertEquals(Utils.stringFromStream(remoteAsset3.getContentStream()),
                Utils.stringFromStream(remoteAsset4.getContentStream()));
    }

    @Test
    public void testForNullAsset() {
        // Null check should be there ?
        assertThrows(StarfishValidationException.class, () -> {
            remoteAgent.registerAsset((Asset) null);
        });

    }

}
