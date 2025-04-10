package javolution.context.internal;

import javolution.context.AbstractContext;
import javolution.context.LogContext;
import javolution.osgi.internal.OSGiServices;
import javolution.text.TextBuilder;
import org.osgi.service.log.LogService;

public final class LogContextImpl
extends LogContext
{
private static final Object[] NONE = new Object[0];
private static final int[] TO_OSGI_LEVEL = new int[] { 4, 3, 2, 1 };

private LogContext.Level level;

private Object[] prefix = NONE;
private Object[] suffix = NONE;

public void prefix(Object... pfx) {
Object[] tmp = new Object[this.prefix.length + pfx.length];
System.arraycopy(pfx, 0, tmp, 0, pfx.length);
System.arraycopy(this.prefix, 0, tmp, pfx.length, this.prefix.length);
this.prefix = tmp;
}

public void setLevel(LogContext.Level level) {
this.level = level;
}

public void suffix(Object... sfx) {
Object[] tmp = new Object[this.suffix.length + sfx.length];
System.arraycopy(this.suffix, 0, tmp, 0, this.suffix.length);
System.arraycopy(sfx, 0, tmp, this.suffix.length, sfx.length);
this.suffix = tmp;
}

protected LogContext inner() {
LogContextImpl ctx = new LogContextImpl();
ctx.prefix = this.prefix;
ctx.suffix = this.suffix;
ctx.level = this.level;
return ctx;
}

protected void log(LogContext.Level level, Object... message) {
if (level.compareTo((Enum)currentLevel()) < 0)
return; 
TextBuilder tmp = new TextBuilder();
Throwable exception = null;
for (Object pfx : this.prefix) {
tmp.append(pfx);
}
for (Object obj : message) {
if (exception == null && obj instanceof Throwable) {
exception = (Throwable)obj;
} else {
tmp.append(obj);
} 
} 
for (Object sfx : this.suffix) {
tmp.append(sfx);
}
int osgiLevel = TO_OSGI_LEVEL[level.ordinal()];
String msg = tmp.toString();
Object[] logServices = OSGiServices.getLogServices();
for (Object logService : logServices)
((LogService)logService).log(osgiLevel, msg, exception); 
}

private LogContext.Level currentLevel() {
if (LEVEL == null) return LogContext.Level.DEBUG; 
if (this.level == null) return (LogContext.Level)LEVEL.get(); 
return this.level;
}
}

