package core.network.client2game.handler.player;

import business.global.chat.ChatMgr;
import business.player.Player;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class LoadChatMessage
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        request.response(ChatMgr.getInstance().loadMessage(player));
    }
}

