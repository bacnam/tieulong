package core.network.game2zone;

import com.zhonglian.server.common.ServerStatus;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import proto.common.Server;

public class ZoneServerMgr
{
private static ZoneServerMgr instance;
private Map<Integer, Server.ServerInfo> servers = new ConcurrentHashMap<>();

public static ZoneServerMgr getInstance() {
if (instance == null) {
instance = new ZoneServerMgr();
}
return instance;
}

public void addServer(Server.ServerInfo s) {
if (s != null) {
this.servers.put(Integer.valueOf(s.serverId), s);
}
}

public void updateServer(Server.ServerInfo serverInfo) {
if (serverInfo.status == ServerStatus.Stop.ordinal()) {
this.servers.remove(Integer.valueOf(serverInfo.serverId));
} else if (serverInfo.status == ServerStatus.Stop.ordinal()) {
this.servers.put(Integer.valueOf(serverInfo.serverId), serverInfo);
} 
}
}

