package com.mchange.v1.util;

import java.util.Map;

public class SimpleMapEntry
extends AbstractMapEntry
implements Map.Entry
{
Object key;
Object value;

public SimpleMapEntry(Object paramObject1, Object paramObject2) {
this.key = paramObject1;
this.value = paramObject2;
}

public Object getKey() {
return this.key;
}
public Object getValue() {
return this.value;
}

public Object setValue(Object paramObject) {
Object object = paramObject;
this.value = paramObject;
return object;
}
}

