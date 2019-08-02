package sg.dex.crypto;

import org.junit.Test;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.util.Hex;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("javadoc")
public class TestKeccak {

    @Test
    public void testEmptyKeccak256() {
        // Empty byte array
        byte[] h1 = new ComputeHashFactory().getHashfunction(Constant.Keccak256).compute(new byte[]{});
        assertEquals("c5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470", Hex.toString(h1));
    }

    @Test
    public void testBasicKeccak256() {
        // Example from https://leventozturk.com/engineering/sha3/
        byte[] h2 = new ComputeHashFactory().getHashfunction(Constant.Keccak256).compute("abc");
        assertEquals("4e03657aea45a94fc7d47ba826c8d667c0d1e6e33a64a036ec44f58fa12d6c45", Hex.toString(h2));

        // Example from web3j test suite
        byte[] h3 = new ComputeHashFactory().getHashfunction(Constant.Keccak256).compute("EVWithdraw(address,uint256,bytes32)");
        assertEquals("953d0c27f84a9649b0e121099ffa9aeb7ed83e65eaed41d3627f895790c72d41", Hex.toString(h3));

        // Random short text
        byte[] h4 = new ComputeHashFactory().getHashfunction(Constant.Keccak256).compute("Niki");
        assertEquals("a08302ed7c06ecccbbc8eb73b91f9a57e097e9c79cff0bfbb2597a9c25a1c439", Hex.toString(h4));

    }

}
