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
import proto.common.Login;
import proto.common.Server;

public class WorldSession
extends ServerSession
{
public WorldSession(IoSession session, long sessionID) {
super(TerminalType.GameServer, ServerConfig.ServerID(), TerminalType.WorldServer, session, sessionID);
setRemoteServerID(0);
}

public void onCreated() {
Login.G_Login loginProto = new Login.G_Login(ServerConfig.ServerID());
request("base.login", loginProto, new ResponseHandler()
{
public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException
{
CommLog.info("login to [[[world server]]] success!");
Login.Z_Login req = (Login.Z_Login)(new Gson()).fromJson(body, Login.Z_Login.class);
for (Server.ServerInfo serverInfo : req.servers) {
WorldServerMgr.getInstance().addServer(serverInfo);
}
WorldConnector.getInstance().setLogined(true);
WorldConnector.getInstance().onConnect();
}

public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
CommLog.error("{} resive error:{}, message:{}", new Object[] { getEvent(), Short.valueOf(statusCode), message });
WorldSession.this.onClosed();
}
});
}

public void onClosed() {
WorldConnector.getInstance().setLogined(false);
SyncTaskManager.task(() -> WorldConnector.getInstance().reconnect(System.getProperty("GameServer.World_IP", "127.0.0.1"), Integer.getInteger("GameServer.Zone_Port", 8002).intValue()), 

3000);
}
}

