package com.kitschframework.common.exceptions.client;

import java.util.logging.Level;

import com.kitschframework.common.exceptions.CommonException;
import com.kitschframework.common.exceptions.PartyAtFault;

/**
 * A ClientException is an Exception that is caused by the client/user of an
 * application or service. The most common use case for a ClientException to be
 * thrown is the user providing a service with invalid data.
 * 
 * Examples:
 * 
 * <pre>
 * 1. Client doesn't provide required parameter
 * 2. Client provides invalid asin/order-id to a service
 * 3. Client attempts to perform an action they don't have rights to perform
 * </pre>
 */
public class ClientException extends CommonException {

    private static final long serialVersionUID = 1L;

    public ClientException(final String message)
    {
        super(message);
    }

    public ClientException(final String message,
                           final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * ClientExceptions should not result in tickets and are logged as INFO.
     */
    @Override
    public final Level getLogLevel() {
        return Level.INFO;
    }

    @Override
    public final PartyAtFault getPartyAtFault() {
        return PartyAtFault.CLIENT;
    }

}
