package sg.dex.starfish.samples;

import sg.dex.starfish.Agent;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.Surfer;
import sg.dex.starfish.util.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * This class is used to get the Remote Agent based on the host.
 * Currently it will connect with default OCEAN.
 */
public class SurferConfig {

    private static Properties getProperties(){
        Properties properties = new Properties();
        try {
            try (InputStream is = SurferConfig.class.getClassLoader().getResourceAsStream("application_test.properties")) {
                properties.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  properties;
    }
    
    public static void main(String[] args) {
        // get ref of remmote agent

        Properties properties = getProperties();
        String ip =properties.getProperty("surfer.host");
        String port = properties.getProperty("surfer.port");

        RemoteAgent surfer = Surfer.getSurfer(ip+":"+port);
        Agent agent = surfer;
        // get already registered Asset.
//		Asset a = agent.getAsset("e399c658b8b5e260e946261b6dd19299e8dda7e9f810452deb4887bd702b0c11");
//
//		Map<String,Object> meta=a.getMetadata();
//		System.out.println(JSON.toPrettyString(meta));

        // test new Asset creation, registration and upload
        //1. cretion
        byte[] data = new byte[]{1, 2, 3};
        Asset m = MemoryAsset.create(data);

        //2. registration : it will just reg the asset and upload its metadata content  and will return a Remote Agent
        Asset newAsset = agent.registerAsset(m);
        // check if asset metadata is registered successfully
        Map<String, Object> metaofRegAsset = newAsset.getMetadata();
        System.out.println(JSON.toPrettyString(metaofRegAsset));

        //3. upload the asset content
        Asset a = agent.uploadAsset(m);
        //4. check if the upload was successful
        Asset downloaded = agent.getAsset(a.getAssetID());

        MemoryAsset result = MemoryAsset.create(downloaded);
        assertEquals(m.getAssetID(), result.getAssetID());


//
    }

}
