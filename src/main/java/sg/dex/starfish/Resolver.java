package sg.dex.starfish;

import com.oceanprotocol.squid.exceptions.*;

import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.JSONObjectCache;

import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.util.Map;

/**
 * This class implement the resolver functionality.
 * This class has method to  get a DDO (as a String) for any given DID registered on-chain.
 * Also it has method to register a DDO for a DID given an ethereum account.
 */
public interface Resolver {

    /**
     * Method to  get a DDO (as a String) for any given DID registered with the resolver.
     *
     * @param did Given DID which is registered with the resolver
     * @return DDO String value
     */
    String getDDOString(DID did);

    /**
     * Gets a DDO Object for a given DDO.
     * Returns null if the DDO cannot be found.
     *
     * @param did DID to resolve
     * @return The DDO as a JSON map
     * @throws UnsupportedOperationException not yet implemented
     */
    public default Map<String, Object> getDDO(DID did) {
    	did=did.withoutPath();
        String ddoString = getDDOString(did);
        if (ddoString==null) return null;
        return JSON.parse(ddoString);
    }
    
    /**
     * Method to register a DDO for a DID .
     *
     * @param did DID to register
     * @param ddoString DDO to associate with the given DID
     */
    void registerDID(DID did, String ddoString);
}
