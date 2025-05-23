package com.mchange.v2.c3p0.cfg;

import com.mchange.v1.lang.BooleanUtils;
import com.mchange.v2.beans.BeansUtils;
import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.impl.C3P0Defaults;
import com.mchange.v2.c3p0.impl.C3P0ImplUtils;
import com.mchange.v2.cfg.MConfig;
import com.mchange.v2.cfg.MultiPropertiesConfig;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public final class C3P0Config
{
static final String PROP_STYLE_NAMED_CFG_PFX = "c3p0.named-configs";
static final int PROP_STYLE_NAMED_CFG_PFX_LEN = "c3p0.named-configs".length();
static final String PROP_STYLE_USER_OVERRIDES_PART = "user-overrides";
static final String PROP_STYLE_USER_OVERRIDES_PFX = "c3p0.user-overrides";
static final int PROP_STYLE_USER_OVERRIDES_PFX_LEN = "c3p0.user-overrides".length();
static final String PROP_STYLE_EXTENSIONS_PART = "extensions";
static final String PROP_STYLE_EXTENSIONS_PFX = "c3p0.extensions";
static final int PROP_STYLE_EXTENSIONS_PFX_LEN = "c3p0.extensions".length();

public static final String CFG_FINDER_CLASSNAME_KEY = "com.mchange.v2.c3p0.cfg.finder";

public static final String DEFAULT_CONFIG_NAME = "default";

public static final String PROPS_FILE_RSRC_PATH = "/c3p0.properties";

private static synchronized MultiPropertiesConfig MPCONFIG() {
return _MPCONFIG;
}
private static synchronized C3P0Config MAIN() {
return _MAIN;
}
private static synchronized void setLibraryMultiPropertiesConfig(MultiPropertiesConfig mpc) {
_MPCONFIG = mpc;
}
public static Properties allCurrentProperties() {
return MPCONFIG().getPropertiesByPrefix("");
}
public static synchronized void setMainConfig(C3P0Config protoMain) {
_MAIN = protoMain;
}
public static synchronized void refreshMainConfig() {
refreshMainConfig(null, null);
}

public static synchronized void refreshMainConfig(MultiPropertiesConfig[] overrides, String overridesDescription) {
MultiPropertiesConfig libMpc = findLibraryMultiPropertiesConfig();
if (overrides != null) {

int olen = overrides.length;
MultiPropertiesConfig[] combineMe = new MultiPropertiesConfig[olen + 1];
combineMe[0] = libMpc;
for (int i = 0; i < olen; i++) {
combineMe[i + 1] = overrides[i];
}
MultiPropertiesConfig overriddenMpc = MConfig.combine(combineMe);
setLibraryMultiPropertiesConfig(overriddenMpc);
setMainConfig(findLibraryC3P0Config(true));

if (logger.isLoggable(MLevel.INFO)) {
logger.log(MLevel.INFO, "c3p0 main configuration was refreshed, with overrides specified" + ((overridesDescription == null) ? "." : (" - " + overridesDescription)));
}
}
else {

setLibraryMultiPropertiesConfig(libMpc);
setMainConfig(findLibraryC3P0Config(false));

if (logger.isLoggable(MLevel.INFO)) {
logger.log(MLevel.INFO, "c3p0 main configuration was refreshed, with no overrides specified (and any previous overrides removed).");
}
} 

C3P0Registry.markConfigRefreshed();
}

static final MLogger logger = MLog.getLogger(C3P0Config.class);
private static MultiPropertiesConfig _MPCONFIG;
private static C3P0Config _MAIN;

static {
setLibraryMultiPropertiesConfig(findLibraryMultiPropertiesConfig());
setMainConfig(findLibraryC3P0Config(false));

warnOnUnknownProperties(MAIN());
}

private static MultiPropertiesConfig findLibraryMultiPropertiesConfig() {
String[] defaults = { "/mchange-commons.properties", "/mchange-log.properties" };
String[] preempts = { "hocon:/reference,/application,/c3p0,/", "/c3p0.properties", "/" };

return MConfig.readVmConfig(defaults, preempts);
}

private static C3P0Config findLibraryC3P0Config(boolean warn_on_conflicting_overrides) {
C3P0Config c3P0Config;
String cname = MPCONFIG().getProperty("com.mchange.v2.c3p0.cfg.finder");

C3P0ConfigFinder cfgFinder = null;

try {
if (cname != null) {
cfgFinder = (C3P0ConfigFinder)Class.forName(cname).newInstance();
}
}
catch (Exception e) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "Could not load specified C3P0ConfigFinder class'" + cname + "'.", e);
}
} 

