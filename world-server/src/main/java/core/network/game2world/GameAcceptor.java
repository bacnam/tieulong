package core.network.game2world;

import com.zhonglian.server.websocket.BaseAcceptor;
import com.zhonglian.server.websocket.BaseIoHandler;
import com.zhonglian.server.websocket.codecfactory.ServerDecoder;
import com.zhonglian.server.websocket.codecfactory.WebEncoder;
import org.apache.mina.core.session.IoSession;

public class GameAcceptor extends BaseAcceptor<GameSession> {
    private static GameAcceptor _instance = new GameAcceptor();

    public static final GameAcceptor getInstance() {
        return _instance;
    }

    private GameAcceptor() {
        super(new GameAcceptor.GameAcceptorIoHandler(), new WebEncoder(), new ServerDecoder());
        GameMessageDispatcher dispatcher = new GameMessageDispatcher();
        dispatcher.init();
        this.handler.setMessageDispatcher(dispatcher);
    }

    public static class GameAcceptorIoHandler extends BaseIoHandler<GameSession> {
        public GameSession createSession(IoSession session, long sessionID) {
            return new GameSession(session, sessionID);
        }
    }
}
