package com.kitschframework.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.kitschframework.common.exceptions.AssertionFailureException;

/**
 * Utility class that makes it easy to do common Reflection operations.
 */
public final class ReflectionUtils
{
    /**
     * Maps primitive types to their associated wrapper class.
     */
    @SuppressWarnings("serial")
    private static Map<Class<?>, Class<?>> primitiveToWrapperTable = new HashMap<Class<?>, Class<?>>() {
                                                                       {
                                                                           put(Boolean.TYPE,
                                                                               Boolean.class);
                                                                           put(Byte.TYPE,
                                                                               Byte.class);
                                                                           put(Character.TYPE,
                                                                               Character.class);
                                                                           put(Short.TYPE,
                                                                               Short.class);
                                                                           put(Integer.TYPE,
                                                                               Integer.class);
                                                                           put(Long.TYPE,
                                                                               Long.class);
                                                                           put(Double.TYPE,
                                                                               Double.class);
                                                                           put(Float.TYPE,
                                                                               Float.class);
                                                                       }
                                                                   };

    /**
     * Maps wrapper types to their associated primitive type.
     */
    @SuppressWarnings("serial")
    private static Map<Class<?>, Class<?>> wrapperToPrimitiveTable = new HashMap<Class<?>, Class<?>>() {
                                                                       {
                                                                           //
                                                                           // Use
                                                                           // the
                                                                           // primitiveToWrapperTable
                                                                           // to
                                                                           // create
                                                                           // an
                                                                           // inverse
                                                                           // of
                                                                           // itself.
                                                                           // IE
                                                                           // swap
                                                                           // the
                                                                           // key
                                                                           // and
                                                                           // values
                                                                           //
                                                                           for (final Class<?> primitiveClass : primitiveToWrapperTable.keySet()) {
                                                                               final Class<?> wrapperClass = primitiveToWrapperTable.get(primitiveClass);

                                                                               put(wrapperClass,
                                                                                   primitiveClass);
                                                                           }
                                                                       }
                                                                   };

    /**
     * Usage:
     * 
     * <pre>
     * class Person
     * {
     *     private String name;
     * }
     * 
     * final Person p = new Person();
     *                                Field nameField = ReflectionUtils.getField(p, &quot;name&quot;);
     * </pre>
     * 
     * @return The field of the target with the given name or null if no such
     *         field exists.
     * 
     * @throws AssertionFailureException
     *             thrown if either parameter is null
     */
    public static Field getField(final Object target, final String name) {

        Assert.argNotNull(target, "target");
        return getField(target.getClass(), name);
    }

    /**
     * Usage:
     * 
     * <pre>
     * class Person
     * {
     *     private String name;
     * }
     * 
     * Field nameField = ReflectionUtils.getField(Person.class, &quot;name&quot;);
     * </pre>
     * 
     * @return The field of the target with the given name or null if no such
     *         field exists.
     * 
     * @throws AssertionFailureException
     *             thrown if either parameter is null
     */
    public static Field getField(final Class<?> target, final String name) {

        Assert.argNotNull(target, "target");
        Assert.argNotNull(name, "name");

        //
        // Walk the class hiearchy looking for the field. This allows the user
        // to access fields in the parent class of the given target.
        //
        for (final Class<?> current : new ClassHierarchyIterator(target)) {

            //
            // find the field with the given name on the current type
            //
            final Field field = findFieldWithName(current.getDeclaredFields(), name);

            //
            // If the field is found, make it accessible and return it. This
            // allows the caller to access private fields.
            //
            if (field != null) {
                field.setAccessible(true);
                return field;
            }
        }

        return null;
    }

