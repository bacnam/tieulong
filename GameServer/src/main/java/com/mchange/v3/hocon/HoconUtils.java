package com.mchange.v3.hocon;

import com.typesafe.config.Config;

import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public final class HoconUtils {
    public static PropertiesConversion configToProperties(Config paramConfig) {
        Set set = paramConfig.entrySet();

        Properties properties = new Properties();
        HashSet<String> hashSet = new HashSet();

        for (Map.Entry entry : set) {

            String str1 = (String) entry.getKey();
            String str2 = null;
            try {
                str2 = paramConfig.getString(str1);
            } catch (com.typesafe.config.ConfigException.WrongType wrongType) {
                hashSet.add(str1);
            }
            if (str2 != null) {
                properties.setProperty(str1, str2);
            }
        }
        PropertiesConversion propertiesConversion = new PropertiesConversion();
        propertiesConversion.properties = properties;
        propertiesConversion.unrenderable = hashSet;
        return propertiesConversion;
    }

    public static class PropertiesConversion {
        Properties properties;
        Set<String> unrenderable;
    }
}

