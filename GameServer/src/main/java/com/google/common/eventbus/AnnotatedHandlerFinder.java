package com.google.common.eventbus;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.lang.reflect.Method;

class AnnotatedHandlerFinder
        implements HandlerFindingStrategy {
    private static EventHandler makeHandler(Object listener, Method method) {
        EventHandler wrapper;
        if (methodIsDeclaredThreadSafe(method)) {
            wrapper = new EventHandler(listener, method);
        } else {
            wrapper = new SynchronizedEventHandler(listener, method);
        }
        return wrapper;
    }

    private static boolean methodIsDeclaredThreadSafe(Method method) {
        return (method.getAnnotation(AllowConcurrentEvents.class) != null);
    }

    public Multimap<Class<?>, EventHandler> findAllHandlers(Object listener) {
        HashMultimap hashMultimap = HashMultimap.create();

        Class<?> clazz = listener.getClass();
        while (clazz != null) {
            for (Method method : clazz.getMethods()) {
                Subscribe annotation = method.<Subscribe>getAnnotation(Subscribe.class);

                if (annotation != null) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 1) {
                        throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation, but requires " + parameterTypes.length + " arguments.  Event handler methods " + "must require a single argument.");
                    }

                    Class<?> eventType = parameterTypes[0];
                    EventHandler handler = makeHandler(listener, method);

                    hashMultimap.put(eventType, handler);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return (Multimap<Class<?>, EventHandler>) hashMultimap;
    }
}

