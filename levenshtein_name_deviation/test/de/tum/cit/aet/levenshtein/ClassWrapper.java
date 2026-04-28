package levenshtein;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import test.Messages;
import test.TestSettings;

import org.assertj.core.api.Assertions;
import org.junit.platform.commons.util.ReflectionUtils;

import static levenshtein.Utils.*;
import static levenshtein.WrapperProperty.Existence.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Wrapper for classes that verifies their existence, name, modifiers, superclass, and interfaces
 * using Levenshtein distance for fuzzy matching. Provides methods to instantiate objects
 * and retrieve attribute, method, and constructor wrappers.
 *
 *
 * @param <T> the type of the class being wrapped
 */
public abstract class ClassWrapper<T> extends Wrapper<T>
{
    /**
     * The expected package name where the class should be located.
     */
    private final String expectedPackage;

    /**
     * The actual class found via reflection.
     */
    private Class<T> clazz;

    /**
     * A default instance of the class for testing purposes.
     */
    protected T obj;

    /**
     * Wrapper for the expected and actual superclass.
     */
    WrapperProperty<ClassWrapper<?>> superClassWrapper;

    /**
     * Wrapper for the expected and actual interfaces implemented by the class.
     */
    WrapperProperty<ClassWrapper<?>[]> interfaceWrappers;

    /**
     * Constructs a new ClassWrapper with superclass and interface expectations.
     *
     * @param expectedName the expected name of the class
     * @param expectedPackage the expected package name
     * @param superClassWrapper the expected superclass wrapper (null if extends Object)
     * @param interfaceWrappers the expected interface wrappers (null or empty if none)
     * @param modifiers the expected modifiers (e.g., "public", "abstract")
     */
    public ClassWrapper(String expectedName, String expectedPackage, ClassWrapper<?> superClassWrapper, ClassWrapper<?>[] interfaceWrappers, String... modifiers) {
        super(null, expectedName, modifiers);
        this.expectedPackage = expectedPackage;
        this.superClassWrapper = new WrapperProperty<>(superClassWrapper);
        this.interfaceWrappers = new WrapperProperty<>(interfaceWrappers == null ? new ClassWrapper<?>[0] : interfaceWrappers);
    }

    /**
     * Constructs a new ClassWrapper without superclass or interface expectations.
     *
     * @param expectedName the expected name of the class
     * @param expectedPackage the expected package name
     * @param modifiers the expected modifiers (e.g., "public", "abstract")
     */
    public ClassWrapper(String expectedName, String expectedPackage, String... modifiers) {
        this(expectedName, expectedPackage, null, null, modifiers);
    }

    @Override
    public void verifyExistence(boolean throwAssertion)
    {
        super.verifyExistence(String.format(Messages.CLASS_NOT_IMPLEMENTED, name.expected, expectedPackage), throwAssertion);
    }

    /**
     * Retrieves the actual class found via reflection.
     * Verifies existence without throwing an assertion.
     *
     * @return the actual class, or null if not found
     */
    public Class<T> getClazz()
    {
        verifyExistence(false);
        return clazz;
    }

