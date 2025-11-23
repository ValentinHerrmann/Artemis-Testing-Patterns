package de.tum.cit.aet.wrappers;

import de.tum.cit.aet.TestSettings;
import de.tum.cit.aet.levenshtein.*;
import static de.tum.cit.aet.Constants.*;

/**
 * Wrapper for the AbstractVehicle abstract class.
 * Defines the expected structure, attributes, constructor, and methods.
 */
public class AbstrWrapper<T> extends ClassWrapper<T> {
    
    private final AttributeWrapper<T, String> manufacturer;
    private final AttributeWrapper<T, String> model;
    private final AttributeWrapper<T, ?> yearOfManufacture;
    private final AttributeWrapper<T, ?> currentSpeed;
    private final AttributeWrapper<T, Boolean> engineRunning;
    
    private final ConstructorWrapper<T> constructor;
    
    private final MethodWrapper<T, String> getManufacturerMethod;
    private final MethodWrapper<T, String> getModelMethod;
    private final MethodWrapper<T, ?> getYearOfManufactureMethod;
    private final MethodWrapper<T, Boolean> isEngineRunningMethod;
    private final MethodWrapper<T, String> getVehicleInfoMethod;
    private final MethodWrapper<T, ?> calculateFuelConsumptionMethod;
    private final MethodWrapper<T, ?> getMaxSpeedMethod;

    // Getters for attributes
    public AttributeWrapper<T, String> manufacturer() {
        return manufacturer;
    }

    public AttributeWrapper<T, String> model() {
        return model;
    }

    public AttributeWrapper<T, ?> yearOfManufacture() {
        return yearOfManufacture;
    }

    public AttributeWrapper<T, ?> currentSpeed() {
        return currentSpeed;
    }

    public AttributeWrapper<T, Boolean> engineRunning() {
        return engineRunning;
    }

    // Getters for constructor
    public ConstructorWrapper<T> constructor() {
        return constructor;
    }

    // Getters for methods
    public MethodWrapper<T, String> getManufacturerMethod() {
        return getManufacturerMethod;
    }

    public MethodWrapper<T, String> getModelMethod() {
        return getModelMethod;
    }

    public MethodWrapper<T, ?> getYearOfManufactureMethod() {
        return getYearOfManufactureMethod;
    }

    public MethodWrapper<T, Boolean> isEngineRunningMethod() {
        return isEngineRunningMethod;
    }

    public MethodWrapper<T, String> getVehicleInfoMethod() {
        return getVehicleInfoMethod;
    }

    public MethodWrapper<T, ?> calculateFuelConsumptionMethod() {
        return calculateFuelConsumptionMethod;
    }

    public MethodWrapper<T, ?> getMaxSpeedMethod() {
        return getMaxSpeedMethod;
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

        model = new AttributeWrapper<>(this,
                                        "model",
                                        String.class,
                                        "protected");

        yearOfManufacture = new AttributeWrapper<>(this,
                                                     "yearOfManufacture",
                                                     yearType(),
                                                     "protected");

        currentSpeed = new AttributeWrapper<>(this,
                                               "currentSpeed",
                                               speedType(),
                                               "protected");

        engineRunning = new AttributeWrapper<>(this,
                                                "engineRunning",
                                                boolean.class,
                                                "protected");

        // Initialize constructor
        constructor = new ConstructorWrapper<>(this,
                                                new Class<?>[]{String.class, String.class, yearType()},
                                                "public");

        // Initialize methods
        getManufacturerMethod = new MethodWrapper<>(this,
                                                     "getManufacturer",
                                                     String.class,
                                                     new Class<?>[]{},
                                                     "public");

        getModelMethod = new MethodWrapper<>(this,
                                              "getModel",
                                              String.class,
                                              new Class<?>[]{},
                                              "public");

        getYearOfManufactureMethod = new MethodWrapper<>(this,
                                                          "getYearOfManufacture",
                                                          yearType(),
                                                          new Class<?>[]{},
                                                          "public");

        isEngineRunningMethod = new MethodWrapper<>(this,
                                                     "isEngineRunning",
                                                     boolean.class,
                                                     new Class<?>[]{},
                                                     "public");

        getVehicleInfoMethod = new MethodWrapper<>(this,
                                                    "getVehicleInfo",
                                                    String.class,
                                                    new Class<?>[]{},
                                                    "public");

        calculateFuelConsumptionMethod = new MethodWrapper<>(this,
                                                               overwrittenMethod(),
                                                               calcReturnType(),
                                                               new Class<?>[]{},
                                                               "public", "abstract");

        getMaxSpeedMethod = new MethodWrapper<>(this,
                                                 "getMaxSpeed",
                                                 speedType(),
                                                 new Class<?>[]{},
                                                 "public", "abstract");
    }

    @Override
    public Object getObj() {
        if (obj == null) {
            obj = constructor.invoke("BMW", "M3", 2023);
        }
        return obj;
    }
}

