package sg.dex.starfish.connection_check;

import org.junit.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class AssumingConnection implements TestRule {

    private ConnectionChecker checker;

    public AssumingConnection(ConnectionChecker checker) {
        this.checker = checker;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                if (!checker.connect()) {
                    throw new AssumptionViolatedException("Could not connect to Surfer. Skipping test!!!!");
                } else {
                    base.evaluate();
                }
            }
        };
    }

}

