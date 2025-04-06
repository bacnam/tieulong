package com.mchange.io;

import com.mchange.util.ByteArrayBinding;
import com.mchange.util.ByteArrayComparator;
import java.io.IOException;

public interface IOSequentialByteArrayMap extends IOByteArrayMap {
  ByteArrayComparator getByteArrayComparator();
  
  Cursor getCursor();
  
  public static interface Cursor {
    ByteArrayBinding getFirst() throws IOException;
    
    ByteArrayBinding getNext() throws IOException;
    
    ByteArrayBinding getPrevious() throws IOException;
    
    ByteArrayBinding getLast() throws IOException;
    
    ByteArrayBinding getCurrent() throws IOException;
    
    ByteArrayBinding find(byte[] param1ArrayOfbyte) throws IOException;
    
    ByteArrayBinding findGreaterThanOrEqual(byte[] param1ArrayOfbyte) throws IOException;
    
    ByteArrayBinding findLessThanOrEqual(byte[] param1ArrayOfbyte) throws IOException;
    
    void deleteCurrent() throws IOException;
    
    void replaceCurrent(byte[] param1ArrayOfbyte) throws IOException;
  }
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/IOSequentialByteArrayMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */