package core.network.client2game.handler.marry;

import business.player.Player;
import business.player.feature.marry.MarryFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ConstEnum.SexType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class SetSex extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        SetSex.Request req = new Gson().fromJson(message, SetSex.Request.class);
        player.getFeature(MarryFeature.class).setSex(req.type);
        request.response(player.getFeature(MarryFeature.class).bo);
    }

    public static class Request {
        SexType type;
    }
}
