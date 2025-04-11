package org.junit.experimental.runners;

import org.junit.runners.Suite;
import org.junit.runners.model.RunnerBuilder;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Enclosed
        extends Suite {
    public Enclosed(Class<?> klass, RunnerBuilder builder) throws Throwable {
        super(builder, klass, filterAbstractClasses(klass.getClasses()));
    }

    private static Class<?>[] filterAbstractClasses(Class<?>[] classes) {
        List<Class<?>> filteredList = new ArrayList<Class<?>>(classes.length);

        for (Class<?> clazz : classes) {
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                filteredList.add(clazz);
            }
        }

        return (Class[]) filteredList.<Class<?>[]>toArray((Class<?>[][]) new Class[filteredList.size()]);
    }
}

