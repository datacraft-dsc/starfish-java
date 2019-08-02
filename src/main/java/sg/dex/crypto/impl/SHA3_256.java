package sg.dex.crypto.impl;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import sg.dex.crypto.ComputeHash;
import sg.dex.starfish.util.Hex;

import java.nio.charset.StandardCharsets;

/**
 * This class compute the SHA3-256 hash of the Content
 */
public class SHA3_256 implements ComputeHash {
    @Override
    public byte[] compute(byte[] data) {
        SHA3.DigestSHA3 md = new SHA3.Digest256();
        md.update(data, 0, data.length);
        final byte[] result = md.digest();
        return result;
    }

    @Override
    public byte[] compute(String string) {
        return compute(string.getBytes(StandardCharsets.UTF_8));
    }

}
