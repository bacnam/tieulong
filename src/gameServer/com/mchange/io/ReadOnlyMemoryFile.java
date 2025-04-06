package com.mchange.io;

import java.io.File;
import java.io.IOException;

public interface ReadOnlyMemoryFile {
  File getFile() throws IOException;
  
  byte[] getBytes() throws IOException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/ReadOnlyMemoryFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */