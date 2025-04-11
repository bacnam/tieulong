package core.network.client2game.handler.marry;

import business.global.gmmail.MailCenter;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerItem;
import business.player.feature.marry.MarryConfig;
import business.player.feature.marry.MarryFeature;
import com.google.gson.Gson;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class DivorceForce
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);

        Player toPlayer = PlayerMgr.getInstance().getPlayer(req.pid);
        MarryFeature to_feature = (MarryFeature) toPlayer.getFeature(MarryFeature.class);

        if (!to_feature.isMarried()) {
            throw new WSException(ErrorCode.Marry_DivorceAlready, "玩家已离婚");
        }

        if (!((PlayerItem) player.getFeature(PlayerItem.class)).checkAndConsume(MarryConfig.getMarryDivorceApplyCostId(), MarryConfig.getMarryDivorceApplyCostCount(),
                ItemFlow.MarryDivorce)) {
            throw new WSException(ErrorCode.NotEnough_Currency, "缺少离婚许可");
        }

        to_feature.divorce();
        toPlayer.pushProto("beDivorce", to_feature.getLoveInfo());
        MailCenter.getInstance().sendMail(toPlayer.getPid(), RefDataMgr.getFactor("ForceDivorceMail", 1700001), new String[0]);

        MarryFeature feature = (MarryFeature) player.getFeature(MarryFeature.class);
        feature.divorce();
        request.response(feature.getLoveInfo());
    }

    public static class Request {
        long pid;
    }
}

