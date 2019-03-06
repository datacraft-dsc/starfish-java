package sg.dex.starfish.impl.squid;

import java.math.BigInteger;

import sg.dex.starfish.impl.AEVMAccount;

import sg.dex.starfish.util.AuthorizationException;

import com.oceanprotocol.squid.models.Account;
import com.oceanprotocol.squid.models.Balance;
import com.oceanprotocol.squid.exceptions.EthereumException;

/**
 * Class implementing a Squid Account
 *
 * @author Tom
 *
 */
public class SquidAccount extends AEVMAccount {

	protected SquidAgent squid = null;
	protected Account account = null;
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
		super(id.toLowerCase(), password);
		this.squid = squid;
		try {
			for (Account a : squid.list()) {
				if (a.getId().equals(this.id)) {
					account = a; // found match
				}
			}
		} catch (EthereumException e) {
		}
		if (account == null) {
			System.out.println("ERROR address: " + id + " UNABLE to get Squid Account");
		}
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
		// there does NOT seem to be an analog to the Python
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

	/**
	 * Returns the Balance of an account
	 *
	 * @return the Balance of the account
	 * @throws EthereumException EthereumException
	 */
	@Override
	public Balance balance() throws EthereumException {
		try {
			return squid.balance(account);
		} catch (EthereumException e) {
			// TODO: handle this error
			// 17:34:59.967 [main] ERROR com.oceanprotocol.squid.manager.AccountsManager - Unable to get account(0x068ed00cf0441e4829d9784fcbe7b9e26d4bd8d0) Ocean balance: Empty value (0x) returned from contract

		}
		return new Balance();
	}
}
