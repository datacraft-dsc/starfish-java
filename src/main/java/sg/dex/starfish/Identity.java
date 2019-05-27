package sg.dex.starfish;

import java.util.Map;

/**
 * Interface representing an Identity in the Ocean ecosystem
 * @author Tom
 * @version 0.5
 */
public interface Identity {

	/**
	 * Gets the ID for an Identity
	 *
	 * @return Identity identifier
	 */
	public String getID();

	/**
	 * Gets the Accounts associated with this Identity
	 * where the key is the Account.getID();
	 * @return A map of Accounts for this entity
	 */
	public Map<String,Account> getAccounts();

	/**
	 * Adds an Account to be associated with this Identity
	 * @param account the Account to add
	 */
	public void addAccount(Account account);

}
