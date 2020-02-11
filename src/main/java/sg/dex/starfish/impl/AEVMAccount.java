package sg.dex.starfish.impl;

import java.util.HashMap;

/**
 * Class representing an Ethereum Account 
 *
 * @author Tom
 */
public abstract class AEVMAccount extends AAccount {

    protected String password;

    /**
     * Create an account with the provided ID and password
     *
     * @param id       The identifier for this account
     * @param password The Account password
     */
    protected AEVMAccount(String id, String password) {
        super(id, new HashMap<>());
        this.password = password;
    }

    /**
     * Gets the address for this Ethereum Account
     *
     * @return Account identifier
     */
    public String getAddress() {
        return id;
    }

    /**
     * Returns the Balance of an account
     * TODO: Needs concrete implementation
     *
     * @return the Balance of the account
     * @throws EthereumException EthereumException
     */
    abstract public Object balance();

}
