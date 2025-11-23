package de.tum.cit.aet.test;

import de.tum.cit.aet.wrappers.*;
import static de.tum.cit.aet.TestManager.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Tests for the Car implementation class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestImpl {

    /**
     * Test the constructor with full parameters.
     */
    public static void testConstructorFull() {
        Assertions.assertThatCode(() ->
            carImpl().constructor_full().invoke("BMW", "M3", 2023, 4, "Petrol", 3.0))
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
        Object doors = carImpl().numberOfDoors().getValue(carImpl().getObj());
        Assertions.assertThat((int)doors)
            .withFailMessage("Attribute %s must be equal to constructor argument.",
                           carImpl().numberOfDoors().getExpectedName())
            .isEqualTo(4);
        
        Object fuelType = carImpl().fuelType().getValue(carImpl().getObj());
        Assertions.assertThat(fuelType)
            .withFailMessage("Attribute %s must be equal to constructor argument.",
                           carImpl().fuelType().getExpectedName())
            .isEqualTo("Petrol");
        
        Object engineCapacity = carImpl().engineCapacity().getValue(carImpl().getObj());
        Assertions.assertThat((double)engineCapacity)
            .withFailMessage("Attribute %s must be equal to constructor argument.",
                           carImpl().engineCapacity().getExpectedName())
            .isEqualTo(3.0, Assertions.within(0.001));
    }
    
    /**
     * Test startEngine method - first call should return true.
     */
    public static void testStartEngineFirstCall() {
        Boolean started = carImpl().startEngineMethod().invoke(carImpl().getObj());
        Assertions.assertThat(started)
            .withFailMessage("Method %s should return true on first call.",
                           carImpl().startEngineMethod().getExpectedName())
            .isTrue();
        
        Boolean engineRunning = vehicleAbstr().engineRunning().getValue(carImpl().getObj());
        Assertions.assertThat(engineRunning)
            .withFailMessage("Attribute %s should be true after starting engine.",
                           vehicleAbstr().engineRunning().getExpectedName())
            .isTrue();
    }
    
    /**
     * Test startEngine method - second call should return false.
     */
    public static void testStartEngineSecondCall() {
        // Start engine again (should already be running)
        Boolean started = carImpl().startEngineMethod().invoke(carImpl().getObj());
        Assertions.assertThat(started)
            .withFailMessage("Method %s should return false if engine is already running.",
                           carImpl().startEngineMethod().getExpectedName())
            .isFalse();
    }
    
    /**
     * Test accelerate method.
     */
    public static void testAccelerate() {
        // Get initial speed
        Object initialSpeed = carImpl().getCurrentSpeedMethod().invoke(carImpl().getObj());

        // Accelerate
        carImpl().accelerateMethod().invoke(carImpl().getObj(), 50.0);

        // Check speed increased
        Object newSpeed = carImpl().getCurrentSpeedMethod().invoke(carImpl().getObj());
        Assertions.assertThat((double)newSpeed)
            .withFailMessage("Speed should increase after calling %s.",
                           carImpl().accelerateMethod().getExpectedName())
            .isGreaterThan((double)initialSpeed);
    }
    
    /**
     * Test brake method.
     */
    public static void testBrake() {
        // Accelerate first to have some speed
        carImpl().accelerateMethod().invoke(carImpl().getObj(), 100.0);

        Object speedBeforeBrake = carImpl().getCurrentSpeedMethod().invoke(carImpl().getObj());

        // Apply brakes
        carImpl().brakeMethod().invoke(carImpl().getObj(), 0.5);

        Object speedAfterBrake = carImpl().getCurrentSpeedMethod().invoke(carImpl().getObj());
        Assertions.assertThat((double)speedAfterBrake)
            .withFailMessage("Speed should decrease after calling %s.",
                           carImpl().brakeMethod().getExpectedName())
            .isLessThan((double)speedBeforeBrake);
    }
    
    /**
     * Test getCurrentSpeed method.
     */
    public static void testGetCurrentSpeed() {
        Object speed = carImpl().getCurrentSpeedMethod().invoke(carImpl().getObj());
        Assertions.assertThat(speed)
            .withFailMessage("Method %s should return a valid speed value.",
                           carImpl().getCurrentSpeedMethod().getExpectedName())
            .isNotNull();
    }
    
    /**
     * Test calculateFuelConsumption method.
     */
    public static void testCalculateFuelConsumption() {
        Object consumption = carImpl().calculateFuelConsumptionMethod().invoke(carImpl().getObj());
        Assertions.assertThat(consumption)
            .withFailMessage("Method %s should return a valid consumption value.",
                           carImpl().calculateFuelConsumptionMethod().getExpectedName())
            .isNotNull();
        
        Assertions.assertThat((double)consumption)
            .withFailMessage("Fuel consumption should be positive.",
                           carImpl().calculateFuelConsumptionMethod().getExpectedName())
            .isGreaterThan(0.0);
    }
    
    /**
     * Test getMaxSpeed method.
     */
    public static void testGetMaxSpeed() {
        Object maxSpeed = carImpl().getMaxSpeedMethod().invoke(carImpl().getObj());
        Assertions.assertThat(maxSpeed)
            .withFailMessage("Method %s should return a valid max speed value.",
                           carImpl().getMaxSpeedMethod().getExpectedName())
            .isNotNull();
        
        Assertions.assertThat((double)maxSpeed)
            .withFailMessage("Max speed should be greater than 0.",
                           carImpl().getMaxSpeedMethod().getExpectedName())
            .isGreaterThan(0.0);
    }
    
    /**
     * Test isElectric method for petrol car.
     */
    public static void testIsElectricPetrol() {
        Boolean isElectric = carImpl().isElectricMethod().invoke(carImpl().getObj());
        Assertions.assertThat(isElectric)
            .withFailMessage("Method %s should return false for petrol car.",
                           carImpl().isElectricMethod().getExpectedName())
            .isFalse();
    }
    
    /**
     * Test isElectric method for electric car.
     */
    public static void testIsElectricElectric() {
        Object electricCar = carImpl().constructor_full().invoke("Tesla", "Model 3", 2024, 4, "Electric", 0.0);
        Boolean isElectric = carImpl().isElectricMethod().invoke(electricCar);
        Assertions.assertThat(isElectric)
            .withFailMessage("Method %s should return true for electric car.",
                           carImpl().isElectricMethod().getExpectedName())
            .isTrue();
    }
    
    /**
     * Test getVehicleInfo method.
     */
    public static void testGetVehicleInfo() {
        String info = carImpl().getVehicleInfoMethod().invoke(carImpl().getObj());
        Assertions.assertThat(info)
            .withFailMessage("Method %s should return vehicle information.",
                           carImpl().getVehicleInfoMethod().getExpectedName())
            .isNotNull()
            .contains("BMW", "M3", "2023");
    }
    
    /**
     * Test getNumberOfDoors method.
     */
    public static void testGetNumberOfDoors() {
        Object doors = carImpl().getNumberOfDoorsMethod().invoke(carImpl().getObj());
        Assertions.assertThat((int)doors)
            .withFailMessage("Method %s should return the correct number of doors.",
                           carImpl().getNumberOfDoorsMethod().getExpectedName())
            .isEqualTo(4);
    }
    
    /**
     * Test getFuelType method.
     */
    public static void testGetFuelType() {
        String fuelType = carImpl().getFuelTypeMethod().invoke(carImpl().getObj());
        Assertions.assertThat(fuelType)
            .withFailMessage("Method %s should return the correct fuel type.",
                           carImpl().getFuelTypeMethod().getExpectedName())
            .isEqualTo("Petrol");
    }
    
    /**
     * Test getEngineCapacity method.
     */
    public static void testGetEngineCapacity() {
        Object capacity = carImpl().getEngineCapacityMethod().invoke(carImpl().getObj());
        Assertions.assertThat((double)capacity)
            .withFailMessage("Method %s should return the correct engine capacity.",
                           carImpl().getEngineCapacityMethod().getExpectedName())
            .isEqualTo(3.0, Assertions.within(0.001));
    }
}

