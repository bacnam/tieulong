package org.osgi.service.log;

import java.util.Enumeration;

public interface LogReaderService {
  void addLogListener(LogListener paramLogListener);

  void removeLogListener(LogListener paramLogListener);

  Enumeration getLog();
}

