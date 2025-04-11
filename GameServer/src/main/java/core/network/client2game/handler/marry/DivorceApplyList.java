package core.network.client2game.handler.marry;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import business.player.feature.marry.MarryFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.MarryApplyInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DivorceApplyList
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        List<MarryApplyInfo> infolist = new ArrayList<>();
        MarryFeature feature = (MarryFeature) player.getFeature(MarryFeature.class);
        feature.receiveDivorceApplys.stream().forEach(x -> {
            MarryApplyInfo info = new MarryApplyInfo();
            info.setSummary(((PlayerBase) PlayerMgr.getInstance().getPlayer(x.getFromPid()).getFeature(PlayerBase.class)).summary());
            info.setLeftTime(paramMarryFeature.getDivorceLeftTime(x));
            paramList.add(info);
        });
        request.response(infolist);
    }
}

