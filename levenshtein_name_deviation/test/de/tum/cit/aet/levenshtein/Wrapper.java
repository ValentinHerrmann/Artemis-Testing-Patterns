package levenshtein;

import static levenshtein.WrapperProperty.Existence;
import static levenshtein.WrapperProperty.Existence.*;
import static levenshtein.Utils.*;

import org.assertj.core.api.Assertions;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import test.Messages;

 /**
 * Abstract base class for wrapping and verifying Java reflection elements (classes, methods, fields, constructors).
 * Provides name and modifier deviation detection using Levenshtein distance or similar fuzzy matching.
 * Tracks whether an element exists exactly, deviates (can be used even though not 100% matches the signature),
  * or is missing.
 *
 * @param <T> the type of the class the element (e.g. method) belongs to.
 */
public abstract class Wrapper<T>
{
    /**
     * The expected and actual name of the wrapped element.
     */
    protected WrapperProperty<String> name;

    /**
     * The expected and actual modifiers (e.g., "public static final") of the wrapped element.
     */
    protected WrapperProperty<String> modifiers;

    /**
     * The overall existence (considering all parts state of the wrapped element (EXACT, DEVIATES, MISSING, or UNCHECKED).
     * DEVIATES means that the element was found but has some deviations from the expected signature, but can still be
     * used for further testing. UNCHECKED means that existence has not yet been verified and is a state which should
     * not be shown to users.
     */
    protected Existence existence;

    /**
     * Reference to the wrapper of the class that contains this element.
     */
    private final ClassWrapper<T> parentClassWrapper;

    
    /**
     * Constructs a new Wrapper for a reflection element with expected name and modifiers.
     *
     * @param parentClass the class wrapper containing this element
     * @param expectedName the expected name of the element
     * @param expectedModifiers the expected modifiers (e.g., "public", "static", "final")
     */
    public Wrapper(ClassWrapper<T> parentClass, String expectedName, String... expectedModifiers) {
        name = new WrapperProperty<>(expectedName);
        this.parentClassWrapper = parentClass;
        this.modifiers = new WrapperProperty<>(String.join(" ",expectedModifiers));
        this.existence = UNCHECKED;
    }


    /**
     * Verifies that the wrapped element exists in the actual class.
     * Subclasses must implement this to perform element-specific existence checks.
     *
     * @param throwAssertion if true, throws an AssertJ assertion failure when the element is missing. Usually set to true
     *                       for behavioral tests, and false for structural tests to allow further deviation analysis.
     */
    public abstract void verifyExistence(boolean throwAssertion);

    /**
     * Verifies existence with a custom failure message.
     * Calls {@link #findWithDeviation()} if the existence state is UNCHECKED,
     * then throws an assertion if the element is MISSING and throwAssertion is true.
     *
     * @param failMessage the custom failure message to display if the element is missing
     * @param throwAssertion if true, throws an AssertJ assertion failure when the element is missing. Usually set to true
     *                       for behavioral tests, and false for structural tests to allow further deviation analysis.
     */
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

    /**
     * Retrieves the overall existence state of this wrapped element.
     * Triggers deviation detection if not yet checked, then parses the existence state.
     * Worst part-wise existence determines the overall existence.
     *
     * @return the existence state (EXACT, DEVIATES, MISSING, or UNCHECKED)
     */
    public Existence getOverallExistence() {
        findWithDeviation();
        parseExistence();
        return existence;
    }

    /**
     * Searches for the wrapped element using deviation detection (e.g., Levenshtein distance).
     * Subclasses must implement this to perform element-specific searches and update existence state.
     */
    protected abstract void findWithDeviation();


    /**
     * Verifies that the actual modifiers match the expected modifiers.
     * Compares the expected modifiers (e.g., "public", "static", "final") with the actual modifier bitmask.
     * Updates the modifiers property with EXACT if all match, DEVIATES for non-critical mismatches,
     * or MISSING for critical mismatches (e.g., missing "static").
     *
     * @param modifierBitmask the actual modifiers as a bitmask (from {@link java.lang.reflect.Member#getModifiers()})
     */
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


    /**
     * Retrieves the parent class wrapper containing this element.
     * If this wrapper is itself a ClassWrapper, returns itself; otherwise returns the parent.
     *
     * @return the parent ClassWrapper
     */
    public ClassWrapper<T> getParentClassWrapper() {
        return this instanceof ClassWrapper<T> t ? t : parentClassWrapper;
    }

    /**
     * <p>Returns a string representation of this wrapped element showing its existence state.
     * The returned string is formatted to be shown to users, indicating whether the element matches exactly,
     * deviates, is missing, or is unchecked. Reuses {@link #expectedToString()} and {@link #actualToString()}
     * for representing the two states.</p>
     * <p> For {@link Existence#EXACT} matches, shows expected (= actual) values.</p>
     * <p> For {@link Existence#DEVIATES}, shows both expected and actual values.</p>
     * <p> For {@link Existence#MISSING}, shows what was expected.</p>
     * <p> For {@link Existence#UNCHECKED}, shows a debug message (should not be shown to users).</p>
     *
     * @return a formatted string describing the element's state
     */
    @Override
    public String toString()
    {
        parseExistence();
        return switch (existence) {
            case EXACT -> expectedToString();
            case DEVIATES -> String.format(
                    Messages.WRAPPER_DEVIATION,
                    getParentClassWrapper().name.expected, expectedToString(), actualToString()
            );
            case MISSING -> String.format(
                    Messages.WRAPPER_MISSING,
                    getParentClassWrapper().name.expected, expectedToString()
            );
            case UNCHECKED -> String.format(
                    Messages.WRAPPER_UNCHECKED,
                    getParentClassWrapper().name.expected, expectedToString()
            );
        };
    }

    /**
     * Returns a string representation of the expected element signature.
     * Subclasses must implement this to provide element-specific formatting.
     *
     * @return a string showing the expected element details
     */
    public abstract String expectedToString();

    /**
     * Returns a string representation of the actual element signature found.
     * Subclasses must implement this to provide element-specific formatting.
     *
     * @return a string showing the actual element details
     */
    public abstract String actualToString();

    /**
     * Parses and updates the existence state based on all properties.
     * Subclasses must implement this to define how to aggregate property states.
     */
    protected abstract void parseExistence();

    /**
     * Helper method to parse existence state from multiple wrapper properties.
     * Selects the worst existence state among all provided properties.
     *
     * @param properties the wrapper properties to check
     */
    protected void parseExistence(WrapperProperty<?> ... properties) {
        int a = Arrays.stream(properties).mapToInt(x->x.existence.ordinal()).max().orElse(MISSING.ordinal());
        existence = Existence.values()[a];
    }

    /**
     * Retrieves the expected name of this wrapped element instead of
     * exposing the {@link #name} property directly.
     *
     * @return the expected name as a string
     */
    public String getExpectedName() {
        return name.expected;
    }


}
