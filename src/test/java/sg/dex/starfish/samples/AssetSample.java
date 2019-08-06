package sg.dex.starfish.samples;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.Purchase;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.impl.remote.ARemoteAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.url.RemoteHttpAsset;
import sg.dex.starfish.impl.url.ResourceAsset;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.ProvUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AssetSample {
    private static RemoteAgent remoteAgent = RemoteAgentConfig.getRemoteAgent();

    public static void main(String[] a) throws IOException {


        //1.Create Asset
        System.out.println("******* Example to create Asset,Register,upload,listing and Purchase : -*************");
        System.out.println();
        System.in.read();
        createAssetFromPath();
       // createAssetFromUrl();

    }

    private static void createAssetFromPath() throws IOException {

        // adding provenance
        String actId = UUID.randomUUID().toString();
        String agentId = UUID.randomUUID().toString();
        Map<String, Object> provmetadata = ProvUtil.createPublishProvenance(actId, agentId);
        Map<String, Object> metaDataAsset = new HashMap<>();
        metaDataAsset.put("provenance", provmetadata);

        String path = "example/software_training_data.pdf";
        //System.out.println("local file :" + path);
        Asset assetPath = ResourceAsset.create(path,false);

        assetFlow(remoteAgent, assetPath);
    }

    private static void createAssetFromUrl() throws IOException {
        String url = "https://s3.eu-west-2.amazonaws.com/blockchainhub.media/Blockchain+Technology+Handbook.pdf";
        System.out.println("url: " + url);
        Asset assetUrl = RemoteHttpAsset.create( url);

        assetFlow(remoteAgent, assetUrl);
    }

    private static Purchase purchaseAsset(Listing listing) {
        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put("listingid", listing.getId());

        return remoteAgent.createPurchase(purchaseData);
    }

    private static Listing publishAsset(Listing listing) {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "published");
        data.put("id", listing.getId());
        return remoteAgent.updateListing(data);

    }

    private static Listing createListing(Asset a) {
        Map<String, Object> data2 = new HashMap<>();
        //data.put( "status", "unpublished");
        data2.put("assetid", a.getAssetID());
        return remoteAgent.createListing(data2);
    }

    private static void uploadAsset(Asset a) {
        remoteAgent.uploadAsset(a);
    }

    private static ARemoteAsset registerAsset(Asset a) {
        return remoteAgent.registerAsset(a);
    }

    private static void verifyAsset(Asset subAsset1, RemoteAgent remoteAgent) {


        //verify id these Asset exist in Surfer
        try {
            ARemoteAsset aRemoteAsset = remoteAgent.getAsset(subAsset1.getAssetID());
            System.out.println("Asset exist :" + aRemoteAsset.getAssetID());
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void assetFlow(RemoteAgent remoteAgent, Asset asset) throws IOException {
        System.out.println("Asset ID  :" + asset.getAssetID());
        System.in.read();
        System.out.println("Asset path  Metadata :" + JSON.toPrettyString(asset.getMetadata()));

        System.out.println("*******VERIFY IF ASSET EXIST in SURFER *******");
        System.in.read();
        verifyAsset(asset, remoteAgent);

        //2/Register Asset
        System.out.println("******* Register and upload of an Asset ************");
        System.in.read();
        //registerAsset(assetUrl);
        registerAsset(asset);

        uploadAsset(asset);
        System.out.println("******* Successfully Register and uploaded the Asset ************");

        System.in.read();
        System.out.println("*******VERIFY Again after registration  Asset exist in Surfer *******");

        verifyAsset(asset, remoteAgent);


        //Adding provenance

        //listing
        System.out.println("******* Create an Listing and publish it ************");
        System.in.read();
        Listing listing = createListing(asset);

        // publish an Asset
        listing = publishAsset(listing);
        System.out.println();

        System.out.println("******* Listing created with metadata as below :***********");
        System.in.read();
        System.out.println(JSON.toPrettyString(listing.getMetaData()));

        // purchase an Asset
        System.out.println("******* Purchase an Asset *************");
        System.in.read();
        Purchase purchase = purchaseAsset(listing);
        System.out.println(JSON.toPrettyString(purchase.getMetaData()));

    }
}
