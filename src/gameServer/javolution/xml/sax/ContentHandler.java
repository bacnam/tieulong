package javolution.xml.sax;

import javolution.text.CharArray;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public interface ContentHandler {
  void setDocumentLocator(Locator paramLocator);
  
  void startDocument() throws SAXException;
  
  void endDocument() throws SAXException;
  
  void startPrefixMapping(CharArray paramCharArray1, CharArray paramCharArray2) throws SAXException;
  
  void endPrefixMapping(CharArray paramCharArray) throws SAXException;
  
  void startElement(CharArray paramCharArray1, CharArray paramCharArray2, CharArray paramCharArray3, Attributes paramAttributes) throws SAXException;
  
  void endElement(CharArray paramCharArray1, CharArray paramCharArray2, CharArray paramCharArray3) throws SAXException;
  
  void characters(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws SAXException;
  
  void ignorableWhitespace(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws SAXException;
  
  void processingInstruction(CharArray paramCharArray1, CharArray paramCharArray2) throws SAXException;
  
  void skippedEntity(CharArray paramCharArray) throws SAXException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/sax/ContentHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */