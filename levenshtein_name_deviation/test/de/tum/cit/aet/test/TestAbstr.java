package de.tum.cit.aet.test;

import de.tum.cit.aet.wrappers.*;
import static de.tum.cit.aet.TestManager.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Tests for the AbstractVehicle class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAbstr {
    
    /**
     * Test that the constructor initializes the manufacturer attribute correctly.
     */
    public static void testConstructorManufacturer() {
        Assertions.assertThatCode(() -> 
            vehicleAbstr().constructor().invoke("Toyota", "Corolla", 2022))
            .withFailMessage("Constructor of %s is not implemented correctly.", 
                           vehicleAbstr().getExpectedName())
            .doesNotThrowAnyException();
        
        Object manufacturer = vehicleAbstr().manufacturer().getValue(vehicleAbstr().getObj());
        Assertions.assertThat(manufacturer)
            .withFailMessage("Value of attribute %s must be equal to the value passed in constructor.",
                           vehicleAbstr().manufacturer().getExpectedName())
            .isEqualTo("Toyota");
    }
    
    /**
     * Test that the constructor initializes the model attribute correctly.
     */
    public static void testConstructorModel() {
        Object model = vehicleAbstr().model().getValue(vehicleAbstr().getObj());
        Assertions.assertThat(model)
            .withFailMessage("Value of attribute %s must be equal to the value passed in constructor.",
                           vehicleAbstr().model().getExpectedName())
            .isEqualTo("Corolla");
    }
    
    /**
     * Test that the constructor initializes the yearOfManufacture attribute correctly.
     */
    public static void testConstructorYear() {
        Object year = vehicleAbstr().yearOfManufacture().getValue(vehicleAbstr().getObj());
        Assertions.assertThat((int)year)
            .withFailMessage("Value of attribute %s must be equal to the value passed in constructor.",
                           vehicleAbstr().yearOfManufacture().getExpectedName())
            .isEqualTo(2022);
    }
    
    /**
     * Test that engineRunning is false initially.
     */
    public static void testEngineNotRunningInitially() {
        Object engineRunning = vehicleAbstr().engineRunning().getValue(vehicleAbstr().getObj());
        Assertions.assertThat((boolean)engineRunning)
            .withFailMessage("Value of attribute %s must be false initially.",
                           vehicleAbstr().engineRunning().getExpectedName())
            .isFalse();
    }
    
    /**
     * Test that currentSpeed is 0 initially.
     */
    public static void testCurrentSpeedInitiallyZero() {
        Object speed = vehicleAbstr().currentSpeed().getValue(vehicleAbstr().getObj());
        Assertions.assertThat((double)speed)
            .withFailMessage("Value of attribute %s must be 0.0 initially.",
                           vehicleAbstr().currentSpeed().getExpectedName())
            .isEqualTo(0.0, Assertions.within(0.001));
    }
    
    /**
     * Test getManufacturer method.
     */
    public static void testGetManufacturer() {
        String manufacturer = vehicleAbstr().getManufacturerMethod().invoke(vehicleAbstr().getObj());
        Assertions.assertThat(manufacturer)
            .withFailMessage("Method %s should return the manufacturer value.",
                           vehicleAbstr().getManufacturerMethod().getExpectedName())
            .isEqualTo("Toyota");
    }
    
    /**
     * Test getModel method.
     */
    public static void testGetModel() {
        String model = vehicleAbstr().getModelMethod().invoke(vehicleAbstr().getObj());
        Assertions.assertThat(model)
            .withFailMessage("Method %s should return the model value.",
                           vehicleAbstr().getModelMethod().getExpectedName())
            .isEqualTo("Corolla");
    }
    
    /**
     * Test getYearOfManufacture method.
     */
    public static void testGetYearOfManufacture() {
        Object year = vehicleAbstr().getYearOfManufactureMethod().invoke(vehicleAbstr().getObj());
        Assertions.assertThat((int)year)
            .withFailMessage("Method %s should return the year value.",
                           vehicleAbstr().getYearOfManufactureMethod().getExpectedName())
            .isEqualTo(2022);
    }
    
    /**
     * Test isEngineRunning method.
     */
    public static void testIsEngineRunning() {
        Boolean running = vehicleAbstr().isEngineRunningMethod().invoke(vehicleAbstr().getObj());
        Assertions.assertThat(running)
            .withFailMessage("Method %s should return false initially.",
                           vehicleAbstr().isEngineRunningMethod().getExpectedName())
            .isFalse();
    }
}

