package javolution.xml.stream;

import java.io.InputStream;
import java.io.Reader;
import javolution.lang.Parallelizable;

@Parallelizable(comment = "Factory configuration should be performed sequentially.")
public interface XMLInputFactory extends Cloneable {
  public static final String IS_COALESCING = "javolution.xml.stream.isCoalescing";

  public static final String IS_VALIDATING = "javolution.xml.stream.isValidating";

  public static final String ENTITIES = "javolution.xml.stream.entities";

  XMLStreamReader createXMLStreamReader(Reader paramReader) throws XMLStreamException;

  XMLStreamReader createXMLStreamReader(InputStream paramInputStream) throws XMLStreamException;

  XMLStreamReader createXMLStreamReader(InputStream paramInputStream, String paramString) throws XMLStreamException;

  void setProperty(String paramString, Object paramObject) throws IllegalArgumentException;

  Object getProperty(String paramString) throws IllegalArgumentException;

  boolean isPropertySupported(String paramString);

  XMLInputFactory clone();
}

