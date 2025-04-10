package core.network.client2game;

import com.zhonglian.server.websocket.BaseAcceptor;
import com.zhonglian.server.websocket.BaseIoHandler;
import com.zhonglian.server.websocket.BaseSession;
import org.apache.mina.core.session.IoSession;

public class ClientAcceptor
extends BaseAcceptor<ClientSession>
{
public static class ClientAcceptorIoHandler
extends BaseIoHandler<ClientSession>
{
public ClientSession createSession(IoSession session, long sessionID) {
return new ClientSession(session, sessionID);
}
}

private static ClientAcceptor _instance = new ClientAcceptor();

public static final ClientAcceptor getInstance() {
return _instance;
}

public ClientAcceptor() {
super(new ClientAcceptorIoHandler());
ClientHandlerDispatcher dispatcher = new ClientHandlerDispatcher();
dispatcher.init();
this.handler.setMessageDispatcher(dispatcher);
}
}

