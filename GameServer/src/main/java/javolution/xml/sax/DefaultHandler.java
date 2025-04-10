package javolution.xml.sax;

import javolution.text.CharArray;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DefaultHandler
implements ContentHandler, ErrorHandler
{
public void warning(SAXParseException e) throws SAXException {}

public void error(SAXParseException e) throws SAXException {}

public void fatalError(SAXParseException e) throws SAXException {
throw e;
}

public void setDocumentLocator(Locator locator) {}

public void startDocument() throws SAXException {}

public void endDocument() throws SAXException {}

public void startPrefixMapping(CharArray prefix, CharArray uri) throws SAXException {}

public void endPrefixMapping(CharArray prefix) throws SAXException {}

public void startElement(CharArray namespaceURI, CharArray localName, CharArray qName, Attributes atts) throws SAXException {}

public void endElement(CharArray namespaceURI, CharArray localName, CharArray qName) throws SAXException {}

public void characters(char[] ch, int start, int length) throws SAXException {}

public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}

public void processingInstruction(CharArray target, CharArray data) throws SAXException {}

public void skippedEntity(CharArray name) throws SAXException {}

protected final void startElement(CharArray uri, CharArray localName, CharArray qName, Attributes atts) throws SAXException {
throw new UnsupportedOperationException();
}
}

