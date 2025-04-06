package javolution.xml.sax;

import javolution.text.CharArray;

public interface Attributes {
  int getLength();
  
  CharArray getURI(int paramInt);
  
  CharArray getLocalName(int paramInt);
  
  CharArray getQName(int paramInt);
  
  CharArray getType(int paramInt);
  
  CharArray getValue(int paramInt);
  
  int getIndex(CharSequence paramCharSequence1, CharSequence paramCharSequence2);
  
  int getIndex(CharSequence paramCharSequence);
  
  CharArray getType(CharSequence paramCharSequence1, CharSequence paramCharSequence2);
  
  CharArray getType(CharSequence paramCharSequence);
  
  CharArray getValue(CharSequence paramCharSequence1, CharSequence paramCharSequence2);
  
  CharArray getValue(CharSequence paramCharSequence);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/sax/Attributes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */