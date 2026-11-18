package core.network.game2world;

import com.zhonglian.server.websocket.def.TerminalType;
import com.zhonglian.server.websocket.server.ServerSession;
import core.server.ServerConfig;
import org.apache.mina.core.session.IoSession;

public class GameSession extends ServerSession {
    private boolean logined = false;

    public GameSession(IoSession session, long sessionID) {
        super(TerminalType.WorldServer, ServerConfig.ServerID(), TerminalType.GameServer, session, sessionID);
    }

    @Override
    public void onCreated() {
    }

    @Override
    public void onClosed() {
    }

    public void setLogined(boolean b) {
        this.logined = b;
    }

    public boolean isLogined() {
        return this.logined;
    }

    public void notifyClient(long cid, Object proto) {
    }
}
