package sg.dex.starfish;

import sg.dex.starfish.exception.ResolverException;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;

import java.util.Map;

/**
 * This interface represents a Resolver instance used to resolve / register DIDs and DDOd.
 */
public interface Resolver {

    /**
     * Method to  get a DDO (as a String) for any given DID registered with the resolver.
     *
     * @param did Given DID which is registered with the resolver
     * @return DDO String value or null
     * @throws ResolverException
     */
    String getDDOString(DID did) throws ResolverException;

    /**
     * Gets a DDO Object for a given DDO.
     * Returns null if the DDO cannot be found.
     *
     * @param did DID to resolve
     * @return The DDO as a JSON map
     * @throws UnsupportedOperationException not yet implemented
     */
    default Map<String, Object> getDDO(DID did) {
        did = did.withoutPath();
        String ddoString = getDDOString(did);
        if (ddoString == null) return null;
        return JSON.parse(ddoString);
    }

    /**
     * Method to register or update a DDO for a DID .
     *
     * @param did       DID to register or update
     * @param ddoString DDO to associate with the given DID
     * @return true if registration is successful, false otherwise
     * @throws ResolverException
     */
    void registerDID(DID did, String ddoString) throws ResolverException;
}
