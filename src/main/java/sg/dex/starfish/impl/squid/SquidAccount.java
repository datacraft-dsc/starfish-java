package sg.dex.starfish.impl.squid;

import sg.dex.starfish.impl.AEVMAccount;
import sg.dex.starfish.Ocean;

import sg.dex.starfish.util.AuthorizationException;

/**
 * Class implementing a Squid Account
 *
 * @author Tom
 *
 */
public class SquidAccount extends AEVMAccount {

	protected Ocean ocean = null;
	protected boolean locked = true;

	/**
	 * Creates a SquidAccount with the specified Ocean connection, ID
	 * and password
	 *
	 * @param id The identifier for this account
	 * @param password The Account password
	 * @param ocean Ocean connection to use
	 */
	protected SquidAccount(String id, String password, Ocean ocean) {
		super(id, password);
		this.ocean = ocean;
	}

	/**
	 * Creates a SquidAccount with the specified Ocean connection, ID
	 * and password
	 *
	 * @param id The identifier for this account
	 * @param password The Account password
	 * @param ocean Ocean connection to use
	 * @return SquidAccount
	 */
	public static SquidAccount create(String id, String password, Ocean ocean) {
		return new SquidAccount(id, password, ocean);
	}

	/**
	 * Unlocks this Account (prevents any value transfer)
	 *
	 * @throws AuthorizationException if the password is invalid
	 */
	public void unlock() {
		if (true) {
			throw new AuthorizationException("authorization failure",
							 new Exception("not implemented yet"));
		} else {
			locked = false;
		}
	}

	/**
	 * Locks this Account (enables value transfer)
	 *
	 */
	public void lock() {
		locked = true;
	}

	/**
	 * Request some ocean tokens to be transfer to this Account
	 *
	 * @param amount The amount of ocean tokens to transfer
	 * @throws AuthorizationException if account is locekd
	 * @return number of tokens requestd
	 */
	public int requestTokens(int amount) {
		return amount;
	}

}
