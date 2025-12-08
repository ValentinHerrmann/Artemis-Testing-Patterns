package de.tum.cit.aet.levenshtein;


import de.tum.cit.aet.TestSettings;
import de.tum.in.test.api.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static de.tum.cit.aet.levenshtein.WrapperProperty.Existence.*;
import static de.tum.cit.aet.levenshtein.LevenshteinUtils.*;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

public class MethodWrapper<T, R> extends Wrapper<T>
{
    private final Class<?>[] paramTypes;
    private final WrapperProperty<Class<?>> returnType;
    private Method method;


    public MethodWrapper(ClassWrapper<T> parentClass, String expectedName, Class<R> expectedReturnType, Class<?>[] paramTypes, String... modifiers) {
        super(parentClass, expectedName, modifiers);
        this.paramTypes = paramTypes;
        this.returnType = new WrapperProperty<>(expectedReturnType);
    }

    public MethodWrapper(ClassWrapper<T> parentClass, String expectedName, Class<R> expectedReturnType, String... modifiers) {
        this(parentClass, expectedName, expectedReturnType, new Class<?>[0], modifiers);
    }

    @Override
    public void verifyExistence(boolean throwAssertion)
    {
        super.verifyExistence(String.format("Method %s in class %s is not implemented as expected.\nThis may lead subsequent tests to fail.", this.expectedToString(), getParentClassWrapper().name.expected),throwAssertion);
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
     * Invokes the method on the classes getObj()  (or if static using null as object).
     * {@param params} Values for the methods parameters.
     * @return The return value of the method, save-cast to R.
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
     *
     * @param objWrapper The object to invoke the method on. If null, uses getObj() of the parent class (or null for static methods).
     * {@param params} Values for the methods parameters.
     * @return The return value of the method, save-cast to R.
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
            fail("Calling method '%s' on class '%s' threw an exception.",actualToString(), getParentClassWrapper().name.expected);
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
                String.join(", ",Arrays.stream(paramTypes).map(Class::toString).collect(Collectors.joining(", ")))
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
                String.join(", ",Arrays.stream(paramTypes).map(Class::toString).collect(Collectors.joining(", ")))
        );
    }
}
