package de.tum.cit.aet.wrappers;

import de.tum.cit.aet.levenshtein.*;
import static de.tum.cit.aet.Constants.*;
import static de.tum.cit.aet.TestSettings.BASE_PACKAGE;

/**
 * Wrapper for the Driveable interface.
 * Simplified version matching the new example structure.
 */
public class DrivableWrapper<T> extends ClassWrapper<T> {

    private final AttributeWrapper<T, Double> maxSpeed;
    private final MethodWrapper<T, Void> startMethod;
    private final MethodWrapper<T, ?> getSpeedMethod;

    public AttributeWrapper<T, Double> maxSpeed() {
        return maxSpeed;
    }

    public MethodWrapper<T, Void> startMethod() {
        return startMethod;
    }

    public MethodWrapper<T, ?> getSpeedMethod() {
        return getSpeedMethod;
    }

    public DrivableWrapper() {
        super(interfaceName(),
              BASE_PACKAGE,
              null,
              null,
              "public", "abstract", "interface");

        // Interface constant
        maxSpeed = new AttributeWrapper<>(this,
                                           "MAX_SPEED",
                                           double.class,
                                           "public", "static", "final");

        // Interface methods
        startMethod = new MethodWrapper<>(this,
                                           "start",
                                           void.class,
                                           new Class<?>[]{},
                                           "public", "abstract");

        getSpeedMethod = new MethodWrapper<>(this,
                                               "getSpeed",
                                               speedType(),
                                               new Class<?>[]{},
                                               "public", "abstract");
    }

    private Object obj;

    @Override
    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}