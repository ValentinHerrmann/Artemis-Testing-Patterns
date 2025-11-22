package de.tum.cit.aet.levenshtein;



public class WrapperProperty<T> {
    public enum Existence  {
        UNCHECKED, // 0
        EXACT, // 1
        DEVIATES, // 2
        MISSING // 3
    }


    T expected;
    T actual;
    Existence existence;

    public WrapperProperty(T expected) {
        this.expected = expected;
        this.actual = null;
        this.existence = Existence.UNCHECKED;
    }

    public String toString() {
        return String.format("Expected: %s, Actual: %s, Existence: %s", expected, actual, existence);
    }
}
