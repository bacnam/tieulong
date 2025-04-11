package javolution.osgi.internal;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public final class ServiceTrackerImpl<C> {
    private final Class<C> type;
    private final Class<? extends C> defaultImplClass;
    private volatile ServiceTracker<C, C> tracker;
    private C defaultImpl;

    public ServiceTrackerImpl(Class<C> type, Class<? extends C> defaultImplClass) {
        this.defaultImplClass = defaultImplClass;
        this.type = type;
    }

    public void activate(BundleContext bc) {
        ServiceTracker<C, C> trk = new ServiceTracker(bc, this.type, null);
        trk.open();
        this.tracker = trk;
    }

    public void deactivate(BundleContext bc) {
        this.tracker.close();
        this.tracker = null;
    }

    public Object[] getServices() {
        ServiceTracker<C, C> trk = this.tracker;
        if (trk != null) {
            Object[] services = trk.getServices();
            if (services != null) return services;
        }
        synchronized (this) {
            if (this.defaultImpl == null) {
                try {
                    this.defaultImpl = this.defaultImplClass.newInstance();
                } catch (Throwable error) {
                    throw new RuntimeException(error);
                }
            }
        }
        return new Object[]{this.defaultImpl};
    }
}

