package de.tum.cit.aet.test;

import de.tum.cit.aet.wrappers.*;
import static de.tum.cit.aet.TestManager.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Tests for the Car implementation class.
 * Simplified to match the new example structure.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestImpl {

    /**
     * Test the full constructor.
     */
    public static void testConstructorFull() {
        Assertions.assertThatCode(() ->
            carImpl().constructor_full().invoke("BMW", 2023, 35000.0))
            .withFailMessage("Constructor of %s is not implemented correctly.",
                           carImpl().getExpectedName())
            .doesNotThrowAnyException();
        
        // Verify inherited attributes
        Object manufacturer = vehicleAbstr().manufacturer().getValue(carImpl().getObj());
        Assertions.assertThat(manufacturer)
            .withFailMessage("Inherited attribute %s must be initialized correctly.",
                           vehicleAbstr().manufacturer().getExpectedName())
            .isEqualTo("BMW");
        
        // Verify Car-specific attributes
        Object price = carImpl().price().getValue(carImpl().getObj());
        Assertions.assertThat((double)price)
            .withFailMessage("Attribute %s must be equal to constructor argument.",
                           carImpl().price().getExpectedName())
            .isEqualTo(35000.0, Assertions.within(0.001));
    }
    
    /**
     * Test the default constructor (constructor overloading).
     */
    public static void testConstructorDefault() {
        Object car = carImpl().constructor_default().invoke("Toyota", 2022);

        Assertions.assertThat(car)
            .withFailMessage("Constructor with default price should create valid object.")
            .isNotNull();

        // Price should be default value (20000.0)
        Object price = carImpl().price().getValue(car);
        Assertions.assertThat((double)price)
            .withFailMessage("Default constructor should set price to 20000.0")
            .isEqualTo(20000.0, Assertions.within(0.001));
    }

    /**
     * Test start method from interface.
     */
    public static void testStart() {
        carImpl().startMethod().invoke(carImpl().getObj());

        Object speed = carImpl().speed().getValue(carImpl().getObj());
        Assertions.assertThat((double)speed)
            .withFailMessage("Speed should be set after calling %s.",
                           carImpl().startMethod().getExpectedName())
            .isEqualTo(10.0, Assertions.within(0.001));
    }
    
    /**
     * Test getSpeed method from interface.
     */
    public static void testGetSpeed() {
        carImpl().startMethod().invoke(carImpl().getObj());

        Object speed = carImpl().getSpeedMethod().invoke(carImpl().getObj());
        Assertions.assertThat((double)speed)
            .withFailMessage("Method %s should return the current speed.",
                           carImpl().getSpeedMethod().getExpectedName())
            .isEqualTo(10.0, Assertions.within(0.001));
    }

    /**
     * Test calculateCost method (no parameters).
     */
    public static void testCalculateCost() {
        Object cost = carImpl().calculateCostMethod().invoke(carImpl().getObj());
        Assertions.assertThat(cost)
            .withFailMessage("Method %s should return a valid cost value.",
                           carImpl().calculateCostMethod().getExpectedName())
            .isNotNull();
        
        // Cost should be 10% of price (35000 * 0.1 = 3500)
        Assertions.assertThat((double)cost)
            .withFailMessage("Cost should be 10%% of price.")
            .isEqualTo(3500.0, Assertions.within(0.001));
    }
    
    /**
     * Test calculateCost method with years parameter (method overloading).
     */
    public static void testCalculateCostWithYears() {
        Object cost = carImpl().calculateCostYearsMethod().invoke(carImpl().getObj(), 5);
        Assertions.assertThat(cost)
            .withFailMessage("Method %s with years parameter should return a valid cost value.",
                           carImpl().calculateCostYearsMethod().getExpectedName())
            .isNotNull();
        
        // Cost should be 10% of price * years (35000 * 0.1 * 5 = 17500)
        Assertions.assertThat((double)cost)
            .withFailMessage("Cost for 5 years should be correct.")
            .isEqualTo(17500.0, Assertions.within(0.001));
    }
    
    /**
     * Test getInfo method (method overriding).
     */
    public static void testGetInfo() {
        String info = carImpl().getInfoMethod().invoke(carImpl().getObj());
        Assertions.assertThat(info)
            .withFailMessage("Method %s should return vehicle information.",
                           carImpl().getInfoMethod().getExpectedName())
            .isNotNull()
            .contains("BMW", "2023", "35000");
    }
    
    /**
     * Test getPrice method.
     */
    public static void testGetPrice() {
        Object price = carImpl().getPriceMethod().invoke(carImpl().getObj());
        Assertions.assertThat((double)price)
            .withFailMessage("Method %s should return the price.",
                           carImpl().getPriceMethod().getExpectedName())
            .isEqualTo(35000.0, Assertions.within(0.001));
    }
}

