package keeper;

import developerTC.AgentService;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Resolver;
import sg.dex.starfish.exception.ResolverException;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAccount;
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

import static org.junit.jupiter.api.Assertions.*;


public class DexResolverTest {
    private DexResolver resolver1;
    private DexResolver resolver2;
    private Resolver default_resolver;
    private String valueSet;
    private String valueUpdate;
    private DID did;

    public DexResolverTest() throws IOException {
        resolver1 = DexResolver.create("application_test.properties");
        resolver2 = DexResolver.create("application_resolver.properties");
        default_resolver = DexResolver.create();
        valueSet = DID.createRandomString();
        valueUpdate = DID.createRandomString();
        did = DID.createRandom();
    }


    @Test
    public void testRegisterResolve() {
        resolver1.registerDID(did, valueSet);
        String val = resolver1.getDDOString(did);
        assertTrue(val.equals(valueSet));
        resolver1.registerDID(did, valueUpdate);
        val = resolver1.getDDOString(did);
        assertTrue(val.equals(valueUpdate));
    }

    @Test
    public void testGetInvalidDID() {
        DID temp = DID.createRandom();
        String val = resolver1.getDDOString(temp);
        assertTrue(val == null);
    }

    @Test
    public void testPermissions() {
        resolver1.registerDID(did, valueSet);
        String val = resolver2.getDDOString(did);
        assertTrue(val.equals(valueSet));
        assertThrows(ResolverException.class, () -> {
            resolver2.registerDID(did, valueUpdate);
        });

    }

    @Test
    public void testResolveAgent() {
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
                "type", "DEP.Meta.v1",
                "serviceEndpoint", surferURL + "/api/v1/meta"));
        services.add(Utils.mapOf(
                "type", "Ocean.Storage.v1",
                "serviceEndpoint", surferURL + "/api/v1/assets"));
        services.add(Utils.mapOf(
                "type", "Ocean.Auth.v1",
                "serviceEndpoint", surferURL + "/api/v1/auth"));
        ddo.put("service", services);

        default_resolver.registerDID(did, JSON.toPrettyString(ddo));
        RemoteAgent remoteAgent = RemoteAgent.connect(default_resolver, did, remoteAccount);
        Asset asset = MemoryAsset.createFromString("Asset from string");
        Asset registeredAsset = remoteAgent.registerAsset(asset);
        // resolving
        RemoteAgent resolvedAgent = RemoteAgent.connect(default_resolver, did, remoteAccount);
        Asset assetFromAgent = resolvedAgent.getAsset(asset.getAssetID());

        assertEquals(resolvedAgent.getDID(), did);
        assertEquals(resolvedAgent.getMetaEndpoint(), surferURL + "/api/v1/meta");
        assertEquals(registeredAsset.getMetadataString(), assetFromAgent.getMetadataString());
        assertEquals(registeredAsset.getDID(), assetFromAgent.getDID());
    }
}
