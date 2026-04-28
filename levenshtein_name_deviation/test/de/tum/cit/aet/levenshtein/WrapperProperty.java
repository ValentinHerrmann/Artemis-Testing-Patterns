package levenshtein;


/**
 * A wrapper for properties of reflection elements that tracks expected vs. actual values
 * and their existence state (exact match, deviation, missing, or unchecked).
 * DEVIATES indicates a partial match or acceptable deviation which allows behavioral testing
 * using this component. For names the acceptable deviation is set via TestSettings; for types
 * it is based on primitive-wrapper equivalence.
 *
 * @param <T> the type of the property being wrapped
 */
public class WrapperProperty<T> {
    /**
     * Enumeration representing the existence state of a property.
     */
    public enum Existence  {
        /** Property has not yet been checked */
        UNCHECKED, // 0
        /** Property matches exactly with expected value */
        EXACT, // 1
        /** Property exists but deviates from expected value */
        DEVIATES, // 2
        /** Property is missing or does not match at all */
        MISSING // 3
    }

    /**
     * The expected value of this property.
     */
    T expected;

    /**
     * The actual value found for this property.
     */
    T actual;

    /**
     * The existence state of this property.
     */
    Existence existence;

    /**
     * Constructs a new WrapperProperty with an expected value.
     * The actual value is initialized to null and existence is set to UNCHECKED.
     *
     * @param expected the expected value of this property
     */
    public WrapperProperty(T expected) {
        this.expected = expected;
        this.actual = null;
        this.existence = Existence.UNCHECKED;
    }

    /**
     * Returns a string representation showing expected, actual, and existence state.
     *
     * @return a formatted string with property details
     */
    public String toString() {
        return String.format("Expected: %s, Actual: %s, Existence: %s", expected, actual, existence);
    }
}
