package com.stephenhitchner.common.utils;

import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.stephenhitchner.common.exceptions.AssertionFailureException;

/**
 * Utility class for programmers to perform assertions in there code. This class
 * should only be used to catch programmer errors.
 * 
 * The validation methods that start with arg may be used to validate method
 * arguments. They simply make it easier to generate the error message.
 */
public final class Assert
{
    /**
     * @throws AssertionFailureException
     *             thrown if the given value is null
     */
    public static <T> void argNotNull(final T value, final String name) {
        notNull(value, createArgAssertionMessage(name, "notNull"));
    }

    private static String createArgAssertionMessage(final String argumentName,
                                                    final String assertionName) {
        return "The argument: '" + argumentName + "' failed assertion: " + assertionName;
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given value is null
     */
    public static <T> void notNull(final T value, final String message) {
        isTrue(value != null, message);
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given value is false
     */
    public static void isTrue(final boolean value, final String message) {
        if (!value) {
            throw new AssertionFailureException(message);
        }
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given value is not null
     */
    public static void isNull(final Object value, final String message) {
        isTrue(value == null, message);
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given value value is blank
     */
    public static void argNotBlank(final String value, final String name) {
        notBlank(value, createArgAssertionMessage(name, "notBlank"));
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given value is blank
     */
    public static void notBlank(final String value, final String message) {
        isFalse(StringUtils.isBlank(value), message);
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given String is empty
     */
    public static void argNotEmpty(final String value, final String name) {
        notEmpty(value, createArgAssertionMessage(name, "notEmpty"));
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given value is empty
     */
    public static void notEmpty(final String value, final String message) {
        isTrue(StringUtils.isNotEmpty(value), message);
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given collection is empty
     */
    public static <T> void argNotEmpty(final Collection<T> collection, final String name) {
        notEmpty(collection, createArgAssertionMessage(name, "notEmpty"));
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given collection is empty
     */
    public static void notEmpty(final Collection<?> collection, final String message) {
        isTrue(collection != null && !collection.isEmpty(), message);
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given array is empty
     */
    public static <T> void argNotEmpty(final T[] array, final String name) {
        notEmpty(array, createArgAssertionMessage(name, "notEmpty"));
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given array is empty
     */
    public static <T> void notEmpty(final T[] array, final String message) {
        notEmpty(array, message);
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given value doesn't match the given pattern
     */
    public static void argMatches(final String value, final String pattern, final String name) {
        matches(value, pattern, createArgAssertionMessage(name, "matches"));
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given value does not match the given pattern
     */
    public static void matches(final String value, final String pattern, final String message) {
        isTrue(Pattern.matches(pattern, value), message);
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given value is <= 0
     */
    public static void argIsPositive(final long value, final String argName) {
        isPositive(value, createArgAssertionMessage(argName, "positive"));
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given value is <= 0
     */
    public static void isPositive(final long value, final String message) {
        isTrue(value > 0, message);
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given value is less than zero
     */
    public static void argIsNonNegative(final long value, final String argName) {
        isNonNegative(value, createArgAssertionMessage(argName, "nonNegative"));

    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given value is less than zero
     */
    public static void isNonNegative(final long value, final String message) {
        isTrue(value >= 0, message);
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given value is true
     */
    public static void isFalse(final boolean value, final String message) {
        isTrue(!value, message);
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given value is not an instance of the given
     *             type
     */
    public static void argIsInstanceOf(final Object value, final Class<?> type, final String name) {

        isInstanceOf(value, type, createArgAssertionMessage(name, "instanceOf"));
    }

    /**
     * @throws AssertionFailureException
     *             thrown if the given value is not an instance of the given
     *             type
     */
    public static void isInstanceOf(final Object value, final Class<?> type, final String message) {
        isTrue(type.isInstance(value), message);
    }

    /**
     * @throws AssertionFailureException
     *             thrown with the given message when invoked
     */
    public static void fail(final String message) {
        throw new AssertionFailureException(message);
    }

    /**
     * @throws AssertionFailureException
     *             thrown with the given message and cause when invoked
     */
    public static void fail(final String message, final Throwable cause) {
        throw new AssertionFailureException(message, cause);
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Assert() {
    }
}