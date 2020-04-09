package usecase.invoke;

import common.AgentFactory;
import sg.dex.starfish.Agent;
import sg.dex.starfish.Asset;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.Operation;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.remote.RemoteDataAsset;
import sg.dex.starfish.impl.resource.ResourceAsset;
import sg.dex.starfish.util.ProvUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * USE CASE 01:
 * <p>
 * One user say user1  has data and other user say user 2  has operation and
 * user1 wanted to run operation on his data and result should be also publish
 * to an Agent(user agent)
 * </p>
 * <p>
 * Example:
 * <p>
 * User 1 - this user has two data set one is  vehicle Data and other is
 * workshop data.
 * This data can be in local pc/DB/Cloud storage.
 * In this example I have used data present in local pc .User upload( will be
 * registered als)
 * these two data set on Remote Agent(Surfer,http://52.230.82.125:3030/)
 * User 2- Running compute service on different Remote Agent(KOI,http://52.230.52.223:8191/)
 * (Azure instance in this case).
 *
 * <p>
 * To implement the use case , only things user 1 has to know the ID where the
 * compute service is running .
 * <p>
 * Implementation details:
 * <p>
 * 1. User 1 will register its both data asset Vehicle data and Workshop data
 * 2. CAll starfish API passing the ID of register asset and the compute ID .
 * 3.Compute will run(ASYNC/SYNC) the operation on the given data asset and
 * register the result as a new Asset to Surfer.
 * 3. Compute will return Asset to the user 1.
 * </p>
 */
public class InvokeExample {


    public static void main(String[] a) throws IOException, URISyntaxException {

        InvokeExample invokeExample = new InvokeExample();
        invokeExample.invokeDemo_Basic();

    }

    private void invokeDemo_Basic() throws IOException, URISyntaxException {

        System.out.println("Create Surfer and KOI instance!! ");
        // Initializing the Agent

        Agent surfer = AgentFactory.getSurfer();
        Agent koi = AgentFactory.getKOI();

        System.in.read();

        // uploading the Data Asset on Surfer

        System.out.println("Register and upload the Vehicle and Workshop data");

        System.in.read();

        Map<String, Object> additionalMetadata = new HashMap<>();
        additionalMetadata.put(Constant.PROVENANCE,
                ProvUtil.createPublishProvenance("Merge Operation", "vehicle_13179"));


        DataAsset vehicleData =
                ResourceAsset.create("assets/vehicle.json");
        // DataAsset vehicle_13180 = ResourceAsset.create("assets/vehicle_13180.json");
        DataAsset workshopData = ResourceAsset.create("assets/workshop.json");


        RemoteDataAsset workshopDataRemote = surfer.uploadAsset(workshopData);
        RemoteDataAsset vehicle_13179_R = surfer.uploadAsset(vehicleData);
        //  RemoteDataAsset vehicle_13180_R = surfer.uploadAsset(vehicle_13180);

        System.out.println("Data is Registered successfully !!!");
        System.out.println();
        System.out.println("Asset ID of registered assets: ");
        System.out.println();
        System.out.println(vehicle_13179_R.getAssetID());
        //  System.out.println(vehicle_13180_R.getAssetID());
        System.out.println();
        System.out.println();

        System.out.println("Workshop data Asset ID: ");
        System.out.println();
        System.out.println(workshopDataRemote.getAssetID());

        System.in.read();


        System.out.println("Creating input for invoke operation");
        // prepare input for the invoke operation
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("workshop-dataset", workshopDataRemote);
        inputMap.put("vehicle-dataset", vehicle_13179_R);

//        inputMap.put("vehicle-dataset", vehicle_13180_R);


//        System.in.read();
//
//        System.out.println("Input to the Operation :");
//        System.out.println();
//        System.out.println((JSONObject)JSON.parse(JSON.toString(inputMap)));

//        System.in.read();

        //System.out.println("getting operation based on ID be2108d9e3221689482fec43c06fbf9b92d455a84548cfe9b3e7a01ea41bd115");

        // get the operation which need to be performed on the data
        Operation remoteOperation = koi.
                getAsset("be2108d9e3221689482fec43c06fbf9b92d455a84548cfe9b3e7a01ea41bd115");

        System.in.read();
        System.out.println("Invoking operation.....");
        // invoking operation on the registered Asset
        Map<String, Object> remoteAsset = remoteOperation.invokeResult(inputMap);

        System.in.read();
        for (Map.Entry<String, Object> res : remoteAsset.entrySet()) {
            Object r = res.getValue();
            if (r instanceof Asset) {
                Asset asset = (Asset) r;
                System.out.println(asset.getDID());
            }

        }

//        System.out.println("Provenance data for vehicle_13179");
//
//        String provData =vehicle_13179_R.getMetadata().get(Constant.PROVENANCE).toString();
//        System.out.println(JSON.toPrettyString(JSON.parse(provData)));
    }


}
