package de.tum.cit.aet.levenshtein;


import de.tum.cit.aet.TestSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import static de.tum.cit.aet.levenshtein.WrapperProperty.Existence.*;
import static de.tum.cit.aet.levenshtein.LevenshteinUtils.isNameWithinDeviation;
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

    @Override
    public void verifyExistence(boolean throwAssertion)
    {
        super.verifyExistence(String.format("Method %s in class %s is not implemented as expected.", this.expectedToString(), getParentClassWrapper().name.expected),throwAssertion);
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

    @SuppressWarnings("unchecked") // is checked at runtime
    public R invoke(Object... params)
    {
        Object val = invoke(null, params);
        if(val == null) {
            return null;
        }
        else {
            return (R)saveCast(val, returnType.expected);
        }
    }

    @SuppressWarnings("unchecked") // is checked at runtime
    public R invoke(ClassWrapper<?> objWrapper, Object... params)
    {
        verifyExistence(true);
        Object[] result = new Object[1];
        assertThatCode(() -> {
            Object obj;
            if (objWrapper != null)
            {
                obj = objWrapper.getObj();
            }
            else
            {
                obj = Arrays.asList(modifiers.actual.split(" ")).contains("static") ? null : getParentClassWrapper().getObj();
            }
            try
            {
                method.setAccessible(true);
            }
            catch (Exception e) { /*Ignore*/ }

            result[0] = method.invoke(obj, params);
        }).withFailMessage("Calling method %s on class %s threw an exception.",toString(), getParentClassWrapper().name.expected)
        .doesNotThrowAnyException();
        return (R)result[0];
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
