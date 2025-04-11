package javolution.context;

import javolution.osgi.internal.OSGiServices;

public abstract class SecurityContext
        extends AbstractContext {
    public static SecurityContext enter() {
        return (SecurityContext) currentSecurityContext().enterInner();
    }

    public static void check(Permission<?> permission) {
        if (!currentSecurityContext().isGranted(permission)) {
            throw new SecurityException(permission + " is not granted.");
        }
    }

    private static SecurityContext currentSecurityContext() {
        SecurityContext ctx = current(SecurityContext.class);
        if (ctx != null)
            return ctx;
        return OSGiServices.getSecurityContext();
    }

    public abstract boolean isGranted(Permission<?> paramPermission);

    public abstract void grant(Permission<?> paramPermission, Object paramObject);

    public abstract void revoke(Permission<?> paramPermission, Object paramObject);

    public final void grant(Permission<?> permission) {
        grant(permission, (Object) null);
    }

    public final void revoke(Permission<?> permission) {
        revoke(permission, (Object) null);
    }

    public static class Permission<T> {
        public static final Permission<Object> ALL = new Permission(null);

        private final Class<? super T> category;

        private final String action;

        private final T instance;

        public Permission(Class<? super T> category) {
            this(category, null, null);
        }

        public Permission(Class<? super T> category, String action) {
            this(category, action, null);
        }

        public Permission(Class<? super T> category, String action, T instance) {
            this.category = category;
            this.action = action;
            this.instance = instance;
        }

        public Class<? super T> getCategory() {
            return this.category;
        }

        public String getAction() {
            return this.action;
        }

        public T getInstance() {
            return this.instance;
        }

        public boolean implies(Permission<?> that) {
            if (this.category == null)
                return true;
            if (!this.category.isAssignableFrom(that.category))
                return false;
            if (this.action == null)
                return true;
            if (!this.action.equals(that.action))
                return false;
            if (this.instance == null)
                return true;
            if (!this.instance.equals(that.instance))
                return false;
            return true;
        }

        public String toString() {
            if (this.category == null)
                return "All permissions";
            if (this.action == null)
                return "Permission for any action on " + this.category.getName();
            if (this.instance == null)
                return "Permission for " + this.action + " on " + this.category.getName();
            return "Permission for " + this.action + " on instance " + this.instance + " of " + this.category.getName();
        }

        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (!(obj instanceof Permission))
                return false;
            Permission<?> that = (Permission) obj;
            if (this.category == null && that.category != null)
                return false;
            if (this.category != null && !this.category.equals(that.category))
                return false;
            if (this.action == null && that.action != null)
                return false;
            if (this.action != null && !this.action.equals(that.action))
                return false;
            if (this.instance == null && that.instance != null)
                return false;
            if (this.instance != null && !this.instance.equals(that.instance))
                return false;
            return false;
        }

        public int hashCode() {
            return ((this.category != null) ? this.category.hashCode() : 0) + ((this.action != null) ? this.action.hashCode() : 0) + ((this.instance != null) ? this.instance.hashCode() : 0);
        }
    }
}

