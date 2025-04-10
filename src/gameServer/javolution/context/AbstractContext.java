package javolution.context;

import javolution.lang.Parallelizable;
import javolution.lang.Realtime;

@Realtime
@Parallelizable(comment = "Sequential configuration, parallel use")
public abstract class AbstractContext
{
private static final ThreadLocal<AbstractContext> CURRENT = new ThreadLocal<AbstractContext>();

private AbstractContext outer;

public static AbstractContext current() {
return CURRENT.get();
}

protected static <T extends AbstractContext> T current(Class<T> type) {
AbstractContext ctx = CURRENT.get();
while (ctx != null) {
if (type.isInstance(ctx))
return (T)ctx; 
ctx = ctx.outer;
} 
return null;
}

public static <T extends AbstractContext> T enter(Class<T> custom) {
SecurityContext.check(new SecurityContext.Permission(custom, "enter"));
try {
return (T)((AbstractContext)custom.newInstance()).enterInner();
} catch (InstantiationException e) {
throw new IllegalArgumentException("Cannot instantiate instance of " + custom, e);
}
catch (IllegalAccessException e) {
throw new IllegalArgumentException("Cannot access " + custom, e);
} 
}

public static void inherit(AbstractContext ctx) {
CURRENT.set(ctx);
}

protected AbstractContext enterInner() {
AbstractContext inner = inner();
inner.outer = CURRENT.get();
CURRENT.set(inner);
return inner;
}

public void exit() {
if (this != CURRENT.get()) {
throw new IllegalStateException("This context is not the current context");
}
CURRENT.set(this.outer);
this.outer = null;
}

protected AbstractContext getOuter() {
return this.outer;
}

protected abstract AbstractContext inner();
}

