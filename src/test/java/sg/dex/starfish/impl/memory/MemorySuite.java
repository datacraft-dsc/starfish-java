package sg.dex.starfish.impl.memory;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sg.dex.starfish.impl.operations.TestMemoryOperations;

/**
 * Test suite for in-memory implementations
 * 
 * @author Mike
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestMemoryAgent.class,
        TestMemoryAsset.class,
        TestMemoryOperations.class,
        TestMemoryBundle.class,
        TestMemoryListing.class,
        TestMemoryPurchase.class
})
public class MemorySuite {

}
