package com.mchange.v2.c3p0.cfg;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class DefaultC3P0ConfigFinder
implements C3P0ConfigFinder
{
static final String XML_CFG_FILE_KEY = "com.mchange.v2.c3p0.cfg.xml";
static final String CLASSLOADER_RESOURCE_PREFIX = "classloader:";
static final MLogger logger = MLog.getLogger(DefaultC3P0ConfigFinder.class);

final boolean warn_of_xml_overrides;

public DefaultC3P0ConfigFinder(boolean warn_of_xml_overrides) {
this.warn_of_xml_overrides = warn_of_xml_overrides;
}
public DefaultC3P0ConfigFinder() {
this(false);
}

public C3P0Config findConfig() throws Exception {
C3P0Config out;
HashMap flatDefaults = C3P0ConfigUtils.extractHardcodedC3P0Defaults();

flatDefaults.putAll(C3P0ConfigUtils.extractC3P0PropertiesResources());

String cfgFile = C3P0Config.getPropsFileConfigProperty("com.mchange.v2.c3p0.cfg.xml");
if (cfgFile == null) {

C3P0Config xmlConfig = C3P0ConfigXmlUtils.extractXmlConfigFromDefaultResource();
if (xmlConfig != null) {

insertDefaultsUnderNascentConfig(flatDefaults, xmlConfig);
out = xmlConfig;

mbOverrideWarning("resource", "/c3p0-config.xml");
} else {

out = C3P0ConfigUtils.configFromFlatDefaults(flatDefaults);
} 
} else {

cfgFile = cfgFile.trim();

InputStream is = null;

try {
if (cfgFile.startsWith("classloader:")) {

ClassLoader cl = getClass().getClassLoader();
String rsrcPath = cfgFile.substring("classloader:".length());

if (rsrcPath.startsWith("/")) {
rsrcPath = rsrcPath.substring(1);
}
is = cl.getResourceAsStream(rsrcPath);
if (is == null) {
throw new FileNotFoundException("Specified ClassLoader resource '" + rsrcPath + "' could not be found. " + "[ Found in configuration: " + "com.mchange.v2.c3p0.cfg.xml" + '=' + cfgFile + " ]");
}

mbOverrideWarning("resource", rsrcPath);
}
else {

is = new BufferedInputStream(new FileInputStream(cfgFile));
mbOverrideWarning("file", cfgFile);
} 

C3P0Config xmlConfig = C3P0ConfigXmlUtils.extractXmlConfigFromInputStream(is);
insertDefaultsUnderNascentConfig(flatDefaults, xmlConfig);
out = xmlConfig;
} finally {

try {
if (is != null) is.close(); 
} catch (Exception e) {
e.printStackTrace();
} 
} 
} 

Properties sysPropConfig = C3P0ConfigUtils.findAllC3P0SystemProperties();
out.defaultConfig.props.putAll(sysPropConfig);

return out;
}

private void insertDefaultsUnderNascentConfig(HashMap flatDefaults, C3P0Config config) {
flatDefaults.putAll(config.defaultConfig.props);
config.defaultConfig.props = flatDefaults;
}

private void mbOverrideWarning(String srcType, String srcName) {
if (this.warn_of_xml_overrides && logger.isLoggable(MLevel.WARNING))
logger.log(MLevel.WARNING, "Configuation defined in " + srcType + "'" + srcName + "' overrides all other c3p0 config."); 
}
}