try {
if (cfgFinder == null) {

Class.forName("org.w3c.dom.Node");
Class.forName("com.mchange.v2.c3p0.cfg.C3P0ConfigXmlUtils");
cfgFinder = new DefaultC3P0ConfigFinder(warn_on_conflicting_overrides);
} 
c3P0Config = cfgFinder.findConfig();
}
catch (Exception e) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "XML configuration disabled! Verify that standard XML libs are available.", e);
}
HashMap flatDefaults = C3P0ConfigUtils.extractHardcodedC3P0Defaults();
flatDefaults.putAll(C3P0ConfigUtils.extractC3P0PropertiesResources());
c3P0Config = C3P0ConfigUtils.configFromFlatDefaults(flatDefaults);
} 

HashMap propStyleConfigNamesToNamedScopes = findPropStyleNamedScopes();
HashMap cfgFoundConfigNamesToNamedScopes = c3P0Config.configNamesToNamedScopes;
HashMap<Object, Object> mergedConfigNamesToNamedScopes = new HashMap<Object, Object>();

HashSet allConfigNames = new HashSet(cfgFoundConfigNamesToNamedScopes.keySet());
allConfigNames.addAll(propStyleConfigNamesToNamedScopes.keySet());

for (Iterator<String> ii = allConfigNames.iterator(); ii.hasNext(); ) {

String cfgName = ii.next();
NamedScope cfgFound = (NamedScope)cfgFoundConfigNamesToNamedScopes.get(cfgName);
NamedScope propStyle = (NamedScope)propStyleConfigNamesToNamedScopes.get(cfgName);
if (cfgFound != null && propStyle != null) {
mergedConfigNamesToNamedScopes.put(cfgName, cfgFound.mergedOver(propStyle)); continue;
}  if (cfgFound != null && propStyle == null) {
mergedConfigNamesToNamedScopes.put(cfgName, cfgFound); continue;
}  if (cfgFound == null && propStyle != null) {
mergedConfigNamesToNamedScopes.put(cfgName, propStyle); continue;
} 
throw new AssertionError("Huh? allConfigNames is the union, every name should be in one of the two maps.");
} 

HashMap propStyleUserOverridesDefaultConfig = findPropStyleUserOverridesDefaultConfig();
HashMap propStyleExtensionsDefaultConfig = findPropStyleExtensionsDefaultConfig();
NamedScope mergedDefaultConfig = new NamedScope(c3P0Config.defaultConfig.props, NamedScope.mergeUserNamesToOverrides(c3P0Config.defaultConfig.userNamesToOverrides, propStyleUserOverridesDefaultConfig), NamedScope.mergeExtensions(c3P0Config.defaultConfig.extensions, propStyleExtensionsDefaultConfig));

return new C3P0Config(mergedDefaultConfig, mergedConfigNamesToNamedScopes);
}

private static void warnOnUnknownProperties(C3P0Config cfg) {
warnOnUnknownProperties(cfg.defaultConfig);
for (Iterator<NamedScope> ii = cfg.configNamesToNamedScopes.values().iterator(); ii.hasNext();) {
warnOnUnknownProperties(ii.next());
}
}

private static void warnOnUnknownProperties(NamedScope scope) {
warnOnUnknownProperties(scope.props);
for (Iterator<Map> ii = scope.userNamesToOverrides.values().iterator(); ii.hasNext();) {
warnOnUnknownProperties(ii.next());
}
}

private static void warnOnUnknownProperties(Map propMap) {
for (Iterator<String> ii = propMap.keySet().iterator(); ii.hasNext(); ) {

String prop = ii.next();
if (!C3P0Defaults.isKnownProperty(prop) && logger.isLoggable(MLevel.WARNING))
logger.log(MLevel.WARNING, "Unknown c3p0-config property: " + prop); 
} 
}

public static String getPropsFileConfigProperty(String prop) {
return MPCONFIG().getProperty(prop);
}
static Properties findResourceProperties() {
return MPCONFIG().getPropertiesByResourcePath("/c3p0.properties");
}

static Properties findAllOneLevelC3P0Properties() {
Properties out = MPCONFIG().getPropertiesByPrefix("c3p0");
for (Iterator ii = out.keySet().iterator(); ii.hasNext();) {
if (((String)ii.next()).lastIndexOf('.') > 4) ii.remove(); 
}  return out;
}

static HashMap findPropStyleUserOverridesDefaultConfig() {
HashMap<Object, Object> userNamesToOverrides = new HashMap<Object, Object>();

Properties props = MPCONFIG().getPropertiesByPrefix("c3p0.user-overrides");
for (Iterator<String> ii = props.keySet().iterator(); ii.hasNext(); ) {

String fullKey = ii.next();
String userProp = fullKey.substring(PROP_STYLE_USER_OVERRIDES_PFX_LEN + 1);
int dot_index = userProp.indexOf('.');
if (dot_index < 0) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "Bad specification of user-override property '" + fullKey + "', propfile key should look like '" + "c3p0.user-overrides" + ".<user>.<property>'. Ignoring.");
}

