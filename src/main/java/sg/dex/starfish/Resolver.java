package sg.dex.starfish;

import com.oceanprotocol.squid.exceptions.*;
import com.oceanprotocol.squid.models.DID;
import org.web3j.crypto.CipherException;

import java.io.IOException;

/**
 * This class implement the resolver functionality.
 * This class has method to  get a DDO (as a String) for any given DID registered on-chain.
 * Also it has method to register a DDO for a DID given an ethereum account.
 */
public interface Resolver {

    /**
     * Method to  get a DDO (as a String) for any given DID registered on-chain.
     *
     * @param did given did which is registered on chain
     * @return DDO id as String
     * @throws CipherException
     * @throws IOException
     */
    String getDDO(DID did) throws EthereumException, DDOException, InvalidConfiguration, InitializationException, CipherException, IOException;

    /**
     * Method to register a DDO for a DID given an ethereum account.
     *
     * @param did      did that need to be register
     * @param checksum to validate , it should me hash of metadata
     * @return true if the given DID is registered successfully.
     * @throws IOException
     * @throws CipherException
     */
    boolean registerDID(DID did, String checksum) throws DIDRegisterException, IOException, CipherException, InitializationException, InvalidConfiguration;
}
