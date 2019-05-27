package sg.dex.starfish.samples;

import sg.dex.starfish.Purchase;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class PurchaseSample {



    public static void main(String[] arg) {

        RemoteAgent surfer = RemoteAgentConfig.getRemoteAgent();

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

        Purchase updatedPurchase = surfer.updatePurchase(newData);
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
