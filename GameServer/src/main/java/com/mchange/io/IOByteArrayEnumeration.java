package com.mchange.io;

import java.io.IOException;

public interface IOByteArrayEnumeration extends IOEnumeration {
  byte[] nextBytes() throws IOException;

  boolean hasMoreBytes() throws IOException;

  Object nextElement() throws IOException;

  boolean hasMoreElements() throws IOException;
}

