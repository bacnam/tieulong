package core.network.game2world.handler.player;

import business.global.chat.ChatMgr;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.game2world.GRequestHandler;
import proto.gameworld.ChatMessage;

public class Chat extends GRequestHandler {
    @Override
    public void handle(WebSocketRequest request, String data) throws WSException {
        ChatMessage req = new Gson().fromJson(data, ChatMessage.class);
        ChatMgr.getInstance().addChat(req);
        request.response();
    }
}
