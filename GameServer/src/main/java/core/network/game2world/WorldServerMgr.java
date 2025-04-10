package core.network.game2world;

import com.zhonglian.server.common.ServerStatus;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import proto.common.Server;

public class WorldServerMgr
{
private static WorldServerMgr instance;
private Map<Integer, Server.ServerInfo> servers = new ConcurrentHashMap<>();

public static WorldServerMgr getInstance() {
if (instance == null) {
instance = new WorldServerMgr();
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

