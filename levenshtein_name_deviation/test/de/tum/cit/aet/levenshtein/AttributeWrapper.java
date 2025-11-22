package de.tum.cit.aet.levenshtein;

import de.tum.cit.aet.TestSettings;
import de.tum.in.test.api.util.ReflectionTestUtils;
import org.assertj.core.api.Assertions;
import org.junit.platform.commons.util.ReflectionUtils;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

import static de.tum.cit.aet.levenshtein.WrapperProperty.Existence.*;
import static de.tum.cit.aet.levenshtein.LevenshteinUtils.isNameWithinDeviation;

public class AttributeWrapper<T, V> extends Wrapper<T>
{
    private Field field;
    private final Class<V> expectedType;
    private final WrapperProperty<Class<?>> type;


    public AttributeWrapper(ClassWrapper<T> parentClass, String expectedName, Class<V> expectedType, String... modifiers) {
        super(parentClass, expectedName, modifiers);
        this.expectedType = expectedType;
        this.type = new WrapperProperty<>(expectedType);
    }



    @Override
    public void verifyExistence(boolean throwAssertion)
    {
        super.verifyExistence(String.format("Attribute %s in class %s is not implemented as expected.", name.expected, getParentClassWrapper().name.expected),throwAssertion);
    }

    public V getValue()
    {
        return getValue(null);
    }
    @SuppressWarnings("unchecked")
    public V getValue(Object obj) { // usually obj is type T, but could be a subclass
        verifyExistence(true);
        try { field.setAccessible(true); } catch (Exception e) { /*Ignore*/ }

        Object object = (obj != null) ? obj : (Modifier.isStatic(field.getModifiers()) ? null : getParentClassWrapper().getObj());

        @SuppressWarnings("deprecation")
        Optional<?> val = ReflectionUtils.readFieldValue(field,object);


        return val.map(o -> (V) saveCast(o, type.expected)).orElse(null);

         /*
            Object v = val.get();
            if(v instanceof Double) {
                Double d = (Double) val.get();
                if(expectedType == Float.class || expectedType == float.class) {
                    return (V) Float.valueOf(d.floatValue());
                }
                else if(expectedType == Long.class || expectedType == long.class) {
                    return (V) Long.valueOf(d.longValue());
                }
                else if(expectedType == Integer.class || expectedType == int.class) {
                    return (V) Integer.valueOf(d.intValue());
                }
                else if(expectedType == Double.class || expectedType == double.class) {
                    return (V) d;
                }
            }
            if(v instanceof Float) {
                Float f = (Float)val.get();
                if(expectedType == Double.class || expectedType == double.class) {
                    return (V) Double.valueOf(f.doubleValue());
                }
                else if(expectedType == Long.class || expectedType == long.class) {
                    return (V) Long.valueOf(f.longValue());
                }
                else if(expectedType == Integer.class || expectedType == int.class) {
                    return (V) Integer.valueOf(f.intValue());
                }
                else if(expectedType == Float.class || expectedType == float.class) {
                    return (V) f;
                }
            }
            return (V)val.get();
        }
        else {
            return null;
        }
        */
    }

    public void setValue(V value)
    {
        setValue(value, null);
    }
    public void setValue(Object value, Object obj)
    {
        verifyExistence(true);
        final Object object = obj == null ? Modifier.isStatic(field.getModifiers()) ? null : getParentClassWrapper().getObj() : obj;
        Assertions.assertThatCode(() ->
                  ReflectionTestUtils.setValueOfNonPublicAttribute(object,field.getName(),value)
         ).doesNotThrowAnyException();
    }



    /**
     * Attempts to find a field in a class, allowing for deviation in the attribute name.
     * Sets values for existence, and field if found.
     */
    @Override
    protected void findWithDeviation()
    {
        Class<?> clazz = getParentClassWrapper().getClazz();
        if(clazz != null) {
            try // First try exact match
            {
                field = clazz.getDeclaredField(name.expected);
                name.actual = name.expected;
                name.existence = EXACT;
            } catch (NoSuchFieldException e) {
                // Try to find a field with similar name
                for (Field f : clazz.getDeclaredFields()) {
                    if (isNameWithinDeviation(name.expected, f.getName(), TestSettings.ATTRIBUTE_NAME_DEVIATION_THRESHOLD)) {
                        name.existence = DEVIATES;
                        name.actual = f.getName();
                        field = f;
                    }
                }
            }
        }
        if (field == null)
        {
            existence = name.existence = type.existence = modifiers.existence = MISSING;
        }
        else
        {
            verifyModifiers(field.getModifiers());
            verifyType(type, field.getType());
        }
        parseExistence();
    }

    @Override
    protected void parseExistence() {
        parseExistence(name, type, modifiers);
    }

    @Override
    public String expectedToString() {
        String t = type.expected == null ? " <missing> " : type.expected.getSimpleName();
        return String.format("%s %s %s", modifiers.expected, t, name.expected);
    }

    @Override
    public String actualToString() {
        if(existence==MISSING) {
            return "<missing>";
        }
        String t = type.actual == null ? " <missing> " : type.actual.getSimpleName();
        return String.format("%s %s %s", modifiers.actual, t, name.actual);
    }

}
