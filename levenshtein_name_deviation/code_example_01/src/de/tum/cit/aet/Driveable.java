package de.tum.cit.aet;

/**
 * Interface for driveable vehicles.
 * Demonstrates interface testing with Levenshtein pattern.
 */
public interface Driveable {
    
    /**
     * Starts the vehicle's engine.
     * @return true if engine started successfully
     */
    boolean startEngine();
    
    /**
     * Accelerates the vehicle.
     * @param speedIncrease the amount to increase speed by
     */
    void accelerate(double speedIncrease);
    
    /**
     * Applies the brakes.
     * @param brakingForce the force to apply (0.0 to 1.0)
     */
    void brake(double brakingForce);
    
    /**
     * Gets the current speed.
     * @return the current speed in km/h
     */
    double getCurrentSpeed();
}