continue;
} 

String user = userProp.substring(0, dot_index);
String propName = userProp.substring(dot_index + 1);

HashMap<Object, Object> userOverridesMap = (HashMap)userNamesToOverrides.get(user);
if (userOverridesMap == null) {

userOverridesMap = new HashMap<Object, Object>();
userNamesToOverrides.put(user, userOverridesMap);
} 
userOverridesMap.put(propName, props.get(fullKey));
} 

return userNamesToOverrides;
}

static HashMap findPropStyleExtensionsDefaultConfig() {
HashMap<Object, Object> extensions = new HashMap<Object, Object>();

Properties props = MPCONFIG().getPropertiesByPrefix("c3p0.extensions");
for (Iterator<String> ii = props.keySet().iterator(); ii.hasNext(); ) {

String fullKey = ii.next();
String extensionsKey = fullKey.substring(PROP_STYLE_EXTENSIONS_PFX_LEN + 1);
extensions.put(extensionsKey, props.get(fullKey));
} 

return extensions;
}

static HashMap findPropStyleNamedScopes() {
HashMap<Object, Object> namesToNamedScopes = new HashMap<Object, Object>();

Properties props = MPCONFIG().getPropertiesByPrefix("c3p0.named-configs");
for (Iterator<String> ii = props.keySet().iterator(); ii.hasNext(); ) {

String fullKey = ii.next();
String nameProp = fullKey.substring(PROP_STYLE_NAMED_CFG_PFX_LEN + 1);
int dot_index = nameProp.indexOf('.');
if (dot_index < 0) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "Bad specification of named config property '" + fullKey + "', propfile key should look like '" + "c3p0.named-configs" + ".<cfgname>.<property>' or '" + "c3p0.named-configs" + ".<cfgname>.user-overrides.<user>.<property>'. Ignoring.");
}

continue;
} 

String configName = nameProp.substring(0, dot_index);
String propName = nameProp.substring(dot_index + 1);

NamedScope scope = (NamedScope)namesToNamedScopes.get(configName);
if (scope == null) {

scope = new NamedScope();
namesToNamedScopes.put(configName, scope);
} 

int second_dot_index = propName.indexOf('.');

if (second_dot_index >= 0) {

if (propName.startsWith("user-overrides")) {

int third_dot_index = propName.substring(second_dot_index + 1).indexOf('.');
if (third_dot_index < 0)
{
if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "Misformatted user-override property; missing user or property name: " + propName);
}
}
String user = propName.substring(second_dot_index + 1, third_dot_index);
String userPropName = propName.substring(third_dot_index + 1);

HashMap<Object, Object> userOverridesMap = (HashMap)scope.userNamesToOverrides.get(user);
if (userOverridesMap == null) {

userOverridesMap = new HashMap<Object, Object>();
scope.userNamesToOverrides.put(user, userOverridesMap);
} 
userOverridesMap.put(userPropName, props.get(fullKey)); continue;
} 
if (propName.startsWith("extensions")) {

String extensionsKey = propName.substring(second_dot_index + 1);
scope.extensions.put(extensionsKey, props.get(fullKey));

continue;
} 
if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "Unexpected compound property, ignored: " + propName);
}
continue;
} 
scope.props.put(propName, props.get(fullKey));
} 

return namesToNamedScopes;
}

public static String getUnspecifiedUserProperty(String propKey, String configName) {
String out = null;

if (configName == null) {
out = (String)(MAIN()).defaultConfig.props.get(propKey);
} else {

NamedScope named = (NamedScope)(MAIN()).configNamesToNamedScopes.get(configName);
if (named != null) {
out = (String)named.props.get(propKey);
} else {
logger.warning("named-config with name '" + configName + "' does not exist. Using default-config for property '" + propKey + "'.");
} 
if (out == null) {
out = (String)(MAIN()).defaultConfig.props.get(propKey);
}
} 
return out;
}

public static Map getExtensions(String configName) {
HashMap<?, ?> raw = (MAIN()).defaultConfig.extensions;
if (configName != null) {

NamedScope named = (NamedScope)(MAIN()).configNamesToNamedScopes.get(configName);
if (named != null) {
raw = named.extensions;
} else {
logger.warning("named-config with name '" + configName + "' does not exist. Using default-config extensions.");
} 
}  return Collections.unmodifiableMap(raw);
}

