package javolution.context.internal;

import javolution.context.AbstractContext;
import javolution.context.SecurityContext;
import javolution.util.FastCollection;
import javolution.util.FastTable;

public final class SecurityContextImpl
extends SecurityContext
{
private FastTable<Action> actions = new FastTable();

public boolean isGranted(SecurityContext.Permission<?> permission) {
boolean isGranted = true;
for (Action a : this.actions) {
if (a.permission.implies(permission))
isGranted = a.grant; 
} 
return isGranted;
}

public void grant(SecurityContext.Permission<?> permission, Object certificate) throws SecurityException {
Action a = new Action();
a.grant = true;
a.permission = permission;
this.actions.add(a);
}

public void revoke(SecurityContext.Permission<?> permission, Object certificate) throws SecurityException {
Action a = new Action();
a.grant = false;
a.permission = permission;
this.actions.add(a);
}

protected SecurityContext inner() {
SecurityContextImpl ctx = new SecurityContextImpl();
ctx.actions.addAll((FastCollection)this.actions);
return ctx;
}

private static class Action {
boolean grant;
SecurityContext.Permission<?> permission;

private Action() {}
}
}

