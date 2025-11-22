package de.tum.cit.aet;

/**
 * Abstract base class for all vehicles.
 * Demonstrates abstract class testing with Levenshtein pattern.
 */
public abstract class AbstractVehicle {
    
    protected String manufacturer;
    protected String model;
    protected int yearOfManufacture;
    protected double currentSpeed;
    protected boolean engineRunning;
    
    /**
     * Constructor for AbstractVehicle.
     * @param manufacturer the vehicle manufacturer
     * @param model the vehicle model
     * @param yearOfManufacture the year the vehicle was manufactured
     */
    public AbstractVehicle(String manufacturer, String model, int yearOfManufacture) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.yearOfManufacture = yearOfManufacture;
        this.currentSpeed = 0.0;
        this.engineRunning = false;
    }
    
    /**
     * Gets the manufacturer name.
     * @return the manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }
    
    /**
     * Gets the model name.
     * @return the model
     */
    public String getModel() {
        return model;
    }
    
    /**
     * Gets the year of manufacture.
     * @return the year
     */
    public int getYearOfManufacture() {
        return yearOfManufacture;
    }
    
    /**
     * Checks if the engine is running.
     * @return true if engine is running
     */
    public boolean isEngineRunning() {
        return engineRunning;
    }
    
    /**
     * Abstract method to calculate fuel consumption.
     * @return fuel consumption in liters per 100km
     */
    public abstract double calculateFuelConsumption();
    
    /**
     * Abstract method to get the maximum speed.
     * @return maximum speed in km/h
     */
    public abstract double getMaxSpeed();
    
    /**
     * Gets vehicle information as string.
     * @return formatted vehicle information
     */
    public String getVehicleInfo() {
        return String.format("%s %s (%d)", manufacturer, model, yearOfManufacture);
    }
}

