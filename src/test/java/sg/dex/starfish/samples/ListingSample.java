package sg.dex.starfish.samples;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sg.dex.starfish.Listing;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;

public class ListingSample {

    private static Properties getProperties() {
        Properties properties = new Properties();
        try {
            try (InputStream is = ListingSample.class.getClassLoader().getResourceAsStream("application_test.properties")) {
                properties.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void main(String arg[]) {

        RemoteAgent surfer = RemoteAgentConfig.getRemoteAgent();

        // 1.create Listing

        Listing listing = createNewListing(surfer);

        // 2.get  listing details
        Map<String,Object>result2 =listing.getMetaData();
        assertNotNull(result2);


        // 3.get all listing details
        List<Listing> allListingLst =surfer.getAllListing();
        assertNotNull(allListingLst);



        // 3. update existing Listing


        Map<String,Object> oldData = listing.getMetaData();
        String status = oldData.get("status").toString();
        String newVal = status.equals("unpublished")?"published":"unpublished";

        Map<String ,Object>  data = new HashMap<>();
        data.put( "status", newVal);
        data.put( "id", oldData.get("id"));

        Listing updateListing =surfer.updateListing(data);
        assertNotNull(updateListing);


    }

    private static Listing  createNewListing(RemoteAgent surfer) {
        Map<String ,Object> data2 = new HashMap<>();
        //data.put( "status", "unpublished");
        data2.put( "assetid", "3d2ddb86aac48f4c51427879fb7cea1ef49e546b49bb5d2078e93ce1ae362ede");
        String info = "{\n" +
        "    \"title\": \"Test\",\n" +
                "    \"description\": \"Listing test\"\n" +
                "  }";
        JSONObject json=null;
        JSONParser parser = new JSONParser();
        try {
          json= (JSONObject) parser.parse(info);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        data2.put( "info", json);
        Listing listing =surfer.createListing(data2);

        // Listing newListing =remoteListing.createListing(data2);
        assertNotNull(listing);
        assertNotNull(listing.getAsset());
        assertNotNull(listing.getInfo());
        assertNotNull(listing.getMetaData());
        //assertNotNull(listing.getAgreement());

        return listing;
    }
}
