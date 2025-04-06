package com.mchange.v2.cfg;

import java.util.Properties;

public interface PropertiesConfig {
  Properties getPropertiesByPrefix(String paramString);
  
  String getProperty(String paramString);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/cfg/PropertiesConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */