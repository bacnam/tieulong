package com.mchange.io;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface StringMemoryFile extends ReadOnlyMemoryFile {
  String asString() throws IOException;
  
  String asString(String paramString) throws IOException, UnsupportedEncodingException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/StringMemoryFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */