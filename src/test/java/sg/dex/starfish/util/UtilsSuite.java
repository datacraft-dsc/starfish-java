package sg.dex.starfish.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ParamTestJSON.class,
        TestDID.class,
        TestHex.class,
        TestJSON.class,
        TestUtils.class
})
public class UtilsSuite {

}
