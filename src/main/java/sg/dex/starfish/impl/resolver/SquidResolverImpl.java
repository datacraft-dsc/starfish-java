package sg.dex.starfish.impl.resolver;

import com.oceanprotocol.squid.exceptions.*;
import com.oceanprotocol.squid.manager.OceanManager;
import com.oceanprotocol.squid.models.DDO;
import com.oceanprotocol.squid.models.DID;
import org.web3j.crypto.CipherException;
import sg.dex.starfish.Resolver;
import sg.dex.starfish.impl.squid.SquidService;

import java.io.IOException;
import java.util.Arrays;

public class SquidResolverImpl implements Resolver {
    private SquidService squidService;

    public SquidResolverImpl() {
        squidService = SquidService.getSquidService();
    }


    @Override
    public String getDDO(DID did) throws InvalidConfiguration, InitializationException, CipherException, IOException, EthereumException, DDOException {

        OceanManager oceanManager = SquidService.getResolverManager();
        DDO ddo = oceanManager.resolveDID(did);
        if (null != ddo) {
            return ddo.id;
        }
        return null;

    }

    @Override
    public boolean registerDID(DID did, String checksum) throws DIDRegisterException, IOException, CipherException, InitializationException, InvalidConfiguration {
        OceanManager oceanManager = SquidService.getResolverManager();
        String url = SquidService.getAquariusService().getDdoEndpoint();
        String providerAddress = SquidService.getProvider();
        return oceanManager.registerDID(did, url, checksum, Arrays.asList(providerAddress));

    }

}
