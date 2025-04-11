package com.mchange.v2.cfg;

import java.util.List;
import java.util.Properties;

public abstract class MultiPropertiesConfig
        implements PropertiesConfig {
    public static MultiPropertiesConfig readVmConfig(String[] paramArrayOfString1, String[] paramArrayOfString2) {
        return ConfigUtils.readVmConfig(paramArrayOfString1, paramArrayOfString2);
    }

    public static MultiPropertiesConfig readVmConfig() {
        return ConfigUtils.readVmConfig();
    }

    public abstract String[] getPropertiesResourcePaths();

    public abstract Properties getPropertiesByResourcePath(String paramString);

    public abstract Properties getPropertiesByPrefix(String paramString);

    public abstract String getProperty(String paramString);

    public abstract List getDelayedLogItems();
}

