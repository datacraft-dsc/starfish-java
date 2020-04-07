package usecase.agent;

import common.AgentFactory;
import sg.dex.starfish.Agent;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Resolver;
import sg.dex.starfish.impl.memory.LocalResolverImpl;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteDataAsset;
import sg.dex.starfish.util.DID;

import java.io.IOException;
import java.net.URISyntaxException;

public class DataExample {

    private RemoteAccount remoteAccount =
            RemoteAccount.create("Aladdin", "OpenSesame");
    private String surferURL = "http://52.230.82.125:3030";


    public static void main(String[] a) throws IOException, URISyntaxException {
        DataExample dataExample = new DataExample();
        dataExample.createRemoteAgent_WithAgentURL();
//        dataExample.createRemoteAgent_WithResolver();
//        dataExample.createRemoteAgent_WithDID();
    }

    private static DID getSurferDid() {
        DID did = DID.parse("did:dex:1acd41655b2d8ea3f3513cc847965e72c31bbc9bfc38e7e7ec901852bd3c457c");
        return did;
    }

    void createRemoteAgent_WithAgentURL() throws IOException, URISyntaxException {

        RemoteAgent remoteAgent = RemoteAgent.connect("http://52.230.82.125:3030", remoteAccount);
        System.in.read();

        System.out.println("Create an Asset and verify the registration");

        Asset asset = MemoryAsset.create("test creation of agent by URL " .getBytes());
        RemoteDataAsset remoteDataAsset = remoteAgent.uploadAsset(asset);

        System.in.read();
        System.out.println("Asset ID : " + asset.getAssetID());
        System.out.println("Asset registration is successful , DID is :");
        System.out.println(remoteDataAsset.getDID());
    }

    void createRemoteAgent_WithResolver() throws IOException, URISyntaxException {

        Resolver defaultResolver = new LocalResolverImpl();
        defaultResolver.registerDID(getSurferDid(), AgentFactory.getDdo(surferURL));

        Agent surfer = RemoteAgent.connect(defaultResolver, getSurferDid(), remoteAccount);
        Asset asset = MemoryAsset.create("test" .getBytes());
        RemoteDataAsset remoteDataAsset = surfer.registerAsset(asset);
        System.out.println("Asset ID :" + asset.getAssetID());
        DID did = remoteDataAsset.getDID();
        // api to get asset
        System.out.println("Registered Asset ID : " + remoteDataAsset.getDID());
    }

    void createRemoteAgent_WithDID() throws IOException, URISyntaxException {

        Agent surfer = RemoteAgent.connect(getSurferDid(), remoteAccount);
        Asset asset = MemoryAsset.create("test" .getBytes());
        RemoteDataAsset remoteDataAsset = surfer.registerAsset(asset);
        System.out.println("Asset ID" + asset.getAssetID());
        System.out.println("Registered Asset ID" + remoteDataAsset.getAssetID());
    }

}
