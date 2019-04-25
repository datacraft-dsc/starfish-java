package sg.dex.starfish.impl.squid;

import java.math.BigInteger;
import java.util.Map;

import sg.dex.starfish.impl.AAccount;
import sg.dex.starfish.impl.AEVMAccount;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.TODOException;

import com.oceanprotocol.squid.models.Account;
import com.oceanprotocol.squid.models.Balance;
import com.oceanprotocol.squid.api.AccountsAPI;
import com.oceanprotocol.squid.exceptions.EthereumException;

/**
 * Class implementing a Squid Account
 *
 * @author Tom
 *
 */
public class SquidAccount extends AAccount {

	protected final AccountsAPI accountAPI;
	protected final Account account;
	
	public SquidAccount(AccountsAPI accountsAPI, Account account) {
		super(account.getAddress());
		this.accountAPI=accountsAPI;
		this.account=account;
	}
	
	public static SquidAccount create(Ocean ocean, Account account) {
		AccountsAPI accountAPI=ocean.getAccountsAPI();
		return new SquidAccount(accountAPI,account);
	}

	public BigInteger getEthBalance() {
		return account.getBalance().getEth();
	}
	
	public BigInteger getOceanBalance() {
		return account.getBalance().getDrops();
	}

}
