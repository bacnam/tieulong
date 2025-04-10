package javolution.xml.stream;

import java.util.Iterator;
import javolution.text.CharArray;

public interface NamespaceContext {
  CharArray getNamespaceURI(CharSequence paramCharSequence);

  CharArray getPrefix(CharSequence paramCharSequence);

  Iterator<CharArray> getPrefixes(CharSequence paramCharSequence);
}

