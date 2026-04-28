package levenshtein;


import de.tum.in.test.api.util.ReflectionTestUtils;
import test.TestSettings;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

import test.Messages;
import static levenshtein.WrapperProperty.Existence.*;
import static levenshtein.Utils.*;
import static org.assertj.core.api.Assertions.fail;

/**
 * Wrapper for class methods that verifies their existence, name, return type, parameter types, and modifiers
 * using Levenshtein distance for fuzzy name matching.
 *
 * @param <T> the type of the class containing the method
 * @param <R> the expected return type of the method
 */
public class MethodWrapper<T, R> extends Wrapper<T>
{
    /**
     * The expected parameter types for the method.
     */
    private final Class<?>[] paramTypes;

    /**
     * Wrapper for the expected and actual return type.
     */
    private final WrapperProperty<Class<?>> returnType;

    /**
     * The actual method found via reflection.
     */
    private Method method;

    /**
     * Constructs a new MethodWrapper for verifying a class method.
     *
     * @param parentClass the class wrapper containing this method
     * @param expectedName the expected name of the method
     * @param expectedReturnType the expected return type of the method
     * @param paramTypes the expected parameter types for the method
     * @param modifiers the expected modifiers (e.g., "public", "static")
     */
    public MethodWrapper(ClassWrapper<T> parentClass, String expectedName, Class<R> expectedReturnType, Class<?>[] paramTypes, String... modifiers) {
        super(parentClass, expectedName, modifiers);
        this.paramTypes = paramTypes;
        this.returnType = new WrapperProperty<>(expectedReturnType);
    }

    /**
     * Constructs a new MethodWrapper for a method with no parameters.
     *
     * @param parentClass the class wrapper containing this method
     * @param expectedName the expected name of the method
     * @param expectedReturnType the expected return type of the method
     * @param modifiers the expected modifiers (e.g., "public", "static")
     */
    @SuppressWarnings("unused")
    public MethodWrapper(ClassWrapper<T> parentClass, String expectedName, Class<R> expectedReturnType, String... modifiers) {
        this(parentClass, expectedName, expectedReturnType, new Class<?>[0], modifiers);
    }

    @Override
    public void verifyExistence(boolean throwAssertion)
    {
        super.verifyExistence(String.format(Messages.METHOD_NOT_IMPLEMENTED, this.expectedToString(), getParentClassWrapper().name.expected), throwAssertion);
    }

    @Override
    protected void findWithDeviation()
    {
        Class<T> clazz = getParentClassWrapper().getClazz();
        if(clazz != null) {

            try // First try exact match
            {
                method = clazz.getDeclaredMethod(name.expected, paramTypes);
                name.actual = method.getName();
                existence = EXACT;
            } catch (NoSuchMethodException e) {
                // Try to find a method with similar name and same parameter types
                for (Method m : clazz.getDeclaredMethods()) {
                    if (m.getParameterCount() == paramTypes.length) {
                        // Check if parameter types match
                        Class<?>[] actualParams = m.getParameterTypes();
                        boolean paramsMatch = true;
                        for (int i = 0; i < paramTypes.length; i++) {
                            if (!actualParams[i].equals(paramTypes[i])) {
                                paramsMatch = false;
                                break;
                            }
                        }

                        // If parameters match, check name deviation
                        if (paramsMatch && isNameWithinDeviation(name.expected, m.getName(), TestSettings.METHOD_NAME_DEVIATION_THRESHOLD)) {
                            this.method = m;
                            name.actual = m.getName();
                            existence = DEVIATES;
                        }
                    }
                }
            }
        }
        if (this.method == null)
        {
            existence = name.existence = returnType.existence = modifiers.existence = MISSING;
        }
        else
        {
            verifyModifiers(method.getModifiers());
            verifyType(returnType, method.getReturnType());
        }
        parseExistence();
    }

    @Override
    protected void parseExistence() {
        parseExistence(name, returnType, modifiers);
    }

    /**
     * Invokes the method on the class's default instance (or null for static methods).
     * Use
     *
     * @param params values for the method's parameters
     * @return the return value of the method, safely cast to R
     */
    @SuppressWarnings("unchecked")
    public R invoke(Object... params)
    {
        Object val = invokeOnSpecificObject(null, params);
        if(val == null) {
            return null;
        }
        else {
            return (R)saveCast(val, returnType.expected);
        }
    }

    /**
     * Invokes the method on a specific object instance.
     *
     * @param obj the object to invoke the method on (null uses default instance or null for static methods)
     * @param params values for the method's parameters
     * @return the return value of the method, safely cast to R
     */
    @SuppressWarnings("unchecked")
    public R invokeOnSpecificObject(Object obj, Object... params)
    {
        verifyExistence(true);
        try {
            boolean useByteBuddy = !Modifier.isPrivate(method.getModifiers());
            boolean stat = Modifier.isStatic(method.getModifiers());

            if(obj == null) {
                obj = stat ? null : getParentClassWrapper().getObj(useByteBuddy);
            }
            try {
                method.setAccessible(true);
            } catch (Exception e) { /*Ignore*/ }

            Object val = ReflectionTestUtils.invokeMethod(obj, method, params);
            if(val == null) {
                return null;
            }
            return (R)saveCast(val, returnType.expected);
        }
        catch(Exception e) {
            fail(Messages.METHOD_INVOCATION_EXCEPTION, actualToString(), getParentClassWrapper().name.expected);
        }
        return null;
    }

    @Override
    public String expectedToString()
    {
        return String.format(
                "%s %s %s(%s)",
                modifiers.expected,
                returnType.expected == null ? "<missing>" : returnType.expected.getSimpleName(),
                name.expected,
                String.join(", ",Arrays.stream(paramTypes).map(Class::getSimpleName).collect(Collectors.joining(", ")))
        );
    }

    @Override
    public String actualToString() {
        if(existence==MISSING) {
            return "<missing>";
        }
        return String.format(
                "%s %s %s(%s)",
                modifiers.actual,
                returnType.actual == null ? "<missing>" : returnType.actual.getSimpleName(),
                name.actual,
                String.join(", ",Arrays.stream(paramTypes).map(Class::getSimpleName).collect(Collectors.joining(", ")))
        );
    }
}
