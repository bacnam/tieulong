package com.zhonglian.server.websocket.handler.requset;

import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.IBaseHandler;
import java.io.IOException;

public abstract class RequestHandler extends IBaseHandler {
  public abstract void handleMessage(WebSocketRequest paramWebSocketRequest, String paramString) throws WSException, IOException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/handler/requset/RequestHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */