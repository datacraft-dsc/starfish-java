package sg.dex.starfish.keeper;

import com.oceanprotocol.squid.exceptions.TokenApproveException;
import com.oceanprotocol.squid.manager.BaseManager;
import com.oceanprotocol.common.web3.KeeperService;

public class TokenManager extends BaseManager {
    private TokenManager(KeeperService keeperService) {
        super(keeperService, null);
    }

    public static TokenManager getInstance(KeeperService keeperService) {
        return new TokenManager(keeperService);
    }

    public boolean tokenApprove(String spenderAddress, String price) throws TokenApproveException {
        return super.tokenApprove(tokenContract, spenderAddress, price);
    }
};