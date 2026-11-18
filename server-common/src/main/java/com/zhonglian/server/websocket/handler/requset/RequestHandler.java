package com.zhonglian.server.websocket.handler.requset;

import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.IBaseHandler;
import java.io.IOException;

public abstract class RequestHandler extends IBaseHandler {
    public abstract void handleMessage(WebSocketRequest var1, String var2) throws WSException, IOException;
}
