package common;

import sg.dex.starfish.Agent;
import sg.dex.starfish.Resolver;
import sg.dex.starfish.impl.memory.LocalResolverImpl;
import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentFactory {
    private static String username = "Aladdin";
    private static String password = "OpenSesame";

    private static String surferURL = "http://52.230.82.125:3030";

    static RemoteAccount remoteAccount = RemoteAccount.create(username, password);

    private static Resolver defaultResolver = new LocalResolverImpl();



    public static Agent getSurfer() {

        defaultResolver.registerDID(getSurferDid(), getDdo(surferURL));

        return RemoteAgent.connect(defaultResolver, getSurferDid(), remoteAccount);
    }


    public static Agent getKOI() {
        defaultResolver.registerDID(getInvokeDid(), getOperationDdo());
        return RemoteAgent.connect(defaultResolver, getInvokeDid(), remoteAccount);
    }


    public static String getDdo(String surferURL) {



//        String invokeURL = "http://52.230.52.223:8191";
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();

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
        return JSON.toPrettyString(ddo);

    }

    public static String getOperationDdo() {

        String surferURL = "http://52.230.82.125:3030";
        String invokeURL = "http://52.230.52.223:8191";
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();
        services.add(Utils.mapOf(
                "type", "Ocean.Invoke.v1",
                "serviceEndpoint", invokeURL + "/api/v1/invoke"));
        services.add(Utils.mapOf(
                "type", "Ocean.Meta.v1",
                "serviceEndpoint", invokeURL + "/api/v1/meta"));
        services.add(Utils.mapOf(
                "type", "Ocean.Auth.v1",
                "serviceEndpoint", surferURL + "/api/v1/auth"));

        ddo.put("service", services);
        return JSON.toPrettyString(ddo);

    }

    private static DID getInvokeDid() {
        DID did = DID.parse("did:op:a1d2dbf875ad06ea96432ca4d091e23c26f211b7caedba1e0b71121b2d88e1fd");

        return did;

    }

    private static DID getSurferDid() {
        DID did = DID.parse("did:dex:1acd41655b2d8ea3f3513cc847965e72c31bbc9bfc38e7e7ec901852bd3c457c");

        return did;

    }
}
