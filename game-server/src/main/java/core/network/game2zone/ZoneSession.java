package core.network.game2zone;

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

public class ZoneSession extends ServerSession {
    public ZoneSession(IoSession session, long sessionID) {
        super(TerminalType.GameServer, ServerConfig.ServerID(), TerminalType.ZoneServer, session, sessionID);
        this.setRemoteServerID(Integer.getInteger("zone_sid"));
    }

    @Override
    public void onCreated() {
        G_Login loginProto = new G_Login(ServerConfig.ServerID());
        this.request("base.login", loginProto, new ResponseHandler() {
            @Override
            public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
                CommLog.info("login to zone success!");
                Z_Login req = new Gson().fromJson(body, Z_Login.class);

                for (ServerInfo serverInfo : req.servers) {
                    ZoneServerMgr.getInstance().addServer(serverInfo);
                }

                ZoneConnector.getInstance().setLogined(true);
            }

            @Override
            public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
                CommLog.error("{} resive error:{}, message:{}", this.getEvent(), statusCode, message);
                ZoneSession.this.onClosed();
            }
        });
    }

    @Override
    public void onClosed() {
        ZoneConnector.getInstance().setLogined(false);
        SyncTaskManager.task(
            () -> ZoneConnector.getInstance()
                .reconnect(System.getProperty("GameServer.Zone_IP", "127.0.0.1"), Integer.getInteger("GameServer.Zone_Port", 8002)),
            3000
        );
    }
}
