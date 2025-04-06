package org.apache.mina.core.file;

import java.nio.channels.FileChannel;

public interface FileRegion {
  FileChannel getFileChannel();
  
  long getPosition();
  
  void update(long paramLong);
  
  long getRemainingBytes();
  
  long getWrittenBytes();
  
  String getFilename();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/file/FileRegion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */