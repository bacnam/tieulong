package com.google.common.io;

import java.io.IOException;

public interface OutputSupplier<T> {
  T getOutput() throws IOException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/common/io/OutputSupplier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */