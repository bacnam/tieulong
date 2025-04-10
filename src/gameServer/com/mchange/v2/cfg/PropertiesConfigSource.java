package com.mchange.v2.cfg;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

public interface PropertiesConfigSource
{
Parse propertiesFromSource(String paramString) throws FileNotFoundException, Exception;

public static class Parse
{
private Properties properties;
private List<DelayedLogItem> parseMessages;

public Properties getProperties() {
return this.properties; } public List<DelayedLogItem> getDelayedLogItems() {
return this.parseMessages;
}

public Parse(Properties param1Properties, List<DelayedLogItem> param1List) {
this.properties = param1Properties;
this.parseMessages = param1List;
}
}
}

