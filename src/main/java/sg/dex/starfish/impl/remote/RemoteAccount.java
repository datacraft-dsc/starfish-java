package sg.dex.starfish.impl.remote;


import sg.dex.starfish.impl.AAccount;

import java.util.HashMap;
import java.util.Map;

/**
 * Class is used to Remote Account Creation
 * this calls will have
 */
public class RemoteAccount extends AAccount {


    private Map<String, Object> userDataMap;

    /**
     * Create an AAccount with the provided ID
     *
     * @param id The identifier for this account
     */
    protected RemoteAccount(String id, Map<String, Object> credentialMap) {
        super(id,credentialMap);
        userDataMap = new HashMap<>();
    }

    public static RemoteAccount create(String id, Map<String, Object> credentialMap) {

        return new RemoteAccount(id, credentialMap);
    }

    public Map<String, Object> getUserDataMap() {
        return userDataMap;
    }

}
