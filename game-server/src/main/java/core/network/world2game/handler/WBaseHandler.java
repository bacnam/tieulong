package core.network.world2game.handler;

import BaseCommon.CommLog;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.RequestHandler;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import java.io.IOException;

public abstract class WBaseHandler extends RequestHandler {
    @Override
    public void handleMessage(WebSocketRequest request, String data) throws WSException, IOException {
        try {
            this.handle(request, data);
        } catch (Throwable var4) {
            CommLog.error(this.getClass().getSimpleName() + " Exception: ", var4);
            request.error(ErrorCode.Unknown, var4.toString());
        }
    }

    public abstract void handle(WebSocketRequest var1, String var2) throws WSException;
}
