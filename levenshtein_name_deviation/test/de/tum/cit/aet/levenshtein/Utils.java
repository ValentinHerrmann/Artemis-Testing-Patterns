package levenshtein;

import test.Messages;
import static levenshtein.WrapperProperty.Existence.*;

public class Utils
{
    /**
     * Calculates the Levenshtein distance between two strings.
     * @param s1 First string
     * @param s2 Second string
     * @return The edit distance between the two strings
     */
    public static int levenshteinDistance(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return -1;
        }

        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }

    public static int levenshteinDistancePercent(String expected, String actual) {
        if (expected == null || actual == null) {
            return -1;
        }
        int distance = levenshteinDistance(expected, actual);
        int maxLength = Math.max(expected.length(), actual.length());
        return (distance*100)/maxLength;
    }

    /**
     * Checks if names are within a specified deviation threshold.
     * @param expectedName The expected name
     * @param actualName The actual name
     * @param threshold The deviation threshold
     * @return true if the deviation is within the threshold, false otherwise
     */
    public static boolean isNameWithinDeviation(String expectedName, String actualName, int threshold) {
        if (expectedName.equals(actualName)) {
            return true;
        }

        int distance = levenshteinDistance(expectedName, actualName);
        int maxLength = Math.max(expectedName.length(), actualName.length());
        double deviation = (double) (distance * 100) / maxLength;

        return deviation <= threshold;
    }

    public static Object saveCast(Object val, Class<?> castTo) {
        return saveCast(val, castTo, false);
    }

    public static Object saveCast(Object val, Class<?> castTo, boolean allowNull) {
        if(!allowNull && val == null) {
            throw new IllegalArgumentException(Messages.NULL_NOT_ALLOWED);
        }
        if(val == null) {
            return null;
        }
        if(castTo.isInstance(val)) {
            return val;
        }
        if(val instanceof Number n) {
            if(castTo == Integer.class || castTo == int.class) {
                return n.intValue();
            }
            else if(castTo == Long.class || castTo == long.class) {
                return n.longValue();
            }
            else if(castTo == Float.class || castTo == float.class) {
                return n.floatValue();
            }
            else if(castTo == Double.class || castTo == double.class) {
                return n.doubleValue();
            }
        }
        //Logging.logWarning(String.format("Cannot cast value %s of type %s to type %s", val, val.getClass().getName(), castTo.getName()));
        return val;
    }


    /**
     * Selects the worse existence state between two states based on ordinal comparison.
     * Used to aggregate multiple property existence states into a single worst-case state.
     *
     * @param worst the current worst existence state
     * @param updated the new existence state to compare
     * @return the worse of the two existence states
     */
    public static WrapperProperty.Existence selectWorstExistence(WrapperProperty.Existence worst, WrapperProperty.Existence updated) {
        return worst.ordinal() > updated.ordinal() ? worst : updated;
    }



    /**
     * Verifies that the actual type matches or is compatible with the expected type.
     * Checks for exact match, assignability, or numeric type containment (e.g., long can contain int).
     * Updates the type property existence state accordingly.
     * TODO: Requires further enhancement to handle more datatypes
     *
     * @param typeWrapperProperty the wrapper property containing the expected type
     * @param actualType the actual type found via reflection
     */
    public static void verifyType(WrapperProperty<Class<?>> typeWrapperProperty, Class<?> actualType) {
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


    /**
     * Checks if the actual primitive type can contain the expected primitive type.
     * For example, a long can contain an int, and a double can contain a float.
     *
     * @param actualType the actual type to check
     * @param expectedType the expected type to verify containment for
     * @return true if actualType can contain values of expectedType, false otherwise
     */
    public static boolean canContain(Class<?> actualType, Class<?> expectedType) {
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

    /**
     * Unwraps a primitive wrapper class to its corresponding primitive type.
     * For example, Integer.class is unwrapped to int.class.
     *
     * @param type the type to unwrap
     * @return the primitive type if type is a wrapper, otherwise the original type
     */
    public static Class<?> unwrapPrimitive(Class<?> type) {
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

    /**
     * Converts a primitive type to its corresponding wrapper type.
     * For example, int.class is converted to Integer.class.
     *
     * @param type the type to convert
     * @return the wrapper type if type is primitive, otherwise the original type
     */
    public static Class<?> toWrapperType(Class<?> type) {
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
}
