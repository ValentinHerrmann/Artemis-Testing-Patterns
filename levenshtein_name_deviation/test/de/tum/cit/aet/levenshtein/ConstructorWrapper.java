package levenshtein;


import org.assertj.core.api.Assertions;

import de.tum.in.test.api.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.stream.Collectors;

import test.Messages;
import static levenshtein.Utils.toWrapperType;
import static levenshtein.WrapperProperty.Existence.*;

/**
 * Wrapper for class constructors that verifies their existence, parameter types, and modifiers.
 * Provides methods to invoke constructors and create instances.
 *
 * @param <T> the type of the class whose constructor is being wrapped
 */
public class ConstructorWrapper<T> extends Wrapper<T>
{
    /**
     * The expected parameter types for the constructor.
     */
    Class<?>[] paramTypes;

    /**
     * The actual constructor found via reflection.
     */
    Constructor<T> constructor;

    /**
     * Constructs a new ConstructorWrapper for verifying a class constructor.
     *
     * @param parentClass the class wrapper containing this constructor
     * @param paramTypes the expected parameter types for the constructor
     * @param modifiers the expected modifiers (e.g., "public", "protected")
     */
    public ConstructorWrapper(ClassWrapper<T> parentClass, Class<?>[] paramTypes, String... modifiers) {
        super(parentClass, "", modifiers);
        this.paramTypes = paramTypes;
    }

    /**
     * Constructs a new ConstructorWrapper for a no-argument constructor.
     *
     * @param parentClass the class wrapper containing this constructor
     * @param modifiers the expected modifiers (e.g., "public")
     */
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

            super.verifyExistence(String.format(Messages.CONSTRUCTOR_NOT_IMPLEMENTED,
                this.expectedToString(), getParentClassWrapper().name.expected), throwAssertion);
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



    /**
     * Invokes the constructor with the specified arguments to create a new instance.
     * For abstract classes, uses ByteBuddy to create a dynamic subclass instance.
     *
     * @param args the arguments to pass to the constructor
     * @return a new instance of the class
     */
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
            Assertions.fail(String.format(Messages.CONSTRUCTOR_INVOCATION_FAILED, this.expectedToString(), getParentClassWrapper().name.expected, e.getMessage()));
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

    /**
     * Retrieves the parameter types expected for this constructor.
     *
     * @return an array of parameter types
     */
    public Class<?>[] getParamTypes() {
        return paramTypes;
    }
}
