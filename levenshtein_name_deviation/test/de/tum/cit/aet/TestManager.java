package de.tum.cit.aet;

import de.tum.cit.aet.test.*;
import de.tum.cit.aet.wrappers.*;


import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.List;

import static de.tum.cit.aet.levenshtein.StructuralLevenshtein.structuralTestFactory;
import static de.tum.cit.aet.levenshtein.StructuralLevenshtein.DetailLevel.*;

/**
 * Main test manager coordinating all vehicle tests.
 * Uses DynamicTimeoutExtension to apply timeout based on debug mode.
 */
@LevenshteinTest
public class TestManager {

    static AbstrWrapper<?> vehicleAbstr;
    static CarWrapper<?> carImpl;
    static DrivableWrapper<?> driveableInterface;

    public static AbstrWrapper<?> vehicleAbstr() {
        return vehicleAbstr;
    }
    public static CarWrapper<?> carImpl() {
        return carImpl;
    }
    public static DrivableWrapper<?> driveableInterface() {
        return driveableInterface;
    }

    @BeforeAll
    static void beforeAll() {
        driveableInterface = new DrivableWrapper<>();
        vehicleAbstr = new AbstrWrapper<>();
        carImpl = new CarWrapper<>(vehicleAbstr, driveableInterface);
    }

    @Test
    void testCompilationAndSetup() {
        // This test will always pass if the code compiles successfully
        // and all wrappers are initialized correctly.
        assertThat(vehicleAbstr).isNotNull();
        assertThat(vehicleAbstr).isInstanceOf(AbstrWrapper.class);
        assertThat(carImpl).isNotNull();
        assertThat(carImpl).isInstanceOf(CarWrapper.class);
        assertThat(driveableInterface).isNotNull();
        assertThat(driveableInterface).isInstanceOf(DrivableWrapper.class);

    }

    @TestFactory
    List<DynamicTest> structFactory() {
        return structuralTestFactory(
            ONE_PER_MEMBER_CATEGORY,
            driveableInterface,
            vehicleAbstr,
            carImpl
        );
    }

    // ============================================================================
    // AbstractVehicle Tests
    // ============================================================================

    @Test
    void testAbstrConstructorManufacturer() {
        try {
            TestAbstr.testConstructorManufacturer();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testAbstrConstructorModel() {
        try {
            TestAbstr.testConstructorModel();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testAbstrConstructorYear() {
        try {
            TestAbstr.testConstructorYear();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testAbstrEngineNotRunningInitially() {
        try {
            TestAbstr.testEngineNotRunningInitially();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testAbstrCurrentSpeedInitiallyZero() {
        try {
            TestAbstr.testCurrentSpeedInitiallyZero();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testAbstrGetManufacturer() {
        try {
            TestAbstr.testGetManufacturer();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testAbstrGetModel() {
        try {
            TestAbstr.testGetModel();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testAbstrGetYearOfManufacture() {
        try {
            TestAbstr.testGetYearOfManufacture();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testAbstrIsEngineRunning() {
        try {
            TestAbstr.testIsEngineRunning();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    // ============================================================================
    // Car Implementation Tests
    // ============================================================================

    @Test
    void testImplConstructorFull() {
        try {
            TestImpl.testConstructorFull();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplStartEngineFirstCall() {
        try {
            TestImpl.testStartEngineFirstCall();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplStartEngineSecondCall() {
        try {
            TestImpl.testStartEngineSecondCall();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplAccelerate() {
        try {
            TestImpl.testAccelerate();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplBrake() {
        try {
            TestImpl.testBrake();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplGetCurrentSpeed() {
        try {
            TestImpl.testGetCurrentSpeed();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplCalculateFuelConsumption() {
        try {
            TestImpl.testCalculateFuelConsumption();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplGetMaxSpeed() {
        try {
            TestImpl.testGetMaxSpeed();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplIsElectricPetrol() {
        try {
            TestImpl.testIsElectricPetrol();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplIsElectricElectric() {
        try {
            TestImpl.testIsElectricElectric();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplGetVehicleInfo() {
        try {
            TestImpl.testGetVehicleInfo();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplGetNumberOfDoors() {
        try {
            TestImpl.testGetNumberOfDoors();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplGetFuelType() {
        try {
            TestImpl.testGetFuelType();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplGetEngineCapacity() {
        try {
            TestImpl.testGetEngineCapacity();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    // ============================================================================
    // Interface Tests
    // ============================================================================
    @Test
    void testInterfaceCarImplementsInterface() {
        try {
            TestInterface.testCarImplementsInterface();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }
}

