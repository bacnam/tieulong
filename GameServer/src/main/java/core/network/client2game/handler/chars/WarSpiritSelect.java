package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.character.WarSpirit;
import business.player.feature.character.WarSpiritFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.WarSpiritInfo;

import java.io.IOException;

public class WarSpiritSelect
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        WarSpirit warSpirit = ((WarSpiritFeature) player.getFeature(WarSpiritFeature.class)).getWarSpirit(req.spiritId);
        if (warSpirit == null) {
            throw new WSException(ErrorCode.Char_NotFound, "战灵[%s]不存在或未解锁", new Object[]{Integer.valueOf(req.spiritId)});
        }
        ((WarSpiritFeature) player.getFeature(WarSpiritFeature.class)).setWarSpiritNow(warSpirit);

        warSpirit.onAttrChanged();

        request.response(new WarSpiritInfo(warSpirit));
    }

    public static class Request {
        int spiritId;
    }
}

