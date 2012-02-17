package com.kitschframework.common.exceptions.dependency;

/**
 * Indicates that a call to a remove service timed out.
 */
public class ServiceTimeoutException extends DependencyFailureException
{

    private static final long serialVersionUID = 1L;

    private final String      service;
    private final String      operation;

    public ServiceTimeoutException(final String service, final String operation,
            final Throwable cause) {

        super("The call to service: " + service + ": " + operation + " timed out", cause);

        this.service = service;
        this.operation = operation;
    }

    public final String getService() {
        return service;
    }

    public final String getOperation() {
        return operation;
    }

}
