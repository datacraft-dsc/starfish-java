package sg.dex.crypto;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.util.Hex;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("javadoc")
public class TestSHA3 {

    @Test
    public void testEmptySHA3() {
        // Empty byte array
        byte[] h1 = Hash.sha3_256(new byte[]{});
        Assertions.assertEquals("a7ffc6f8bf1ed76651c14756a061d662f580ff4de43b49fa82d80a4b80f8434a", Hex.toString(h1));
        Assertions.assertTrue(Arrays.equals(Hash.EMPTY_BYTES_SHA3, h1));
    }

    @Test
    public void testBasicSHA3() {
        // test vectors from https://www.di-mgt.com.au/sha_testvectors.html

        byte[] h1 = Hash.sha3_256("abc");
        assertEquals("3a985da74fe225b2045c172d6bd390bd855f086e3e9d525b46bfe24511431532", Hex.toString(h1));

        byte[] h2 = Hash.sha3_256("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq");
        assertEquals("41c0dba2a9d6240849100376a8235e2c82e1b9998a999e21db32dd97496d3376", Hex.toString(h2));
    }

}
