package com.mchange.io;

import java.io.File;
import java.io.IOException;

public interface ReadOnlyMemoryFile {
  File getFile() throws IOException;

  byte[] getBytes() throws IOException;
}