public static Map getUnspecifiedUserProperties(String configName) {
Map<Object, Object> out = new HashMap<Object, Object>();

out.putAll((MAIN()).defaultConfig.props);

if (configName != null) {

NamedScope named = (NamedScope)(MAIN()).configNamesToNamedScopes.get(configName);
if (named != null) {
out.putAll(named.props);
} else {
logger.warning("named-config with name '" + configName + "' does not exist. Using default-config.");
} 
} 
return out;
}

public static Map getUserOverrides(String configName) {
Map<Object, Object> out = new HashMap<Object, Object>();

NamedScope namedConfigScope = null;

if (configName != null) {
namedConfigScope = (NamedScope)(MAIN()).configNamesToNamedScopes.get(configName);
}
out.putAll((MAIN()).defaultConfig.userNamesToOverrides);

if (namedConfigScope != null) {
out.putAll(namedConfigScope.userNamesToOverrides);
}
return out.isEmpty() ? null : out;
}

public static String getUserOverridesAsString(String configName) throws IOException {
Map userOverrides = getUserOverrides(configName);
if (userOverrides == null) {
return null;
}
return C3P0ImplUtils.createUserOverridesAsString(userOverrides).intern();
}

static final Class[] SUOAS_ARGS = new Class[] { String.class };

static final Collection SKIP_BIND_PROPS = Arrays.asList(new String[] { "loginTimeout", "properties" }); NamedScope defaultConfig;
HashMap configNamesToNamedScopes;

public static void bindUserOverridesAsString(Object bean, String uoas) throws Exception {
Method m = bean.getClass().getMethod("setUserOverridesAsString", SUOAS_ARGS);
m.invoke(bean, new Object[] { uoas });
}

public static void bindUserOverridesToBean(Object bean, String configName) throws Exception {
bindUserOverridesAsString(bean, getUserOverridesAsString(configName));
}

public static void bindNamedConfigToBean(Object bean, String configName, boolean shouldBindUserOverridesAsString) throws IntrospectionException {
Map<?, ?> defaultUserProps = getUnspecifiedUserProperties(configName);
Map extensions = getExtensions(configName);
Map<Object, Object> union = new HashMap<Object, Object>();
union.putAll(defaultUserProps);
union.put("extensions", extensions);
BeansUtils.overwriteAccessiblePropertiesFromMap(union, bean, false, SKIP_BIND_PROPS, true, MLevel.FINEST, MLevel.WARNING, false);

try {
if (shouldBindUserOverridesAsString) {
bindUserOverridesToBean(bean, configName);
}
} catch (NoSuchMethodException e) {

e.printStackTrace();

}
catch (Exception e) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "An exception occurred while trying to bind user overrides for named config '" + configName + "'. Only default user configs " + "will be used.", e);
}
} 
}

public static String initializeUserOverridesAsString() {
try {
return getUserOverridesAsString(null);
} catch (Exception e) {

if (logger.isLoggable(MLevel.WARNING))
logger.log(MLevel.WARNING, "Error initializing default user overrides. User overrides may be ignored.", e); 
return null;
} 
}

public static Map initializeExtensions() {
return getExtensions(null);
}

public static String initializeStringPropertyVar(String propKey, String dflt) {
String out = getUnspecifiedUserProperty(propKey, null);
if (out == null) out = dflt; 
return out;
}

public static int initializeIntPropertyVar(String propKey, int dflt) {
boolean set = false;
int out = -1;

String outStr = getUnspecifiedUserProperty(propKey, null);
if (outStr != null) {

try {

out = Integer.parseInt(outStr.trim());
set = true;
}
catch (NumberFormatException e) {

logger.info("'" + outStr + "' is not a legal value for property '" + propKey + "'. Using default value: " + dflt);
} 
}

if (!set) {
out = dflt;
}

return out;
}

public static boolean initializeBooleanPropertyVar(String propKey, boolean dflt) {
boolean set = false;
boolean out = false;

String outStr = getUnspecifiedUserProperty(propKey, null);
if (outStr != null) {

try {

out = BooleanUtils.parseBoolean(outStr.trim());
set = true;
}
catch (IllegalArgumentException e) {

logger.info("'" + outStr + "' is not a legal value for property '" + propKey + "'. Using default value: " + dflt);
} 
}

if (!set) {
out = dflt;
}
return out;
}

public static MultiPropertiesConfig getMultiPropertiesConfig() {
return MPCONFIG();
}

C3P0Config(NamedScope defaultConfig, HashMap configNamesToNamedScopes) {
this.defaultConfig = defaultConfig;
this.configNamesToNamedScopes = configNamesToNamedScopes;
}
}

