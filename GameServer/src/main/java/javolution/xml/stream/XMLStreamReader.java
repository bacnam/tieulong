package javolution.xml.stream;

import javolution.text.CharArray;

public interface XMLStreamReader extends XMLStreamConstants {
    Object getProperty(String paramString) throws IllegalArgumentException;

    int next() throws XMLStreamException;

    void require(int paramInt, CharSequence paramCharSequence1, CharSequence paramCharSequence2) throws XMLStreamException;

    CharArray getElementText() throws XMLStreamException;

    int nextTag() throws XMLStreamException;

    boolean hasNext() throws XMLStreamException;

    void close() throws XMLStreamException;

    CharArray getNamespaceURI(CharSequence paramCharSequence);

    boolean isStartElement();

    boolean isEndElement();

    boolean isCharacters();

    boolean isWhiteSpace();

    CharArray getAttributeValue(CharSequence paramCharSequence1, CharSequence paramCharSequence2);

    int getAttributeCount();

    CharArray getAttributeNamespace(int paramInt);

    CharArray getAttributeLocalName(int paramInt);

    CharArray getAttributePrefix(int paramInt);

    CharArray getAttributeType(int paramInt);

    CharArray getAttributeValue(int paramInt);

    boolean isAttributeSpecified(int paramInt);

    int getNamespaceCount();

    CharArray getNamespacePrefix(int paramInt);

    CharArray getNamespaceURI(int paramInt);

    NamespaceContext getNamespaceContext();

    int getEventType();

    CharArray getText();

    char[] getTextCharacters();

    int getTextCharacters(int paramInt1, char[] paramArrayOfchar, int paramInt2, int paramInt3) throws XMLStreamException;

    int getTextStart();

    int getTextLength();

    String getEncoding();

    boolean hasText();

    Location getLocation();

    CharArray getLocalName();

    boolean hasName();

    CharArray getNamespaceURI();

    CharArray getPrefix();

    CharArray getVersion();

    boolean isStandalone();

    boolean standaloneSet();

    CharArray getCharacterEncodingScheme();

    CharArray getPITarget();

    CharArray getPIData();
}

