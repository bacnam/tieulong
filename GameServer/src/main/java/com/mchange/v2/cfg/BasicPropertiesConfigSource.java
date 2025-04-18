package com.mchange.v2.cfg;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Properties;

public final class BasicPropertiesConfigSource
implements PropertiesConfigSource
{
public PropertiesConfigSource.Parse propertiesFromSource(String paramString) throws FileNotFoundException, Exception {
InputStream inputStream = MultiPropertiesConfig.class.getResourceAsStream(paramString);
if (inputStream != null) {

BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
Properties properties = new Properties();
LinkedList<DelayedLogItem> linkedList = new LinkedList();
try {
properties.load(bufferedInputStream);
} finally {
try {
if (bufferedInputStream != null) bufferedInputStream.close(); 
} catch (IOException iOException) {
linkedList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, "An IOException occurred while closing InputStream from resource path '" + paramString + "'.", iOException));
} 
}  return new PropertiesConfigSource.Parse(properties, linkedList);
} 

throw new FileNotFoundException(String.format("Resource not found at path '%s'.", new Object[] { paramString }));
}
}

