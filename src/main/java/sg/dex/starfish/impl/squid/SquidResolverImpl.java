package sg.dex.starfish.impl.squid;

import com.oceanprotocol.squid.exceptions.DDOException;
import com.oceanprotocol.squid.exceptions.DIDFormatException;
import com.oceanprotocol.squid.exceptions.DIDRegisterException;
import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.manager.OceanManager;
import com.oceanprotocol.squid.models.DDO;
import org.web3j.crypto.CipherException;
import sg.dex.starfish.Resolver;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SquidResolverImpl implements Resolver {
    private final Map<DID, String> ddoCache = new HashMap<>();

    @Override
    public String getDDOString(DID did) {
        try {
            com.oceanprotocol.squid.models.DID squidDID = new com.oceanprotocol.squid.models.DID(did.toString());
            OceanManager oceanManager = SquidService.getResolverManager();
            DDO ddo = oceanManager.resolveDID(squidDID);
            if (ddo != null) {
                return ddo.toJson();
            }
        } catch (EthereumException | DDOException | DIDFormatException | IOException | CipherException e) {
            throw Utils.sneakyThrow(e);
        }
        return null;

    }

    public DDO getSquidDDO(DID did) throws EthereumException, DDOException, IOException, CipherException, DIDFormatException {

        com.oceanprotocol.squid.models.DID squidDID = new com.oceanprotocol.squid.models.DID(did.toString());
        OceanManager oceanManager = SquidService.getResolverManager();
        DDO ddo = oceanManager.resolveDID(squidDID);
        if (ddo != null) {
            return ddo;
        }

        return null;

    }

    @Override
    public void registerDID(DID did, String ddo) {
        //TODO  need to register in the network
        installLocalDDO(did, ddo);

        try {
            com.oceanprotocol.squid.models.DID didSquid = new com.oceanprotocol.squid.models.DID(did.toString());
            SquidService.getResolverManager().
                    registerDID(didSquid, SquidService.getAquariusService().getDdoEndpoint(),
                            "checksum", Arrays.asList(SquidService.getProvider()));

        } catch (DIDRegisterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (DIDFormatException e) {
            e.printStackTrace();
        }


    }

    /**
     * Registers a DID with a DDO in the context of this Ocean connection on the local machine.
     * <p>
     * This registration is intended for testing purposes.
     *
     * @param did       A did to register
     * @param ddoString A string containing a valid DDO in JSON Format
     */
    private void installLocalDDO(DID did, String ddoString) {
        if (did == null) throw new IllegalArgumentException("DID cannot be null");
        did = did.withoutPath();
        ddoCache.put(did, ddoString);
    }

}
