package core.network.game2world;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import BaseThread.BaseMutexObject;
import com.zhonglian.server.common.Scid;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.IExceptionCallback;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.response.ResponseHandler;
import com.zhonglian.server.websocket.server.ServerSession;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameSessionMgr {
    public static GameSessionMgr instance;
    private Map<Integer, GameSession> serverid2sessions = new ConcurrentHashMap<>();
    private Map<Long, GameSession> sessions = new ConcurrentHashMap<>();
    protected BaseMutexObject m_mutex = new BaseMutexObject();

    public static GameSessionMgr getInstance() {
        if (instance == null) {
            instance = new GameSessionMgr();
        }

        return instance;
    }

    private GameSessionMgr() {
        SyncTaskManager.schedule(1000, () -> {
            for (GameSession session : this.sessions.values()) {
                session.checkTimeoutRequest();
            }

            return true;
        });
    }

    protected void lock() {
        this.m_mutex.lock();
    }

    protected void unlock() {
        this.m_mutex.unlock();
    }

    public void add(GameSession session) {
        this.sessions.put(session.getSessionId(), session);
    }

    public void remove(GameSession session) {
        this.lock();
        this.sessions.remove(session.getSessionId());
        this.serverid2sessions.remove(session.getRemoteServerID());
        this.unlock();
    }

    public Collection<GameSession> getServers() {
        return this.serverid2sessions.values();
    }

    public GameSession getSessionByServerID(int serverID) {
        return this.serverid2sessions.get(serverID);
    }

    public void notifyAllServerMessage(int serverID, String event, Object proto) {
        for (GameSession server : this.getServers()) {
            if (server.getRemoteServerID() != serverID) {
                server.notifyMessage(event, proto);
            }
        }
    }

    public void notifyAllServerMessage(String event, Object proto) {
        for (GameSession server : this.getServers()) {
            server.notifyMessage(event, proto);
        }
    }

    public void notifyServer(int serverID, String event, Object proto) {
        GameSession session = this.getSessionByServerID(serverID);
        if (session == null) {
            CommLog.error("server " + serverID + " not login");
        } else {
            session.notifyMessage(event, proto);
        }
    }

    public void request(int serverID, String event, Object proto, ResponseHandler handler) throws WSException {
        GameSession session = this.getSessionByServerID(serverID);
        if (session == null) {
            throw new WSException(ErrorCode.Server_NotFound, "server " + serverID + " not login");
        } else {
            session.request(event, proto, handler);
        }
    }

    public void request(String event, Object proto, ResponseHandler handler) {
        for (GameSession server : this.getServers()) {
            server.request(event, proto, handler);
        }
    }

    public void notifyClient(String event, long cid, Object proto) {
        GameSession session = this.getSessionByServerID(Scid.getSid(cid));
        if (session == null) {
            CommLog.error("server[{}] not login", Scid.getSid(cid));
        } else {
            session.notifyClient(cid, proto);
        }
    }

    public String getServerList() {
        StringBuilder sb = new StringBuilder();
        sb.append("server amount:").append(this.serverid2sessions.size()).append("<br/>\r\n");

        for (GameSession session : this.serverid2sessions.values()) {
            sb.append("session id:")
                .append(session.getSessionId())
                .append(" server id:")
                .append(session.getRemoteServerID())
                .append(" ip:")
                .append(session.remoteIP())
                .append(" remote port:")
                .append(session.remotePort())
                .append("<br/>\r\n");
        }

        return sb.toString();
    }

    public String output() {
        StringBuilder sb = new StringBuilder();
        sb.append("SocketSessionMgr:\n");
        sb.append(String.format("%10s%10s%20s%10s\n", "sid", "serverId", "ip", "logined"));

        for (GameSession s : this.sessions.values()) {
            sb.append(String.format("%10s%10s%20s\n", s.getSessionId(), s.getRemoteServerID(), s.remoteAddress().toString()));
        }

        return sb.toString();
    }

    public void login(int serverID, ServerSession sessoin) throws WSException {
        GameSession gsessoin = (GameSession)sessoin;
        String remoteAddr = "";
        SocketAddress addr = gsessoin.remoteAddress();
        if (addr != null) {
            remoteAddr = addr.toString();
        }

        GameSession pre = this.serverid2sessions.get(serverID);
        if (pre != null && pre.isConnected() && pre != gsessoin) {
            String msg = String.format("server:%d login from %s, but already logined by %s", serverID, remoteAddr, pre.remoteAddress().toString());
            throw new WSException(ErrorCode.Server_AlreadyLogin, msg, new GameSessionMgr.LoginErrorCallback(sessoin));
        } else {
            gsessoin.setLogined(true);
            CommLog.info("login session:{}, serverID:{} login from {} ok!!!", gsessoin.getSessionId(), serverID, remoteAddr);
            this.serverid2sessions.put(serverID, gsessoin);
            gsessoin.setRemoteServerID(serverID);
        }
    }

    class LoginErrorCallback implements IExceptionCallback {
        private ServerSession _session;

        LoginErrorCallback(ServerSession session) {
            this._session = session;
        }

        @Override
        public void callback() {
            if (this._session != null) {
                CommLog.error("close session:{} for login error", this._session.getSessionId());
                this._session.close();
            }
        }
    }
}
