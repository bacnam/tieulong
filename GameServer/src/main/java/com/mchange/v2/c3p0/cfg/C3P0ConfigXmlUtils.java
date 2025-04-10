package com.mchange.v2.c3p0.cfg;

import com.mchange.v1.xml.DomParseUtils;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.io.InputStream;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class C3P0ConfigXmlUtils
{
public static final String XML_CONFIG_RSRC_PATH = "/c3p0-config.xml";
static final MLogger logger = MLog.getLogger(C3P0ConfigXmlUtils.class);

public static final String LINESEP;

private static final String[] MISSPELL_PFXS = new String[] { "/c3p0", "/c3pO", "/c3po", "/C3P0", "/C3PO" };
private static final char[] MISSPELL_LINES = new char[] { '-', '_' };
private static final String[] MISSPELL_CONFIG = new String[] { "config", "CONFIG" };
private static final String[] MISSPELL_XML = new String[] { "xml", "XML" };

static {
String str;
}

private static final void warnCommonXmlConfigResourceMisspellings() {
if (logger.isLoggable(MLevel.WARNING))
{
for (int a = 0, lena = MISSPELL_PFXS.length; a < lena; a++) {

StringBuffer sb = new StringBuffer(16);
sb.append(MISSPELL_PFXS[a]);
for (int b = 0, lenb = MISSPELL_LINES.length; b < lenb; b++) {

sb.append(MISSPELL_LINES[b]);
for (int c = 0, lenc = MISSPELL_CONFIG.length; c < lenc; c++) {

sb.append(MISSPELL_CONFIG[c]);
sb.append('.');
for (int d = 0, lend = MISSPELL_XML.length; d < lend; d++) {

sb.append(MISSPELL_XML[d]);
String test = sb.toString();
if (!test.equals("/c3p0-config.xml")) {

Object hopefullyNull = C3P0ConfigXmlUtils.class.getResource(test);
if (hopefullyNull != null) {

logger.warning("POSSIBLY MISSPELLED c3p0-conf.xml RESOURCE FOUND. Please ensure the file name is c3p0-config.xml, all lower case, with the digit 0 (NOT the letter O) in c3p0. It should be placed  in the top level of c3p0's effective classpath.");
return;
} 
} 
} 
} 
} 
} 
}
}

static {
try {
str = System.getProperty("line.separator", "\r\n");
} catch (Exception e) {
str = "\r\n";
} 
LINESEP = str;
}

public static C3P0Config extractXmlConfigFromDefaultResource() throws Exception {
InputStream is = null;

try {
is = C3P0ConfigUtils.class.getResourceAsStream("/c3p0-config.xml");
if (is == null) {

warnCommonXmlConfigResourceMisspellings();
return null;
} 

return extractXmlConfigFromInputStream(is);
} finally {

try {
if (is != null) is.close(); 
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, "Exception on resource InputStream close.", e);
}
} 
} 
}

public static C3P0Config extractXmlConfigFromInputStream(InputStream is) throws Exception {
DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
DocumentBuilder db = fact.newDocumentBuilder();
Document doc = db.parse(is);

return extractConfigFromXmlDoc(doc);
}

public static C3P0Config extractConfigFromXmlDoc(Document doc) throws Exception {
Element docElem = doc.getDocumentElement();
if (docElem.getTagName().equals("c3p0-config")) {
NamedScope defaults;

HashMap<Object, Object> configNamesToNamedScopes = new HashMap<Object, Object>();

Element defaultConfigElem = DomParseUtils.uniqueChild(docElem, "default-config");
if (defaultConfigElem != null) {
defaults = extractNamedScopeFromLevel(defaultConfigElem);
} else {
defaults = new NamedScope();
}  NodeList nl = DomParseUtils.immediateChildElementsByTagName(docElem, "named-config");
for (int i = 0, len = nl.getLength(); i < len; i++) {

Element namedConfigElem = (Element)nl.item(i);
String configName = namedConfigElem.getAttribute("name");
if (configName != null && configName.length() > 0) {

NamedScope namedConfig = extractNamedScopeFromLevel(namedConfigElem);
configNamesToNamedScopes.put(configName, namedConfig);
} else {

logger.warning("Configuration XML contained named-config element without name attribute: " + namedConfigElem);
} 
}  return new C3P0Config(defaults, configNamesToNamedScopes);
} 

throw new Exception("Root element of c3p0 config xml should be 'c3p0-config', not '" + docElem.getTagName() + "'.");
}

private static NamedScope extractNamedScopeFromLevel(Element elem) {
HashMap props = extractPropertiesFromLevel(elem);
HashMap<Object, Object> userNamesToOverrides = new HashMap<Object, Object>();

NodeList nl = DomParseUtils.immediateChildElementsByTagName(elem, "user-overrides");
for (int i = 0, len = nl.getLength(); i < len; i++) {

Element perUserConfigElem = (Element)nl.item(i);
String userName = perUserConfigElem.getAttribute("user");
if (userName != null && userName.length() > 0) {

HashMap userProps = extractPropertiesFromLevel(perUserConfigElem);
userNamesToOverrides.put(userName, userProps);
} else {

logger.warning("Configuration XML contained user-overrides element without user attribute: " + LINESEP + perUserConfigElem);
} 
} 
HashMap extensions = extractExtensionsFromLevel(elem);

return new NamedScope(props, userNamesToOverrides, extensions);
}

private static HashMap extractExtensionsFromLevel(Element elem) {
HashMap<Object, Object> out = new HashMap<Object, Object>();
NodeList nl = DomParseUtils.immediateChildElementsByTagName(elem, "extensions");
for (int i = 0, len = nl.getLength(); i < len; i++) {

Element extensionsElem = (Element)nl.item(i);
out.putAll(extractPropertiesFromLevel(extensionsElem));
} 
return out;
}

private static HashMap extractPropertiesFromLevel(Element elem) {
HashMap<Object, Object> out = new HashMap<Object, Object>();

try {
NodeList nl = DomParseUtils.immediateChildElementsByTagName(elem, "property");
int len = nl.getLength();
for (int i = 0; i < len; i++) {

Element propertyElem = (Element)nl.item(i);
String propName = propertyElem.getAttribute("name");
if (propName != null && propName.length() > 0) {

String propVal = DomParseUtils.allTextFromElement(propertyElem, true);
out.put(propName, propVal);
}
else {

logger.warning("Configuration XML contained property element without name attribute: " + LINESEP + propertyElem);
} 
} 
} catch (Exception e) {

logger.log(MLevel.WARNING, "An exception occurred while reading config XML. Some configuration information has probably been ignored.", e);
} 

return out;
}
}

