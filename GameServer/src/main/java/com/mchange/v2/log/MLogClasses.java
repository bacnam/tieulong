package com.mchange.v2.log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class MLogClasses {
    static final String LOG4J_CNAME = "com.mchange.v2.log.log4j.Log4jMLog";
    static final String SLF4J_CNAME = "com.mchange.v2.log.slf4j.Slf4jMLog";
    static final String JDK14_CNAME = "com.mchange.v2.log.jdk14logging.Jdk14MLog";
    static final String[] SEARCH_CLASSNAMES = new String[]{"com.mchange.v2.log.log4j.Log4jMLog", "com.mchange.v2.log.slf4j.Slf4jMLog", "com.mchange.v2.log.jdk14logging.Jdk14MLog"};

    static final Map<String, String> ALIASES;

    static {
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        hashMap.put("log4j", "com.mchange.v2.log.log4j.Log4jMLog");
        hashMap.put("slf4j", "com.mchange.v2.log.slf4j.Slf4jMLog");
        hashMap.put("jdk14", "com.mchange.v2.log.jdk14logging.Jdk14MLog");
        hashMap.put("jul", "com.mchange.v2.log.jdk14logging.Jdk14MLog");
        hashMap.put("java.util.logging", "com.mchange.v2.log.jdk14logging.Jdk14MLog");
        hashMap.put("fallback", "com.mchange.v2.log.FallbackMLog");
        ALIASES = Collections.unmodifiableMap(hashMap);
    }

    static String resolveIfAlias(String paramString) {
        String str = ALIASES.get(paramString.toLowerCase());
        if (str == null) str = paramString;
        return str;
    }
}

