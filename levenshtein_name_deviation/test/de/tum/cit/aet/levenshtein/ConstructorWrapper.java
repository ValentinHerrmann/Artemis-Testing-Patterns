package de.tum.cit.aet.levenshtein;


import org.assertj.core.api.Assertions;

import de.tum.in.test.api.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

import static de.tum.cit.aet.levenshtein.WrapperProperty.Existence; 
import static de.tum.cit.aet.levenshtein.WrapperProperty.Existence.*;

public class ConstructorWrapper<T> extends Wrapper<T>
{
    Class<?>[] paramTypes;
    Constructor<T> constructor;

    public ConstructorWrapper(ClassWrapper<T> parentClass, Class<?>[] paramTypes, String... modifiers) {
        super(parentClass, "", modifiers);
        this.paramTypes = paramTypes;
    }

    @Override
    public void verifyExistence(boolean throwAssertion) {
        if (Arrays.asList(getParentClassWrapper().modifiers.expected.split(" ")).contains("abstract")  &&
                Modifier.isAbstract(getParentClassWrapper().getClazz().getModifiers()))
        {
            existence = DEVIATES;
        }
        else
        {
            super.verifyExistence(String.format("Constructor %s in class %s is not implemented as expected.", this.expectedToString(), getParentClassWrapper().name.expected),throwAssertion);
        }
    }

    @Override
    protected void findWithDeviation()
    {
        ClassWrapper<T> parentClassWrapper = getParentClassWrapper();
        Class<T> clazz = parentClassWrapper.getClazz();
        if(clazz != null) {
            constructor = ReflectionTestUtils.getConstructor(clazz, paramTypes);
        }
        existence = constructor == null ? MISSING : EXACT;
    }

    @SuppressWarnings("unchecked")
    public T invoke(Object... args)
    {
        verifyExistence(true);
        Object[] res = new Object[1];
        try { constructor.setAccessible(true); } catch (Exception e) { /*Ignore*/ }
        Assertions.assertThatCode(() -> {
            res[0] = constructor.newInstance(args);
        }).doesNotThrowAnyException();
        if (res[0] == null) {
           return null; 
        }
        else {

            getParentClassWrapper().obj = (T)(res[0]);
            return (T)res[0];
        }
    }

    protected void parseExistence() {
        if(existence != MISSING) {
            parseExistence(modifiers);
        }
    }

    @Override
    public String expectedToString() {
        return String.format(
                "%s %s(%s)",
                modifiers.expected,
                getParentClassWrapper().name.expected,
                Arrays.stream(paramTypes).map(Class::toString).collect(Collectors.joining(", "))
        );
    }

    @Override
    public String actualToString() {
        if(existence==MISSING) {
            return "<missing>";
        }
        return String.format(
                "%s %s(%s)",
                modifiers.actual,
                getParentClassWrapper().name.actual,
                Arrays.stream(paramTypes).map(Class::toString).collect(Collectors.joining(", "))
        );
    }
}
