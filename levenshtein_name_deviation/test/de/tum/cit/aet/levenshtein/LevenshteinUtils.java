package de.tum.cit.aet.levenshtein;

public class LevenshteinUtils
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
        double deviation = (distance*100) / maxLength;

        return deviation <= threshold;
    }

    public static Object saveCast(Object val, Class<?> castTo) {
        return saveCast(val, castTo, false);
    }

    public static Object saveCast(Object val, Class<?> castTo, boolean allowNull) {
        if(!allowNull && val == null) {
            throw new IllegalArgumentException("A value was null that is not allowed to be null");
        }
        if(val == null) {
            return null;
        }
        if(castTo.isInstance(val)) {
            return val;
        }
        if(val instanceof Number) {
            Number n = (Number) val;
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
}
