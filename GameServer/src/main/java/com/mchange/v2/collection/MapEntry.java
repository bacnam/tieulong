package com.mchange.v2.collection;

import com.mchange.v2.lang.ObjectUtils;
import java.util.Map;

public class MapEntry
implements Map.Entry
{
Object key;
Object value;

public MapEntry(Object paramObject1, Object paramObject2) {
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
throw new UnsupportedOperationException();
}

public boolean equals(Object paramObject) {
if (paramObject instanceof Map.Entry) {

Map.Entry entry = (Map.Entry)paramObject;
return (ObjectUtils.eqOrBothNull(this.key, entry.getKey()) && ObjectUtils.eqOrBothNull(this.value, entry.getValue()));
} 

return false;
}

public int hashCode() {
return ObjectUtils.hashOrZero(this.key) ^ ObjectUtils.hashOrZero(this.value);
}
}