    /**
     * Creates a dynamic subclass instance using ByteBuddy for abstract classes.
     * This allows testing abstract classes by creating concrete implementations at runtime.
     *
     * @param constructorParamTypes the parameter types for the constructor to invoke
     * @param constructorArgs the arguments to pass to the constructor
     * @return a new instance of the dynamic subclass
     */
    public Object getDynamicSubclassObj(Class<?>[] constructorParamTypes, Object... constructorArgs) {
        Object dynObj = null;
        try {
            // Use ByteBuddy to create a concrete subclass of the abstract class
            Class<?> dynamicType = new ByteBuddy()
                    .subclass(getClazz())
                    .make()
                    .load(getClazz().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                    .getLoaded();

            // Instantiate the dynamic subclass with constructor arguments
            java.lang.reflect.Constructor<?> ctor = dynamicType.getConstructor(constructorParamTypes);
        dynObj = ctor.newInstance(constructorArgs);
        }
        catch (Throwable e) {
            String errorMsg = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
            if (e.getCause() != null && e.getCause().getMessage() != null) {
                errorMsg += " (Cause: " + e.getCause().getMessage() + ")";
            }
            fail(Messages.CLASS_SUBCLASS_INSTANTIATION_FAILED, getClazz().getSimpleName(), errorMsg);

        }
        return dynObj;
    }

    /**
     * Returns an instance of the class represented by this ClassWrapper.
     * Should usually be overridden in concrete wrapper subclasses by calling getObj(constructor, constructorArgs).
     *
     * @param forceNew whether to force creation of a new instance instead of reusing cached obj
     * @param useByteBuddy whether to use ByteBuddy to create a dynamic subclass instance (set to false for private elements!)
     * @return an instance of the wrapped class
     */
    public abstract Object getObj(boolean forceNew, boolean useByteBuddy);

    /**
     * Returns an instance of the class, optionally creating a new one.
     *
     * @param useByteBuddy whether to use ByteBuddy for abstract classes
     * @return an instance of the wrapped class which might have been cached
     */
    public Object getObj(boolean useByteBuddy) {
        return getObj(false, useByteBuddy);
    }

    /**
     * Returns an instance of the class using a specific constructor.
     * For abstract classes with useByteBuddy=true, creates a dynamic subclass instance.
     * For concrete classes, invokes the specified constructor.
     *
     * @param forceNew whether to force creation of a new instance
     * @param useByteBuddy whether to use ByteBuddy for abstract classes
     * @param constructorWrapper the constructor wrapper to use for instantiation
     * @param constructorArgs the arguments to pass to the constructor
     * @return an instance of the wrapped class
     */
    @SuppressWarnings("unchecked")
    public T getObj(boolean forceNew, boolean useByteBuddy, ConstructorWrapper<?> constructorWrapper, Object... constructorArgs) {
        if (obj == null || forceNew) {
            if(useByteBuddy) {
                Class<?>[] types = new Class<?>[0];
                if(constructorWrapper != null) {
                    types = constructorWrapper.getParamTypes();
                }
                Object dynObj = getDynamicSubclassObj(types, constructorArgs);
                obj = (T)saveCast(dynObj, getClazz());
            }
            else {
                if(Modifier.isAbstract(getClazz().getModifiers())) {
                    fail(String.format(Messages.CLASS_CANNOT_INSTANTIATE_ABSTRACT, name.actual));
                }
                if(Modifier.isInterface(getClazz().getModifiers())) {
                    fail(String.format(Messages.CLASS_CANNOT_INSTANTIATE_INTERFACE, name.actual));
                }
                Assertions.assertThatCode(() ->
                            obj = (T)constructorWrapper.invoke(constructorArgs)
                        )
                        .withFailMessage(Messages.CLASS_INSTANTIATION_FAILED, name.expected)
                        .doesNotThrowAnyException();
                return obj;
            }
        }
        return obj;
    }

    /**
     * Attempts to find a class in the package for class validation tests,
     * using TestSettings.CLASS_NAME_DEVIATION_THRESHOLD.
     */
    @Override @SuppressWarnings("unchecked")
    protected void findWithDeviation() {
        // First try exact match
        try {
            if(expectedPackage.equals("")) {
                clazz = (Class<T>)Class.forName(name.expected);
            }
            else {
                clazz = (Class<T>)Class.forName(expectedPackage + "." + name.expected);
            }
            name.actual = clazz.getSimpleName();
            name.existence = EXACT;
        }
        catch (Exception e) {
            // Try common variations by scanning for similar class names
            String[] possibleVariations = generateClassNameVariations(name.expected);

            for (String variation : possibleVariations)
            {
                try
                {
                    Class<T> clz = (Class<T>)Class.forName(expectedPackage + "." + variation);
                    if (isClassNameWithinClassTestDeviation(name.expected, clz.getSimpleName()))
                    {
                        clazz = clz;
                        name.actual = clz.getSimpleName();
                        name.existence = DEVIATES;
                    }
                }
                catch (Exception f) { /* Continue to next variation*/ }
            }
        }
        if (clazz==null) {
            existence = name.existence = modifiers.existence = MISSING;
        }
        else {
            verifyModifiers(clazz.getModifiers());
            verifySuperClass();
            verifyInterfaces();
        }
    }

    /**
     * Verifies that the actual superclass matches or is compatible with the expected superclass.
     * Updates the superClassWrapper's existence state based on exact match, assignability, or mismatch.
     */
    public void verifySuperClass() {
        Class<?> clz = clazz.getSuperclass();
        superClassWrapper.actual = clz == null || clz == Object.class ? null : new GenericClassWrapper<>(clz);

        if (superClassWrapper.expected == null) {
            superClassWrapper.existence = clz == null || clz.equals(Object.class) ? EXACT : DEVIATES;
        }
        else {
            if (superClassWrapper.expected.getClazz().equals(superClassWrapper.actual.getClazz())) {
                superClassWrapper.existence = EXACT;
            }
            else if (superClassWrapper.actual != null && superClassWrapper.expected.getClazz().isAssignableFrom(superClassWrapper.actual.getClazz())) {
                superClassWrapper.existence = DEVIATES;
            }
            else {
                superClassWrapper.existence = MISSING;
            }
        }
    }

    /**
     * Verifies that the actual interfaces match the expected interfaces.
     * Checks if all expected interfaces are implemented, allowing for additional interfaces.
     * Updates the interfaceWrappers' existence state accordingly.
     */
    public void verifyInterfaces() {
        Class<?>[] clzs = clazz.getInterfaces();
        interfaceWrappers.actual = Arrays.stream(clzs).sorted().map(GenericClassWrapper::new).toArray(ClassWrapper<?>[]::new);

        if (interfaceWrappers.expected == null || interfaceWrappers.expected.length == 0) {
            interfaceWrappers.existence = interfaceWrappers.actual.length == 0 ? EXACT : DEVIATES;
        }
        else {
            for(ClassWrapper<?> e : interfaceWrappers.expected) {
                boolean found = false;
                for(ClassWrapper<?> a : interfaceWrappers.actual) {
                    if (e.equals(a)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    interfaceWrappers.existence = MISSING;
                    return;
                }
            }
        }
        boolean expNull = interfaceWrappers.expected == null;
        int expLen = expNull ? 0 : interfaceWrappers.expected.length;
        int actLen = interfaceWrappers.actual.length;

        if (actLen > expLen) {
            interfaceWrappers.existence = DEVIATES;
        }
        else {
            interfaceWrappers.existence = EXACT;
        }
    }


    /**
     * Generates possible variations of a class name to try when looking for similar classes.
     * This includes singular/plural forms and common character variations.
     */
    private static String[] generateClassNameVariations(String className) {
        List<String> variations = new ArrayList<>();

        // Add the original
        variations.add(className);

        // Add singular/plural variations
        if (className.endsWith("s")) {
            variations.add(className.substring(0, className.length() - 1)); // Remove 's'
        } else {
            variations.add(className + "s"); // Add 's'
        }

        // Add variations with common suffixes removed/added
        if (className.endsWith("es")) {
            variations.add(className.substring(0, className.length() - 2)); // Remove 'es'
        }

        // Add common character-level variations (one character removed at different positions)
        for (int i = 0; i < className.length(); i++) {
            String variation = className.substring(0, i) + className.substring(i + 1);
            if (!variation.isEmpty()) {
                variations.add(variation);
            }
        }

        // Add variations with characters swapped (transpositions)
        for (int i = 0; i < className.length() - 1; i++) {
            char[] chars = className.toCharArray();
            char temp = chars[i];
            chars[i] = chars[i + 1];
            chars[i + 1] = temp;
            variations.add(new String(chars));
        }

        return variations.toArray(new String[0]);
    }

    /**
     * Checks if the actual class name is within the class test deviation threshold.
     * @param expectedClassName The expected class name
     * @param actualClassName The actual class name
     * @return true if the deviation is within the threshold, false otherwise
     */
    private static boolean isClassNameWithinClassTestDeviation(String expectedClassName, String actualClassName) {
        if (expectedClassName.equals(actualClassName)) {
            return true;
        }
        int distance = levenshteinDistance(expectedClassName, actualClassName);
        int maxLength = Math.max(expectedClassName.length(), actualClassName.length());
        double deviation = (double) distance / maxLength;

        return deviation <= TestSettings.CLASS_NAME_DEVIATION_THRESHOLD;
    }

    /**
     * Retrieves all AttributeWrapper fields defined in this ClassWrapper subclass.
     * Uses reflection to find all fields of type AttributeWrapper.
     *
     * @return a list of attribute wrappers for this class
     */
    @SuppressWarnings("unchecked")
    public List<Wrapper<T>> getAttributeWrappers() {
        Class<?> currentClass = this.getClass();

        List<Wrapper<T>> attributeWrappers = new ArrayList<>();
        ReflectionUtils.findFields(currentClass, x->x.getType().equals(AttributeWrapper.class), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
            .forEach(field -> {
                try {
                    field.setAccessible(true);
                    attributeWrappers.add((AttributeWrapper<T, ?>) field.get(this));
                } catch(Throwable ignored) {}
            }); 
        return attributeWrappers;
    }

    /**
     * Retrieves all MethodWrapper fields defined in this ClassWrapper subclass.
     * Uses reflection to find all fields of type MethodWrapper.
     *
     * @return a list of method wrappers for this class
     */
    @SuppressWarnings("unchecked")
    public List<Wrapper<T>> getMethodsWrappers() {
        Class<?> currentClass = this.getClass();

        List<Wrapper<T>> methodWrappers = new ArrayList<>();
        ReflectionUtils.findFields(currentClass, x->x.getType().equals(MethodWrapper.class), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        methodWrappers.add((MethodWrapper<T, ?>) field.get(this));
                    } catch(Throwable ignored) {}
                });
        return methodWrappers;
    }

    /**
     * Retrieves all ConstructorWrapper fields defined in this ClassWrapper subclass.
     * Uses reflection to find all fields of type ConstructorWrapper.
     *
     * @return a list of constructor wrappers for this class
     */
    @SuppressWarnings("unchecked")
    public List<Wrapper<T>> getConstructorWrappers() {
        Class<?> currentClass = this.getClass();

        List<Wrapper<T>> ctrWrappers = new ArrayList<>();
        ReflectionUtils.findFields(currentClass, x->true, ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        Class<?> typ = field.getType();

                        if (typ.equals(ConstructorWrapper.class)) {
                            ctrWrappers.add((ConstructorWrapper<T>) field.get(this));
                        }
                    } catch(Throwable ignored) {}
                });
        return ctrWrappers;
    }



    @Override
    protected void parseExistence() {
        parseExistence(name, modifiers, superClassWrapper, interfaceWrappers);
    }


    public String expectedToString()
    {
        String interfaceString =
                interfaceWrappers.expected != null && interfaceWrappers.expected.length > 0 ? " implements " +
                        Arrays.stream(interfaceWrappers.expected).
                                map(ClassWrapper::getClazz).
                                map(Class::getSimpleName).
                                collect(Collectors.joining(", ")) : "";

        String superName = "";
        try {
            superName = superClassWrapper.expected != null ? " extends " + superClassWrapper.expected.getExpectedName() : "";
        }
        catch (AssertionError ignored) {}

        return String.format(
                "%s class %s%s%s",
                modifiers.expected,
                name.expected,
                superName,
                interfaceString
        );
    }
    public String actualToString() {
        if(existence==MISSING) {
            return "<missing>";
        }
        String interfaceString =
                interfaceWrappers.actual != null && interfaceWrappers.actual.length > 0 ? " implements " +
                Arrays.stream(interfaceWrappers.actual).
                        map(ClassWrapper::getClazz).
                        map(Class::getSimpleName).
                        collect(Collectors.joining(", ")) : "";

        String superName = "";
        try {
            superName = superClassWrapper.expected != null ? " extends " + superClassWrapper.expected.getExpectedName() : "";
        }
        catch (AssertionError ignored) {}

        return String.format(
                "%s class %s%s%s",
                modifiers.actual,
                name.actual,
                superName,
                interfaceString
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if(other == null) {
            return false;
        }
        if(other instanceof ClassWrapper<?> o) {
            boolean n = name.expected.equals(o.name.expected);
            boolean p = expectedPackage.equals(o.expectedPackage);
            return n && p;
        }
        else {
            return false;
        }
    }

    @Override
    public String toString()
    {
        parseExistence();
        String intro = switch (existence) {
            case EXACT -> expectedToString();
            case DEVIATES -> Messages.CLASS_DEVIATION;
            case MISSING -> Messages.CLASS_MISSING;
            default -> Messages.CLASS_UNCHECKED;
        } + " in package %s".formatted(expectedPackage);
        return String.format("""
                %s
                Expect:\t%s
                Actual:\t%s
                """, intro, expectedToString(), actualToString());
    }

    /**
     * Tests that a getter method returns the correct value for an attribute.
     * Verifies that calling the getter on an instance returns the same value as the attribute.
     *
     * @param attribute the attribute wrapper to test
     * @param getter the getter method wrapper to test
     */
    @SuppressWarnings("unused")
    public void testGetter(AttributeWrapper<?,?> attribute, MethodWrapper<?,?> getter)  {
        attribute.verifyExistence(true);
        getter.verifyExistence(true);


        boolean useByteBuddy = !attribute.modifiers.actual.contains("private")
                && !getter.modifiers.actual.contains("private");

        Object obj = getObj(useByteBuddy);
        Object expected = attribute.getValue(obj);
        Object actual = getter.invokeOnSpecificObject(obj);

        assertThat(expected).withFailMessage(
                Messages.GETTER_WRONG_VALUE,
            getter.actualToString(),
            expected.toString(),
            actual != null ? actual.toString() : "null"
        ).isEqualTo(actual);
    }
}
