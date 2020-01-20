package developerTC;

import org.junit.jupiter.api.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Resolver;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.squid.DexResolver;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * As a developer building or managing an Ocean Agent,
 * I need to be able to register my Agent on the network and obtain an Agent ID
 */
public class TestAgentRegistration_04 {


    @Test
    public void testRegistration() throws IOException {


        Resolver resolver=DexResolver.create();
        DID did = DID.createRandom();
        resolver.registerDID(did,getAgentDDO());


        RemoteAgent remoteAgent = RemoteAgent.connectAgent(resolver,did,  AgentService.getRemoteAccount());

        Asset asset = MemoryAsset.createFromString("Asset from string");
        Asset registeredAsset = remoteAgent.registerAsset(asset);

        Asset assetFromAgent = remoteAgent.getAsset(asset.getAssetID());

        assertEquals(remoteAgent.getDID(), did);
        assertEquals(remoteAgent.getMetaEndpoint(),  AgentService.getSurferUrl()+"/api/v1/meta");
        assertEquals(registeredAsset.getMetadataString(), assetFromAgent.getMetadataString());


    }

    private String getAgentDDO(){
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();
        services.add(Utils.mapOf(
                "type", "Ocean.Meta.v1",
                "serviceEndpoint", AgentService.getSurferUrl()+"/api/v1/meta"));

        services.add(Utils.mapOf(
                "type", "Ocean.Auth.v1",
                "serviceEndpoint",AgentService.getSurferUrl()+ "/api/v1/auth"));

        ddo.put("service", services);
        return JSON.toPrettyString(ddo);
    }

}
