package javolution.context;

import javolution.lang.Configurable;
import javolution.lang.MathLib;
import javolution.osgi.internal.OSGiServices;

public abstract class ConcurrentContext
extends AbstractContext
{
public static final Configurable<Integer> CONCURRENCY = new Configurable<Integer>()
{
protected Integer getDefault() {
return Integer.valueOf(Runtime.getRuntime().availableProcessors() - 1);
}

protected Integer initialized(Integer value) {
return Integer.valueOf(MathLib.min(value.intValue(), 65536));
}

protected Integer reconfigured(Integer oldCount, Integer newCount) {
throw new UnsupportedOperationException("Concurrency reconfiguration not supported.");
}
};

public static ConcurrentContext enter() {
ConcurrentContext ctx = current(ConcurrentContext.class);
if (ctx == null) {
ctx = OSGiServices.getConcurrentContext();
}
return (ConcurrentContext)ctx.enterInner();
}

public static void execute(Runnable... logics) {
ConcurrentContext ctx = enter();
try {
for (Runnable logic : logics) {
ctx.execute(logic);
}
} finally {
ctx.exit();
} 
}

public abstract void execute(Runnable paramRunnable);

public abstract void setConcurrency(int paramInt);

public abstract int getConcurrency();

public void exit() {
super.exit();
}
}

