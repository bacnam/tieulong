package com.google.common.io;

import com.google.common.annotations.Beta;
import java.io.IOException;

@Beta
public interface LineProcessor<T> {
  boolean processLine(String paramString) throws IOException;
  
  T getResult();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/common/io/LineProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */