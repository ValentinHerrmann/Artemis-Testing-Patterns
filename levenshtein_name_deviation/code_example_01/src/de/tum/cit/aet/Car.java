package de.tum.cit.aet;

/**
 * Concrete implementation of a Car that extends AbstractVehicle and implements Driveable.
 * Demonstrates implementation testing with Levenshtein pattern including:
 * - Inheritance from abstract class
 * - Interface implementation
 * - Constructor with multiple parameters
 * - Method overriding
 * - State management
 */
public class Car extends AbstractVehicle implements Driveable {

    private int numberOfDoors;
    private String fuelType;
    private double engineCapacity;
    private double maxSpeed;
    private double fuelConsumptionRate;

    /**
     * Constructor for Car.
     * @param manufacturer the car manufacturer
     * @param model the car model
     * @param yearOfManufacture the year of manufacture
     * @param numberOfDoors the number of doors
     * @param fuelType the type of fuel (e.g., "Petrol", "Diesel", "Electric")
     * @param engineCapacity the engine capacity in liters
     */
    public Car(String manufacturer, String model, int yearOfManufacture,
               int numberOfDoors, String fuelType, double engineCapacity) {
        super(manufacturer, model, yearOfManufacture);
        this.numberOfDoors = numberOfDoors;
        this.fuelType = fuelType;
        this.engineCapacity = engineCapacity;
        this.maxSpeed = calculateMaxSpeedFromEngine();
        this.fuelConsumptionRate = engineCapacity * 4.5; // Simplified calculation
    }

    /**
     * Gets the number of doors.
     * @return the number of doors
     */
    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    /**
     * Gets the fuel type.
     * @return the fuel type
     */
    public String getFuelType() {
        return fuelType;
    }

    /**
     * Gets the engine capacity.
     * @return the engine capacity in liters
     */
    public double getEngineCapacity() {
        return engineCapacity;
    }

    @Override
    public boolean startEngine() {
        if (!engineRunning) {
            engineRunning = true;
            return true;
        }
        return false;
    }

    @Override
    public void accelerate(double speedIncrease) {
        if (engineRunning && speedIncrease > 0) {
            currentSpeed = Math.min(currentSpeed + speedIncrease, maxSpeed);
        }
    }

    @Override
    public void brake(double brakingForce) {
        if (brakingForce >= 0.0 && brakingForce <= 1.0) {
            double speedDecrease = currentSpeed * brakingForce * 0.5;
            currentSpeed = Math.max(0, currentSpeed - speedDecrease);
        }
    }

    @Override
    public double getCurrentSpeed() {
        return currentSpeed;
    }

    @Override
    public double calculateFuelConsumption() {
        // Simplified: base consumption + speed factor
        double speedFactor = currentSpeed / 100.0;
        return fuelConsumptionRate * (1.0 + speedFactor * 0.3);
    }

    @Override
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Calculates maximum speed based on engine capacity.
     * @return calculated max speed in km/h
     */
    private double calculateMaxSpeedFromEngine() {
        // Simplified formula: larger engines typically allow higher speeds
        return 120 + (engineCapacity * 30);
    }

    /**
     * Checks if the car is electric.
     * @return true if fuel type is electric
     */
    public boolean isElectric() {
        return "Electric".equalsIgnoreCase(fuelType);
    }

    @Override
    public String getVehicleInfo() {
        return String.format("%s - %d doors, %s, %.1fL engine",
                           super.getVehicleInfo(), numberOfDoors, fuelType, engineCapacity);
    }
}

