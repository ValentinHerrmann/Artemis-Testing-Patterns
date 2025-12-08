package de.tum.cit.aet.levenshtein;

import static de.tum.cit.aet.levenshtein.WrapperProperty.Existence;
import static de.tum.cit.aet.levenshtein.WrapperProperty.Existence.*;

import org.assertj.core.api.Assertions;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public abstract class Wrapper<T>
{    
    protected WrapperProperty<String> name;
    protected WrapperProperty<String> modifiers;
    protected Existence existence;

    private final ClassWrapper<T> parentClassWrapper;

    
    public Wrapper(ClassWrapper<T> parentClass, String expectedName, String... expectedModifiers) {
        name = new WrapperProperty<>(expectedName);
        this.parentClassWrapper = parentClass;
        this.modifiers = new WrapperProperty<>(String.join(" ",expectedModifiers));
        this.existence = UNCHECKED;
    }


    public abstract void verifyExistence(boolean throwAssertion);

    protected void verifyExistence(String failMessage, boolean throwAssertion)
    {
        if (existence == UNCHECKED)
        {
            findWithDeviation();
        }
        if(throwAssertion) {
            Assertions.assertThat(existence)
                    .withFailMessage(failMessage)
                    .isNotEqualTo(MISSING);
        }
    }

    public Existence getOverallExistence() {
        findWithDeviation();
        parseExistence();
        return existence;
    }

    protected abstract void findWithDeviation();

    private Existence selectWorstExistence(Existence worst, Existence updated) {
        return worst.ordinal() > updated.ordinal() ? worst : updated;
    }

    public void verifyModifiers(int modifierBitmask) {
        String[] expectedModifiers = modifiers.expected.split(" ");
        modifiers.actual = Modifier.toString(modifierBitmask);

        Existence worst = EXACT; // the worst existence defines if the modifiers deviate or are wrong
        for (String modifier : expectedModifiers) {
            switch (modifier) {
                case "public" -> worst = Modifier.isPublic(modifierBitmask) ? worst : selectWorstExistence(worst, DEVIATES);
                case "private" -> worst = Modifier.isPrivate(modifierBitmask) ? worst : selectWorstExistence(worst, DEVIATES);
                case "protected" -> worst = Modifier.isProtected(modifierBitmask) ? worst : selectWorstExistence(worst, DEVIATES);
                case "static" -> worst = Modifier.isStatic(modifierBitmask) ? worst : selectWorstExistence(worst, MISSING);
                case "final" -> worst = Modifier.isFinal(modifierBitmask) ? worst : selectWorstExistence(worst, DEVIATES);
                case "abstract" -> worst = Modifier.isAbstract(modifierBitmask) ? worst : selectWorstExistence(worst, DEVIATES);
                case "synchronized" -> worst = Modifier.isSynchronized(modifierBitmask) ? worst : selectWorstExistence(worst, DEVIATES);
                case "native" -> worst = Modifier.isNative(modifierBitmask) ? worst : selectWorstExistence(worst, DEVIATES);
                case "transient" -> worst = Modifier.isTransient(modifierBitmask) ? worst : selectWorstExistence(worst, DEVIATES);
                case "volatile" -> worst = Modifier.isVolatile(modifierBitmask) ? worst : selectWorstExistence(worst, DEVIATES);
                case "strictfp" -> worst = Modifier.isStrict(modifierBitmask) ? worst : selectWorstExistence(worst, DEVIATES);
                case "interface" -> worst = Modifier.isInterface(modifierBitmask) ? worst : selectWorstExistence(worst, DEVIATES);
                case "" -> worst = (
                                    !Modifier.isPublic(modifierBitmask) &&
                                    !Modifier.isPrivate(modifierBitmask) &&
                                    !Modifier.isProtected(modifierBitmask))
                                ? worst : selectWorstExistence(worst, DEVIATES);
                default -> throw new RuntimeException("Unknown modifier: " + modifier);
            }
        }
        modifiers.existence = worst;
    }

    public void verifyType(WrapperProperty<Class<?>> typeWrapperProperty, Class<?> actualType) {
        typeWrapperProperty.actual = actualType;
        if (typeWrapperProperty.expected.equals(actualType)) {
            typeWrapperProperty.existence = EXACT;
        }
        else if (actualType.isAssignableFrom(typeWrapperProperty.expected)) {
            typeWrapperProperty.existence = DEVIATES;
        }
        else if (canContain(actualType, typeWrapperProperty.expected)) {
            typeWrapperProperty.existence = DEVIATES;
        }
        else {
            typeWrapperProperty.existence = MISSING;
        }
    }

    private boolean canContain(Class<?> actualType, Class<?> expectedType) {
        // Unwrap primitive wrapper classes
        Class<?> actual = unwrapPrimitive(actualType);
        Class<?> expected = unwrapPrimitive(expectedType);

        if (!actual.isPrimitive() || !expected.isPrimitive()) {
            return false;
        }

        // Check if actual numeric type can contain expected numeric type
        return (actual == long.class && (expected == int.class || expected == short.class || expected == byte.class || expected == char.class)) ||
                (actual == int.class && (expected == short.class || expected == byte.class || expected == char.class)) ||
                (actual == short.class && expected == byte.class) ||
                (actual == double.class && (expected == float.class || expected == long.class || expected == int.class || expected == short.class || expected == byte.class)) ||
                (actual == float.class && (expected == long.class || expected == int.class || expected == short.class || expected == byte.class));
    }

    private Class<?> unwrapPrimitive(Class<?> type) {
        if (type == Integer.class) return int.class;
        if (type == Long.class) return long.class;
        if (type == Short.class) return short.class;
        if (type == Byte.class) return byte.class;
        if (type == Double.class) return double.class;
        if (type == Float.class) return float.class;
        if (type == Character.class) return char.class;
        if (type == Boolean.class) return boolean.class;
        return type;
    }

    public ClassWrapper<T> getParentClassWrapper() {
        return this instanceof ClassWrapper<T> t ? t : parentClassWrapper;
    }

    @Override
    public String toString()
    {
        parseExistence();
        return switch (existence) {
            case EXACT -> expectedToString();
            case DEVIATES -> String.format(
                    """
                    !! DEVIATION in %s !!
                    If possible actual will be used for further testing.
                    Expect:\t%s
                    Actual:\t%s
                    """,
                    getParentClassWrapper().name.expected, expectedToString(),actualToString()
            );
            case MISSING -> String.format(
                    """
                    X MISSING in %s X
                    Expect:\t%s
                    """,
                    getParentClassWrapper().name.expected, expectedToString()
            );
            case UNCHECKED -> String.format(
                    """
                    ? UNCHECKED in %s ?
                    Expect:\t%s
                    """,
                    getParentClassWrapper().name.expected, expectedToString()
            );
        };
    }
    public abstract String expectedToString();
    public abstract String actualToString();

    protected abstract void parseExistence();
    protected void parseExistence(WrapperProperty<?> ... properties) {
        int a = Arrays.stream(properties).mapToInt(x->x.existence.ordinal()).max().orElse(MISSING.ordinal());
        existence = Existence.values()[a];
    }

    public String getExpectedName() {
        return name.expected;
    }


}
