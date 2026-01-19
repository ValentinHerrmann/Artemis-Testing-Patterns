package de.tum.cit.aet;

/**
 * Central configuration class for the Levenshtein Name Deviation Testing Pattern.
 *
 * <h2>Tutorial: Understanding Test Configuration</h2>
 *
 * <p>This class defines all configuration parameters that control how the testing framework behaves.
 * Understanding these settings is crucial for using the pattern effectively.</p>
 *
 * <h3>1. Package Configuration</h3>
 * <p>The {@link #BASE_PACKAGE} defines where student code is expected to be located.
 * All {@code ClassWrapper} instances use this as the default package for finding classes.</p>
 *
 * <pre>{@code
 * // Example: When looking for class "Car", the framework searches:
 * // de.tum.cit.aet.Car
 * }</pre>
 *
 * <h3>2. Debugging Mode</h3>
 * <p>The {@link #DEBUGGING} flag controls test timeouts. Set to {@code true} during development
 * to prevent timeouts while debugging. Set to {@code false} for production/exam mode.</p>
 *
 * <pre>{@code
 * // During development:
 * DEBUGGING = true;   // 300 second timeout - plenty of time to debug
 *
 * // For exams/production:
 * DEBUGGING = false;  // 3 second timeout - prevents infinite loops
 * }</pre>
 *
 * <h3>3. Levenshtein Distance Thresholds</h3>
 * <p>These thresholds control how tolerant the framework is to naming deviations.
 * Values are percentages (0-100) of the maximum string length.</p>
 *
 * <h4>How Thresholds Work:</h4>
 * <pre>{@code
 * // Example with CLASS_NAME_DEVIATION_THRESHOLD = 10:
 *
 * Expected: "Car" (3 characters)
 * Allowed deviation: 10% of 3 = 0.3 → rounds to 0 characters
 * ✗ "Cra" - 1 char difference - EXCEEDS threshold - MISSING
 *
 * Expected: "AbstractVehicle" (15 characters)
 * Allowed deviation: 10% of 15 = 1.5 → rounds to 1 character
 * ✓ "AbstractVehcle" - 1 char difference - WITHIN threshold - DEVIATES
 * ✗ "AbstactVehicle" - 2 char difference - EXCEEDS threshold - MISSING
 * }</pre>
 *
 * <h4>Choosing Threshold Values:</h4>
 * <ul>
 *   <li><b>0%</b> - No tolerance, exact match required (traditional testing)</li>
 *   <li><b>10%</b> - Very strict, only catches single-character typos in long names</li>
 *   <li><b>20%</b> - Moderate tolerance, good for educational exercises (recommended)</li>
 *   <li><b>30%+</b> - Very lenient, may accept unintended matches</li>
 * </ul>
 *
 * <h4>Different Thresholds for Different Elements:</h4>
 * <p>You can set different tolerance levels for different code elements:</p>
 * <pre>{@code
 * CLASS_NAME_DEVIATION_THRESHOLD = 10;      // Classes: strict (important structure)
 * METHOD_NAME_DEVIATION_THRESHOLD = 20;     // Methods: moderate (common typos)
 * ATTRIBUTE_NAME_DEVIATION_THRESHOLD = 20;  // Attributes: moderate (common typos)
 * }</pre>
 *
 * <h3>Usage Example in Tests:</h3>
 * <pre>{@code
 * // The framework automatically uses these settings:
 *
 * // 1. When searching for classes in BASE_PACKAGE:
 * ClassWrapper<Car> car = new CarWrapper<>("Car", TestSettings.BASE_PACKAGE, "public");
 *
 * // 2. When checking if names match within deviation:
 * boolean matches = isNameWithinDeviation(
 *     "price",              // expected
 *     "pric",               // actual
 *     TestSettings.ATTRIBUTE_NAME_DEVIATION_THRESHOLD  // 20%
 * );
 * // Result: true (1 char diff in 5-char string = 20%)
 *
 * // 3. When applying timeouts to tests:
 * &#64;Test
 * &#64;Timeout(value = TestSettings.TIMEOUT_SECONDS)
 * void myTest() {
 *     // Test runs with configured timeout
 * }
 * }</pre>
 *
 * <h3>Best Practices:</h3>
 * <ol>
 *   <li><b>Development:</b> Set {@code DEBUGGING = true} and higher thresholds (20-30%)</li>
 *   <li><b>Testing:</b> Set {@code DEBUGGING = false} and moderate thresholds (10-20%)</li>
 *   <li><b>Production:</b> Set {@code DEBUGGING = false} and strict thresholds (5-10%)</li>
 *   <li><b>Exams:</b> Consider slightly stricter thresholds to ensure competency</li>
 * </ol>
 *
 * @see de.tum.cit.aet.levenshtein.Utils#isNameWithinDeviation(String, String, int)
 * @see de.tum.cit.aet.levenshtein.ClassWrapper
 * @see Constants
 */
