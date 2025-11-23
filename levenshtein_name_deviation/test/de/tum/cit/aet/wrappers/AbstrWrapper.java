package de.tum.cit.aet.wrappers;

import de.tum.cit.aet.TestSettings;
import de.tum.cit.aet.levenshtein.*;
import static de.tum.cit.aet.Constants.*;

/**
 * Wrapper for the AbstractVehicle abstract class.
 * Simplified version matching the new example structure.
 */
public class AbstrWrapper<T> extends ClassWrapper<T> {
    
    private final AttributeWrapper<T, String> manufacturer;
    private final AttributeWrapper<T, ?> year;

    private final ConstructorWrapper<T> constructor;
    
    private final MethodWrapper<T, String> getManufacturerMethod;
    private final MethodWrapper<T, ?> getYearMethod;
    private final MethodWrapper<T, ?> calculateCostMethod;
    private final MethodWrapper<T, String> getInfoMethod;

    // Getters for attributes
    public AttributeWrapper<T, String> manufacturer() {
        return manufacturer;
    }

    public AttributeWrapper<T, ?> year() {
        return year;
    }

    // Getter for constructor
    public ConstructorWrapper<T> constructor() {
        return constructor;
    }

    // Getters for methods
    public MethodWrapper<T, String> getManufacturerMethod() {
        return getManufacturerMethod;
    }

    public MethodWrapper<T, ?> getYearMethod() {
        return getYearMethod;
    }

    public MethodWrapper<T, ?> calculateCostMethod() {
        return calculateCostMethod;
    }

    public MethodWrapper<T, String> getInfoMethod() {
        return getInfoMethod;
    }

    public AbstrWrapper() {
        super(abstractClass(),
              TestSettings.BASE_PACKAGE,
              null,
              null,
              "public", "abstract");

        // Initialize attributes
        manufacturer = new AttributeWrapper<>(this,
                                               abstractClassAttribute(),
                                               String.class,
                                               "protected");

        year = new AttributeWrapper<>(this,
                                       "year",
                                       yearType(),
                                       "protected");

        // Initialize constructor
        constructor = new ConstructorWrapper<>(this,
                                                new Class<?>[]{String.class, yearType()},
                                                "public");

        // Initialize methods
        getManufacturerMethod = new MethodWrapper<>(this,
                                                     "getManufacturer",
                                                     String.class,
                                                     new Class<?>[]{},
                                                     "public");

        getYearMethod = new MethodWrapper<>(this,
                                             "getYear",
                                             yearType(),
                                             new Class<?>[]{},
                                             "public");

        calculateCostMethod = new MethodWrapper<>(this,
                                                   overwrittenMethod(),
                                                   calcReturnType(),
                                                   new Class<?>[]{},
                                                   "public", "abstract");

        getInfoMethod = new MethodWrapper<>(this,
                                             "getInfo",
                                             String.class,
                                             new Class<?>[]{},
                                             "public");
    }

    @Override
    public Object getObj() {
        if (obj == null) {
            obj = constructor.invoke("BMW", 2023);
        }
        return obj;
    }
}

