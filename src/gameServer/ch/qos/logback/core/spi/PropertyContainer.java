package ch.qos.logback.core.spi;

import java.util.Map;

public interface PropertyContainer {
  String getProperty(String paramString);

  Map<String, String> getCopyOfPropertyMap();
}

