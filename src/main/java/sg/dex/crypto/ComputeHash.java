package sg.dex.crypto;

/**
 * Interface  for hash functions
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
