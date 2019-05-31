package sg.dex.starfish.samples;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.Purchase;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.ARemoteAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;
import sg.dex.starfish.util.JSON;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AssetSample {
    private static RemoteAgent remoteAgent = RemoteAgentConfig.getRemoteAgent();

    public static void main(String[] a) throws IOException {

        //1.Create Asset
        System.out.println("******* Create an Asset With Content -{2,3,4,5,6} -*************");
        System.in.read();
        Asset asset = createAnAsset();
        System.out.println("Asset Metadata :" + asset.getMetadata());
        //2/Register Asset
        System.out.println("******* Register an Asset ************");
        System.in.read();
        registerAsset(asset);

        //3.upload Asset
        System.out.println("******* Upload an Asset **************");
        System.in.read();
        uploadAsset(asset);
        System.out.println("Asset ID :" + asset.getAssetID());
        System.out.println("Content of Asset :" + Arrays.toString(asset.getContent()));
        //listing
        System.out.println("******* Create an Listing ************");
        System.in.read();
        Listing listing = createListing(asset);

        // publish an Asset
        System.out.println("******* Publish  an Asset Listing*******");
        System.in.read();
        listing = publishAsset(listing);
        System.out.println(JSON.toPrettyString(listing.getMetaData()));

        // purchase an Asset
        System.out.println("******* Purchase an Asset *************");
        System.in.read();
        Purchase purchase = purchaseAsset(listing);
        System.out.println(JSON.toPrettyString(purchase.getMetaData()));


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

    private static Asset createAnAsset() {
        byte data[] = {2, 3, 4, 5, 6};
        return MemoryAsset.create(data);
    }
}
