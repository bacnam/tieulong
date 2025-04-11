package proto.common;

import java.util.List;

public class Login {
    public static class G_Login {
        public int serverId;

        public G_Login(int server) {
            this.serverId = server;
        }
    }

    public static class Z_Login {
        public List<Server.ServerInfo> servers;

        public Z_Login(List<Server.ServerInfo> serverlist) {
            this.servers = serverlist;
        }
    }
}

