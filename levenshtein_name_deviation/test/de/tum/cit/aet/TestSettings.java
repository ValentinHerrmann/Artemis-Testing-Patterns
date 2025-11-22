package de.tum.cit.aet;

public class TestSettings
{
    static final String SHOW_HIDDEN_AFTER = "2025-12-02 23:59 Europe/Berlin";
    static final String SHOW_HIDDEN_BEFORE = "2025-12-01 23:59 Europe/Berlin";

    static Constants.Variant variant = Constants.Variant.DEFAULT;

    public static final String BASE_PACKAGE = "de.tum.cit.aet";

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
