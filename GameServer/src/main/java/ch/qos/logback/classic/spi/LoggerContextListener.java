package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

public interface LoggerContextListener {
  boolean isResetResistant();

  void onStart(LoggerContext paramLoggerContext);

  void onReset(LoggerContext paramLoggerContext);

  void onStop(LoggerContext paramLoggerContext);

  void onLevelChange(Logger paramLogger, Level paramLevel);
}

