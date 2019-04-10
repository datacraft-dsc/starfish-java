package sg.dex.starfish.impl;

import sg.dex.starfish.Account;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class representing an Account in the Ocean Ecosystem
 *
 * @author Tom
 */
public abstract class AAccount implements Account {

    protected String id;
    private Map<String, Object> credentialMap;

    /**
     * Create an AAccount with the provided ID
     *
     * @param id The identifier for this account
     */
    protected AAccount(String id,Map<String, Object> credentialData) {
        this.id = id;
        credentialMap = credentialData;
    }


    /**
     * Gets the ID for an Account
     *
     * @return Account identifier
     */
    @Override
    public String getID() {
        return id;
    }

    @Override
    public Map<String, Object> getCredentials() {
        // deep cloning the map
        return  credentialMap.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }
}
