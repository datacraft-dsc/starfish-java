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
    private final String accountFrom = "0x413c9ba0a05b8a600899b41b0c62dd661e689354";
    private final String password = "ocean_secret";
    private final String credentialFile = "src/main/resources/accounts/parity/0x413c9ba0a05b8a600899b41b0c62dd661e689354.json";

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
