package com.mchange.util;

import java.io.IOException;

public interface MessageLogger {
  void log(String paramString) throws IOException;

  void log(Throwable paramThrowable, String paramString) throws IOException;
}

