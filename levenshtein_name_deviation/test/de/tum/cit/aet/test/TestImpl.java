package de.tum.cit.aet.test;

import static de.tum.cit.aet.TestManager.*;
import static de.tum.cit.aet.levenshtein.LevenshteinUtils.saveCast;
import static org.assertj.core.api.Assertions.fail;

import de.tum.cit.aet.levenshtein.ClassWrapper;
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
        try {
            /*
            A ClassWrapper's obj attribute can be overwritten anytime. When needing a
            specific Object store it yourself!
            */
            Object obj = carImpl().constructor_full().invoke("BMW", 2023, 35000.0);

            // Verify inherited attributes
            Object manufacturer = vehicleAbstr().manufacturer().getValue(obj);
            Assertions.assertThat(manufacturer)
                .withFailMessage("Inherited attribute %s must be initialized correctly.",
                               vehicleAbstr().manufacturer().getExpectedName())
                .isEqualTo("BMW");

            // Verify Car-specific attributes
            Object price = carImpl().price().getValue(obj);
            Assertions.assertThat((double)price)
                .withFailMessage("Attribute %s must be equal to constructor argument.",
                               carImpl().price().getExpectedName())
                .isEqualTo(35000.0, Assertions.within(0.001));
        }
        catch (Exception e) {
            Assertions.fail("Invoking constructor of %s caused an exception: %s",
                    carImpl().getExpectedName(), e.getMessage());
        }
    }
    
    /**
     * Test the default constructor (constructor overloading).
     */
    public static void testConstructorDefault() {
        Object car = carImpl().constructor_default().invoke("BMW", 2023);

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
        Object obj = carImpl().getObj(true);

        carImpl().startMethod().invokeOnSpecificObject(obj);
        Object speed = carImpl().speed().getValue(obj);

        Assertions.assertThat((double)speed)
            .withFailMessage("Speed should be set after calling %s.",
                           carImpl().startMethod().getExpectedName())
            .isEqualTo(10.0, Assertions.within(0.001));
    }
    
    /**
     * Test getSpeed method from interface.
     */
    public static void testGetSpeed() {
        carImpl().testGetter(carImpl().speed(), carImpl().getSpeed());
    }

    /**
     * Test calculateCost method (no parameters).
     */
    public static void testCalculateCost() {
        Object obj = carImpl().getObj(true);
        double cost = (double)saveCast(carImpl().calculateCost().invokeOnSpecificObject(obj),double.class);
        double val = (double)saveCast(carImpl().price().getValue(carImpl().getObj(false)),double.class);
        
        // Cost should be 10% of price (35000 * 0.1 = 3500)
        Assertions.assertThat(cost)
            .withFailMessage("Cost should be 10%% of the price.")
            .isEqualTo(val*0.1, Assertions.within(0.001));
    }
    
    /**
     * Test calculateCost method with years parameter (method overloading).
     */
    public static void testCalculateCostWithYears() {
        Object obj = carImpl().getObj(true);
        Object cost = carImpl().calculateCostYears().invokeOnSpecificObject(obj, 5);
        Assertions.assertThat(cost)
            .withFailMessage("Method %s with years parameter should return a valid cost value.",
                           carImpl().calculateCostYears().getExpectedName())
            .isNotNull();
        
        // Cost should be 10% of price * years (35000 * 0.1 * 5 = 17500)
        Assertions.assertThat((double)cost)
            .withFailMessage("Cost for 5 years should be correct.")
            .isEqualTo(15000.0, Assertions.within(0.001));
    }
    
    /**
     * Test getInfo method (method overriding).
     */
    public static void testGetInfo() {
        String info = carImpl().getInfo().invoke();
        Assertions.assertThat(info)
            .withFailMessage("Method %s should return vehicle information.",
                           carImpl().getInfo().getExpectedName())
            .isNotNull()
            .contains("BMW", "2023", "30000");
    }
    
    /**
     * Test getPrice method.
     */
    public static void testGetPrice() {
        carImpl().testGetter(carImpl().price(), carImpl().getPrice());
    }
}

