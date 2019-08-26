package sg.dex.starfish.integration.developerTC;

import org.junit.Assume;
import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.url.RemoteHttpAsset;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.ProvUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * "As a developer or data scientist working with Ocean,
 * I need a way to view the provenance for an asset
 * "
 */
public class QueryProvenance_15 {

    private static RemoteAgent remoteAgent = RemoteAgentConfig.getRemoteAgent();

    @Test
    public void testProvenance() {
      // adding provenance
        String actId = UUID.randomUUID().toString();
        String agentId = UUID.randomUUID().toString();
        Map<String, Object> provmetadata = ProvUtil.createPublishProvenance(actId, agentId);
        Map<String, Object> metaDataAsset = new HashMap<>();
        metaDataAsset.put("provenance", provmetadata);

        String url = "https://s3.eu-west-2.amazonaws.com/blockchainhub.media/Blockchain+Technology+Handbook.pdf";
        Asset assetUrl = RemoteHttpAsset.create( url,metaDataAsset);
        remoteAgent.registerAsset(assetUrl);
        Assume.assumeNotNull(assetUrl.getMetadata().get("provenance"));
        Map<String,Object> provData =JSON.toMap(assetUrl.getMetadata().get("provenance").toString());

        Assume.assumeNotNull(provData.get("activity"));
        Assume.assumeNotNull(provData.get("wasGeneratedBy"));
    }
}