public class TestSettings
{
    /**
     * The variant of the exercise being tested.
     * Used in conjunction with the Exercise Variants pattern.
     */
    static Constants.Variant variant = Constants.Variant.DEFAULT;

    /**
     * The base package where student code is expected to be located.
     * All ClassWrapper instances search for classes in this package by default.
     *
     * <p><b>Example:</b> With {@code BASE_PACKAGE = "de.tum.cit.aet"}, the framework
     * searches for class "Car" at {@code de.tum.cit.aet.Car}.</p>
     */
    public static final String BASE_PACKAGE = "de.tum.cit.aet";

    /**
     * Enable debugging mode for tests. When set to true, tests will
     * have extended timeouts (300 seconds instead of 3 seconds).
     *
     * <p><b>Set to {@code true} during development</b> to allow time for debugging.</p>
     * <p><b>Set to {@code false} for exams/production</b> to prevent slow tests.</p>
     */
    public static final boolean DEBUGGING = true;

    /**
     * Timeout value in seconds for tests.
     * Automatically set based on {@link #DEBUGGING} mode:
     * <ul>
     *   <li>Debug mode: 300 seconds (5 minutes)</li>
     *   <li>Production mode: 3 seconds</li>
     * </ul>
     */
    public static final int TIMEOUT_SECONDS = DEBUGGING ? 300 : 3;

    /**
     * Maximum allowed deviation (as a percentage) for class name matching.
     *
     * <p><b>How it works:</b> Deviation is calculated as:
     * {@code (levenshteinDistance * 100) / maxStringLength}</p>
     *
     * <p><b>Example with threshold = 10:</b></p>
     * <ul>
     *   <li>"Car" vs "Cra" → 1 diff / 3 chars = 33% → MISSING (exceeds 10%)</li>
     *   <li>"AbstractVehicle" vs "AbstractVehcle" → 1 diff / 15 chars = 6.7% → DEVIATES (within 10%)</li>
     * </ul>
     *
     * <p><b>Recommended values:</b></p>
     * <ul>
     *   <li>10% - Very strict, only minor typos in long names</li>
     *   <li>20% - Moderate, good balance for educational use</li>
     *   <li>30% - Lenient, may accept unintended matches</li>
     * </ul>
     */
    public static final int CLASS_NAME_DEVIATION_THRESHOLD = 10;

    /**
     * Maximum allowed deviation (as a percentage) for method name matching.
     * Works the same way as {@link #CLASS_NAME_DEVIATION_THRESHOLD}.
     *
     * <p><b>Example with threshold = 10:</b></p>
     * <ul>
     *   <li>"calculateCost" vs "calculateCost" → 0% → EXACT</li>
     *   <li>"calculateCost" vs "calclateCost" → 7.7% → DEVIATES</li>
     *   <li>"start" vs "stat" → 20% → MISSING</li>
     * </ul>
     */
    public static final int METHOD_NAME_DEVIATION_THRESHOLD = 10;

    /**
     * Maximum allowed deviation (as a percentage) for attribute name matching.
     * Works the same way as {@link #CLASS_NAME_DEVIATION_THRESHOLD}.
     *
     * <p><b>Example with threshold = 10:</b></p>
     * <ul>
     *   <li>"manufacturer" vs "manufacturer" → 0% → EXACT</li>
     *   <li>"manufacturer" vs "manufacurer" → 8.3% → DEVIATES</li>
     *   <li>"price" vs "pric" → 20% → MISSING</li>
     * </ul>
     */
    public static final int ATTRIBUTE_NAME_DEVIATION_THRESHOLD = 10;
    

}



