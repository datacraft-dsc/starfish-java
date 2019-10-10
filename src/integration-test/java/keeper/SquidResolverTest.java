package sg.dex.starfish.integration.keeper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import sg.dex.starfish.impl.squid.SquidResolverImpl;
import sg.dex.starfish.util.DID;

import org.web3j.crypto.CipherException;
import java.io.IOException;

public class SquidResolverTest {
    private  SquidResolverImpl resolver;
    private String value;
    private DID did;

    public SquidResolverTest() throws IOException, CipherException {
        resolver = SquidResolverImpl.create();
        value = DID.createRandomString();
        did = DID.createRandom();
    }

    @Test
    public void testRegister()   {
        resolver.registerDID(did, value);
    }

    @Test
    public void testResolve() {
        String val = resolver.getDDOString(did);
        assertEquals(val, value);
    }
}