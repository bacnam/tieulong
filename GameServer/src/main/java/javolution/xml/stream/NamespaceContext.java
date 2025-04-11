package javolution.xml.stream;

import javolution.text.CharArray;

import java.util.Iterator;

public interface NamespaceContext {
    CharArray getNamespaceURI(CharSequence paramCharSequence);

    CharArray getPrefix(CharSequence paramCharSequence);

    Iterator<CharArray> getPrefixes(CharSequence paramCharSequence);
}

