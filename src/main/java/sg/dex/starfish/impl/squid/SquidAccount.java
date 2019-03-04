package sg.dex.starfish.impl.squid;

import java.math.BigInteger;

import sg.dex.starfish.impl.AEVMAccount;

import sg.dex.starfish.util.AuthorizationException;

import com.oceanprotocol.squid.exceptions.EthereumException;

/**
 * Class implementing a Squid Account
 *
 * @author Tom
 *
 */
public class SquidAccount extends AEVMAccount {

	protected SquidAgent squid = null;
	protected boolean locked = true;

	/**
	 * Creates a SquidAccount with the specified Ocean connection, ID
	 * and password
	 *
	 * @param id The identifier for this account
	 * @param password The Account password
	 * @param squid The SquidAgent to use
	 */
	protected SquidAccount(String id, String password, SquidAgent squid) {
		super(id, password);
		this.squid = squid;
	}

	/**
	 * Creates a SquidAccount with the specified Ocean connection, ID
	 * and password
	 *
	 * @param id The identifier for this account
	 * @param password The Account password
	 * @param squid The SquidAgent to use
	 * @return SquidAccount
	 */
	public static SquidAccount create(String id, String password, SquidAgent squid) {
		return new SquidAccount(id, password, squid);
	}

	/**
	 * Unlocks this Account (prevents any value transfer)
	 *
	 * @throws AuthorizationException if the password is invalid
	 */
	public void unlock() {
		// TODO verify that password is correct
		// TODO there does NOT seem to be an analog to the Python
		// approach of choosing a specific account?
		if (false) {
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
	 * @return number of tokens requested
	 */
	public BigInteger requestTokens(BigInteger amount) {
		if (locked) {
			throw new AuthorizationException("authorization failure",
							 new Exception("account is not unlocked"));
		} else {
			try {
				amount = squid.requestTokens(amount);
			} catch (EthereumException e) {
				// TODO Handle/log this Exception
				System.out.println("Ethereum exception: " + e);
				amount = BigInteger.ZERO;
			}
		}
		return amount;
	}

	/**
	 * Request some ocean tokens to be transfer to this Account
	 *
	 * @param amount The amount of ocean tokens to transfer
	 * @throws AuthorizationException if account is locekd
	 * @return number of tokens requested
	 */
	public int requestTokens(int amount) {
		return requestTokens(BigInteger.valueOf(amount)).intValue();
	}

}
