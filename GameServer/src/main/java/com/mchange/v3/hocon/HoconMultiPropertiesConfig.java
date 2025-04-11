package com.mchange.v3.hocon;

import com.mchange.v2.cfg.DelayedLogItem;
import com.mchange.v2.cfg.MultiPropertiesConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

import java.util.*;

public class HoconMultiPropertiesConfig
        extends MultiPropertiesConfig {
    String quasiResourcePath;
    Properties props;
    List<DelayedLogItem> delayedLogItems = new LinkedList<DelayedLogItem>();

    Map<String, Properties> propsByPrefix = new HashMap<String, Properties>();

    public HoconMultiPropertiesConfig(String paramString, Config paramConfig) {
        this.quasiResourcePath = paramString;
        this.props = propsForConfig(paramConfig);
    }

    private static String asSimpleString(ConfigValue paramConfigValue) throws IllegalArgumentException {
        ConfigList<ConfigValue> configList;
        StringBuilder stringBuilder;
        byte b;
        int i;
        ConfigValueType configValueType = paramConfigValue.valueType();
        switch (configValueType) {

            case BOOLEAN:
            case NUMBER:
            case STRING:
                return String.valueOf(paramConfigValue.unwrapped());
            case LIST:
                configList = (ConfigList) paramConfigValue;
                for (ConfigValue configValue : configList) {
                    if (!isSimple(configValue))
                        throw new IllegalArgumentException("value is a complex list, could not be rendered as a simple property: " + paramConfigValue);
                }
                stringBuilder = new StringBuilder();
                for (b = 0, i = configList.size(); b < i; b++) {

                    if (b != 0) stringBuilder.append(',');
                    stringBuilder.append(asSimpleString(configList.get(b)));
                }
                return stringBuilder.toString();
            case OBJECT:
                throw new IllegalArgumentException("value is a ConfigValue object rather than an atom or list of atoms: " + paramConfigValue);
            case NULL:
                throw new IllegalArgumentException("value is a null; will be excluded from the MultiPropertiesConfig: " + paramConfigValue);
        }
        throw new IllegalArgumentException("value of an unexpected type: (value->" + paramConfigValue + ", type->" + configValueType + ")");
    }

    private static boolean isSimple(ConfigValue paramConfigValue) {
        ConfigValueType configValueType = paramConfigValue.valueType();
        switch (configValueType) {

            case BOOLEAN:
            case NUMBER:
            case STRING:
                return true;
        }
        return false;
    }

    private Properties propsForConfig(Config paramConfig) {
        Properties properties = new Properties();
        for (Map.Entry entry : paramConfig.entrySet()) {

            try {
                properties.put(entry.getKey(), asSimpleString((ConfigValue) entry.getValue()));
            } catch (IllegalArgumentException illegalArgumentException) {
                this.delayedLogItems.add(new DelayedLogItem(DelayedLogItem.Level.FINE, "For property '" + (String) entry.getKey() + "', " + illegalArgumentException.getMessage()));
            }
        }
        return properties;
    }

    public String[] getPropertiesResourcePaths() {
        return new String[]{this.quasiResourcePath};
    }

    public Properties getPropertiesByResourcePath(String paramString) {
        if (paramString.equals(this.quasiResourcePath)) {

            Properties properties = new Properties();
            properties.putAll(this.props);
            return properties;
        }

        return null;
    }

    public synchronized Properties getPropertiesByPrefix(String paramString) {
        Properties properties = this.propsByPrefix.get(paramString);
        if (properties == null) {

            properties = new Properties();

            if ("".equals(paramString)) {
                properties.putAll(this.props);
            } else {

                String str = paramString + '.';
                for (Map.Entry<Object, Object> entry : this.props.entrySet()) {

                    String str1 = (String) entry.getKey();
                    if (str1.startsWith(str)) {
                        properties.put(str1, entry.getValue());
                    }
                }
            }
            this.propsByPrefix.put(paramString, properties);
        }
        return properties;
    }

    public String getProperty(String paramString) {
        return (String) this.props.get(paramString);
    }

    public List getDelayedLogItems() {
        return this.delayedLogItems;
    }
}

