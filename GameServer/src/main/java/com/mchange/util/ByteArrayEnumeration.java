package com.mchange.util;

import com.mchange.io.IOByteArrayEnumeration;

public interface ByteArrayEnumeration extends MEnumeration, IOByteArrayEnumeration {
  byte[] nextBytes();

  boolean hasMoreBytes();
}

