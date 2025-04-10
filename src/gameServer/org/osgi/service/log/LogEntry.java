package org.osgi.service.log;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

public interface LogEntry {
  Bundle getBundle();

  ServiceReference getServiceReference();

  int getLevel();

  String getMessage();

  Throwable getException();

  long getTime();
}

