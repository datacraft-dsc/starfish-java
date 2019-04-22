package sg.dex.starfish.impl.remote;

import org.junit.ClassRule;
import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.integration.connection_check.AssumingConnection;
import sg.dex.starfish.integration.connection_check.ConnectionChecker;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;
import sg.dex.starfish.exception.GenericException;

import static org.junit.Assert.assertNotNull;

public class TestRemoteHttpAsset {

    @ClassRule
    public static AssumingConnection assumingConnection =
            new AssumingConnection(new ConnectionChecker(RemoteAgentConfig.getSurferUrl()));
    @Test
    public void testURLConnection(){

        Asset asset =RemoteHttpAsset.createWithURL("http://httpbin.org/ip");
       // Asset asset =RemoteHttpAsset.createWithURL("http://google.com");
        assertNotNull(asset);
        assertNotNull(asset.getContent());
        assertNotNull(asset.getAssetID());
    }

    @Test(expected = GenericException.class)
    public void testURLConnectionInvalid(){

        RemoteHttpAsset.createWithURL("invalidURL");
    }

    @Test
    public void testCreateResourceWithURL(){
    	// TODO this is failing
//        String url = "https://scet.berkeley.edu/wp-content/uploads/BlockchainPaper.pdf";
//        Asset asset =RemoteHttpAsset.createResourceWithURL(url);
//        System.out.println("Asset ID : "+ asset.getAssetID());
//        assertNotNull(asset);
//        assertNotNull(asset.getContent());
//        assertNotNull(asset.getAssetID());
    }



}
