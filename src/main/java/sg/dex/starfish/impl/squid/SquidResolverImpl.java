package sg.dex.starfish.impl.squid;

import com.oceanprotocol.squid.exceptions.DDOException;
import com.oceanprotocol.squid.exceptions.DIDFormatException;
import com.oceanprotocol.squid.exceptions.DIDRegisterException;
import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.manager.OceanManager;
import com.oceanprotocol.squid.models.DDO;
import org.web3j.crypto.CipherException;
import sg.dex.starfish.Resolver;
import sg.dex.starfish.exception.DexChainException;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Hex;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SquidResolverImpl implements Resolver {
    private final Map<DID, String> ddoCache = new HashMap<>();
    private SquidService squidService;

    /**
     * Create SquidResolverImpl
     *
     * @param SquidService squidService
     */
    public SquidResolverImpl(SquidService squidService) {
        this.squidService = squidService;
    }

    @Override
    public String getDDOString(DID did) throws DexChainException {
        try {
            com.oceanprotocol.squid.models.DID squidDID = new com.oceanprotocol.squid.models.DID(did.toString());
            OceanManager oceanManager = squidService.getResolverManager();
            DDO ddo = oceanManager.resolveDID(squidDID);
            if (ddo != null) {
                return ddo.toJson();
            }
        } catch (EthereumException | DDOException | DIDFormatException | IOException | CipherException e) {
            throw new DexChainException(e);
        }
        return null;

    }

    DDO getSquidDDO(DID did) throws EthereumException, DDOException, IOException, CipherException, DIDFormatException {

        com.oceanprotocol.squid.models.DID squidDID = new com.oceanprotocol.squid.models.DID(did.toString());
        OceanManager oceanManager = squidService.getResolverManager();
        DDO ddo = oceanManager.resolveDID(squidDID);
        if (ddo != null) {
            return ddo;
        }

        return null;

    }

    @Override
    public void registerDID(DID did, String ddo) throws DexChainException {
        installLocalDDO(did, ddo);

        try {
            com.oceanprotocol.squid.models.DID didSquid = new com.oceanprotocol.squid.models.DID(did.toString());
            squidService.getResolverManager().
                    registerDID(didSquid, squidService.getAquariusService().getDdoEndpoint(),
                            Hex.toZeroPaddedHexNoPrefix("0"), Arrays.asList(squidService.getProvider()));

        } catch (DIDRegisterException e) {
            throw new DexChainException(e);
        } catch (IOException e) {
            throw new DexChainException(e);
        } catch (CipherException e) {
            throw new DexChainException(e);
        } catch (DIDFormatException e) {
            throw new DexChainException(e);
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