package javolution.xml.stream;

public interface XMLStreamWriter {
  void writeStartElement(CharSequence paramCharSequence) throws XMLStreamException;

  void writeStartElement(CharSequence paramCharSequence1, CharSequence paramCharSequence2) throws XMLStreamException;

  void writeStartElement(CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3) throws XMLStreamException;

  void writeEmptyElement(CharSequence paramCharSequence1, CharSequence paramCharSequence2) throws XMLStreamException;

  void writeEmptyElement(CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3) throws XMLStreamException;

  void writeEmptyElement(CharSequence paramCharSequence) throws XMLStreamException;

  void writeEndElement() throws XMLStreamException;

  void writeEndDocument() throws XMLStreamException;

  void close() throws XMLStreamException;

  void flush() throws XMLStreamException;

  void writeAttribute(CharSequence paramCharSequence1, CharSequence paramCharSequence2) throws XMLStreamException;

  void writeAttribute(CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, CharSequence paramCharSequence4) throws XMLStreamException;

  void writeAttribute(CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3) throws XMLStreamException;

  void writeNamespace(CharSequence paramCharSequence1, CharSequence paramCharSequence2) throws XMLStreamException;

  void writeDefaultNamespace(CharSequence paramCharSequence) throws XMLStreamException;

  void writeComment(CharSequence paramCharSequence) throws XMLStreamException;

  void writeProcessingInstruction(CharSequence paramCharSequence) throws XMLStreamException;

  void writeProcessingInstruction(CharSequence paramCharSequence1, CharSequence paramCharSequence2) throws XMLStreamException;

  void writeCData(CharSequence paramCharSequence) throws XMLStreamException;

  void writeDTD(CharSequence paramCharSequence) throws XMLStreamException;

  void writeEntityRef(CharSequence paramCharSequence) throws XMLStreamException;

  void writeStartDocument() throws XMLStreamException;

  void writeStartDocument(CharSequence paramCharSequence) throws XMLStreamException;

  void writeStartDocument(CharSequence paramCharSequence1, CharSequence paramCharSequence2) throws XMLStreamException;

  void writeCharacters(CharSequence paramCharSequence) throws XMLStreamException;

  void writeCharacters(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws XMLStreamException;

  CharSequence getPrefix(CharSequence paramCharSequence) throws XMLStreamException;

  void setPrefix(CharSequence paramCharSequence1, CharSequence paramCharSequence2) throws XMLStreamException;

  void setDefaultNamespace(CharSequence paramCharSequence) throws XMLStreamException;

  Object getProperty(String paramString) throws IllegalArgumentException;
}

