package com.stephenhitchner.common.exceptions;

import java.util.logging.Level;

/**
 * Base class for all IPCLab framework exceptions. This class is only useful if
 * you want to separate IPCLab exceptions from other exceptions.
 */
public abstract class CommonException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CommonException(final String message) {
        super(message);
    }

    public CommonException(final String message,
                           final Throwable cause) {
        super(message, cause);
    }

    /**
     * Indicates which party was the cause of this exception.
     */
    public abstract PartyAtFault getPartyAtFault();

    /**
     * Indicates the severity at which this exception should be logged
     */
    public abstract Level getLogLevel();
}
