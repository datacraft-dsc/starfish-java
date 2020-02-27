package keeper;

import org.junit.jupiter.api.Test;
import sg.dex.starfish.dexchain.DexProvenance;
import sg.dex.starfish.util.DID;

public class ProvenanceTest {
    private DexProvenance provenance;
    private DID assetId;

    public ProvenanceTest() {
        provenance = DexProvenance.create();
        assetId = DID.createRandom();
    }

    @Test
    public void testRegisterAsset()  {
        provenance.registerAsset(assetId.getID());
        provenance.getAssetProvenance(assetId.getID());
    }
}
