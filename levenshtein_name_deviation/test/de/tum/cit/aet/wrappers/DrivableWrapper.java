package de.tum.cit.aet.wrappers;

import de.tum.cit.aet.levenshtein.ClassWrapper;
import de.tum.cit.aet.levenshtein.MethodWrapper;
import static de.tum.cit.aet.Constants.*;

public class DrivableWrapper<T> extends ClassWrapper<T> {

    private final MethodWrapper<T,?> startEngine;
    private final MethodWrapper<T,?> accelerate;
    private final MethodWrapper<T,?> brake;
    private final MethodWrapper<T,?> getCurrentSpeed;

    public MethodWrapper<T,?> startEngine() { return startEngine; }
    public MethodWrapper<T,?> accelerate() { return accelerate; }
    public MethodWrapper<T,?> brake() { return brake; }
    public MethodWrapper<T,?> getCurrentSpeed() { return getCurrentSpeed; }

    public DrivableWrapper() {
        super(
            interfaceName(),    // without Constants Pattern: "Drivable"
            "de.tum.cit.aet",
            null,
            null,
            "public", "abstract", "interface"
        );

        startEngine = new MethodWrapper<>(
            this,
            "startEngine",
            boolean.class,
            new Class[]{},
            "public", "abstract"
        );

        accelerate = new MethodWrapper<>(
            this,
            "accelerate",
            void.class,
            new Class[]{double.class},
            "public", "abstract"
        );

        brake = new MethodWrapper<>(
            this,
            "brake",
            void.class,
            new Class[]{double.class},
            "public", "abstract"
        );

        getCurrentSpeed = new MethodWrapper<>(
            this,
            "getCurrentSpeed",
            double.class,
            new Class[]{},
            "public", "abstract"
        );
    }


    private Object obj;
    @Override
    public T getObj()
    {
        return (T)obj;
    }

    public void setObj(T obj)
    {
        this.obj = obj;
    }
}
