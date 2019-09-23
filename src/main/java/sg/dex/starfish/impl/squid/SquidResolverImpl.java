package sg.dex.starfish.impl.squid;

import java.io.IOException;

import org.web3j.crypto.CipherException;

import com.oceanprotocol.squid.exceptions.DDOException;
import com.oceanprotocol.squid.exceptions.DIDFormatException;
import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.exceptions.InitializationException;
import com.oceanprotocol.squid.exceptions.InvalidConfiguration;
import com.oceanprotocol.squid.manager.OceanManager;
import com.oceanprotocol.squid.models.DDO;

import sg.dex.starfish.Resolver;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Utils;

public class SquidResolverImpl implements Resolver {
    private SquidService squidService;

    public SquidResolverImpl() {
        squidService = SquidService.getSquidService();
    }


    @Override
    public String getDDOString(DID did) {
 		try {
 		   	com.oceanprotocol.squid.models.DID squidDID=new com.oceanprotocol.squid.models.DID(did.toString());
	        OceanManager oceanManager = SquidService.getResolverManager();
	        DDO ddo = oceanManager.resolveDID(squidDID);
	        if (ddo != null) {
	            return ddo.toJson();
	        }
		}
		catch (EthereumException | DDOException | DIDFormatException | InvalidConfiguration | InitializationException | IOException | CipherException e) {
			throw Utils.sneakyThrow(e);
		}
        return null;

    }

    @Override
    public void registerDID(DID did, String ddo) {
// TODO: fix this
//		com.oceanprotocol.squid.models.DID squidDID=new com.oceanprotocol.squid.models.DID(did.toString());
//        OceanManager oceanManager = SquidService.getResolverManager();
//        String url = SquidService.getAquariusService().getDdoEndpoint();
//        String providerAddress = SquidService.getProvider();
//        return oceanManager.registerDID(squidDID, url, checksum, Arrays.asList(providerAddress));

    }

}
