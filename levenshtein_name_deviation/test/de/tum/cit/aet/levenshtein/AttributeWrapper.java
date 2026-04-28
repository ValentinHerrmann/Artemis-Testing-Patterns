package levenshtein;

import de.tum.in.test.api.util.ReflectionTestUtils;
import test.TestSettings;

import org.assertj.core.api.Assertions;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import test.Messages;
import static levenshtein.WrapperProperty.Existence.*;
import static levenshtein.Utils.*;
import static org.assertj.core.api.Assertions.fail;

/**
 * Wrapper for class attributes (fields) that verifies their existence, name, type, and modifiers
 * using Levenshtein distance for fuzzy matching.
 *
 * @param <T> the type of the class containing the attribute
 * @param <V> the expected type of the attribute value
 */
public class AttributeWrapper<T, V> extends Wrapper<T>
{

    /**
     * The field (Reflection object) representing the detected attribute in the class.
     */
    private Field field;

    /**
     * Just here to determine generic type V. Not used for anything else.
     * Use {@link AttributeWrapper#type} to get the expected type.
     */
    @SuppressWarnings("unused")
    private final Class<V> expectedType;

    /**
     * Stored expected and actual type of attribute and
     * information about its existence.
     */
    private final WrapperProperty<Class<?>> type;

    /**
     * Constructs a new AttributeWrapper for verifying a class attribute.
     *
     * @param parentClass the class wrapper containing this attribute
     * @param expectedName the expected name of the attribute
     * @param expectedType the expected type of the attribute
     * @param modifiers the expected modifiers (e.g., "public", "static", "final")
     */
    public AttributeWrapper(ClassWrapper<T> parentClass, String expectedName, Class<V> expectedType, String... modifiers) {
        super(parentClass, expectedName, modifiers);
        this.expectedType = expectedType;
        this.type = new WrapperProperty<>(expectedType);
    }



    @Override
    public void verifyExistence(boolean throwAssertion)
    {
        super.verifyExistence(String.format(Messages.ATTRIBUTE_NOT_IMPLEMENTED,
                name.expected, getParentClassWrapper().name.expected), throwAssertion);
    }

    /**
     * Retrieves the value of this attribute from a default instance.
     * Uses the parent class's default object instance.
     * To get the value from a specific instance, use {@link AttributeWrapper#getValue(Object)}.
     * Uses {@link Assertions#fail()} to report access failures.
     *
     * @return the attribute value
     */
    @SuppressWarnings("unused")
    public V getValue()
    {
        return getValue(null);
    }

    /**
     * Retrieves the value of this attribute from a specific object instance.
     * If obj is null, creates or uses a default instance from the parent class.
     * Attempts direct field access, falling back to getter methods if blocked by security manager.
     * Uses {@link Assertions#fail()} to report access failures.
     *
     * @param obj the object instance to get the value from (null for static fields or default instance)
     * @return the attribute value, safely cast to type V
     */
    @SuppressWarnings("unchecked")
    public V getValue(Object obj) { // usually obj is type T, but could be a subclass
        verifyExistence(true);

        if(obj == null) {
            boolean useByteBuddy = !Modifier.isPrivate(field.getModifiers());
            boolean stat = Modifier.isStatic(field.getModifiers());
            obj = stat ? null : getParentClassWrapper().getObj(useByteBuddy);

        }
        Object val = null;
        try {
            field.setAccessible(true);
            val = field.get(obj);
        } catch (Throwable e) {
            // If access is blocked by the Artemis SecurityManager (suppressAccessChecks),
            // fall back to calling a public getter method (e.g. getYear) if available.
            boolean isSecEx = e instanceof SecurityException || (e.getCause() != null && e.getCause() instanceof SecurityException);
            if (isSecEx) {
                // Attempt to call a conventional getter: get<FieldName>()
                try {
                    String fname = name.expected;
                    String getter = "get" + Character.toUpperCase(fname.charAt(0)) + fname.substring(1);
                    val = ReflectionTestUtils.invokeMethod(obj, getter);
                } catch (Throwable ex2) {
                    // Print both exceptions for debugging and fail
                    try { System.err.println("[AttributeWrapper] primary access blocked, getter fallback failed for '" + name.expected + "'"); e.printStackTrace(System.err); ex2.printStackTrace(System.err); } catch (Throwable ignore) {}
                    String causeMsg = ex2 == null ? (e == null ? "<no-exception>" : e.toString()) : ex2.toString();
                    Assertions.fail(String.format(Messages.ATTRIBUTE_ACCESS_FAILED,
                            name.expected, getParentClassWrapper().name.expected, causeMsg));
                }
            } else {
                // Not a security exception -> fail with original exception
                try { System.err.println("[AttributeWrapper] valueForNonPublicAttribute failed for '" + name.expected + "'"); e.printStackTrace(System.err); } catch (Throwable ignore) {}
                String causeMsg = e == null ? "<no-exception>" : e.toString();
                Assertions.fail(String.format(Messages.ATTRIBUTE_ACCESS_FAILED,
                        name.expected, getParentClassWrapper().name.expected, causeMsg));
            }
        }
        return (V)saveCast(val, type.expected);
    }

    /**
     * Sets the value of this attribute on a default instance.
     * Uses the parent class's default object instance. To set the value on a specific instance, use {@link AttributeWrapper#setValue(Object, Object)}.
     * Fails if the attribute is declared final.
     * Uses {@link Assertions#fail()} to report access failures.
     *
     * @param value the value to set
     */
    public void setValue(Object value)
    {
        setValue(null, value);
    }



    /**
     * Sets the value of this attribute on a specific object instance.
     * If obj is null, creates or uses a default instance from the parent class.
     * Fails if the attribute is declared final.
     * Uses {@link Assertions#fail()} to report access failures.
     *
     * @param obj the object instance to set the value on (null for static fields or default instance)
     * @param value the value to set
     */
    public void setValue(Object obj, Object value)
    {
        verifyExistence(true);

        boolean useByteBuddy = !Modifier.isPrivate(field.getModifiers());
        boolean isFinal = Modifier.isFinal(field.getModifiers());
        if(isFinal) {
            fail(String.format(Messages.ATTRIBUTE_FINAL_CANNOT_SET,
                    name.expected, getParentClassWrapper().name.expected));
        }

        final Object object = obj == null ? Modifier.isStatic(field.getModifiers()) ? null : getParentClassWrapper().getObj(useByteBuddy) : obj;
        try {
            try { field.setAccessible(true); } catch (Exception e) { /*Ignore*/ }
            field.set(object, value);
        }
        catch (AssertionError e) {  fail(e.getMessage()); }
        catch (Exception e) {
            Assertions.fail(String.format(Messages.ATTRIBUTE_SET_FAILED,
                    name.expected, getParentClassWrapper().name.expected), e);
        }
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
