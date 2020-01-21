//
//package connection_check;
//
//
//public class AssumingConnection implements TestRule {
//
//    private ConnectionChecker checker;
//
//    public AssumingConnection(ConnectionChecker checker) {
//        this.checker = checker;
//    }
//
//    @Override
//    public Statement apply(Statement base, Description description) {
//        return new Statement() {
//            @Override
//            public void evaluate() throws Throwable {
//                if (!checker.connect()) {
//                    throw new AssertionError("Integration Test failed as " + "Server (" + checker.toString() + ") is not reachable.");
//                } else {
//                    base.evaluate();
//                }
//            }
//        };
//    }
//
//}
//