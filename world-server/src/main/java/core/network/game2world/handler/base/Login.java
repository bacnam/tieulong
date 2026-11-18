package core.network.game2world.handler.base;

import com.google.gson.Gson;
import com.zhonglian.server.common.ServerStatus;
import com.zhonglian.server.common.utils.NetUtil;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.game2world.GRequestHandler;
import core.network.game2world.GameSession;
import core.network.game2world.GameSessionMgr;
import java.util.ArrayList;
import java.util.List;
import proto.common.Login.G_Login;
import proto.common.Login.Z_Login;
import proto.common.Server.ServerInfo;

public class Login extends GRequestHandler {
    @Override
    public void handle(WebSocketRequest request, String data) throws WSException {
        G_Login req = new Gson().fromJson(data, G_Login.class);
        GameSessionMgr mgr = GameSessionMgr.getInstance();
        mgr.login(req.serverId, request.getSession());
        List<ServerInfo> serverlist = new ArrayList<>();

        for (GameSession server : mgr.getServers()) {
            ServerInfo b = new ServerInfo();
            b.serverId = server.getRemoteServerID();
            b.ip = NetUtil.ipAddressStringToInt(server.remoteIP());
            b.port = server.remotePort();
            b.status = ServerStatus.Run.ordinal();
            serverlist.add(b);
        }

        request.response(new Z_Login(serverlist));
        ServerInfo b = new ServerInfo();
        b.serverId = request.getSession().getRemoteServerID();
        b.ip = NetUtil.ipAddressStringToInt(request.getSession().remoteIP());
        b.port = request.getSession().remotePort();
        b.status = ServerStatus.Run.ordinal();
        GameSessionMgr.getInstance().notifyAllServerMessage(req.serverId, "base.ServerNotify", b);
    }
}
