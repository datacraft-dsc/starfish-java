package sg.dex.starfish.keeper;

import com.oceanprotocol.common.web3.KeeperService;
import com.oceanprotocol.squid.exceptions.TokenApproveException;
import com.oceanprotocol.squid.manager.BaseManager;

/**
 * Adapter to access token smart contract.
 * <p>
 * It exposes tokenApprove method since it is not available in squid API (yet).
 * This class will be removed if squid will expose tokenApprove in API.
 * </p>
 *
 * @author Ilya Bukalov
 */
public class TokenManager extends BaseManager {
    private TokenManager(KeeperService keeperService) {
        super(keeperService, null);
    }

    public static TokenManager getInstance(KeeperService keeperService) {
        return new TokenManager(keeperService);
    }

    /**
     * Returns whether the transaction is successful
     *
     * @param spenderAddress The address who will be allowed to spend tokens of signer
     * @param price          Amount of tokens to spend
     * @return boolean
     * @throws TokenApproveException
     */
    public boolean tokenApprove(String spenderAddress, String price) throws TokenApproveException {
        return super.tokenApprove(tokenContract, spenderAddress, price);
    }
}