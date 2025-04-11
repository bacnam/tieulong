package com.google.common.base.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Finalizer
        extends Thread {
    private static final Logger logger = Logger.getLogger(Finalizer.class.getName());

    private static final String FINALIZABLE_REFERENCE = "com.google.common.base.FinalizableReference";
    private static final Field inheritableThreadLocals = getInheritableThreadLocalsField();
    private final WeakReference<Class<?>> finalizableReferenceClassReference;
    private final PhantomReference<Object> frqReference;
    private final ReferenceQueue<Object> queue = new ReferenceQueue();

    private Finalizer(Class<?> finalizableReferenceClass, Object frq) {
        super(Finalizer.class.getName());

        this.finalizableReferenceClassReference = new WeakReference<Class<?>>(finalizableReferenceClass);

        this.frqReference = new PhantomReference(frq, this.queue);

        setDaemon(true);

        try {
            if (inheritableThreadLocals != null) {
                inheritableThreadLocals.set(this, (Object) null);
            }
        } catch (Throwable t) {
            logger.log(Level.INFO, "Failed to clear thread local values inherited by reference finalizer thread.", t);
        }
    }

    public static ReferenceQueue<Object> startFinalizer(Class<?> finalizableReferenceClass, Object frq) {
        if (!finalizableReferenceClass.getName().equals("com.google.common.base.FinalizableReference")) {
            throw new IllegalArgumentException("Expected com.google.common.base.FinalizableReference.");
        }

        Finalizer finalizer = new Finalizer(finalizableReferenceClass, frq);
        finalizer.start();
        return finalizer.queue;
    }

    public static Field getInheritableThreadLocalsField() {
        try {
            Field inheritableThreadLocals = Thread.class.getDeclaredField("inheritableThreadLocals");

            inheritableThreadLocals.setAccessible(true);
            return inheritableThreadLocals;
        } catch (Throwable t) {
            logger.log(Level.INFO, "Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will inherit thread local values.");

            return null;
        }
    }

    public void run() {
        try {
            while (true) {
                try {
                    while (true)
                        cleanUp(this.queue.remove());
                    break;
                } catch (InterruptedException e) {
                }
            }
        } catch (ShutDown shutDown) {
            return;
        }
    }

    private void cleanUp(Reference<?> reference) throws ShutDown {
        Method finalizeReferentMethod = getFinalizeReferentMethod();

        do {
            reference.clear();

            if (reference == this.frqReference) {

                throw new ShutDown();
            }

            try {
                finalizeReferentMethod.invoke(reference, new Object[0]);
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Error cleaning up after reference.", t);

            }

        }
        while ((reference = this.queue.poll()) != null);
    }

    private Method getFinalizeReferentMethod() throws ShutDown {
        Class<?> finalizableReferenceClass = this.finalizableReferenceClassReference.get();

        if (finalizableReferenceClass == null) {

            throw new ShutDown();
        }
        try {
            return finalizableReferenceClass.getMethod("finalizeReferent", new Class[0]);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    private static class ShutDown extends Exception {
        private ShutDown() {
        }
    }
}

