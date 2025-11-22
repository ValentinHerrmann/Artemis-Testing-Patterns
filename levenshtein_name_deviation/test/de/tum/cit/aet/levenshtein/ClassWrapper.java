package de.tum.cit.aet.levenshtein;

import de.tum.cit.aet.TestSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.platform.commons.util.ReflectionUtils;

import static de.tum.cit.aet.levenshtein.WrapperProperty.Existence.*;
import static de.tum.cit.aet.levenshtein.LevenshteinUtils.levenshteinDistance;


public abstract class ClassWrapper<T> extends Wrapper<T>
{
    private final String expectedPackage;
    private Class<T> clazz;
    protected T obj;

    //WrapperProperty<Class<?>> superClass;
    //WrapperProperty<Class<?>[]> interfaces;
    WrapperProperty<ClassWrapper<?>> superClassWrapper;
    WrapperProperty<ClassWrapper<?>[]> interfaceWrappers;



    public ClassWrapper(String expectedName, String expectedPackage, ClassWrapper<?> superClassWrapper, ClassWrapper<?>[] interfaceWrappers, String... modifiers) {
        super(null, expectedName, modifiers);
        this.expectedPackage = expectedPackage;
        this.superClassWrapper = new WrapperProperty<>(superClassWrapper);
        this.interfaceWrappers = new WrapperProperty<>(interfaceWrappers == null ? new ClassWrapper<?>[0] : interfaceWrappers);
    }

    @Override
    public void verifyExistence(boolean throwAssertion)
    {
        super.verifyExistence(String.format("Class %s in package %s is not implemented as expected.", name.expected, expectedPackage),throwAssertion);
    }

    public Class<T> getClazz()
    {
        verifyExistence(false);
        return clazz;
    }

    public abstract Object getObj();

    /**
     * Attempts to find a class in the package for class validation tests,
     * using TestSettings.CLASS_NAME_DEVIATION_THRESHOLD.
     */
    @Override @SuppressWarnings("unchecked")
    protected void findWithDeviation()
    {
        // First try exact match
        try {
            clazz = (Class<T>)Class.forName(expectedPackage + "." + name.expected);
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
        if (
                interfaceWrappers.actual.length > interfaceWrappers.expected.length) {
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
            case EXACT -> "✅ ";
            case DEVIATES -> "⚠️ DEVIATION ⚠️ \nIf possible actual will be used for further testing";
            case MISSING -> "❌ MISSING ❌";
            default -> "Existence unchecked.";
        } + " in package %s".formatted(expectedPackage);
        return String.format("%s\nExpect:\t%s\nActual:\t%s", intro, expectedToString(), actualToString());
    }
}
