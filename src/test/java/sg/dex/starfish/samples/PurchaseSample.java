package sg.dex.starfish.samples;

import sg.dex.starfish.Purchase;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.Surfer;
import sg.dex.starfish.util.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
        RemoteAgent surfer = Surfer.getSurfer(ip + ":" + port);

        // 1.create Listing

        Purchase purchase = createNewPurchase(surfer);


        // 2.get purchase details

        Map<String, Object> purchaseMetaData = purchase.getMetaData();
        assertNotNull(purchaseMetaData);


        // 3. update existing Listing


        Map<String, Object> oldData = purchase.getMetaData();
        String status = oldData.get("status").toString();
        String newVal = status.equals("ordered") ? "wishlist" : "ordered";
        Map<String, Object> newData = new HashMap<>();
        newData.put("status", newVal);
        newData.put("id", oldData.get("id"));

        System.out.println(JSON.toPrettyString(oldData));
        Purchase updatedPurchase = surfer.updatePurchase(newData);
        System.out.println(JSON.toPrettyString(updatedPurchase.getMetaData()));
        assertNotNull(updatedPurchase);


    }

    private static Purchase createNewPurchase(RemoteAgent surfer) {
        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put("listingid", "8f6827f7a8f64f2c3ee6e6d43a3198b68ec5fe05fd96d4d1d34a0294144c2fad");

        Purchase purchase = surfer.createPurchase(purchaseData);
        assertNotNull(purchase);
        assertNotNull(purchase.getMetaData());
        //assertNotNull(purchase.getInfo());
        assertNotNull(purchase.getListing());

        return purchase;
    }
}
