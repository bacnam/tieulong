package com.zhonglian.server.common.utils;

import BaseCommon.CommLog;
import java.util.HashSet;
import java.util.Set;

public class CommProperties
{
private static Set<String> loadedProperties = new HashSet<>();

public static boolean load(String file) {
return load(file, true);
}

public static boolean load(String file, boolean userlogger) {
try {
Exception exception2, exception1 = null;

}
catch (Exception e) {
if (userlogger) {
CommLog.error("加载properties[{}]配置错误：", file, e);
} else {
System.err.println("加载properties[" + file + "]配置错误：");
e.printStackTrace();
} 
return false;
} 
return true;
}

public static void printPropertis() {
for (String filepath : loadedProperties) {
CommLog.info("");
CommLog.info("print properties in file:{}", filepath); try {
Exception exception2, exception1 = null;

}
catch (Exception e) {
CommLog.error("file({}) load failed", e);
} 
} 
}
}

