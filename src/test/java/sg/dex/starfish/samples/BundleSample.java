package sg.dex.starfish.samples;

import sg.dex.starfish.Asset;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.impl.remote.ARemoteAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteBundle;
import sg.dex.starfish.impl.url.RemoteHttpAsset;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;
import sg.dex.starfish.util.JSON;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BundleSample {

    private static RemoteAgent remoteAgent = RemoteAgentConfig.getRemoteAgent();

    public static void main(String[] arg) throws IOException {

        RemoteHttpAsset remoteHttpAsset1 = RemoteHttpAsset.create( "https://www.utdallas.edu/~lxz144130/cs6301-readings/rapps-tse85.pdf");
        RemoteHttpAsset remoteHttpAsset2 = RemoteHttpAsset.create("https://www.utdallas.edu/~lxz144130/cs6301-readings/rapps-tse85.pdf");

        String bundleName = "Training-Year-2017";

        RemoteBundle rb = createBundleWith_two_Sub_Asset(remoteHttpAsset1, remoteHttpAsset2, bundleName);
        System.in.read();
        System.out.println("*******DISPLAY CONTENT OF BUNDLE  *******");
        //get the SubAsset
        System.out.println(JSON.toPrettyString(rb.getMetadata()));

    }

    private static RemoteBundle createBundleWith_two_Sub_Asset(RemoteHttpAsset remoteHttpAsset1,
                                                               RemoteHttpAsset remoteHttpAsset2,
                                                               String bundleName) throws IOException {
        System.out.println("*******BUNDLE CREATION EXAMPLE *******");

        System.in.read();

        System.out.println("1 > *******CREATING SUB-ASSET form the url*******");
        System.out.println();
        System.out.println("Test Data url 1 : https://www.utdallas.edu/~lxz144130/cs6301-readings/rapps-tse85.pdf");
        System.out.println("Training data  2 https://www.utdallas.edu/~lxz144130/cs6301-readings/rapps-tse85.pdf");


        System.out.println("Two Sub-Asset created With ID :");
        System.out.println(remoteHttpAsset1.getAssetID());
        System.out.println(remoteHttpAsset2.getAssetID());


        System.in.read();

        System.out.println("*******VERIFY IF SUB-ASSET EXIST in SURFER *******");

        verifyAsset(remoteHttpAsset1, remoteAgent);
        verifyAsset(remoteHttpAsset2, remoteAgent);

        System.in.read();

        System.out.println("2 > ******CREATE-SUB ASSET MAP FOR BUNDLE with specific name and display *******");


        // create map of sub-asset where key is the name of the sub-asset
        Map<String, Asset> subAssetMap = new HashMap<>();
        subAssetMap.put("Software-Testing-Data", remoteHttpAsset1);
        subAssetMap.put("Software-Training-Data", remoteHttpAsset2);


        System.out.println(JSON.toPrettyString(subAssetMap));


        System.in.read();

        System.out.println(" 3 > *******CREATING And REGISTERING Bundle *******");
        Map<String, Object> meta = new HashMap<>();
        meta.put("name", bundleName);
        // create bundle with sub asset
        RemoteBundle remoteBundle = RemoteBundle.create(remoteAgent, subAssetMap, meta);
        remoteAgent.registerAsset(remoteBundle);

        System.in.read();
        System.out.println("*******VERIFY IF SUB-ASSET EXIST in Agent(Surfer) *******");
        verifyAsset(remoteHttpAsset1, remoteAgent);
        verifyAsset(remoteHttpAsset2, remoteAgent);


        // uploading the data of the sub-asset
        remoteAgent.uploadAsset(remoteHttpAsset1);
        remoteAgent.uploadAsset(remoteHttpAsset2);


        return remoteBundle;
    }

    private static void verifyAsset(Asset subAsset1, RemoteAgent remoteAgent) {


        try {
            ARemoteAsset aRemoteAsset = remoteAgent.getAsset(subAsset1.getAssetID());
            System.out.println("Asset exist :" + aRemoteAsset.getAssetID());
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

    }

}