    private static Field findFieldWithName(final Field[] fields, final String name) {
        for (final Field field : fields) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    /**
     * Usage:
     * 
     * <pre>
     * class Person
     * {
     *     private String name;
     * }
     * 
     * final Person p = new Person();
     *                                final String name = ReflectionUtils.getFieldValue(p, &quot;name&quot;);
     * </pre>
     * 
     * @return the value of the given target's field with the given name. The
     *         field must exist, if you don't know if the field exists call
     *         getField and check for null.
     * 
     * @throws AssertionFailureException
     *             thrown if either parameter is null or if the field cannot be
     *             accessed or doesn't exist.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(final Object target, final String name) {
        Assert.argNotNull(target, "target");

        // cast needed here to make compiler happy
        return (T) getFieldValue(target, target.getClass(), name);
    }

    /**
     * Usage:
     * 
     * <pre>
     * class Constants
     * {
     *     private static final int DRINKING_AGE = 21;
     * }
     * 
     * final int drinkingAge = ReflectionUtilts.getStaticFieldValue(Constants.class, &quot;DRINKING_AGE&quot;);
     * </pre>
     * 
     * @return the value of a static field defined in the given class. The field
     *         must exist, if you don't know if the field exists call getField
     *         and check for null.
     * 
     * @throws AssertionFailureException
     *             thrown if either parameter is null or if the field cannot be
     *             accessed or doesn't exist.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getStaticFieldValue(final Class<?> target, final String name) {
        // cast needed here to make compiler happy
        return (T) getFieldValue(null, target, name);
    }

    /**
     * Helper method to get the value of a field. Allows us to define the code
     * in one place.
     */
    @SuppressWarnings("unchecked")
    private static <T> T getFieldValue(final Object target, final Class<?> type, final String name) {
        Assert.argNotNull(type, "type");
        Assert.argNotNull(name, "name");

        //
        // Fetch the field with the given name from the given type
        //
        final Field field = getField(type, name);

        //
        // Make sure the field was found.
        //
        Assert.notNull(field, "Field: " + name + ", does not exist for type: " + type);

        //
        // Extract the found Field's value.
        //
        try {
            return (T) field.get(target);
        }
        catch (final Exception e) {

            //
            // All exceptions are turned into Assertions because we are assuming
            // they
            // are programmer errors.
            //
            Assert.fail("Failed to fetch the value of field: " + name + ", of instance: " + target,
                        e);

            return null; // DEAD CODE for MR. Compiler
        }
    }

    /**
     * Sets the value of the target's field with the given name.
     * 
     * Usage:
     * 
     * <pre>
     * class Person
     * {
     *     private String name;
     * }
     * 
     * final Person p = new Person();
     *                                final String name = ReflectionUtils.setFieldValue(p, &quot;name&quot;, &quot;bob&quot;);
     * </pre>
     * 
     * @throws AssertionFailureException
     *             thrown if any parameters are null or if the field doesn't
     *             exist, cannot be accessed, or is not the right type.
     */
    public static void setFieldValue(final Object target, final String name, final Object value) {
        Assert.argNotNull(target, "target");

        setFieldValue(target, target.getClass(), name, value);
    }

    /**
     * Sets the value of a static field of the given class with the given name.
     * 
     * Usage:
     * 
     * <pre>
     * class Constants
     * {
     *     private static int DRINKING_AGE = 21;
     * }
     * 
     * ReflectionUtilts.setStaticFieldValue(Constants.class, &quot;DRINKING_AGE&quot;, 10);
     * </pre>
     * 
     * @throws AssertionFailureException
     *             thrown if any parameters are null or if there is no static
     *             field with the given name.
     */
    public static void setStaticFieldValue(final Class<?> target,
                                           final String name,
                                           final Object value) {
        setFieldValue(null, target, name, value);
    }

    /**
     * Helper method to set the value of a field. Allows us to define code in
     * one place.
     */
    private static void setFieldValue(final Object target,
                                      final Class<?> type,
                                      final String name,
                                      final Object value) {
        //
        // Lookup the field
        //
        final Field field = getField(type, name);

        //
        // Make sure the field exists
        //
        Assert.notNull(field, "Field: " + name + " does not exist on type: " + type);

        //
        // Update the field's value
        //
        try {
            field.set(target, value);
        }
        catch (final Exception e) {
            Assert.fail("Failed to set field: " + name
                        + " to value: "
                        + value
                        + ", on type: "
                        + type, e);
        }
    }

    /**
     * @return The method of the target with the given name. Returns null if no
     *         such method exists.
     */
    public static Method getMethod(final Object target,
                                   final String methodName,
                                   final Class<?>... parameterTypes) {
        Assert.argNotNull(target, "target");
        return getMethod(target.getClass(), methodName, parameterTypes);
    }

    /**
     * Usage:
     * 
     * <pre>
     * class Person
     * {
     *     String getAge();
     * 
     *     void setAge(int age);
     * 
     *     void setFullName(String first, String middle, String last);
     * }
     * 
     * Person p = new Person();
     * ReflectionUtils.getMethod(p, &quot;getAge&quot;);
     * ReflectionUtils.getMethod(p, &quot;setAge&quot;, int.class);
     * ReflectionUtils.getMethod(p, &quot;setFullName&quot;, String.class, String.class, String.class);
     * </pre>
     * 
     * @return The method with the given name. Returns null if no such method
     *         exists.
     */
    public static Method getMethod(final Class<?> target,
                                   final String methodName,
                                   final Class<?>... parameterTypes) {
        Assert.argNotNull(methodName, "methodName");

        //
        // Walk the class hierarchy. This allows us to find methods defined in a
        // parent
        // class of the provided target.
        //
        for (final Class<?> current : new ClassHierarchyIterator(target)) {

            //
            // Find the method with the given name and parameters using the
            // current class
            //
            final Method method = findMethodWithNameAndParameterTypes(current.getDeclaredMethods(),
                                                                      methodName,
                                                                      parameterTypes);

            //
            // If the method was found make it accessible. This allows the user
            // to access private methods.
            //
            if (method != null) {
                method.setAccessible(true);
                return method;
            }
        }

        return null;
    }

    private static Method findMethodWithNameAndParameterTypes(final Method[] methods,
                                                              final String name,
                                                              final Class<?>[] argumentTypes) {

        for (final Method method : methods) {
            if (method.getName().equals(name)) {
                if (isAssignableTo(argumentTypes, method.getParameterTypes())) {
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * Usage:
     * 
     * <pre>
     * Class[] source = new Class[] { String.class, ArrayList.class, Integer.class };
     * Class[] goodDest = new Class[] { String.class, List.class, int.class };
     * Class[] badDest = new Class[] { Integer.class, LinkedList.class, Long.class };
     * 
     * ReflectionUtils.isAssignableTo(source, goodDest); // returns true
     * ReflectionUtils.isAssignableTo(source, badDest); // returns false
     * </pre>
     * 
     * @return true if all the classes in the sources array are assignable to
     *         all the classes in the destinations array.
     */
    public static boolean isAssignableTo(final Class<?>[] sources, final Class<?>[] destinations) {
        Assert.argNotNull(sources, "sources");
        Assert.argNotNull(destinations, "destinations");

        //
        // The source length must match the destination length for the arrays to
        // be assignable
        //
        if (sources.length != destinations.length) {
            return false;
        }

        //
        // Check if each source type is assignable to its associated destination
        // type. If any are founds that don't match then the source array is not
        // assignable to the destination array.
        //
        for (int i = 0; i < sources.length; ++i) {
            final Class<?> source = sources[i];
            final Class<?> destination = destinations[i];
            if (!isAssignableTo(source, destination)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Usage:
     * 
     * <pre>
     * // null assignable to any object
     * ReflectionUtils.isAssignableTo(null, Integer.class); // true
     * 
     * // null not assignable to primitives
     * ReflectionUtils.isAssignableTo(null, int.class); // false
     * 
     * // primitives are assignable to themselves
     * ReflectionUtils.isAssignableTo(int.class, int.class); // true
     * 
     * // objects are assignable to themselves
     * ReflectionUtils.isAssignableTo(String.class, String.class); // true
     * 
     * // primitives are assignable to their associated wrapper
     * ReflectionUtils.isAssignableTo(int.class, Integer.class); // true
     * 
     * // wrappers are assignable to their associated primitives
     * ReflectionUtils.isAssignableTo(Integer.class, int.class); // true
     * 
     * // primitives are not assignable to other primitives
     * ReflectionUtils.isAssignableTo(int.class, long.class); // false
     * 
     * // primitives are not assignable to other wrappers
     * ReflectionUtils.isAssignableTo(int.class, Long.class); // false
     * 
     * // wrapper are not assignable to other wrappers
     * ReflectionUtils.isAssignableTo(Integer.class, Long.class); // false
     * 
     * // child is assignable to parent
     * ReflectionUtils.isAssignableTo(ArrayList.class, List.class); // true
     * 
     * // parent is not assignable to child
     * ReflectionUtils.isAssignableTo(List.class, ArrayList.class); // false
     * </pre>
     * 
     * @return true if the source class is assignable to the destination class
     */
    public static boolean isAssignableTo(final Class<?> source, final Class<?> destination) {
        //
        // If the value is null it can be passed as an argument to anything that
        // takes an object. Thus, as long as the destination is not a primitive
        // it is assignable.
        //
        if (source == null) {
            return !destination.isPrimitive();
        }

        //
        // If the destination type is a super class/interface of the source type
        // then the source type is assignable to the destination type.
        //
        if (destination.isAssignableFrom(source)) {
            return true;
        }

        //
        // If the destination type is a primitive then source is only assignable
        // to destination if they both have the same wrapper type. IE, if
        // destination is an int then source must be an Integer in order to be
        // assignable to dest.
        //
        if (destination.isPrimitive() || source.isPrimitive()) {
            final Class<?> destinationPrimitiveType = toPrimitive(destination);
            final Class<?> sourcePrimitiveType = toPrimitive(source);
            return ObjectUtils.equals(destinationPrimitiveType, sourcePrimitiveType);
        }

        return false;
    }

    /**
     * Usage:
     * 
     * <pre>
     * Reflectionutils.toWrapper(int.class); // returns Integer.class
     * ReflectionUtils.toWrapper(Integer.class); // returns Integer.class
     * ReflectionUtils.toWrapper(String.class); // returns null
     * </pre>
     * 
     * @returns the wrapper class associated with given type or null if the
     *          given type is not a primitive or wrapper
     * 
     * @throws AssertionFailureException
     *             thrown if the given type is null
     */
    public static Class<?> toWrapper(final Class<?> type) {

        Assert.argNotNull(type, "type");

        //
        // If the given type is a primitive then just return its associated
        // wrapper
        //
        if (type.isPrimitive()) {
            return primitiveToWrapperTable.get(type);
        }

        //
        // Type isn't a primitive, if it is a wrapper then just return itself
        //
        if (wrapperToPrimitiveTable.containsKey(type)) {
            return type;
        }

        //
        // Type isn't a primitive or a wrapper, return null
        //
        return null;
    }

    /**
     * Usage:
     * 
     * <pre>
     * ReflectionUtils.toPrimitive(int.class); // returns int.class
     * Reflectionutils.toPrimitive(Integer.class); // return int.class
     * ReflectionUtils.toPrimitive(String.class); // returns null
     * </pre>
     * 
     * @returns the primitive class associated with the given type or null if
     *          the given type is not a primitive or a wrapper
     * 
     * @throws AssertionFailureException
     *             thrown if the given type is null
     */
    public static Class<?> toPrimitive(final Class<?> type) {

        Assert.argNotNull(type, "type");

        //
        // if the type is a primitive then no conversion is needed and we can
        // just return the type passed to us.
        //
        if (type.isPrimitive()) {
            return type;
        }

        //
        // Lookup the primitive registered for the given type. If none exists
        // then this method will return null.
        //
        return wrapperToPrimitiveTable.get(type);
    }

    /**
     * Invokes the target's method with the given name and and parameters
     * 
     * Usage:
     * 
     * <pre>
     * class Person
     * {
     *     public void getName();
     * 
     *     public void setName(final String name);
     * }
     * 
     * Person p = new Person();
     * String name = ReflectionUtils.invokeMethod(p, &quot;getName&quot;);
     * ReflectionUtils.invokeMethod(p, &quot;setName&quot;, &quot;bob&quot;);
     * </pre>
     * 
     * @return the result of the method invocation
     * 
     * @throws AssertionFailureException
     *             if the given method doesn't exist or if any required params
     *             are null
     */
    @SuppressWarnings("unchecked")
    public static <T> T invokeMethod(final Object target,
                                     final String methodName,
                                     final Object... parameters) {
        Assert.argNotNull(target, "target");

        return (T) invokeMethod(target.getClass(), target, methodName, parameters);
    }

    /**
     * Invokes the static method with the given name and parameters on the given
     * target.
     * 
     * @return the result of the method invocation
     * 
     * @throws AssertionFailureException
     *             thrown if the given method doesn't exist or if any required
     *             parameters are null
     */
    @SuppressWarnings("unchecked")
    public static <T> T invokeStaticMethod(final Class<?> targetType,
                                           final String methodName,
                                           final Object... parameters) {
        //
        // passing null in here because we are invoking a static method and do
        // not have
        // an instance
        //
        return (T) invokeMethod(targetType, null, methodName, parameters);
    }

    /**
     * Implementation of invokeMethod. Contains the common code for invoking
     * instance and static methods. If invoking a static method then
     * targetInstance will be null
     */
    @SuppressWarnings("unchecked")
    private static <T> T invokeMethod(final Class<?> targetType,
                                      final Object targetInstance,
                                      final String methodName,
                                      final Object... parameters) {
        Assert.argNotNull(targetType, "targetType");
        Assert.argNotNull(methodName, "methodName");

        //
        // Extract the class type for each parameter so we can find the method
        // that takes the parameters given.
        //
        final Class<?>[] paramTypes = toClassArray(parameters);

        //
        // Find the method to invoke
        //
        final Method method = getMethod(targetType, methodName, paramTypes);

        //
        // Make sure the method exists. Not using Assert.notNull here because we
        // want to avoid creating the failure string if we can
        //
        if (method == null) {
            Assert.fail("Method: " + methodName
                        + " with parameters: "
                        + Arrays.toString(paramTypes)
                        + " does not exist on type: "
                        + targetType);
        }

        //
        // Invoke the method and return the result.
        //
        try {
            return (T) method.invoke(targetInstance, parameters);
        }
        catch (final Exception e) {
            Assert.fail("Failed to call method: " + methodName
                        + " on type: "
                        + targetType
                        + ", with params: "
                        + Arrays.toString(parameters));

            return null; // DEAD CODE
        }
    }

    /**
     * Extracts the class from each object given.
     * 
     * @return an array containing the class of each object provided
     */
    public static Class<?>[] toClassArray(final Object... args) {
        final Class<?>[] result = new Class[args.length];
        for (int i = 0; i < result.length; ++i) {
            final Object arg = args[i];
            result[i] = arg != null ? arg.getClass() : null;
        }
        return result;
    }

    /**
     * Returns the constructor of the given class with the given arguments.
     * Returns null if no such constructor exists.
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getConstructor(final Class<T> target,
                                                    final Class<?>... argumentTypes) {
        //
        // Iterate through all the constructors and find one that tages the
        // given
        // arguments.
        //
        for (final Constructor<?> c : target.getDeclaredConstructors()) {

            if (isAssignableTo(argumentTypes, c.getParameterTypes())) {

                // set accessible so we can access private constructors
                c.setAccessible(true);
                return (Constructor<T>) c;
            }
        }
        return null;
    }

    /**
     * Invokes the constructor of the target with the given params. Throws an
     * IllegalArgumentException if no such constructor exists.
     */
    public static <T> T invokeConstructor(final Class<T> target, final Object... params) {
        final Class<?>[] paramTypes = toClassArray(params);
        final Constructor<T> constructor = getConstructor(target, paramTypes);

        if (constructor == null) {
            Assert.fail("No constructor found with arguments: " + Arrays.toString(params)
                        + ", for type: "
                        + target);
        }

        try {
            return constructor.newInstance(params);
        }
        catch (final Exception e) {
            Assert.fail("Failed to invoke constructor", e);
            return null; // DEAD CODE
        }
    }

    /**
     * Very very simple argument type lookup. Doesn't work for complicated class
     * hierarchies. Only works when all the type arguments are defined by a
     * child class.
     * 
     * Sample usage: <code>
     * class Dao<T> {}
     * class HibernateDao extends Dao<T>{
     *    Class myType;
     *    public HibernateDao()
     *    {
     *       myType = getTypeArgument(getClass(), 0);
     *    }
     * }
     * class EventHibernateDao extends HibernateDao<Event>
     * {}
     * </code>
     * 
     * In this case this is just a fancy wrapper around this code: <code>
     * (Class)((ParameterizedType)getClass().
     *       getGenericSuperclass()).getActualTypeArguments()[0];
     * </code>
     * 
     * What this method buys you is it will keep going up the class hierarchy
     * until it finds a match parameter. For example, this code will only work
     * with this method: <code>
     * class Op<T> {}
     * class BinaryOp<T,U>{}
     * class EventOp extends BinaryOp<EventAction, Event> {}
     * class MyEventOp extends EventOp {}
     * 
     * Class arg1 = getTypeArgument(MyEventOp.class, 0);
     * </code>
     * 
     * This code is super simple, it does not work if all type arguments are not
     * defined in the same class. For instance, <code>
     * class A<T, U> {}
     * class B<T> extends A<Integer> {}
     * class C extends B<Long> {}
     * </code>
     * 
     * With this code getTypeArgument(0) will return Long.class, but there is no
     * way to retrieve the Integer.class argument.
     * 
     * @param target
     *            the class to lookup type arguments for. This class must
     *            inherit another class that takes type arguments
     * @param index
     *            the index of the type argument
     * 
     * @return the class of the type argument or null
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getTypeArgument(final Class<?> target, final int index) {
        Assert.argNotNull(target, "target");
        Assert.argIsNonNegative(index, "index");

        //
        // Walk the hierarchy looking for a type argument at the specified index
        //
        for (final Class<?> current : new ClassHierarchyIterator(target)) {

            //
            // Must be a parameterized type to have type arguments. If
            //
            final Type type = current.getGenericSuperclass();
            if (!(type instanceof ParameterizedType)) {
                continue;
            }

            //
            // Get the type argument at the given index from the
            // ParameterizedType. This
            // may return null if the ParameterizedType doesn't have that may
            // type
            // arguments.
            //
            final ParameterizedType pType = (ParameterizedType) type;
            final Class<?> typeArgument = getTypeArgument(pType, index);

            //
            // If we got a valid TypeArgument return it.
            //
            if (typeArgument != null) {
                return (Class<T>) typeArgument;
            }
        }

        return null;
    }

    private static Class<?> getTypeArgument(final ParameterizedType pType, final int index) {

        final Type[] types = pType.getActualTypeArguments();
        if (index < types.length) {

            final Type argType = types[index];
            if (argType instanceof Class) {
                return (Class<?>) argType;
            }

        }

        return null;
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private ReflectionUtils() {
    }
}
