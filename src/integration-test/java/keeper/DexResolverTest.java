package keeper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.exceptions.TransactionException;
import sg.dex.starfish.impl.squid.DexResolver;
import sg.dex.starfish.util.DID;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;


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
        resolver1.registerDID(did, valueSet);
        String val = resolver1.getDDOString(did);
        assertTrue(val.equals( valueSet));
        resolver1.registerDID(did, valueUpdate);
        val = resolver1.getDDOString(did);
        assertTrue(val.equals( valueUpdate));
    }

    @Test
    public void testGetInvalidDID()   {
        DID temp = DID.createRandom();
        String val = resolver1.getDDOString(temp);
        assertTrue(val==null);
    }

    @Test
    public void testPermissions()   {
        resolver1.registerDID(did, valueSet);
        String val = resolver2.getDDOString(did);
        assertTrue(val.equals(valueSet) );
        exception.expect(TransactionException.class);
        resolver2.registerDID(did, valueUpdate);
    }
}
