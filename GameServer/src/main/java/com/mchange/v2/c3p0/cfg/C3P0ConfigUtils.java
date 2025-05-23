package com.mchange.v2.c3p0.cfg;

import com.mchange.v2.c3p0.impl.C3P0Defaults;
import com.mchange.v2.lang.Coerce;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public final class C3P0ConfigUtils
{
public static final String PROPS_FILE_RSRC_PATH = "/c3p0.properties";
public static final String PROPS_FILE_PROP_PFX = "c3p0.";
public static final int PROPS_FILE_PROP_PFX_LEN = 5;
private static final String[] MISSPELL_PFXS = new String[] { "/c3pO", "/c3po", "/C3P0", "/C3PO" };

static final MLogger logger = MLog.getLogger(C3P0ConfigUtils.class);

static {
if (logger.isLoggable(MLevel.WARNING) && C3P0ConfigUtils.class.getResource("/c3p0.properties") == null)
{

for (int i = 0; i < MISSPELL_PFXS.length; i++) {

String test = MISSPELL_PFXS[i] + ".properties";
if (C3P0ConfigUtils.class.getResource(MISSPELL_PFXS[i] + ".properties") != null) {

logger.warning("POSSIBLY MISSPELLED c3p0.properties CONFIG RESOURCE FOUND. Please ensure the file name is c3p0.properties, all lower case, with the digit 0 (NOT the letter O) in c3p0. It should be placed  in the top level of c3p0's effective classpath.");
break;
} 
} 
}
}

public static HashMap extractHardcodedC3P0Defaults(boolean stringify_coercibles) {
HashMap<Object, Object> out = new HashMap<Object, Object>();

try {
Method[] methods = C3P0Defaults.class.getMethods();
for (int i = 0, len = methods.length; i < len; i++) {

Method m = methods[i];
int mods = m.getModifiers();
if ((mods & 0x1) != 0 && (mods & 0x8) != 0 && (m.getParameterTypes()).length == 0) {

Object val = m.invoke(null, null);
if (val != null) {
out.put(m.getName(), (stringify_coercibles && Coerce.canCoerce(val)) ? String.valueOf(val) : val);
}
} 
} 
} catch (Exception e) {

logger.log(MLevel.WARNING, "Failed to extract hardcoded default config!?", e);
} 

return out;
}

public static HashMap extractHardcodedC3P0Defaults() {
return extractHardcodedC3P0Defaults(true);
}

public static HashMap extractC3P0PropertiesResources() {
HashMap<Object, Object> out = new HashMap<Object, Object>();

Properties props = findAllOneLevelC3P0Properties();
for (Iterator<String> ii = props.keySet().iterator(); ii.hasNext(); ) {

String key = ii.next();
String val = (String)props.get(key);
if (key.startsWith("c3p0.")) {
out.put(key.substring(5).trim(), val.trim());
}
} 
return out;
}

public static C3P0Config configFromFlatDefaults(HashMap flatDefaults) {
NamedScope defaults = new NamedScope();
defaults.props.putAll(flatDefaults);

HashMap<Object, Object> configNamesToNamedScopes = new HashMap<Object, Object>();

return new C3P0Config(defaults, configNamesToNamedScopes);
}

public static String getPropsFileConfigProperty(String prop) {
return C3P0Config.getPropsFileConfigProperty(prop);
}
public static Properties findResourceProperties() {
return C3P0Config.findResourceProperties();
}
private static Properties findAllOneLevelC3P0Properties() {
return C3P0Config.findAllOneLevelC3P0Properties();
}

static Properties findAllC3P0SystemProperties() {
Properties out = new Properties();

try {
for (Iterator<String> ii = C3P0Defaults.getKnownProperties(null).iterator(); ii.hasNext(); ) {

String key = ii.next();
String prefixedKey = "c3p0." + key;
String value = System.getProperty(prefixedKey);
if (value != null && value.trim().length() > 0) {
out.put(key, value);
}
} 
} catch (SecurityException e) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "A SecurityException occurred while trying to read c3p0 System properties. c3p0 configuration set via System properties may be ignored!", e);
}
} 

return out;
}

public static Object extractUserOverride(String propName, String userName, Map userOverrides) {
Map specificUserOverrides = (Map)userOverrides.get(userName);
if (specificUserOverrides != null) {
return specificUserOverrides.get(propName);
}
return null;
}

public static Boolean extractBooleanOverride(String propName, String userName, Map userOverrides) {
Object check = extractUserOverride(propName, userName, userOverrides);
if (check == null || check instanceof Boolean)
return (Boolean)check; 
if (check instanceof String) {
return Boolean.valueOf((String)check);
}
throw new ClassCastException("Parameter '" + propName + "' as overridden for user '" + userName + "' is " + check + ", which cannot be converted to Boolean.");
}
}

