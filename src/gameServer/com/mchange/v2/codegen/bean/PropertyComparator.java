package com.mchange.v2.codegen.bean;

class PropertyComparator
{
public int compare(Object paramObject1, Object paramObject2) {
Property property1 = (Property)paramObject1;
Property property2 = (Property)paramObject2;

return property1.getName().compareTo(property2.getName());
}
}

