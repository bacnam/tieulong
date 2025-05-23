package com.mchange.util;

import java.util.Iterator;

public interface FailSuppressedMessageLogger extends RobustMessageLogger {
  Iterator getFailures();

  void clearFailures();
}

