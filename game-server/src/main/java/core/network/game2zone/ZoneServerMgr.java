package core.network.game2zone;

import com.zhonglian.server.common.ServerStatus;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import proto.common.Server.ServerInfo;

public class ZoneServerMgr {
    private static ZoneServerMgr instance;
    private Map<Integer, ServerInfo> servers = new ConcurrentHashMap<>();

    private ZoneServerMgr() {
    }

    public static ZoneServerMgr getInstance() {
        if (instance == null) {
            instance = new ZoneServerMgr();
        }

        return instance;
    }

    public void addServer(ServerInfo s) {
        if (s != null) {
            this.servers.put(s.serverId, s);
        }
    }

    public void updateServer(ServerInfo serverInfo) {
        if (serverInfo.status == ServerStatus.Stop.ordinal()) {
            this.servers.remove(serverInfo.serverId);
        } else if (serverInfo.status == ServerStatus.Stop.ordinal()) {
            this.servers.put(serverInfo.serverId, serverInfo);
        }
    }
}
