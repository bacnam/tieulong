package core.network.game2world;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.TerminalType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.response.ResponseHandler;
import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
import com.zhonglian.server.websocket.server.ServerSession;
import core.server.ServerConfig;
import java.io.IOException;
import org.apache.mina.core.session.IoSession;
import proto.common.Login.G_Login;
import proto.common.Login.Z_Login;
import proto.common.Server.ServerInfo;

public class WorldSession extends ServerSession {
    public WorldSession(IoSession session, long sessionID) {
        super(TerminalType.GameServer, ServerConfig.ServerID(), TerminalType.WorldServer, session, sessionID);
        this.setRemoteServerID(0);
    }

    @Override
    public void onCreated() {
        G_Login loginProto = new G_Login(ServerConfig.ServerID());
        this.request("base.login", loginProto, new ResponseHandler() {
            @Override
            public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
                CommLog.info("login to [[[world server]]] success!");
                Z_Login req = new Gson().fromJson(body, Z_Login.class);

                for (ServerInfo serverInfo : req.servers) {
                    WorldServerMgr.getInstance().addServer(serverInfo);
                }

                WorldConnector.getInstance().setLogined(true);
                WorldConnector.getInstance().onConnect();
            }

            @Override
            public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
                CommLog.error("{} resive error:{}, message:{}", this.getEvent(), statusCode, message);
                WorldSession.this.onClosed();
            }
        });
    }

    @Override
    public void onClosed() {
        WorldConnector.getInstance().setLogined(false);
        SyncTaskManager.task(
            () -> WorldConnector.getInstance()
                .reconnect(System.getProperty("GameServer.World_IP", "127.0.0.1"), Integer.getInteger("GameServer.Zone_Port", 8002)),
            3000
        );
    }
}
