package com.mchange.util;

import com.mchange.io.IOStringEnumeration;

public interface StringEnumeration extends MEnumeration, IOStringEnumeration {
  boolean hasMoreStrings();

  String nextString();
}

