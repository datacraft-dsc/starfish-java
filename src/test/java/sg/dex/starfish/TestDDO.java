package sg.dex.starfish;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.impl.memory.LocalResolverImpl;
import sg.dex.starfish.util.DID;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;


public class TestDDO {

    @Test
    public void testLocalDDO() {

        Resolver resolver = new LocalResolverImpl();
        String endpoint = "http://localhost:8080/api/v1/meta";
        String ddoString = "{\"service\" [{\"type\" : \"Ocean.Meta.v1\" , \"serviceEndpoint\": \"" + endpoint + "\"}]}";
        DID did = DID.createRandom();
        resolver.registerDID(did, ddoString);

        String ddos = resolver.getDDOString(did);
        Assertions.assertEquals(ddoString, ddos);

        Map<String, Object> ddo = resolver.getDDO(did);
        Assertions.assertEquals(1, ddo.size());

    }

    @Test
    public void testMissingDDO() {
        Resolver resolver = new LocalResolverImpl();
        DID did = DID.createRandom();
        String ddo = resolver.getDDOString(did);
        assertNull(ddo);
    }

    @Test
    public void testDIDwithoutPath() {
        DID did1 = DID.createRandom();
        DID did2 = did1.withPath("foo");

        DID did3 = did2.withoutPath();
        Assertions.assertEquals(did1, did3);
    }
}
