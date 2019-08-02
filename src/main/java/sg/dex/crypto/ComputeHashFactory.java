package sg.dex.crypto;

import sg.dex.crypto.impl.Keccak256;
import sg.dex.crypto.impl.SHA3_256;
import sg.dex.starfish.constant.Constant;

/**
 * This class is the Factory class for all Crypto hash implementation
 *
 */
public class ComputeHashFactory {

    //use ComputeHash method to get object of type ComputeHash
    public ComputeHash getHashfunction(String hashFunctionName) {
        if (hashFunctionName == null) {
            return null;
        }
        if (hashFunctionName.equalsIgnoreCase(Constant.Keccak256)) {
            return new Keccak256();
        } else if (hashFunctionName.equalsIgnoreCase(Constant.SHA3_256)) {
            return new SHA3_256();
        }
        // todo Custom  implementation
        return null;
    }
}
