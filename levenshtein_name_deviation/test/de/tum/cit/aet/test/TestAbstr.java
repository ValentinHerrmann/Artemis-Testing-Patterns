package de.tum.cit.aet.test;

import de.tum.cit.aet.wrappers.*;
import static de.tum.cit.aet.TestManager.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Tests for the AbstractVehicle class.
 * Simplified to match the new example structure.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAbstr {
    
    /**
     * Test that the constructor initializes the manufacturer attribute correctly.
     */
    public static void testConstructorManufacturer() {
        Assertions.assertThatCode(() -> 
            vehicleAbstr().constructor().invoke("Toyota", 2022))
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
     * Test that the constructor initializes the year attribute correctly.
     */
    public static void testConstructorYear() {
        Object year = vehicleAbstr().year().getValue(vehicleAbstr().getObj());
        Assertions.assertThat((int)year)
            .withFailMessage("Value of attribute %s must be equal to the value passed in constructor.",
                           vehicleAbstr().year().getExpectedName())
            .isEqualTo(2022);
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
     * Test getYear method.
     */
    public static void testGetYear() {
        Object year = vehicleAbstr().getYearMethod().invoke(vehicleAbstr().getObj());
        Assertions.assertThat((int)year)
            .withFailMessage("Method %s should return the year value.",
                           vehicleAbstr().getYearMethod().getExpectedName())
            .isEqualTo(2022);
    }
    
    /**
     * Test getInfo method.
     */
    public static void testGetInfo() {
        String info = vehicleAbstr().getInfoMethod().invoke(vehicleAbstr().getObj());
        Assertions.assertThat(info)
            .withFailMessage("Method %s should return vehicle information.",
                           vehicleAbstr().getInfoMethod().getExpectedName())
            .isNotNull()
            .contains("Toyota", "2022");
    }
}

