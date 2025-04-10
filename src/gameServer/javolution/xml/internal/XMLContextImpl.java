package javolution.xml.internal;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javolution.context.AbstractContext;
import javolution.util.FastMap;
import javolution.xml.DefaultXMLFormat;
import javolution.xml.XMLContext;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

public final class XMLContextImpl
extends XMLContext
{
private final FastMap<Class<?>, XMLFormat<?>> formats = new FastMap();

protected XMLContext inner() {
XMLContextImpl ctx = new XMLContextImpl();
ctx.formats.putAll(this.formats);
return ctx;
}

protected <T> XMLFormat<T> searchFormat(Class<? extends T> type) {
XMLFormat<T> xml = (XMLFormat)this.formats.get(type);
if (xml != null)
return xml; 
DefaultXMLFormat format = type.<DefaultXMLFormat>getAnnotation(DefaultXMLFormat.class);
if (format != null) {
Class<? extends XMLFormat> formatClass = format.value();
try {
xml = formatClass.newInstance();
synchronized (this.formats) {

this.formats.put(type, xml);
} 
return xml;
} catch (Throwable ex) {
throw new RuntimeException(ex);
} 
} 

if (Map.class.isAssignableFrom(type))
return MAP_XML; 
if (Collection.class.isAssignableFrom(type))
return COLLECTION_XML; 
return OBJECT_XML;
}

public <T> void setFormat(Class<? extends T> type, XMLFormat<T> format) {
this.formats.put(type, format);
}

private static final XMLFormat OBJECT_XML = (XMLFormat)new XMLFormat.Default();

private static final XMLFormat COLLECTION_XML = new XMLFormat()
{

public void read(XMLFormat.InputElement xml, Object obj) throws XMLStreamException
{
Collection<Object> collection = (Collection)obj;
while (xml.hasNext()) {
collection.add(xml.getNext());
}
}

public void write(Object obj, XMLFormat.OutputElement xml) throws XMLStreamException {
Collection collection = (Collection)obj;
for (Iterator i = collection.iterator(); i.hasNext();) {
xml.add(i.next());
}
}
};

private static final XMLFormat MAP_XML = new XMLFormat()
{

public void read(XMLFormat.InputElement xml, Object obj) throws XMLStreamException
{
Map<Object, Object> map = (Map)obj;
while (xml.hasNext()) {
Object key = xml.get("Key");
Object value = xml.get("Value");
map.put(key, value);
} 
}

public void write(Object obj, XMLFormat.OutputElement xml) throws XMLStreamException {
Map map = (Map)obj;
for (Iterator<Map.Entry> it = map.entrySet().iterator(); it.hasNext(); ) {
Map.Entry entry = it.next();
xml.add(entry.getKey(), "Key");
xml.add(entry.getValue(), "Value");
} 
}
};
}

