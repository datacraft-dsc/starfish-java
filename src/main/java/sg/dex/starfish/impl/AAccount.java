package sg.dex.starfish.impl;

import sg.dex.starfish.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class representing an Account in the Ocean Ecosystem
 * <p>
 * This class include method to get the accoutn ID,
 * get the credential.
 * </p>
 *
 * @author Tom
 */
public abstract class AAccount implements Account {

    protected String id;
    private Map<String, Object> credentials;

    /**
     * Create an AAccount with the provided ID
     *
     * @param id The identifier for this account
     */
    protected AAccount(String id) {
        this.id = id;
        this.credentials = new HashMap<>();
    }

    protected AAccount(String id, Map<String, Object> credentials) {
        this.id = id;
        this.credentials = (credentials == null) ? new HashMap<>() : credentials;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public Map<String, Object> getCredentials() {
        // deep cloning the map
        return credentials.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(),
                        e -> e.getValue()));
    }

    @Override
    public String toString() {
        final StringBuilder tmp = new StringBuilder();
        tmp.append(getClass().getName());
        tmp.append(":");
        tmp.append(id);
        tmp.append("{");
        for (Map.Entry<String, Object> entry : credentials.entrySet()) {
            tmp.append(entry.getKey());
            tmp.append("=");
            tmp.append(entry.getValue().toString());
            tmp.append(",");
        }
        tmp.append("}");
        return tmp.toString();
    }
}
