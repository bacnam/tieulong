package javolution.xml.stream;

import java.util.Iterator;
import javolution.text.CharArray;

public interface NamespaceContext {
  CharArray getNamespaceURI(CharSequence paramCharSequence);
  
  CharArray getPrefix(CharSequence paramCharSequence);
  
  Iterator<CharArray> getPrefixes(CharSequence paramCharSequence);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/stream/NamespaceContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */