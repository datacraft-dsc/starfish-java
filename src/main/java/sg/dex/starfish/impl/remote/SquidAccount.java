package sg.dex.starfish.impl.remote;

import sg.dex.starfish.impl.AAccount;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.util.DID;

/**
 * Class implementing a remote storage account using the Storage API
 *
 * @author Mike
 *
 */
public class SquidAccount extends AAccount {

	/**
	 * Creates a SquidAccount with the specified Ocean connection and DID
	 * and password
	 *
	 * @param ocean Ocean connection to use
	 * @param did DID for this account
	 * @param password The Account password
	 */
	protected SquidAccount(Ocean ocean, DID did, String password) {
		super(ocean, did, password);
	}

	/**
	 * Creates a SquidAccount with the specified Ocean connection and DID
	 * and password
	 *
	 * @param ocean Ocean connection to use
	 * @param did DID for this account
	 * @param password The Account password
	 * @return SquidAccount
	 */
	public static SquidAccount create(Ocean ocean, DID did, String password) {
		return new SquidAccount(ocean, did, password);
	}

	/**
	 * Creates a SquidAccount with the specified Ocean connection and DID
	 * and password
	 *
	 * @param ocean Ocean connection to use
	 * @param did DID for this account
	 * @param password The Account password
	 * @return SquidAccount
	 */
	public static SquidAccount create(Ocean ocean, String did, String password) {
		return new SquidAccount(ocean, DID.parse(did), password);
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
