package sg.dex.starfish.impl.squid;

import com.oceanprotocol.squid.api.AccountsAPI;
import com.oceanprotocol.squid.models.Account;
import sg.dex.starfish.impl.AAccount;

import java.math.BigInteger;

/**
 * Class implementing a Squid Account
 *
 * @author Tom
 */
public class SquidAccount extends AAccount {

    protected final AccountsAPI accountAPI;
    protected final Account account;

    public SquidAccount(AccountsAPI accountsAPI, Account account) {
        super(account.getAddress());
        this.accountAPI = accountsAPI;
        this.account = account;
    }

    public static SquidAccount create(Account account, SquidService squidService) {
        AccountsAPI accountAPI = squidService.getOceanAPI().getAccountsAPI();
        return new SquidAccount(accountAPI, account);
    }

    public String getAddress() {
        return account.getAddress();
    }

    public BigInteger getEthBalance() {
        return account.getBalance().getEth();
    }

    public BigInteger getOceanBalance() {
        return account.getBalance().getDrops();
    }

}
