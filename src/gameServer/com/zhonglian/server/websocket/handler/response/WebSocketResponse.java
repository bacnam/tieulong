package com.zhonglian.server.websocket.handler.response;

import com.zhonglian.server.websocket.handler.MessageHeader;
import com.zhonglian.server.websocket.server.ServerSession;

public class WebSocketResponse
{
private ServerSession session;
private MessageHeader header;

public WebSocketResponse(ServerSession session, MessageHeader header) {
this.session = session;
this.header = header;
}

public MessageHeader getHeader() {
return this.header;
}

public int getRemoteServerID() {
return this.session.getRemoteServerID();
}

public ServerSession getSession() {
return this.session;
}
}

