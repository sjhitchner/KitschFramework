package com.stephenhitchner.common.exceptions.internal;

import java.util.logging.Level;

import com.stephenhitchner.common.exceptions.CommonException;
import com.stephenhitchner.common.exceptions.PartyAtFault;

/**
 * An derivative of InternalException should be thrown when an application is
 * not able to process a user's request for a reason that is our fault. Throwing
 * an InternalException means we have messed up and we need to have an engineer
 * fix it.
 * 
 * Examples:
 * 
 * <pre>
 * 1. Missing required brazil-config key Spring configuration messed up
 * 2. Code level assertion failure (Assert.notNull)
 * </pre>
 */
public abstract class InternalException extends CommonException
{

    private static final long serialVersionUID = 1L;

    public InternalException(final String message) {
        super(message);
    }

    public InternalException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Internal exceptions always our fault.
     */
    @Override
    public final PartyAtFault getPartyAtFault() {
        return PartyAtFault.SELF;
    }

    /**
     * Internal exceptions should be logged as FATALs which should cause
     * somebody to get paged.
     */
    @Override
    public final Level getLogLevel() {
        return Level.SEVERE;
    }

}
