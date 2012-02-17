package com.stephenhitchner.common.exceptions;

/**
 * Indicates a code-level assertion has failed due to programmer error. These
 * exceptions should never be thrown as a result of invalid user input (it
 * should be validated upfront).
 */
public class AssertionFailureException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public AssertionFailureException(final String message) {
        super(message);
    }

    public AssertionFailureException(final String message,
                                     final Throwable cause)
    {
        super(message, cause);
    }
}
