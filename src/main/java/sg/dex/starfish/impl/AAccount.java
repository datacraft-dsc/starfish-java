package sg.dex.starfish.impl;

import sg.dex.starfish.Account;

/**
 * Class representing an Account in the Ocean Ecosystem
 *
 * @author Tom
 *
 */
public abstract class AAccount implements Account {

	protected String id;

	/**
	 * Create an AAccount with the provided ID
	 *
	 * @param id The identifier for this account
	 */
	protected AAccount(String id) {
		this.id=id;
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
}
