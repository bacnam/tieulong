package core.network.game2world.handler.player;

import business.global.chat.ChatMgr;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.game2world.GRequestHandler;

public class LoadChatMessage extends GRequestHandler {
    @Override
    public void handle(WebSocketRequest request, String data) throws WSException {
        request.response(ChatMgr.getInstance().loadMessage());
    }
}
