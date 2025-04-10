package com.mchange.v2.ser;

import java.io.IOException;
import java.io.Serializable;

public interface IndirectlySerialized extends Serializable {
  Object getObject() throws ClassNotFoundException, IOException;
}

