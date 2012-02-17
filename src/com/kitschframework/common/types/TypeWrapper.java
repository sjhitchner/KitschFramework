package com.stephenhitchner.common.types;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.stephenhitchner.common.exceptions.AssertionFailureException;
import com.stephenhitchner.common.utils.Assert;

/**
 * Attempts to alleviate the problem of having logically different types that
 * have the same java type. For instance, marketplaceId, merchantCustomerId, and
 * legacyMerchantId are all longs. Thus, the programmer(me) can erroneously pass
 * a long that identifies a marketplace into a method that required a merchant
 * customer id.
 * 
 * T extends Serializable to make it so these classes can be used directly by
 * Hibernate.
 */
public abstract class TypeWrapper<T extends Serializable> implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final T           value;

    /**
     * Constructor
     * 
     * @throws AssertionFailureException
     *             if the given value is null
     */
    protected TypeWrapper(final T value) {
        Assert.argNotNull(value, "value");
        this.value = value;
    }

    /**
     * Returns the value held by this wrapper.
     */
    public final T getValue() {
        return value;
    }

    /**
     * @return the result of calling the wrapped value's toString method.
     */
    @Override
    public String toString() {
        return getValue().toString();
    }

    /**
     * @return the result of calling the wrapped value's hashCode method
     */
    @Override
    public final int hashCode() {
        return getValue().hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TypeWrapper<?>)) {
            return false;
        }

        final TypeWrapper<?> that = (TypeWrapper<?>) other;

        if (!canEqual(that)) {
            return false;
        }

        return new EqualsBuilder().append(this.getValue(), that.getValue()).isEquals();
    }

    /**
     * This method should return true if the other object is an instance of the
     * class in which canEqual is (re)defined, false otherwise. It is called
     * from equals to make sure that the objects are comparable both ways See:
     * http://www.artima.com/lejava/articles/equality.html
     * 
     * @return true iff the given other object can be compared to this
     */
    protected abstract boolean canEqual(final TypeWrapper<?> other);

}
