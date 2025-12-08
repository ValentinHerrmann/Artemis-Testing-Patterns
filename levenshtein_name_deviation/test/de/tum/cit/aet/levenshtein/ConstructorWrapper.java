package de.tum.cit.aet.levenshtein;


import org.assertj.core.api.Assertions;

import de.tum.in.test.api.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.stream.Collectors;

import static de.tum.cit.aet.levenshtein.WrapperProperty.Existence.*;

public class ConstructorWrapper<T> extends Wrapper<T>
{
    Class<?>[] paramTypes;
    Constructor<T> constructor;

    public ConstructorWrapper(ClassWrapper<T> parentClass, Class<?>[] paramTypes, String... modifiers) {
        super(parentClass, "", modifiers);
        this.paramTypes = paramTypes;
    }

    @SuppressWarnings("unused")
    public ConstructorWrapper(ClassWrapper<T> parentClass, String modifiers) {
        this(parentClass, new Class<?>[] {}, modifiers);
    }

    @Override
    public void verifyExistence(boolean throwAssertion) {
        /* Not sure why that was there. Does not seem to make sense.
        var mod = getParentClassWrapper().modifiers.expected;
        var actMod = Modifier.toString(getParentClassWrapper().getClazz().getModifiers());
        if (Arrays.asList(getParentClassWrapper().modifiers.expected.split(" ")).contains("abstract")  &&
                Modifier.isAbstract(getParentClassWrapper().getClazz().getModifiers()))
        {
            existence = DEVIATES;
        }
        else
        {
        */

            super.verifyExistence(String.format("""
                Constructor %s in class %s is not implemented as expected.
                --> See structural Tests for details about this.
                --> This may lead subsequent tests to fail.
                """,
                this.expectedToString(), getParentClassWrapper().name.expected),throwAssertion);
        //}
    }

    @Override
    protected void findWithDeviation()
    {
        ClassWrapper<T> parentClassWrapper = getParentClassWrapper();
        Class<T> clazz = parentClassWrapper.getClazz();
        if(clazz != null) {
            constructor = ReflectionTestUtils.getConstructor(clazz, paramTypes);
            // Falls nicht gefunden, versuche mit konvertierten Wrapper-Typen
            if(constructor == null) {
                Class<?>[] wrapperTypes = new Class<?>[paramTypes.length];
                for(int i = 0; i < paramTypes.length; i++) {
                    wrapperTypes[i] = toWrapperType(paramTypes[i]);
                }
                constructor = ReflectionTestUtils.getConstructor(clazz, wrapperTypes);
            }
        }
        if(constructor != null) {
            this.existence = EXACT;
        }
        else {
            this.existence = MISSING;
        }
    }

    private Class<?> toWrapperType(Class<?> type) {
        if(type == int.class) return Integer.class;
        if(type == long.class) return Long.class;
        if(type == double.class) return Double.class;
        if(type == float.class) return Float.class;
        if(type == boolean.class) return Boolean.class;
        if(type == short.class) return Short.class;
        if(type == byte.class) return Byte.class;
        if(type == char.class) return Character.class;
        return type;
    }

    @SuppressWarnings("unchecked")
    public T invoke(Object... args)
    {
        verifyExistence(true);
        try { constructor.setAccessible(true); } catch (Exception e) { /*Ignore*/ }

        try {
            if(getParentClassWrapper().modifiers.actual.contains("abstract")) {
                return getParentClassWrapper().getObj(true,true,this,args);
            }
            else {
                return (T) ReflectionTestUtils.newInstance(constructor, args);
            }
        }
        catch (Exception e) {
            Assertions.fail(String.format("Failed to invoke constructor '%s' in class %s: %s", this.expectedToString(), getParentClassWrapper().name.expected, e.getMessage()));
        }
        return null;
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
                Arrays.stream(paramTypes).map(Class::getSimpleName).collect(Collectors.joining(", "))
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
                Arrays.stream(paramTypes).map(Class::getSimpleName).collect(Collectors.joining(", "))
        );
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }
}
