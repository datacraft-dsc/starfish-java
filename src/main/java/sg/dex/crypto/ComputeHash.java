package sg.dex.crypto;

/**
 * Interface has hash function that will be implement by different ETH System
 * Note: ETH uses a hash function that today is referred to as Keccak-256 instead of SHA3-256
 *
 */
public interface ComputeHash {
    /**
     * Compute the  hash of a byte array
     *
     * @param data Input data
     * @return byte[]
     */
    byte[] compute(byte[] data);

    /**
     * Compute the  hash of string, with UTF-8 encoding
     *
     * @param string Input string
     * @return byte[]
     */
    byte[] compute(String string);


}
