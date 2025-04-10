package ch.qos.logback.classic.util;

import java.util.HashMap;

public class CopyOnInheritThreadLocal
extends InheritableThreadLocal<HashMap<String, String>>
{
protected HashMap<String, String> childValue(HashMap<String, String> parentValue) {
if (parentValue == null) {
return null;
}
return new HashMap<String, String>(parentValue);
}
}

