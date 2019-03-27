package sg.dex.starfish.samples;

import sg.dex.starfish.Listing;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteListing;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

        // create Listing
        Random rand = new Random();
        Utils.createRandomHexString(16);
        // creating listing object
        RemoteListing remoteListing1 =RemoteListing.create(surfer, "5317b2e7dd18b6090dbc58912ddef337f32fdd2a001e36edecf20eb50a2fe18c" );

        System.out.println(remoteListing1.getListingMetaData());


        // 1.get specific listing
        Map<String,Object>result2 =remoteListing1.getListingMetaData();
        System.out.println(remoteListing1.getListingMetaData());
        assertNotNull(result2);

        // 2.get all listing
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


        //4. create new Listing

        Map<String ,Object>  data2 = new HashMap<>();
        //data.put( "status", "unpublished");
        data2.put( "assetid", "3d2ddb86aac48f4c51427879fb7cea1ef49e546b49bb5d2078e93ce1ae362ede");
        RemoteListing newListing = RemoteListing.create(surfer,data2);

       // Listing newListing =remoteListing.createListing(data2);
        assertNotNull(newListing);

        // get meta data for newly created Listing
        System.out.println(newListing.getListingMetaData());



    }
}
