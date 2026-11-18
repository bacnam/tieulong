package com.zhonglian.server.common.utils;

import BaseCommon.CommLog;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

public class CommProperties {
    private static Set<String> loadedProperties = new HashSet<>();

    public static boolean load(String file) {
        return load(file, true);
    }

    public static boolean load(String file, boolean userlogger) {
        try {
            try (InputStream propertiesInputStream = new FileInputStream(file); BufferedReader bf = new BufferedReader(new InputStreamReader(propertiesInputStream))) {
                Properties properties = new Properties();
                properties.load(bf);
                Properties pre = System.getProperties();

                for (Entry<Object, Object> entry : properties.entrySet()) {
                    String key = entry.getKey().toString().trim();
                    String value = entry.getValue().toString().trim();
                    if (value.endsWith(";")) {
                        value = value.substring(0, value.length() - 1);
                    }

                    pre.setProperty(key, value);
                }

                loadedProperties.add(file);
            }

            return true;
        } catch (Exception var27) {
            if (userlogger) {
                CommLog.error("加载properties[{}]配置错误：", file, var27);
            } else {
                System.err.println("加载properties[" + file + "]配置错误：");
                var27.printStackTrace();
            }

            return false;
        }
    }

    public static void printPropertis() {
        for (String filepath : loadedProperties) {
            CommLog.info("");
            CommLog.info("print properties in file:{}", filepath);

            try {
                try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)))) {
                    Properties properties = new Properties();
                    properties.load(bf);

                    for (Entry<Object, Object> entry : properties.entrySet()) {
                        String key = entry.getKey().toString().trim();
                        String value = entry.getValue().toString().trim();
                        if (value.endsWith(";")) {
                            value = value.substring(0, value.length() - 1);
                        }

                        CommLog.info("property key:{}  value:{}", key, value);
                    }
                }
            } catch (Exception var18) {
                CommLog.error("file({}) load failed", (Throwable)var18);
            }
        }
    }
}
