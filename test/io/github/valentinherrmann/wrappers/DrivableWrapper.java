package io.github.valentinherrmann.wrappers;


import io.github.valentinherrmann.levenshtein.*;

import static io.github.valentinherrmann.test.Constants.*;
import static io.github.valentinherrmann.test.TestSettings.BASE_PACKAGE;

/**
 * Wrapper for the Driveable interface.
 * Simplified version matching the new example structure.
 */
public class DrivableWrapper<T> extends ClassWrapper<T> {

    private final AttributeWrapper<T, ?> maxSpeed;
    private final MethodWrapper<T, ?> startMethod;
    private final MethodWrapper<T, ?> getSpeedMethod;

    public AttributeWrapper<T, ?> maxSpeed() {
        return maxSpeed;
    }

    public MethodWrapper<T, ?> startMethod() {
        return startMethod;
    }

    public MethodWrapper<T, ?> getSpeedMethod() {
        return getSpeedMethod;
    }

    public DrivableWrapper() {
        super(interfaceName(),
              BASE_PACKAGE,
              "public", "abstract", "interface"
        );

        // Interface constant
        maxSpeed = new AttributeWrapper<>(
                this,
                maxSpeedConstant(), // "MAX_SPEED"
                speedType(),        // double.class,
                "public", "static"
        );

        // Interface methods
        startMethod = new MethodWrapper<>(
                this,
                interfaceMethod(),  // "start"
                startRetType(),     // void.class,
                "public", "abstract"
        );

        getSpeedMethod = new MethodWrapper<>(
                this,
                getSpeedMethodName(), // "getSpeed"
                speedType(), // double.class
                "public", "abstract"
        );
    }

    private Object obj;

    /**
     *
     * @param forceNew force to create a new object even if member already holds one.
     * @param useByteBuddy whether to use ByteBuddy to create a dynamic subclass instance (set to false for private elements!)
     * @return
     */
    @Override
    public Object getObj(boolean forceNew, boolean useByteBuddy) {
        return getObj(forceNew, true, null);
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}