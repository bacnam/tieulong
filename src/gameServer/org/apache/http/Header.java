package org.apache.http;

public interface Header {
  String getName();
  
  String getValue();
  
  HeaderElement[] getElements() throws ParseException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/Header.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */