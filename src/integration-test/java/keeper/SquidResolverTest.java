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
    private String value1;
    private String value2;
    private DID did;

    public SquidResolverTest() throws IOException, CipherException {
        resolver = SquidResolverImpl.create();
        value1 = DID.createRandomString();
        value2 = DID.createRandomString();
        did = DID.createRandom();
    }

    @Test
    public void testRegisterResolve()   {
        boolean result = resolver.registerDID(did, value1);
        assertTrue(result);
        String val = resolver.getDDOString(did);
        assertEquals(val, value1);
        result = resolver.registerDID(did, value2);
        assertTrue(result);
        val = resolver.getDDOString(did);
        assertEquals(val, value2);
    }
}
