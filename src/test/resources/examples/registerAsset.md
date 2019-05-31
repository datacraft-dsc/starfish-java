 Asset
============================

Types of Asset :

	Data Asset
	Operation Asset
	Bundle Asset
Register,Upload,Listing of an Asset.
===================================

1.Creating a new Ocean instance
-----------------------------
First import the main starfish ocean library, and the logging library
  Ocean ocean=Ocean.connect();

2.Creating  an Account Instance:
--------------------------------------
	Now we need to load an account and see how much ocean tokens and Etherum ether we have. We will always need some ether to be able to pay for the transaction costs to register and buy an asset on the Ethereum network. For our test Ocean network, we will use some ethereum for registering an asset, but no ocean tokens, since we are not purchasing an asset yet
	Eg:
        
        Map<String,Object> credentialMap = new HashMap<>();
        credentialMap.put("username",userName);
         credentialMap.put("password",password);
         return RemoteAccount.create(Utils.createRandomHexString(32), credentialMap);

3.Create Data Asset
-----------------
Data asset can be local(in-memory) or remote(URL/remote path)

 1.Create Data Asset from local(in-momory) from byte Array
 
    byte[] data = new byte[]{1, 2, 3};
    Asset a = MemoryAsset.create(data);
   
   The above line will create a memory asset. This asset have basic metadata as below:
   
    - DATE_CREATED
    - CONTENT_HASH, hash);
    - TYPE, DATA_SET);
    -SIZE, Integer.toString(data.length));
    -CONTENT_TYPE, OCTET_STREAM);
    
   To test you can print the metada
   
      System.out.print(a.getMetadata());

  2.To create data assst from string literal:( it will also have same metadata a above)
  
     MemoryAsset memoryAsset = MemoryAsset.create("Test Content Size".getBytes());

  3.To create data assst from File, eg:
  
      FileAsset fa=FileAsset.create(file);

4.Setup the Agent
-----------------
We now need an agent to register and manage our Asset. The agents 
task is to do the actual work of registration.

    DID did = DID.createRandom();
    RemoteAgent agentI =RemoteAgent.create(ocean,did,remoteAccount);


5.Register the Asset
------------------
Now we can register the asset with the ocean account. This will return
a reference of Remote Asset which.

	Asset remoteAsset = agentI.registerAsset(asset);
	
6.Upload an Asset
------------------
 Upload will check if the asset is already registered if not it will first register than upload.
 Also upload is only for Data Asset, if Asset is Operation or Bundle ,an Exception will be thrown.
        
	RemoteAsset ra = (RemoteAsset)remoteAgent.uploadAsset(asset);
7.Listing an Asset
------------------
To create listing ,assetId is mandatory.By default asset will be in un-published 

 	Map<String, Object> data2 = new HashMap<>();
 	data2.put("assetid", remoteAsset.getAssetID());
 	Listing listing = remoteAgent.createListing(data2);
	
8.Publish as asset
-------------------
To publish an asset ,its status need to be modify.
Listing listing = remoteAgent.createListing(getListingMetaDataMap());

        // updating the listing meta data map
        listingMetaData.put("id", listing.getMetaData().get("id"));
        listingMetaData.put("status", "published");

        // updating the listing with new values
        remoteAgent.updateListing(listingMetaData);
 8.Purchase an Asset
---------------------
To Purcahse an Asset ,listing should be created and status must be publishd.
ListingId is mandatory for Purchase.

        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put("listingid", listing.getMetaData().get("id"));
        Purchase purchase = remoteAgent.createPurchase(purchaseData);
	
Full Example:
-------------------

         DID did=getInvokeDid();
         RemoteAccount remoteAccount = getRemoteAccount("Aladdin","OpenSesame");
         Ocean ocean=Ocean.connect();
         ocean.registerLocalDID(did,getDdo());

         private String getDdo(){
         Map<String, Object> ddo = new HashMap<>();
         List<Map<String, Object>> services = new ArrayList<>();

         services.add(Utils.mapOf(
                "type", "Ocean.Invoke.v1",
                "serviceEndpoint", "http://localhost:3000/api/v1" ));
         services.add(Utils.mapOf(
                "type", "Ocean.Meta.v1",
                "serviceEndpoint", "http://localhost:8080" + "/api/v1/meta"));
        services.add(Utils.mapOf(
                "type", "Ocean.Storage.v1",
                "serviceEndpoint", "http://localhost:8080" + "/api/v1/assets"));
        services.add(Utils.mapOf(
                "type", "Ocean.Auth.v1",
                "serviceEndpoint", "http://localhost:8080" + "/api/v1/auth"));
         ddo.put("service", services);
         return JSON.toPrettyString(ddo);

       }

       private RemoteAccount getRemoteAccount(String userName,String password){
        //Creating remote Account
         Map<String,Object> credentialMap = new HashMap<>();
         credentialMap.put("username",userName);
         credentialMap.put("password",password);
        return RemoteAccount.create(Utils.createRandomHexString(32), credentialMap);

     }

        String data = "Simple  Asset";
        Asset asset = MemoryAsset.create(data);

        RemoteAgent agentI =RemoteAgent.create(ocean,did,remoteAccount);
         Asset remoteAsset = agentI.registerAsset(asset);
	 // listing
	 Map<String, Object> data2 = new HashMap<>();
 	data2.put("assetid", remoteAsset.getAssetID());
 	Listing listing = remoteAgent.createListing(data2)
	
	//publish
	// updating the listing meta data map
        listingMetaData.put("id", listing.getMetaData().get("id"));
        listingMetaData.put("status", "published");

        // updating the listing with new values
        remoteAgent.updateListing(listingMetaData);
	
	//Purchase
	Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put("listingid", listing.getMetaData().get("id"));
        Purchase purchase = remoteAgent.createPurchase(purchaseData);
	
	}
	



