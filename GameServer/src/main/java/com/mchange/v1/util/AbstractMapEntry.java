package com.mchange.v1.util;

import com.mchange.v2.lang.ObjectUtils;
import java.util.Map;

public abstract class AbstractMapEntry
implements Map.Entry
{
public abstract Object getKey();

public abstract Object getValue();

public abstract Object setValue(Object paramObject);

public boolean equals(Object paramObject) {
if (paramObject instanceof Map.Entry) {

Map.Entry entry = (Map.Entry)paramObject;
return (ObjectUtils.eqOrBothNull(getKey(), entry.getKey()) && ObjectUtils.eqOrBothNull(getValue(), entry.getValue()));
} 

return false;
}

public int hashCode() {
return ((getKey() == null) ? 0 : getKey().hashCode()) ^ ((getValue() == null) ? 0 : getValue().hashCode());
}
}

