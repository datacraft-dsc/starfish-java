package keeper;

import org.junit.jupiter.api.Test;
import sg.dex.starfish.dexchain.DexChain;
import sg.dex.starfish.dexchain.DexConfig;
import sg.dex.starfish.dexchain.DexProvenance;
import sg.dex.starfish.util.DID;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProvenanceTest {
    private DexProvenance provenance;

    public ProvenanceTest() {
        provenance = DexProvenance.create();
    }

    @Test
    public void testRegisterAssetOne()  {
        DID assetId = DID.createRandom();
        provenance.registerAsset(assetId.getID());
        ArrayList<DexProvenance.DexProvenanceResult> results = provenance.getAssetProvenance(assetId.getID());
        assertTrue(results.size() == 1);
    }

    @Test
    public void testRegisterAssetTwo()  {
        DID assetId = DID.createRandom();
        provenance.registerAsset(assetId.getID());
        provenance.registerAsset(assetId.getID());
        ArrayList<DexProvenance.DexProvenanceResult> results = provenance.getAssetProvenance(assetId.getID());
        assertTrue(results.size() == 2);
    }

    @Test
    public void testCheckNonExisting()  {
        DID assetId = DID.createRandom();
        ArrayList<DexProvenance.DexProvenanceResult> results = provenance.getAssetProvenance(assetId.getID());
        assertTrue(results.size() == 0);
    }

    @Test
    public void testRegisterAssetSearchByUserAndAssetId()  {
        DID assetId = DID.createRandom();
        provenance.registerAsset(assetId.getID());
        DexConfig dexConfig = DexChain.getInstance().getDexConfig();
        ArrayList<DexProvenance.DexProvenanceResult> results = provenance.getAssetProvenance(assetId.getID(), dexConfig.getMainAccountAddress());
        assertTrue(results.size() == 1);
    }

    @Test
    public void testRegisterAssetSearchByUser()  {
        DID assetId = DID.createRandom();
        provenance.registerAsset(assetId.getID());
        DexConfig dexConfig = DexChain.getInstance().getDexConfig();
        ArrayList<DexProvenance.DexProvenanceResult> results = provenance.getAssetProvenanceByUser(dexConfig.getMainAccountAddress());
        assertTrue(results.size() > 0);
    }
}
