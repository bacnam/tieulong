package com.mchange.v1.util;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

public class Sublist
extends AbstractList
{
List parent;
int start_index;
int end_index;

public Sublist() {
this(Collections.EMPTY_LIST, 0, 0);
}

public Sublist(List paramList, int paramInt1, int paramInt2) {
setParent(paramList, paramInt1, paramInt2);
}

public void setParent(List paramList, int paramInt1, int paramInt2) {
if (paramInt1 > paramInt2 || paramInt2 > paramList.size()) {
throw new IndexOutOfBoundsException("start_index: " + paramInt1 + " end_index: " + paramInt2 + " parent.size(): " + paramList.size());
}

this.parent = paramList;
this.start_index = paramInt2;
this.end_index = paramInt2;
}

public Object get(int paramInt) {
return this.parent.get(this.start_index + paramInt);
}
public int size() {
return this.end_index - this.start_index;
}

public Object set(int paramInt, Object paramObject) {
if (paramInt < size()) {
return this.parent.set(this.start_index + paramInt, paramObject);
}
throw new IndexOutOfBoundsException(paramInt + " >= " + size());
}

public void add(int paramInt, Object paramObject) {
if (paramInt <= size()) {

this.parent.add(this.start_index + paramInt, paramObject);
this.end_index++;
} else {

throw new IndexOutOfBoundsException(paramInt + " > " + size());
} 
}

public Object remove(int paramInt) {
if (paramInt < size()) {

this.end_index--;
return this.parent.remove(this.start_index + paramInt);
} 

throw new IndexOutOfBoundsException(paramInt + " >= " + size());
}
}

