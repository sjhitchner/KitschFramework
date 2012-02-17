package com.stephenhitchner.common.utils;

import java.util.Iterator;

/**
 * Utility class for iterating over a class Hierarchy from child to parent. This
 * is useful when looking for methods to reflectively invoke.
 * 
 * NOTE: This iterator does not look at interfaces, it only looks at super
 * classes when walking up the hierarchy
 * 
 * Example:
 * 
 * <pre>
 * class A {}
 * class B extends A {}
 * class C extends B {}
 * 
 * this code will print 'CBA'
 * ClassHierarchyIterator i = new ClassHierarchyIterator(C.class);
 * while( i.hasNext() ) {
 *    System.out.println(i.next()); 
 * }
 * </pre>
 */
public class ClassHierarchyIterator implements Iterator<Class<?>>, Iterable<Class<?>>
{

    private Class<?> current;

    public ClassHierarchyIterator(final Object source) {
        this(source.getClass());
    }

    public ClassHierarchyIterator(final Class<?> source) {
        Assert.argNotNull(source, "source");

        this.current = source;
    }

    /**
     * Indicates if their is another super class in the hierarchy
     */
    @Override
    public boolean hasNext() {
        return current != null;
    }

    /**
     * Returns the next class in the hierarchy
     */
    @Override
    public Class<?> next() {
        final Class<?> result = current;
        current = current.getSuperclass();
        return result;
    }

    /**
     * @throws UnsupportedOperationException
     *             don't call this.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("You can't remove a class guy!!!");
    }

    /**
     * Convience method for use in for loops
     */
    @Override
    public Iterator<Class<?>> iterator() {
        return this;
    }

}