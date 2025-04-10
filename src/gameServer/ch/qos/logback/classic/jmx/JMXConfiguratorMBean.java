package ch.qos.logback.classic.jmx;

import ch.qos.logback.core.joran.spi.JoranException;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;

public interface JMXConfiguratorMBean {
  void reloadDefaultConfiguration() throws JoranException;

  void reloadByFileName(String paramString) throws JoranException, FileNotFoundException;

  void reloadByURL(URL paramURL) throws JoranException;

  void setLoggerLevel(String paramString1, String paramString2);

  String getLoggerLevel(String paramString);

  String getLoggerEffectiveLevel(String paramString);

  List<String> getLoggerList();

  List<String> getStatuses();
}

