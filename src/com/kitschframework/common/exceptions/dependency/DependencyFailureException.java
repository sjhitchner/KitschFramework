package com.stephenhitchner.common.exceptions.dependency;

import java.util.logging.Level;

import com.stephenhitchner.common.exceptions.CommonException;
import com.stephenhitchner.common.exceptions.PartyAtFault;

/**
 * Indicates that the Application/Service is unable to process a request due to
 * a failure by one of its dependencies. There is nothing wrong with the user's
 * request or our code and if the user tries again at a later date then request
 * will most likely succeed.
 * 
 * Examples:
 * 
 * <pre>
 * 1. Call to a external service times-out 
 * 2. Failure to acquire a database connection 
 * 3. External service has an internal failure (Not caused by bad user input)
 * </pre>
 */
public abstract class DependencyFailureException extends CommonException
{

    private static final long serialVersionUID = 1L;

    public DependencyFailureException(final String message) {
        super(message);
    }

    public DependencyFailureException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * External exceptions are logged as ERROR so they can easily be found in
     * the logs.
     */
    @Override
    public final Level getLogLevel() {
        return Level.WARNING;
    }

    /**
     * External exceptions are always caused by a dependency
     */
    @Override
    public final PartyAtFault getPartyAtFault() {
        return PartyAtFault.DEPENDENCY;
    }

}
