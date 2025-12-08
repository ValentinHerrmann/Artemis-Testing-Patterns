package de.tum.cit.aet.test;

import static de.tum.cit.aet.Constants.*;
import static de.tum.cit.aet.TestManager.*;
import static de.tum.cit.aet.levenshtein.LevenshteinUtils.*;
import static org.assertj.core.api.Assertions.fail;

import de.tum.cit.aet.levenshtein.ClassWrapper;
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
        try {
            Object obj = vehicleAbstr().constructor().invoke("Toyota", 2022);

            var manufacturer = saveCast(vehicleAbstr().getManufacturer().invokeOnSpecificObject(obj), manufacturerType());
            Assertions.assertThat(manufacturer)
                .withFailMessage("Value of attribute %s must be equal to the value passed in constructor.",
                               vehicleAbstr().manufacturer().getExpectedName())
                .isEqualTo("Toyota");
        }
        catch (Exception e) {
            fail("Invoking constructor of %s caused an exception: %s",
                    vehicleAbstr().getExpectedName(), e.getMessage());
        }
    }
    
    /**
     * Test that the constructor initializes the year attribute correctly.
     */
    public static void testConstructorYear() {
        var year = saveCast(vehicleAbstr().year().getValue(), yearType());
        Assertions.assertThat(year)
            .withFailMessage("Value of attribute %s must be equal to the value passed in constructor.",
                           vehicleAbstr().year().getExpectedName())
            .isEqualTo(2023);
    }
    
    /**
     * Test getManufacturer method.
     */
    public static void testGetManufacturer() {
        vehicleAbstr().testGetter(vehicleAbstr().manufacturer(), vehicleAbstr().getManufacturer());
    }
    
    /**
     * Test getYear method.
     */
    public static void testGetYear() {
        vehicleAbstr().testGetter(vehicleAbstr().year(), vehicleAbstr().getYear());
    }
    
    /**
     * Test getInfo method.
     */
    public static void testGetInfo() {
        Object obj = vehicleAbstr().getObj(true, true,
                vehicleAbstr().constructor(),
                "BMW",2023);
        var man = (String)saveCast(vehicleAbstr().manufacturer().getValue(obj),manufacturerType());
        var year = saveCast(vehicleAbstr().year().getValue(obj),yearType());
        String info = vehicleAbstr().getInfo().invokeOnSpecificObject(obj); // DEMO of generic type usage, no need to cast anything
        Assertions.assertThat(info)
            .withFailMessage("Method %s should return vehicle information.",
                           vehicleAbstr().getInfo().getExpectedName())
            .isNotNull()
            .contains(man, ""+year);
    }
}

