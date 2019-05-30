Creation and Testing an Bundle Asset
============================

Asset in Strafish can be of any of the 3 types :
    -Data Asset
    -Operation Asset
    -Bundle Asset

Creating a new Ocean instance
-----------------------------
First import the main starfish ocean library, and the logging library
  Ocean ocean=Ocean.connect();

Creating  an Account Instance:
--------------------------------------
    Now we need to load an account and see how much ocean tokens and Etherum ether we have. We will always need some ether to be able to pay for the transaction costs to register and buy an asset on the Ethereum network. For our test Ocean network, we will use some ethereum for registering an asset, but no ocean tokens, since we are not purchasing an asset yet
    Eg:
        
        Map<String,Object> credentialMap = new HashMap<>();
        credentialMap.put("username",userName);
         credentialMap.put("password",password);
         return RemoteAccount.create(Utils.createRandomHexString(32), credentialMap);

Create Bundle Asset
--------------------
Bundle asset can have zero or more Sub-asset.
Sub-Asset can be added to bundle either at the time of Bundle creation or may be after bundle creation.

 >>> Create a new Bundle Asset with two sub-Asset.
   1.Creat Sub-Asset
       //Create a sub-asset for bundle
        Asset subAsset1 = createSubAsset("First Sub -Asset");
        Asset subAsset2 = createSubAsset("Second Sub -Asset");
   2.create map of sub-asset where key is the name of the sub-asset

        Map<String, Asset> subAssetMap = new HashMap<>();
        subAssetMap.put("First-Asset", subAsset1);
        subAssetMap.put("Second-Asset", subAsset2);
   3. Create bundle with sub asset
        RemoteBundle remoteBundle = RemoteBundle.create(remoteAgent, subAssetMap);
   4. Register the Bundle
        ARemoteAsset remoteAsset = remoteAgent.registerAsset(remoteBundle);
   5.Display bundle content:
     System.out.println(JSON.toPrettyString(remoteBundle.getMetadata()));


Full Example:
-------------------

         package sg.dex.starfish.samples;

import sg.dex.starfish.Asset;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.ARemoteAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteBundle;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;
import sg.dex.starfish.util.JSON;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BundleSample {

    public static void main(String[] arg) throws IOException {

        System.in.read();
        System.out.println("*******CREATING SUB-ASSET *******");
        //Create a sub-asset for bundle
        Asset subAsset1 = createSubAsset("First Sub -Asset");
        Asset subAsset2 = createSubAsset("Second Sub -Asset");

        System.out.println("Two Sub-Asset created With ID :");
        System.out.println(subAsset1.getAssetID());
        System.out.println(subAsset2.getAssetID());


        //get the Remote Agent (in this eg it is Surfer)
        RemoteAgent remoteAgent = RemoteAgentConfig.getRemoteAgent();

        System.in.read();
        System.out.println("*******VERIFY IF SUB-ASSET EXIST in SURFER *******");
        verifyAsset(subAsset1, remoteAgent);
        verifyAsset(subAsset2, remoteAgent);

        System.in.read();
        System.out.println("******CREATE-SUB ASSET MAP FOR BUNDLE with name *******");

        // create map of sub-asset where key is the name of the sub-asset
        Map<String, Asset> subAssetMap = new HashMap<>();
        subAssetMap.put("First-Asset", subAsset1);
        subAssetMap.put("Second-Asset", subAsset2);

        System.out.println(JSON.toPrettyString(subAssetMap));
        System.in.read();
        System.out.println("*******CREATING And REGISTERING Bundle *******");
        // create bundle with sub asset
        RemoteBundle remoteBundle = RemoteBundle.create(remoteAgent, subAssetMap);

        // Register the Bundle
        ARemoteAsset remoteAsset = remoteAgent.registerAsset(remoteBundle);
        System.in.read();
        System.out.println("*******VERIFY IF SUB-ASSET EXIST in Agent(Surfer) *******");
        verifyAsset(subAsset1, remoteAgent);
        verifyAsset(subAsset2, remoteAgent);

        System.out.println("*******VERIFY IF Bundle Registered Successfully *******");
        System.in.read();
        // verify bundle and Sub-asset in Surfer
        ARemoteAsset remoteBundle_V = remoteAgent.getAsset(remoteAsset.getAssetID());

        System.out.println("*******VERIFY Bundle  *******");
        System.out.println("Created BundleID :  " + remoteAsset.getAssetID());
        System.out.println("Bundle From Agent : " + remoteBundle_V.getAssetID());

        System.in.read();
        System.out.println("*******DISPLAY CONTENT OF BUNDLE  *******");
        System.in.read();
        //get the SubAsset
        System.out.println(JSON.toPrettyString(remoteBundle.getMetadata()));


    }

    private static void verifyAsset(Asset subAsset1, RemoteAgent remoteAgent) throws IOException {

        System.in.read();
        //verify id these Asset exist in Surfer
        try {
            ARemoteAsset aRemoteAsset = remoteAgent.getAsset(subAsset1.getAssetID());
            System.out.println("Asset exist :" + aRemoteAsset.getAssetID());
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

    }

    private static Asset createSubAsset(String data) {
        return MemoryAsset.create(data);
    }
}



