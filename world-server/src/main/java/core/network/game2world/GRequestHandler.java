package core.network.game2world;

import BaseCommon.CommLog;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.RequestHandler;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import java.io.IOException;

public abstract class GRequestHandler extends RequestHandler {
    @Override
    public void handleMessage(WebSocketRequest request, String data) throws WSException, IOException {
        if (!(request.getSession() instanceof GameSession)) {
            CommLog.warn("{} not handled.", request.getHeader().event);
        } else {
            try {
                this.handle(request, data);
            } catch (Throwable var4) {
                CommLog.error("消息处理时发生异常:", var4);
                request.error(ErrorCode.Unknown, var4.getMessage());
            }
        }
    }

    public abstract void handle(WebSocketRequest var1, String var2) throws WSException;
}
