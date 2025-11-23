package de.tum.cit.aet.wrappers;

import de.tum.cit.aet.levenshtein.*;
import static de.tum.cit.aet.Constants.*;
import static de.tum.cit.aet.TestSettings.BASE_PACKAGE;

import org.assertj.core.api.Assertions;

/**
 * Wrapper for the Car concrete class.
 * Defines the expected structure including inheritance from AbstractVehicle
 * and implementation of Driveable interface.
 */
public class CarWrapper<T> extends ClassWrapper<T> {
    
    // Car-specific attributes
    private final AttributeWrapper<T, ?> numberOfDoors;
    private final AttributeWrapper<T, String> fuelType;
    private final AttributeWrapper<T, ?> engineCapacity;
    private final AttributeWrapper<T, ?> maxSpeed;
    private final AttributeWrapper<T, ?> fuelConsumptionRate;

    // Constructor
    private final ConstructorWrapper<T> constructor_full;

    // Car-specific methods
    private final MethodWrapper<T, ?> getNumberOfDoors;
    private final MethodWrapper<T, String> getFuelType;
    private final MethodWrapper<T, ?> getEngineCapacity;
    private final MethodWrapper<T, Boolean> isElectric;

    // Interface methods (from Driveable)
    private final MethodWrapper<T, Boolean> startEngine;
    private final MethodWrapper<T, Void> accelerate;
    private final MethodWrapper<T, Void> brake;
    private final MethodWrapper<T, ?> getCurrentSpeed;

    // Overridden abstract methods (from AbstractVehicle)
    private final MethodWrapper<T, ?> calculateFuelConsumption;
    private final MethodWrapper<T, ?> getMaxSpeed;
    private final MethodWrapper<T, String> getVehicleInfo;

    // Getters for attributes
    public AttributeWrapper<T, ?> numberOfDoors() {
        return numberOfDoors;
    }

    public AttributeWrapper<T, String> fuelType() {
        return fuelType;
    }

    public AttributeWrapper<T, ?> engineCapacity() {
        return engineCapacity;
    }

    public AttributeWrapper<T, ?> maxSpeed() {
        return maxSpeed;
    }

    public AttributeWrapper<T, ?> fuelConsumptionRate() {
        return fuelConsumptionRate;
    }

    // Getter for constructor
    public ConstructorWrapper<T> constructor_full() {
        return constructor_full;
    }

    // Getters for Car-specific methods
    public MethodWrapper<T, ?> getNumberOfDoors() {
        return getNumberOfDoors;
    }

    public MethodWrapper<T, String> getFuelType() {
        return getFuelType;
    }

    public MethodWrapper<T, ?> getEngineCapacity() {
        return getEngineCapacity;
    }

    public MethodWrapper<T, Boolean> isElectric() {
        return isElectric;
    }

    // Getters for interface methods
    public MethodWrapper<T, Boolean> startEngine() {
        return startEngine;
    }

    public MethodWrapper<T, Void> accelerate() {
        return accelerate;
    }

    public MethodWrapper<T, Void> brake() {
        return brake;
    }

    public MethodWrapper<T, ?> getCurrentSpeed() {
        return getCurrentSpeed;
    }

    // Getters for overridden abstract methods
    public MethodWrapper<T, ?> calculateFuelConsumption() {
        return calculateFuelConsumption;
    }

    public MethodWrapper<T, ?> getMaxSpeed() {
        return getMaxSpeed;
    }

    public MethodWrapper<T, String> getVehicleInfo() {
        return getVehicleInfo;
    }

    public CarWrapper(ClassWrapper<?> superClassWrapper, ClassWrapper<?>... interfaceWrappers) {
        super(concreteClass(),
              BASE_PACKAGE,
              superClassWrapper,
              interfaceWrappers,
              "public");

        // Initialize Car-specific attributes
        numberOfDoors = new AttributeWrapper<>(this,
                                                concreteClassAttribute(),
                                                doorType(),
                                                "private");

        fuelType = new AttributeWrapper<>(this,
                                           "fuelType",
                                           String.class,
                                           "private");

        engineCapacity = new AttributeWrapper<>(this,
                                                 "engineCapacity",
                                                 engineCapacityType(),
                                                 "private");

        maxSpeed = new AttributeWrapper<>(this,
                                           "maxSpeed",
                                           speedType(),
                                           "private");

        fuelConsumptionRate = new AttributeWrapper<>(this,
                                                       "fuelConsumptionRate",
                                                       speedType(),
                                                       "private");

        // Initialize constructor
        constructor_full = new ConstructorWrapper<>(this,
                                                     new Class<?>[]{
                                                         String.class,
                                                         String.class,
                                                         yearType(),
                                                         doorType(),
                                                         String.class,
                                                         engineCapacityType()
                                                     },
                                                     "public");

        // Initialize Car-specific methods
        getNumberOfDoors =  new MethodWrapper<>(this,
                                                       "getNumberOfDoors",
                                                       doorType(),
                                                       new Class<?>[]{},
                                                       "public");

        getFuelType =  new MethodWrapper<>(this,
                                                 "getFuelType",
                                                 String.class,
                                                 new Class<?>[]{},
                                                 "public");

        getEngineCapacity =  new MethodWrapper<>(this,
                                                        "getEngineCapacity",
                                                        engineCapacityType(),
                                                        new Class<?>[]{},
                                                        "public");

        isElectric =  new MethodWrapper<>(this,
                                                "isElectric",
                                                boolean.class,
                                                new Class<?>[]{},
                                                "public");

        // Initialize interface methods
        startEngine =  new MethodWrapper<>(this,
                                                 "startEngine",
                                                 boolean.class,
                                                 new Class<?>[]{},
                                                 "public");

        accelerate =  new MethodWrapper<>(this,
                                                "accelerate",
                                                void.class,
                                                new Class<?>[]{double.class},
                                                "public");

        brake =  new MethodWrapper<>(this,
                                           "brake",
                                           void.class,
                                           new Class<?>[]{double.class},
                                           "public");

        getCurrentSpeed =  new MethodWrapper<>(this,
                                                      "getCurrentSpeed",
                                                      speedType(),
                                                      new Class<?>[]{},
                                                      "public");

        // Initialize overridden abstract methods
        calculateFuelConsumption =  new MethodWrapper<>(this,
                                                               "calculateFuelConsumption",
                                                               calcReturnType(),
                                                               new Class<?>[]{},
                                                               "public");

        getMaxSpeed =  new MethodWrapper<>(this,
                                                 "getMaxSpeed",
                                                 speedType(),
                                                 new Class<?>[]{},
                                                 "public");

        getVehicleInfo =  new MethodWrapper<>(this,
                                                    "getVehicleInfo",
                                                    String.class,
                                                    new Class<?>[]{},
                                                    "public");
    }

    @Override
    public Object getObj() {
        if (obj == null) {
            Assertions.assertThatCode(() -> {
                obj = constructor_full.invoke("BMW", "M3", 2023, 4, "Petrol", 3.0);
            }).withFailMessage("Creating instances of class %s failed. Constructor may not be implemented correctly.",
                              concreteClass()).doesNotThrowAnyException();
        }
        return obj;
    }
}

