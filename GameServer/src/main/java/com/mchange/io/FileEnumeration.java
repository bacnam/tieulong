package com.mchange.io;

import java.io.File;
import java.io.IOException;

public interface FileEnumeration extends IOEnumeration {
  boolean hasMoreFiles() throws IOException;

  File nextFile() throws IOException;
}

