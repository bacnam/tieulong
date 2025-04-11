package core.network.world2game.handler.player;

import business.global.chat.ChatMgr;
import business.player.Player;
import business.player.PlayerMgr;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.world2game.handler.WBaseHandler;
import proto.gameworld.ChatMessage;

public class WorldChat
        extends WBaseHandler {
    public void handle(WebSocketRequest request, String message) throws WSException {
        ChatMessage req = (ChatMessage) (new Gson()).fromJson(message, ChatMessage.class);
        ChatMgr.getInstance().addAllWorldChat(req);
        for (Player p : PlayerMgr.getInstance().getOnlinePlayers()) {
            p.pushProto("chat", req);
        }
        request.response();
    }
}

