package com.mchange.util;

public interface RobustMessageLogger extends MessageLogger {
  void log(String paramString);

  void log(Throwable paramThrowable, String paramString);
}

