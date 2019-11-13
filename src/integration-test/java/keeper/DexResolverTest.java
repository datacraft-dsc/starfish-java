package keeper;

import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.rules.ExpectedException;
import org.web3j.protocol.exceptions.TransactionException;
import sg.dex.starfish.impl.squid.DexResolver;
import sg.dex.starfish.util.DID;

import org.web3j.crypto.CipherException;
import java.io.IOException;

public class DexResolverTest {
    private DexResolver resolver1;
    private DexResolver resolver2;
    private String valueSet;
    private String valueUpdate;
    private DID did;
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    public DexResolverTest() throws IOException, CipherException {
        resolver1 = DexResolver.create("application_test.properties");
        resolver2 = DexResolver.create("application_resolver.properties");
        valueSet = DID.createRandomString();
        valueUpdate = DID.createRandomString();
        did = DID.createRandom();
    }

    @Test
    public void testRegisterResolve()   {
        boolean result = resolver1.registerDID(did, valueSet);
        assertTrue(result);
        String val = resolver1.getDDOString(did);
        assertEquals(val, valueSet);
        result = resolver1.registerDID(did, valueUpdate);
        assertTrue(result);
        val = resolver1.getDDOString(did);
        assertEquals(val, valueUpdate);
    }

    @Test
    public void testGetInvalidDID()   {
        DID temp = DID.createRandom();
        String val = resolver1.getDDOString(temp);
        assertNull(val);
    }

    @Test
    public void testPermissions()   {
        boolean result = resolver1.registerDID(did, valueSet);
        assertTrue(result);
        String val = resolver2.getDDOString(did);
        assertEquals(val, valueSet);
        exception.expect(TransactionException.class);
        resolver2.registerDID(did, valueUpdate);
    }
}
