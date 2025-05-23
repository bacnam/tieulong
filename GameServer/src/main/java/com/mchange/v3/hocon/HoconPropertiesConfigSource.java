package com.mchange.v3.hocon;

import com.mchange.v2.cfg.DelayedLogItem;
import com.mchange.v2.cfg.PropertiesConfigSource;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigMergeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class HoconPropertiesConfigSource
implements PropertiesConfigSource
{
private static Config extractConfig(ClassLoader paramClassLoader, String paramString, List<DelayedLogItem> paramList) throws FileNotFoundException, Exception {
int i = paramString.indexOf(':');

ArrayList<Config> arrayList = new ArrayList();

if (i >= 0 && "hocon".equals(paramString.substring(0, i).toLowerCase())) {

String str = paramString.substring(i + 1).trim();
String[] arrayOfString = str.split("\\s*,\\s*");

for (String str1 : arrayOfString) {
String str2, str3;

int k = str1.lastIndexOf('#');
if (k > 0) {

str2 = str1.substring(0, k);
str3 = str1.substring(k + 1).replace('/', '.').trim();
}
else {

str2 = str1;
str3 = null;
} 

Config config1 = null;

if ("/".equals(str2)) {
config1 = ConfigFactory.systemProperties();
} else {

Config config2 = null;

if ("application".equals(str2) || "/application".equals(str2)) {
String str4;

if ((str4 = System.getProperty("config.resource")) != null) {
str2 = str4;
} else if ((str4 = System.getProperty("config.file")) != null) {

File file = new File(str4);
if (file.exists()) {

if (file.canRead()) {
config2 = ConfigFactory.parseFile(file);
} else {
paramList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, String.format("Specified config.file '%s' is not readable. Falling back to standard application.(conf|json|properties).}", new Object[] { file.getAbsolutePath() })));
} 
} else {

paramList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, String.format("Specified config.file '%s' does not exist. Falling back to standard application.(conf|json|properties).}", new Object[] { file.getAbsolutePath() })));
}

}
else if ((str4 = System.getProperty("config.url")) != null) {
config2 = ConfigFactory.parseURL(new URL(str4));
} 
} 
if (config2 == null) {

if (str2.charAt(0) == '/') {
str2 = str2.substring(1);
}
boolean bool = (str2.indexOf('.') >= 0) ? true : false;

if (bool) {
config2 = ConfigFactory.parseResources(paramClassLoader, str2);
} else {
config2 = ConfigFactory.parseResourcesAnySyntax(paramClassLoader, str2);
} 
} 
if (config2.isEmpty()) {
paramList.add(new DelayedLogItem(DelayedLogItem.Level.FINE, String.format("Missing or empty HOCON configuration for resource path '%s'.", new Object[] { str2 })));
} else {
config1 = config2;
} 
} 
if (config1 != null) {

if (str3 != null) {
config1 = config1.getConfig(str3);
}
arrayList.add(config1);
} 
} 

if (arrayList.size() == 0) {
throw new FileNotFoundException(String.format("Could not find HOCON configuration at any of the listed resources in '%s'", new Object[] { paramString }));
}

Config config = ConfigFactory.empty();
for (int j = arrayList.size(); --j >= 0;)
config = config.withFallback((ConfigMergeable)arrayList.get(j)); 
return config.resolve();
} 

throw new IllegalArgumentException(String.format("Invalid resource identifier for hocon config file: '%s'", new Object[] { paramString }));
}

public PropertiesConfigSource.Parse propertiesFromSource(ClassLoader paramClassLoader, String paramString) throws FileNotFoundException, Exception {
LinkedList<DelayedLogItem> linkedList = new LinkedList();

Config config = extractConfig(paramClassLoader, paramString, linkedList);
HoconUtils.PropertiesConversion propertiesConversion = HoconUtils.configToProperties(config);

for (String str : propertiesConversion.unrenderable) {
linkedList.add(new DelayedLogItem(DelayedLogItem.Level.FINE, String.format("Value at path '%s' could not be converted to a String. Skipping.", new Object[] { str })));
} 
return new PropertiesConfigSource.Parse(propertiesConversion.properties, linkedList);
}

public PropertiesConfigSource.Parse propertiesFromSource(String paramString) throws FileNotFoundException, Exception {
return propertiesFromSource(HoconPropertiesConfigSource.class.getClassLoader(), paramString);
}
}

