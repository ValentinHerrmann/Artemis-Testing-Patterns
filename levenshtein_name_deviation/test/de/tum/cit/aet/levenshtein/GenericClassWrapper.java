package de.tum.cit.aet.levenshtein;

import java.lang.reflect.Modifier;

/**
The {@code GenericClassWrapper<T>} class is only suited for actually existing classes and
 shall only be used to verify inheritance of other wrapped classes.
 */
public class GenericClassWrapper<T> extends ClassWrapper<T>{

    private final Class<T> clazz;
    public GenericClassWrapper(Class<T> clz) {
        super(clz.getSimpleName(), clz.getPackageName(), Modifier.toString(clz.getModifiers()));
        this.clazz = clz;
    }
    public Class<T> getClazz() {
        return clazz;
    }
    @Override
    public Object getObj(boolean forceNew, boolean useByteBuddy) {
        return null;
    }
}
