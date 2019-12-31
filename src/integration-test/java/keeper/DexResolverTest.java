package keeper;

import developerTC.AgentService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.exception.ResolverException;
import org.web3j.crypto.CipherException;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.squid.DexResolver;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.RemoteAgentConfig;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class DexResolverTest {
    private DexResolver resolver1;
    private DexResolver resolver2;
    private String valueSet;
    private String valueUpdate;
    private DID did;

    public DexResolverTest() throws IOException, CipherException {
        resolver1 = DexResolver.create("application_test.properties");
        resolver2 = DexResolver.create("application_resolver.properties");
        valueSet = DID.createRandomString();
        valueUpdate = DID.createRandomString();
        did = DID.createRandom();
    }


    @Disabled
    @Test
    public void testRegisterResolve()   {
        resolver1.registerDID(did, valueSet);
        String val = resolver1.getDDOString(did);
        assertTrue(val.equals( valueSet));
        resolver1.registerDID(did, valueUpdate);
        val = resolver1.getDDOString(did);
        assertTrue(val.equals( valueUpdate));
    }

    @Disabled
    @Test
    public void testGetInvalidDID()   {
        DID temp = DID.createRandom();
        String val = resolver1.getDDOString(temp);
        assertTrue(val==null);
    }

    @Disabled
    @Test
    public void testPermissions()   {
        resolver1.registerDID(did, valueSet);
        String val = resolver2.getDDOString(did);
        assertTrue(val.equals(valueSet) );
        assertThrows(ResolverException.class, () -> {
            resolver2.registerDID(did, valueUpdate);
        });

    }

    @Disabled
    @Test
    public void testResolveAgent()   {
        // Creating remote Account
        HashMap<String, Object> credentialMap = new HashMap<>();
        credentialMap.put("username", "Aladdin");
        credentialMap.put("password", "OpenSesame");
        RemoteAccount remoteAccount = RemoteAccount.create(Utils.createRandomHexString(32), credentialMap);
        // Creating ddo string
        String surferURL = AgentService.getSurferUrl();
        String invokeURL = AgentService.getInvokeUrl();
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();

        services.add(Utils.mapOf(
                "type", "Ocean.Invoke.v1",
                "serviceEndpoint", invokeURL + "/api/v1"));
        services.add(Utils.mapOf(
                "type", "Ocean.Meta.v1",
                "serviceEndpoint", surferURL + "/api/v1/meta"));
        services.add(Utils.mapOf(
                "type", "Ocean.Storage.v1",
                "serviceEndpoint", surferURL + "/api/v1/assets"));
        services.add(Utils.mapOf(
                "type", "Ocean.Auth.v1",
                "serviceEndpoint", surferURL + "/api/v1/auth"));
        ddo.put("service", services);

        resolver1.registerDID(did, JSON.toPrettyString(ddo));
        RemoteAgent remoteAgent = RemoteAgentConfig.getRemoteAgent(did, resolver1, remoteAccount);
        Asset asset = MemoryAsset.createFromString("Asset from string");
        Asset registeredAsset = remoteAgent.registerAsset(asset);
        // resolving
        RemoteAgent resolvedAgent = RemoteAgentConfig.getRemoteAgent(did, resolver2, remoteAccount);
        Asset assetFromAgent = resolvedAgent.getAsset(asset.getAssetID());

        assertEquals(resolvedAgent.getDID(), did);
        assertEquals(resolvedAgent.getMetaEndpoint(), surferURL + "/api/v1/meta");
        assertEquals(registeredAsset.getMetadataString(), assetFromAgent.getMetadataString());
        assertEquals(registeredAsset.getDID(), assetFromAgent.getDID());
    }
}
