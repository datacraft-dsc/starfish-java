package keeper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import sg.dex.starfish.impl.squid.DexResolver;
import sg.dex.starfish.util.DID;

import org.web3j.crypto.CipherException;
import java.io.IOException;

public class DexResolverTest {
    private DexResolver resolver;
    private String valueSet;
    private String valueUpdate;
    private DID did;
    private final String accountFrom = "0x1936111c43e86Ca38866fe015F58bbEC63c64EC5";
    private final String password = "1234567890";
    private final String credentialFile = "src/integration-test/resources/accounts/parity/0x1936111c43e86Ca38866fe015F58bbEC63c64EC5.json";

    public DexResolverTest() throws IOException, CipherException {
        resolver = DexResolver.create(accountFrom, password, credentialFile);
        valueSet = DID.createRandomString();
        valueUpdate = DID.createRandomString();
        did = DID.createRandom();
    }

    @Test
    public void testRegisterResolve()   {
        boolean result = resolver.registerDID(did, valueSet);
        assertTrue(result);
        String val = resolver.getDDOString(did);
        assertEquals(val, valueSet);
        result = resolver.registerDID(did, valueUpdate);
        assertTrue(result);
        val = resolver.getDDOString(did);
        assertEquals(val, valueUpdate);
    }

    @Test
    public void testGetInvalidDID()   {
        DID temp = DID.createRandom();
        String val = resolver.getDDOString(temp);
        assertNull(val);
    }
}
