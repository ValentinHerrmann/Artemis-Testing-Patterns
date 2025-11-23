package de.tum.cit.aet;

import de.tum.cit.aet.wrappers.AbstrWrapper;
import de.tum.cit.aet.wrappers.CarWrapper;
import de.tum.cit.aet.wrappers.DrivableWrapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import static de.tum.cit.aet.Constants.*;

/**
 * Tests for the AbstractVehicle class.
 * Simplified to match the new example structure.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAbstr {

    private static AbstrWrapper<?> abstrWrap;
    private static CarWrapper<?> carWrap;
    private static DrivableWrapper<?> interfaceWrap;

    public static void setAbstrWrap(AbstrWrapper<?> abstrWrap) {
        TestAbstr.abstrWrap = abstrWrap;
    }

    public static void setCarWrap(CarWrapper<?> carWrap) {
        TestAbstr.carWrap = carWrap;
    }

    public static void setInterfaceWrap(DrivableWrapper<?> interfaceWrap) {
        TestAbstr.interfaceWrap = interfaceWrap;
    }

    /**
     * Test that the constructor initializes the manufacturer attribute correctly.
     */
    static void testConstructorManufacturer() {
        Assertions.assertThatCode(() ->
            abstrWrap.constructor().invoke("Toyota", 2022))
            .withFailMessage("Constructor of %s is not implemented correctly.",
                           abstrWrap.getExpectedName())
            .doesNotThrowAnyException();

        Object manufacturer = abstrWrap.manufacturer().getValue(abstrWrap.getObj());
        Assertions.assertThat(manufacturer)
            .withFailMessage("Value of attribute %s must be equal to the value passed in constructor.",
                           abstrWrap.manufacturer().getExpectedName())
            .isEqualTo("Toyota");
    }

    /**
     * Test that the constructor initializes the year attribute correctly.
     */
    static void testConstructorYear() {
        Object year = abstrWrap.year().getValue(abstrWrap.getObj());
        Assertions.assertThat((int)year)
            .withFailMessage("Value of attribute %s must be equal to the value passed in constructor.",
                           abstrWrap.year().getExpectedName())
            .isEqualTo(2022);
    }

    /**
     * Test getManufacturer method.
     */
    static void testGetManufacturer() {
        String manufacturer = abstrWrap.getManufacturerMethod().invoke(abstrWrap.getObj());
        Assertions.assertThat(manufacturer)
            .withFailMessage("Method %s should return the manufacturer value.",
                           abstrWrap.getManufacturerMethod().getExpectedName())
            .isEqualTo("Toyota");
    }

    /**
     * Test getYear method.
     */
    static void testGetYear() {
        Object year = abstrWrap.getYearMethod().invoke(abstrWrap.getObj());
        Assertions.assertThat((int)year)
            .withFailMessage("Method %s should return the year value.",
                           abstrWrap.getYearMethod().getExpectedName())
            .isEqualTo(2022);
    }

    /**
     * Test getInfo method.
     */
    static void testGetInfo() {
        String info = abstrWrap.getInfoMethod().invoke(abstrWrap.getObj());
        Assertions.assertThat(info)
            .withFailMessage("Method %s should return vehicle information.",
                           abstrWrap.getInfoMethod().getExpectedName())
            .isNotNull()
            .contains("Toyota", "2022");
    }
}

