package com.mchange.util.impl;

import com.mchange.util.IntChecklist;
import com.mchange.util.IntEnumeration;

public class HashIntChecklist
implements IntChecklist
{
private static final Object DUMMY = new Object();

IntObjectHash ioh = new IntObjectHash();

public void check(int paramInt) {
this.ioh.put(paramInt, DUMMY);
}
public void uncheck(int paramInt) {
this.ioh.remove(paramInt);
}
public boolean isChecked(int paramInt) {
return this.ioh.containsInt(paramInt);
}
public void clear() {
this.ioh.clear();
}
public int countChecked() {
return this.ioh.getSize();
}

public int[] getChecked() {
synchronized (this.ioh) {

int[] arrayOfInt = new int[this.ioh.getSize()];
IntEnumeration intEnumeration = this.ioh.ints();
for (byte b = 0; intEnumeration.hasMoreInts(); ) { arrayOfInt[b] = intEnumeration.nextInt(); b++; }
return arrayOfInt;
} 
}

public IntEnumeration checked() {
return this.ioh.ints();
}
}

