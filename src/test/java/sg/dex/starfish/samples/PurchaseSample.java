package sg.dex.starfish.samples;

import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemotePurchase;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;

public class PurchaseSample {

    private static Properties getProperties() {
        Properties properties = new Properties();
        try {
            try (InputStream is = SurferConfig.class.getClassLoader().getResourceAsStream("application_test.properties")) {
                properties.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void main(String[] arg) {
        Properties properties = getProperties();
        String ip = properties.getProperty("surfer.host");
        String port = properties.getProperty("surfer.port");
        RemoteAgent surfer = SurferConfig.getSurfer(ip + ":" + port);

        // 1.create Listing

        RemotePurchase remotePurchase1 = createNewPurchase(surfer);


        // 2.get  listing details
        Map<String, Object> result2 = remotePurchase1.getPurchasingMetaData();
        System.out.println(result2);
        assertNotNull(result2);


//        // 3.get all listing details
        try {
            List<RemotePurchase> allPurchaseLst = remotePurchase1.getAllPurchasing();
            assertNotNull(allPurchaseLst);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }



        // 3. update existing Listing

        Map<String, Object> data = new HashMap<>();
        Map<String, Object> oldData = remotePurchase1.getPurchasingMetaData();
        String status = oldData.get("status").toString();
        String newVal = status.equals("ordered") ? "wishlist" : "ordered";
        data.put("status", newVal);

        System.out.println(remotePurchase1.getPurchasingMetaData());
        RemotePurchase updated = remotePurchase1.updatePurchase(data);
        System.out.println(updated.getPurchasingMetaData());
        assertNotNull(updated);


    }

    private static RemotePurchase createNewPurchase(RemoteAgent surfer) {
        Map<String, Object> data2 = new HashMap<>();
        data2.put("listingid", "8f6827f7a8f64f2c3ee6e6d43a3198b68ec5fe05fd96d4d1d34a0294144c2fad");

        RemotePurchase newListing = RemotePurchase.create(surfer, data2);

        // Listing newListing =remoteListing.createListing(data2);
        assertNotNull(newListing);

        // get meta data for newly created Listing
        System.out.println(newListing.getPurchasingMetaData());


        return newListing;
    }
}
