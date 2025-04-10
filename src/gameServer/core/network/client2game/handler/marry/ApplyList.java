package core.network.client2game.handler.marry;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import business.player.feature.marry.MarryFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.MarryApplyBO;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.MarryApplyInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApplyList
extends PlayerHandler
{
private static class Response
{
List<MarryApplyInfo> applys;
MarryApplyInfo sendapply;

private Response(List<MarryApplyInfo> applys, MarryApplyInfo sendapply) {
this.applys = applys;
this.sendapply = sendapply;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
List<MarryApplyInfo> infolist = new ArrayList<>();
MarryFeature feature = (MarryFeature)player.getFeature(MarryFeature.class);
feature.getApplyList().stream().forEach(x -> {
MarryApplyInfo info = new MarryApplyInfo();
info.setSummary(((PlayerBase)PlayerMgr.getInstance().getPlayer(x.getFromPid()).getFeature(PlayerBase.class)).summary());
info.setLeftTime(paramMarryFeature.getLeftTime(x));
paramList.add(info);
});
MarryApplyInfo info = new MarryApplyInfo();
if (feature.sendApplys != null) {
int time = feature.getLeftTime(feature.sendApplys);
if (time > 0) {
info.setSummary(((PlayerBase)PlayerMgr.getInstance().getPlayer(feature.sendApplys.getPid()).getFeature(PlayerBase.class)).summary());
info.setLeftTime(time);
} 
} 
request.response(new Response(infolist, info, null));
}
}

