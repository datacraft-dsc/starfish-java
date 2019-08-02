package sg.dex.crypto.impl;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import sg.dex.crypto.ComputeHash;

import java.nio.charset.StandardCharsets;

/**
 * This class compute the Keccak256 hash of the Content
 */
public class Keccak256 implements ComputeHash {

    @Override
    public byte[] compute(byte[] data) {
        Keccak.DigestKeccak keccak = new Keccak.Digest256();
        keccak.update(data, 0, data.length);
        return keccak.digest();
    }

    @Override
    public byte[] compute(String string) {
        return compute(string.getBytes(StandardCharsets.UTF_8));
    }

}
