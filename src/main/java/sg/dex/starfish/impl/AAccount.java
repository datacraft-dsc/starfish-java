package sg.dex.starfish.impl;

import sg.dex.starfish.Account;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.util.DID;

/**
 * Class representing an Account in the Ocean Ecosystem
 *
 * Accounts are addressed with a W3C DID
 *
 * @author Tom
 *
 */
public abstract class AAccount implements Account {

	protected final Ocean ocean;
	protected DID did;
	protected String password;

	/**
	 * Create an account with the provided Ocean connection and DID
	 * and password
	 *
	 * @param ocean The ocean connection to use for this account
	 * @param did The DID for this account
	 * @param password The Account password
	 */
	protected AAccount(Ocean ocean, DID did, String password) {
		this.ocean=ocean;
		this.did=did;
		this.password=password;
	}

	/**
	 * Gets the DID for an Agent
	 *
	 * @return DID The DID that can be used to address this agent in the Ocean Ecosystem
	 */
	@Override
	public DID getDID() {
		return did;
	}

	/**
	 * Unlocks this Account (prevents any value transfer)
	 *
	 * @throws AuthorizationException if the password is invalid
	 */
	@Override
	public void unlock() {

	}

	/**
	 * Locks this Account (enables value transfer)
	 *
	 */
	@Override
	public void lock() {

	}

	/**
	 * Request some ocean tokens to be transfer to this Account
	 *
	 * @param amount The amount of ocean tokens to transfer
	 * @throws AuthorizationException if account is locekd
	 * @return number of tokens requestd
	 */
	@Override
	public int requestTokens(int amount) {
		return amount;
	}

}
