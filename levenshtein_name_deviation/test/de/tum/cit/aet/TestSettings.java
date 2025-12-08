package de.tum.cit.aet;

import java.lang.management.ManagementFactory;

public class TestSettings
{
    static Constants.Variant variant = Constants.Variant.DEFAULT;
    public static final String BASE_PACKAGE = "de.tum.cit.aet";

    /**
     * Enable debugging mode for tests. When set to true, tests will
     * have extended timeouts.
     */
    public static final boolean DEBUGGING = true;

    /**
     * Timeout value in seconds for tests.
     */
    public static final int TIMEOUT_SECONDS = DEBUGGING ? 300 : 3;

    /**
     * Maximum allowed deviation (as a percentage) for class name matching.
     * A value of 10 means 10% deviation is allowed.
     */
    public static final int CLASS_NAME_DEVIATION_THRESHOLD = 10;
    /**
     * Maximum allowed deviation (as a percentage) for method name matching.
     * A value of 10 means 10% deviation is allowed.
     */
    public static final int METHOD_NAME_DEVIATION_THRESHOLD = 10;

    /**
     * Maximum allowed deviation (as a percentage) for attribute name matching.
     * A value of 10 means 10% deviation is allowed.
     */
    public static final int ATTRIBUTE_NAME_DEVIATION_THRESHOLD = 10;
    

}
