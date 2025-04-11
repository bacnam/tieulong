package core.network.client2game.handler.base;

import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.BaseHandler;

import java.io.IOException;

public class HeartBeat
        extends BaseHandler {
    public void handle(WebSocketRequest request, String message) throws IOException {
        request.response();
    }
}

