package com.mchange.v2.ser;

import java.io.IOException;
import java.io.Serializable;

public interface IndirectlySerialized extends Serializable {
  Object getObject() throws ClassNotFoundException, IOException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/ser/IndirectlySerialized.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */