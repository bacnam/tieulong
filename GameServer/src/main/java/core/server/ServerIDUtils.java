package core.server;

import BaseCommon.CommLog;

public class ServerIDUtils {
    public static String getSidsBySid(int sid) {
        return String.valueOf(sid);
    }

    public static int getSidBySids(String sids) {
        try {
            return Integer.valueOf(sids).intValue();
        } catch (Exception e) {
            CommLog.error("无法将[{}]转化为相关的数字型id", sids);

            return 0;
        }
    }
}

