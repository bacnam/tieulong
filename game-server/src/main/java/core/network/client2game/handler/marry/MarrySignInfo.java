package core.network.client2game.handler.marry;

import business.player.Player;
import business.player.feature.PlayerBase;
import business.player.feature.marry.MarryFeature;
import com.zhonglian.server.common.utils.StringUtils;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.List;

public class MarrySignInfo extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        MarryFeature feature = player.getFeature(MarryFeature.class);
        if (!feature.isMarried()) {
            throw new WSException(ErrorCode.Marry_NotYet, "尚未结婚");
        } else {
            core.network.proto.Player.Summary lover = feature.getLover().getFeature(PlayerBase.class).summary();
            int loverSign = feature.getLoverFeature().getSignin();
            core.network.proto.Player.Summary my = player.getFeature(PlayerBase.class).summary();
            int mySign = feature.getSignin();
            boolean isSign = feature.bo.getIsSign();
            List<Integer> alreadyPick = StringUtils.string2Integer(feature.bo.getSignReward());
            request.response(new MarrySignInfo.Response(lover, loverSign, my, mySign, isSign, alreadyPick));
        }
    }

    private static class Response {
        core.network.proto.Player.Summary lover;
        int loverSign;
        core.network.proto.Player.Summary my;
        int mySign;
        boolean isSign;
        List<Integer> alreadyPick;

        public Response(
            core.network.proto.Player.Summary lover, int loverSign, core.network.proto.Player.Summary my, int mySign, boolean isSign, List<Integer> alreadyPick
        ) {
            this.lover = lover;
            this.loverSign = loverSign;
            this.my = my;
            this.mySign = mySign;
            this.isSign = isSign;
            this.alreadyPick = alreadyPick;
        }
    }
}
