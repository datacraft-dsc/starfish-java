package sg.dex.starfish;

import java.util.Map;

/**
 * Interface representing an invokable service agent.
 *
 * @author Mike
 * @version 0.5
 */
public interface Invokable {

    /**
     * Invokes the specified operation on this agent. If the invoke is
     * successfully launched,
     * will return a Job instance that can be used to access the result,
     * otherwise throws an
     * exception.
     *
     * @param operation The operation to invoke on this agent
     * @param params    Positional parameters for the invoke operation
     * @return A Job instance allowing access to the invoke job status and
     *         result
     */
    Job invoke(Operation operation, Object... params);

    /**
     * Invokes the specified operation on this agent. If the invoke is
     * successfully launched,
     * will return a Job instance that can be used to access the result,
     * otherwise throws an
     * exception.
     *
     * @param operation The operation to invoke on this agent
     * @param params    Named parameters for the invoke operation
     * @return A Job instance allowing access to the invoke job status and
     *         result
     */
    Job invoke(Operation operation, Map<String, Object> params);

    /**
     * Invokes this operation with the given named parameters. Operations should
     * override
     * this method to provide an implementation of asynchronous invocation via
     * the
     * Job interface
     *
     * @param params    Positional parameters for this invoke job
     * @param operation The operation for which to obtain the parameter
     *                  specification
     * @return The Job for this invoked operation
     * @throws IllegalArgumentException if required parameters are not
     *                                  available, or of incorrect type
     */
    Job invokeAsync(Operation operation, Map<String, Object> params);

    /**
     * Gets a Job from this invokable agent with the given jobID. Job IDs are
     * unique
     * identifiers for Jobs issued by an Invokable Agent.
     *
     * @param jobID ID for the Job in the context for this Agent
     * @return A Job instance, or null if the Job cannot be found
     */
    Job getJob(String jobID);
}
