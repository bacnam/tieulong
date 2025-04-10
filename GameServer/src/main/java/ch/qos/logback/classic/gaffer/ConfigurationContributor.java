package ch.qos.logback.classic.gaffer;

import java.util.Map;

public interface ConfigurationContributor {
  Map<String, String> getMappings();
}

