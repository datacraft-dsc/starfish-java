package keeper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import sg.dex.starfish.impl.squid.SquidResolverImpl;
import sg.dex.starfish.util.DID;

import org.web3j.crypto.CipherException;
import java.io.IOException;

public class SquidResolverTest {
    private  SquidResolverImpl resolver;
    private String valueSet;
    private String valueUpdate;
    private DID did;

    public SquidResolverTest() throws IOException, CipherException {
        resolver = SquidResolverImpl.create();
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
}
