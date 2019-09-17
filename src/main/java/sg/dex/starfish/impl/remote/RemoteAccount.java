package sg.dex.starfish.impl.remote;

import sg.dex.starfish.impl.AAccount;

import java.util.HashMap;
import java.util.Map;

/**
 * Class is used to Remote Account Creation
 * this calls will have
 *
 * @author Ayush
 */
public class RemoteAccount extends AAccount {


    private Map<String, Object> userDataMap;

    /**
     * Create an AAccount with the provided ID
     *
     * @param id          The identifier for this account
     * @param credentials The credential for this account
     */
    protected RemoteAccount(String id, Map<String, Object> credentials) {
        super(id, credentials);
        userDataMap = new HashMap<>();
    }

    /**
     * This method is to create instance of Remote Account
     *
     * @param id          The identifier for this account
     * @param credentials The credential for this account
     * @return RemoteAccount RemoteAccount instance
     */
    public static RemoteAccount create(String id,
                                       Map<String, Object> credentials) {

        return new RemoteAccount(id, credentials);
    }

    /**
     * This method is to get the user data map.
     * Userdata map contains value related to user which may change like Role ,token,..
     *
     * @return userdata map
     */
    public Map<String, Object> getUserDataMap() {
        return userDataMap;
    }

    @Override
    public String toString() {
        final StringBuilder tmp = new StringBuilder();
        tmp.append(super.toString());
        tmp.append("userDataMap");
        tmp.append("{");
        for (Map.Entry<String, Object> entry : userDataMap.entrySet()) {
            tmp.append(entry.getKey());
            tmp.append("=");
            tmp.append(entry.getValue().toString());
            tmp.append(",");
        }
        tmp.append("}");
        return tmp.toString();
    }
}
