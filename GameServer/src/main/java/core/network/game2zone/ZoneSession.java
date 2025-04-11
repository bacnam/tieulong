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
import org.apache.mina.core.session.IoSession;
import proto.common.Login;
import proto.common.Server;

import java.io.IOException;

public class ZoneSession
        extends ServerSession {
    public ZoneSession(IoSession session, long sessionID) {
        super(TerminalType.GameServer, ServerConfig.ServerID(), TerminalType.ZoneServer, session, sessionID);
        setRemoteServerID(Integer.getInteger("zone_sid").intValue());
    }

    public void onCreated() {
        Login.G_Login loginProto = new Login.G_Login(ServerConfig.ServerID());
        request("base.login", loginProto, new ResponseHandler() {
            public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
                CommLog.info("login to zone success!");
                Login.Z_Login req = (Login.Z_Login) (new Gson()).fromJson(body, Login.Z_Login.class);
                for (Server.ServerInfo serverInfo : req.servers) {
                    ZoneServerMgr.getInstance().addServer(serverInfo);
                }
                ZoneConnector.getInstance().setLogined(true);
            }

            public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
                CommLog.error("{} resive error:{}, message:{}", new Object[]{getEvent(), Short.valueOf(statusCode), message});
                ZoneSession.this.onClosed();
            }
        });
    }

    public void onClosed() {
        ZoneConnector.getInstance().setLogined(false);
        SyncTaskManager.task(() -> ZoneConnector.getInstance().reconnect(System.getProperty("GameServer.Zone_IP", "127.0.0.1"), Integer.getInteger("GameServer.Zone_Port", 8002).intValue()),

                3000);
    }
}

