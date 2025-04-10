package com.mchange.v2.coalesce;

public final class CoalescerFactory
{
public static Coalescer createCoalescer() {
return createCoalescer(true, true);
}

public static Coalescer createCoalescer(boolean paramBoolean1, boolean paramBoolean2) {
return createCoalescer(null, paramBoolean1, paramBoolean2);
}

public static Coalescer createCoalescer(CoalesceChecker paramCoalesceChecker, boolean paramBoolean1, boolean paramBoolean2) {
Coalescer coalescer;
if (paramCoalesceChecker == null) {

coalescer = (Coalescer)(paramBoolean1 ? new WeakEqualsCoalescer() : new StrongEqualsCoalescer());

}
else {

coalescer = (Coalescer)(paramBoolean1 ? new WeakCcCoalescer(paramCoalesceChecker) : new StrongCcCoalescer(paramCoalesceChecker));
} 

return paramBoolean2 ? new SyncedCoalescer(coalescer) : coalescer;
}
}

