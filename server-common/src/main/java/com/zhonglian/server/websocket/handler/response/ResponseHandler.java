package com.zhonglian.server.websocket.handler.response;

import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.IBaseHandler;
import java.io.IOException;

public abstract class ResponseHandler extends IBaseHandler {
    public ResponseHandler() {
        super("WSResponseHandler");
    }

    public abstract void handleResponse(WebSocketResponse var1, String var2) throws WSException, IOException;

    public abstract void handleError(WebSocketResponse var1, short var2, String var3);
}
