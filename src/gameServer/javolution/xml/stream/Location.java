package javolution.xml.stream;

public interface Location {
  int getLineNumber();
  
  int getColumnNumber();
  
  int getCharacterOffset();
  
  String getPublicId();
  
  String getSystemId();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/stream/Location.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */