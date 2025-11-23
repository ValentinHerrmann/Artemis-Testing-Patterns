package de.tum.cit.aet.wrappers;

import de.tum.cit.aet.levenshtein.*;
import static de.tum.cit.aet.Constants.*;
import static de.tum.cit.aet.TestSettings.BASE_PACKAGE;

import org.assertj.core.api.Assertions;

/**
 * Wrapper for the Car concrete class.
 * Simplified version matching the new example structure.
 */
public class CarWrapper<T> extends ClassWrapper<T> {
    
    // Car-specific attributes
    private final AttributeWrapper<T, ?> price;
    private final AttributeWrapper<T, ?> speed;

    // Constructors (demonstrates constructor overloading)
    private final ConstructorWrapper<T> constructor_full;
    private final ConstructorWrapper<T> constructor_default;

    // Car-specific methods
    private final MethodWrapper<T, ?> getPriceMethod;

    // Interface methods (from Driveable)
    private final MethodWrapper<T, Void> startMethod;
    private final MethodWrapper<T, ?> getSpeedMethod;

    // Overridden methods
    private final MethodWrapper<T, ?> calculateCostMethod;
    private final MethodWrapper<T, ?> calculateCostYearsMethod; // Method overloading
    private final MethodWrapper<T, String> getInfoMethod;

    // Getters for attributes
    public AttributeWrapper<T, ?> price() {
        return price;
    }

    public AttributeWrapper<T, ?> speed() {
        return speed;
    }

    // Getters for constructors
    public ConstructorWrapper<T> constructor_full() {
        return constructor_full;
    }

    public ConstructorWrapper<T> constructor_default() {
        return constructor_default;
    }

    // Getters for Car-specific methods
    public MethodWrapper<T, ?> getPriceMethod() {
        return getPriceMethod;
    }

    // Getters for interface methods
    public MethodWrapper<T, Void> startMethod() {
        return startMethod;
    }

    public MethodWrapper<T, ?> getSpeedMethod() {
        return getSpeedMethod;
    }

    // Getters for overridden methods
    public MethodWrapper<T, ?> calculateCostMethod() {
        return calculateCostMethod;
    }

    public MethodWrapper<T, ?> calculateCostYearsMethod() {
        return calculateCostYearsMethod;
    }

    public MethodWrapper<T, String> getInfoMethod() {
        return getInfoMethod;
    }

    // Getters for overridden abstract methods

    public CarWrapper(ClassWrapper<?> superClassWrapper, ClassWrapper<?>... interfaceWrappers) {
        super(concreteClass(),
              BASE_PACKAGE,
              superClassWrapper,
              interfaceWrappers,
              "public");

        // Initialize attributes
        price = new AttributeWrapper<>(this,
                                        "price",
                                        double.class,
                                        "private");

        speed = new AttributeWrapper<>(this,
                                        "speed",
                                        speedType(),
                                        "private");

        // Initialize constructors (constructor overloading)
        constructor_full = new ConstructorWrapper<>(this,
                                                     new Class<?>[]{
                                                         String.class,
                                                         yearType(),
                                                         double.class
                                                     },
                                                     "public");

        constructor_default = new ConstructorWrapper<>(this,
                                                        new Class<?>[]{
                                                            String.class,
                                                            yearType()
                                                        },
                                                        "public");

        // Initialize Car-specific methods
        getPriceMethod = new MethodWrapper<>(this,
                                              "getPrice",
                                              double.class,
                                              new Class<?>[]{},
                                              "public");

        // Initialize interface methods
        startMethod = new MethodWrapper<>(this,
                                           "start",
                                           void.class,
                                           new Class<?>[]{},
                                           "public");

        getSpeedMethod = new MethodWrapper<>(this,
                                              "getSpeed",
                                              speedType(),
                                              new Class<?>[]{},
                                              "public");

        // Initialize overridden methods
        calculateCostMethod = new MethodWrapper<>(this,
                                                   overwrittenMethod(),
                                                   calcReturnType(),
                                                   new Class<?>[]{},
                                                   "public");

        // Method overloading - same name, different parameters
        calculateCostYearsMethod = new MethodWrapper<>(this,
                                                        overwrittenMethod(),
                                                        calcReturnType(),
                                                        new Class<?>[]{int.class},
                                                        "public");

        getInfoMethod = new MethodWrapper<>(this,
                                             "getInfo",
                                             String.class,
                                             new Class<?>[]{},
                                             "public");
    }

    @Override
    public Object getObj() {
        if (obj == null) {
            Assertions.assertThatCode(() -> {
                obj = constructor_full.invoke("BMW", 2023, 30000.0);
            }).withFailMessage("Creating instances of class %s failed. Constructor may not be implemented correctly.",
                              concreteClass()).doesNotThrowAnyException();
        }
        return obj;
    }
}

