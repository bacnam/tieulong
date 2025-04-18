package com.mchange.io;

import java.io.IOException;

public interface IOStringEnumeration extends IOEnumeration {
  boolean hasMoreStrings() throws IOException;

  String nextString() throws IOException;
}

