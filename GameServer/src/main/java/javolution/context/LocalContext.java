package javolution.context;

import javolution.lang.Configurable;
import javolution.osgi.internal.OSGiServices;

public abstract class LocalContext
extends AbstractContext
{
public static abstract class Parameter<T>
extends Configurable<T>
{
public static final SecurityContext.Permission<Parameter<?>> SUPERSEDE_PERMISSION = new SecurityContext.Permission<Parameter<?>>((Class)Parameter.class, "supersede");

private final SecurityContext.Permission<Parameter<T>> supersedePermission = new SecurityContext.Permission<Parameter<T>>((Class)Parameter.class, "supersede", this);

public SecurityContext.Permission<Parameter<T>> getSupersedePermission() {
return this.supersedePermission;
}

public T get() {
LocalContext ctx = AbstractContext.<LocalContext>current(LocalContext.class);
return (ctx != null) ? ctx.<T>getValue(this, (T)super.get()) : (T)super.get();
}
}

public static LocalContext enter() {
LocalContext ctx = current(LocalContext.class);
if (ctx == null) {
ctx = OSGiServices.getLocalContext();
}
return (LocalContext)ctx.enterInner();
}

public abstract <T> void supersede(Parameter<T> paramParameter, T paramT);

protected abstract <T> T getValue(Parameter<T> paramParameter, T paramT);
}

