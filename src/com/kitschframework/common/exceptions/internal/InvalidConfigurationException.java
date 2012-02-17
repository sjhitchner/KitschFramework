package com.kitschframework.common.exceptions.internal;


/**
 * Indicates the application/service has been misconfigured and is unable to
 * run.
 */
public class InvalidConfigurationException extends InternalException
{
    private static final long serialVersionUID = 1L;

    private InvalidConfigurationException(final String message) {
        super(message);
    }

    private InvalidConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
