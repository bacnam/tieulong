package core.server;

import com.zhonglian.server.common.Config;

public class ServerConfig extends Config {
    private static int LoadCachedPlayerCnt = 100;
    private static int MaxCachedPlayerCnt = 20000;
    private static int MaxOnlinePlayerCnt = 4000;

    public static int getLoadCachedPlayerCnt() {
        return Integer.getInteger("GameServer.LoadCachedPlayerCnt", LoadCachedPlayerCnt);
    }

    public static int getMaxCachedPlayerCnt() {
        return Integer.getInteger("GameServer.MaxCachedPlayerCnt", MaxCachedPlayerCnt);
    }

    public static int getMaxOnlinePlayerCnt() {
        return Integer.getInteger("GameServer.MaxOnlinePlayerCnt", MaxOnlinePlayerCnt);
    }

    public static boolean IsWorldServerOn() {
        return "on".equalsIgnoreCase(System.getProperty("GameServer.WorldOpen"));
    }
}
