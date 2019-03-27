package sg.dex.starfish.samples;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sg.dex.starfish.Listing;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteListing;

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
            try (InputStream is = SurferConfig.class.getClassLoader().getResourceAsStream("application_test.properties")) {
                properties.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void main(String arg[]) {
        Properties properties = getProperties();
        String ip = properties.getProperty("surfer.host");
        String port = properties.getProperty("surfer.port");
        RemoteAgent surfer = SurferConfig.getSurfer(ip + ":" + port);

        // 1.create Listing

        RemoteListing remoteListing1 = createNewListing(surfer);
        System.out.println("Listing is created....");

        // 2.get  listing details
        Map<String,Object>result2 =remoteListing1.getListingMetaData();
        System.out.println(result2);
        assertNotNull(result2);


        // 3.get all listing details
        List<RemoteListing> allListingLst =remoteListing1.getAllListing();
        assertNotNull(allListingLst);



        // 3. update existing Listing

        Map<String ,Object>  data = new HashMap<>();
        Map<String,Object> oldData = remoteListing1.getListingMetaData();
        String status = oldData.get("status").toString();
        String newVal = status.equals("unpublished")?"published":"unpublished";
        data.put( "status", newVal);

        System.out.println(remoteListing1.getListingMetaData());
        Listing updated =remoteListing1.updateListing(data);
        System.out.println(((RemoteListing) updated).getListingMetaData());
        assertNotNull(updated);


    }

    private static RemoteListing  createNewListing(RemoteAgent surfer) {
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
        RemoteListing newListing = RemoteListing.create(surfer,data2);

        // Listing newListing =remoteListing.createListing(data2);
        assertNotNull(newListing);

        // get meta data for newly created Listing
        System.out.println(newListing.getListingMetaData());

        // testing info
        // test info API
        Map<String,Object> testInfo =newListing.getInfo();
        System.out.println("Test info is "+ testInfo);
        assertNotNull(testInfo);

        return newListing;
    }
}
