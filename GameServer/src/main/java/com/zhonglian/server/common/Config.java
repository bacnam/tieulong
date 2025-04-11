package com.zhonglian.server.common;

import com.zhonglian.server.common.utils.Random;

public class Config {
    private static Integer serverIid = null;
    private static String serverSid = null;
    private static int startTime = 0;

    public static int GameID() {
        return 3;
    }

    public static String getPlatform() {
        return System.getProperty("Platform", "");
    }

    public static int ServerID() {
        if (serverIid == null) {
            serverIid = Integer.getInteger("ServerID_Int");
            if (serverIid == null) {
                throw new UnsupportedOperationException("ServerID_Int 不能为空");
            }
        }
        return serverIid.intValue();
    }

    public static String ServerIDStr() {
        if (serverSid == null) {
            serverSid = System.getProperty("ServerID_Str");
            if (serverSid == null || serverSid.trim().isEmpty()) {
                throw new UnsupportedOperationException("ServerID_Str 不能为空");
            }
        }
        return serverSid;
    }

    public static String DBUrl() {
        return System.getProperty("DB_URL");
    }

    public static String DBUser() {
        return System.getProperty("DB_USER");
    }

    public static String DBPassword() {
        return System.getProperty("DB_PASSWORD");
    }

    public static String LogDBUrl() {
        return System.getProperty("LOGDB_URL");
    }

    public static String LogDBUser() {
        return System.getProperty("LOGDB_USER");
    }

    public static String LogDBPassword() {
        return System.getProperty("LOGDB_PASSWORD");
    }

    public static int getServerStartTime() {
        return startTime;
    }

    static void setStartTime(int starttime) {
        startTime = starttime;
    }

    public static long getInitialID() {
        return ServerID() * 100000000000L + 100000000L + Random.nextInt(100000000);
    }

    public static String GmHeartBeatAddr() {
        String baseurl = System.getProperty("downConfUrl");
        if (baseurl == null || baseurl.isEmpty()) {
            return null;
        }
        return System.getProperty("GmHeartBeatAddr", "http:
    }

    public static String PhpMargueeNoticeAddr() {
        return System.getProperty("PhpMargueeNoticeAddr");
    }

    public static String PhpExchangeCodeAddr() {
        return System.getProperty("PhpExchangeCodeAddr");
    }
}

