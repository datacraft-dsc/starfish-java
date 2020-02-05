package sg.dex.starfish.impl;

import sg.dex.starfish.Account;
import sg.dex.starfish.Identity;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing an Identity in the Ocean Ecosystem
 *
 * @author Tom
 */
public abstract class AIdentity implements Identity {

    protected String id;
    protected Map<String, Account> accounts;

    /**
     * Create an AIdentity with the provided ID
     *
     * @param id The identifier for this account
     */
    protected AIdentity(String id) {
        this.id = id;
        this.accounts = new HashMap<String, Account>();
    }

    /**
     * Gets the ID for an Identity
     *
     * @return Identity identifier
     */
    @Override
    public String getID() {
        return id;
    }

    /**
     * Gets the Accounts associated with this Identity
     * where the key is the Account.getID();
     *
     * @return accounts
     */
    @Override
    public Map<String, Account> getAccounts() {
        return accounts;
    }

    /**
     * Adds an Account to be associated with this Identity
     *
     * @param account the Account to add
     */
    @Override
    public void addAccount(Account account) {
        accounts.put(account.getID(), account);
    }

}
