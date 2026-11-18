package core.network.game2world;

import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;

public abstract class LRequestHandler extends GRequestHandler {
    @Override
    public void handle(WebSocketRequest request, String data) throws WSException {
        this.handle(request.getHeader().srcId, request, data);
    }

    public abstract void handle(long var1, WebSocketRequest var3, String var4) throws WSException;
}
