package com.mchange.v2.cfg;

import java.util.Properties;

public interface PropertiesConfig {
  Properties getPropertiesByPrefix(String paramString);

  String getProperty(String paramString);
}

