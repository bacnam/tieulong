package com.mchange.io;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface StringMemoryFile extends ReadOnlyMemoryFile {
  String asString() throws IOException;

  String asString(String paramString) throws IOException, UnsupportedEncodingException;
}

