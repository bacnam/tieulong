package com.mchange.v2.c3p0.impl;

import java.util.Enumeration;
import java.util.Properties;

public class AuthMaskingProperties
extends Properties
{
public AuthMaskingProperties() {}

public AuthMaskingProperties(Properties p) {
super(p);
}

public static AuthMaskingProperties fromAnyProperties(Properties p) {
AuthMaskingProperties out = new AuthMaskingProperties();
for (Enumeration<?> e = p.propertyNames(); e.hasMoreElements(); ) {

String key = (String)e.nextElement();
out.setProperty(key, p.getProperty(key));
} 
return out;
}

private String normalToString() {
return super.toString();
}

public String toString() {
boolean hasUser = (get("user") != null);
boolean hasPassword = (get("password") != null);
if (hasUser || hasPassword) {

AuthMaskingProperties clone = (AuthMaskingProperties)clone();
if (hasUser)
clone.put("user", "******"); 
if (hasPassword)
clone.put("password", "******"); 
return clone.normalToString();
} 

return normalToString();
}
}

