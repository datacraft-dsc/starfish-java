package sg.dex.starfish.samples;

import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;
import sg.dex.starfish.impl.remote.RemoteBundleAsset;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class TestRemoteBundleAsset {

    RemoteAgent remoteAgent = RemoteAgentConfig.getRemoteAgent();
    @Test
    public void testAssetBundleCreationWithoutBundleName() {

        // creating  assets

        byte[] data1 = {2, 3, 4};
        Asset a1 = MemoryAsset.create(data1);
        RemoteAsset ra1 = remoteAgent.registerAsset(a1);

        byte[] data2 = {5, 6, 7};
        Asset a2 = MemoryAsset.create(data2);
        RemoteAsset ra2 = remoteAgent.registerAsset(a2);

        byte[] data3 = {2, 3, 4};
        Asset a3 = MemoryAsset.create(data3);
        RemoteAsset ra3 = remoteAgent.registerAsset(a3);

        byte[] data4 = {2, 3, 4};
        Asset a4 = MemoryAsset.create(data4);
        RemoteAsset ra4 = remoteAgent.registerAsset(a4);

        //assigning each asset with name and adding to map
        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("one", a1);
        assetBundle.put("two", a2);
        assetBundle.put("three", a3);
        assetBundle.put("four", a4);



        // create assetbundle without any custom metadata // so passing null
        RemoteBundleAsset remoteBundleAsset = RemoteBundleAsset.create(remoteAgent, assetBundle, null);

        // getting the ceated assetbundle metadata
        Map<String, Object> metadata = remoteBundleAsset.getMetadata();

        // checking default name
        assertEquals(metadata.get("name"), null);

        // getting the contents of asset bundle throug metadata
        Map<String, Map<String, String>> contents = (Map<String, Map<String, String>>) metadata.get("contents");

        //comparing id
        assertEquals(contents.get("two").get("assetID"), a2.getAssetID());
        assertEquals(contents.get("one").get("assetID"), a1.getAssetID());
        assertEquals(contents.get("three").get("assetID"), a3.getAssetID());
        assertEquals(contents.get("four").get("assetID"), a4.getAssetID());


        // getting the contents of asset bundle through API
        Map<String, Object> allAssetMap = remoteBundleAsset.getAll();


        assertEquals(((Map<String, Asset>) allAssetMap.get("two")).get("assetID"), a2.getAssetID());
        assertEquals(((Map<String, Asset>) allAssetMap.get("three")).get("assetID"), a3.getAssetID());
        assertEquals(((Map<String, Asset>) allAssetMap.get("one")).get("assetID"), a1.getAssetID());
        assertEquals(((Map<String, Asset>) allAssetMap.get("four")).get("assetID"), a4.getAssetID());

        // validating the singe asset
        Map<String, String> assetIdOfOne = (Map<String, String>) remoteBundleAsset.get("one");

        Asset oneAsset = remoteAgent.getAsset(assetIdOfOne.get("assetID"));
        assertEquals(oneAsset.getAssetID(), a1.getAssetID());


    }
}
