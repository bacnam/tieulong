package core.network.client2game.handler.marry;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import business.player.feature.marry.MarryFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.MarryApplyInfo;

import java.io.IOException;

public class MarryTo
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        MarryFeature feature = (MarryFeature) player.getFeature(MarryFeature.class);
        feature.marryApply(req.pid);
        MarryApplyInfo info = new MarryApplyInfo();
        info.setSummary(((PlayerBase) PlayerMgr.getInstance().getPlayer(feature.sendApplys.getPid()).getFeature(PlayerBase.class)).summary());
        info.setLeftTime(feature.getLeftTime(feature.sendApplys));
        request.response(info);
    }

    public static class Request {
        long pid;
    }
}

