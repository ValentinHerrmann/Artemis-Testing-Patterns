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
    // AbstractVehicle Tests (5 tests)
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
    void testAbstrConstructorYear() {
        try {
            TestAbstr.testConstructorYear();
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
    void testAbstrGetYear() {
        try {
            TestAbstr.testGetYear();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testAbstrGetInfo() {
        try {
            TestAbstr.testGetInfo();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    // ============================================================================
    // Car Implementation Tests (8 tests)
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
    void testImplConstructorDefault() {
        try {
            TestImpl.testConstructorDefault();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplStart() {
        try {
            TestImpl.testStart();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplGetSpeed() {
        try {
            TestImpl.testGetSpeed();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplCalculateCost() {
        try {
            TestImpl.testCalculateCost();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplCalculateCostWithYears() {
        try {
            TestImpl.testCalculateCostWithYears();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplGetInfo() {
        try {
            TestImpl.testGetInfo();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testImplGetPrice() {
        try {
            TestImpl.testGetPrice();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    // ============================================================================
    // Interface Tests (5 tests)
    // ============================================================================

    @Test
    void testInterfaceExists() {
        try {
            TestInterface.testInterfaceExists();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testInterfaceMaxSpeedConstant() {
        try {
            TestInterface.testMaxSpeedConstant();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testInterfaceStartMethodExists() {
        try {
            TestInterface.testStartMethodExists();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testInterfaceGetSpeedMethodExists() {
        try {
            TestInterface.testGetSpeedMethodExists();
        }
        catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

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

