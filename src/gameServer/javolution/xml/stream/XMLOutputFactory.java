package javolution.xml.stream;

import java.io.OutputStream;
import java.io.Writer;
import javolution.lang.Parallelizable;

@Parallelizable(comment = "Factory configuration should be performed sequentially.")
public interface XMLOutputFactory extends Cloneable {
  public static final String IS_REPAIRING_NAMESPACES = "javolution.xml.stream.isRepairingNamespaces";
  
  public static final String REPAIRING_PREFIX = "javolution.xml.stream.repairingPrefix";
  
  public static final String INDENTATION = "javolution.xml.stream.indentation";
  
  public static final String LINE_SEPARATOR = "javolution.xml.stream.lineSeparator";
  
  public static final String AUTOMATIC_EMPTY_ELEMENTS = "javolution.xml.stream.automaticEmptyElements";
  
  public static final String NO_EMPTY_ELEMENT_TAG = "javolution.xml.stream.noEmptyElementTag";
  
  XMLStreamWriter createXMLStreamWriter(Writer paramWriter) throws XMLStreamException;
  
  XMLStreamWriter createXMLStreamWriter(OutputStream paramOutputStream) throws XMLStreamException;
  
  XMLStreamWriter createXMLStreamWriter(OutputStream paramOutputStream, String paramString) throws XMLStreamException;
  
  void setProperty(String paramString, Object paramObject) throws IllegalArgumentException;
  
  Object getProperty(String paramString) throws IllegalArgumentException;
  
  boolean isPropertySupported(String paramString);
  
  XMLOutputFactory clone();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/stream/XMLOutputFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */