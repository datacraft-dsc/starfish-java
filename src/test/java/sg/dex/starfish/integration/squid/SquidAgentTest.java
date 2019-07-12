package sg.dex.starfish.integration.squid;

import com.oceanprotocol.squid.exceptions.DDOException;
import com.oceanprotocol.squid.exceptions.DIDFormatException;
import com.oceanprotocol.squid.exceptions.EncryptionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.squid.SquidAgent;
import sg.dex.starfish.impl.squid.SquidAsset;
import sg.dex.starfish.util.DID;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class SquidAgentTest {


    private SquidAgent squidAgent;
    private Ocean ocean = SquidHelperTest.getOcean();
    private Map<String, String> configMap = SquidHelperTest.getPropertiesMap();
    private SquidAsset squidAsset;

    @Before
    public void setup() {
        // create random DID
        DID did = DID.createRandom();
        squidAgent = SquidAgent.create(configMap, ocean, did);

        byte[] data = {1, 2, 3, 4};

        MemoryAsset asset = MemoryAsset.create(data);
        assertNotNull(asset);
        // register created asset
        // any sf asset
        squidAsset = squidAgent.registerAsset(asset);
    }

    @Test
    public void testRegisterAsset() {
        byte[] data = {1, 2, 3, 4};

        MemoryAsset asset = MemoryAsset.create(data);
        assertNotNull(asset);
        // register created asset
        // any sf asset
        SquidAsset squidAsset = squidAgent.registerAsset(asset);

        assertNotNull(squidAsset);
    }

    @Test
    public void testGetAssetByAssetDID() {

        SquidAsset squidAsset_1 = squidAgent.getAsset(squidAsset.getAssetID());
        assertNotNull(squidAsset_1);
        // assertEquals(squidAsset_1.getAssetID(), squidAsset.getAssetID());


    }

    @Test
    public void testUploadAsset() {
        throw new UnsupportedOperationException("Upload not supported by squid ");
    }

    @Test
    public void testGetAssetByAssetID() {
        assertNull(squidAgent.getAsset(squidAsset.getAssetID()));
    }

    @Test
    public void testSearchAsset() throws DDOException {
        List<SquidAsset> allSquidAsset = squidAgent.searchAsset("Test_Developer");
        assertNotNull(allSquidAsset);

    }

    @Test
    public void testEncryption() throws DIDFormatException, EncryptionException {

        String did = com.oceanprotocol.squid.models.DID.builder().getHash();

        String  test ="Testting";
        String encryptedDocument = ocean.getOceanAPI().getSecretStoreAPI().encrypt(did, test, 0);

        String decryptedDocument = ocean.getOceanAPI().getSecretStoreAPI().decrypt(did, encryptedDocument);

        assertEquals(test, decryptedDocument);
    }

    @Test
    public void testConsumeAsset() throws IOException, URISyntaxException {
       // BrizoService.consumeUrl()
       // HttpHelper.downloadResource("https://www.robots.ox.ac.uk/~vgg/publications/2012/parkhi12a/parkhi12a.pdf", "/Users/ayush/Downloads/123/");
    }
}
