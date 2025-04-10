package core.network.client2game.handler.marry;

import business.global.rank.RankManager;
import business.global.rank.Record;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.marry.MarryFeature;
import business.player.feature.pvp.WorshipFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.LoverInfo;
import core.network.proto.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RankList
extends PlayerHandler
{
private static class Request
{
RankType rank;
}

private static class Response {
int worshipTimes;
int rank;
long value;
List<RankList.RankInfo> rankList;

public Response(int worshipTimes, int rank, long value, List<Record> records) {
this.worshipTimes = worshipTimes;
this.rank = rank;
this.value = value;
this.rankList = new ArrayList<>();
List<Record> remove = new ArrayList<>();
for (int i = 0; i < records.size(); i++) {
Record r = records.get(i);
if (r != null) {

Player player = PlayerMgr.getInstance().getPlayer(r.getPid());
MarryFeature marry = (MarryFeature)player.getFeature(MarryFeature.class);
LoverInfo info = marry.getLoveInfo();
if (info == null)
{ remove.add(r); }

else

{ this.rankList.add(new RankList.RankInfo(r)); } 
} 
}  for (Record r : remove) {
Player player = PlayerMgr.getInstance().getPlayer(r.getPid());
RankManager.getInstance().clearPlayerData(player, RankType.Lovers);
} 
}
}

public static class RankInfo
{
int rank;
long value;
Player.Summary husband;
Player.Summary wife;

public RankInfo(Record record) {
Player player = PlayerMgr.getInstance().getPlayer(record.getPid());
MarryFeature marry = (MarryFeature)player.getFeature(MarryFeature.class);
LoverInfo info = marry.getLoveInfo();
this.rank = record.getRank();
this.value = record.getValue();
this.husband = info.getHusband();
this.wife = info.getWife();
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
List<Record> records = RankManager.getInstance().getRankList(req.rank, 30);
int rank = 0;
long value = 0L;
MarryFeature marry = (MarryFeature)player.getFeature(MarryFeature.class);
Player man = null;
if (marry.bo.getMarried() != 0) {
Player lover = PlayerMgr.getInstance().getPlayer(marry.bo.getLoverPid());
if (marry.bo.getSex() == ConstEnum.SexType.Man.ordinal()) {
man = player;
} else {
man = lover;
} 
} 
if (man != null) {
rank = RankManager.getInstance().getRank(req.rank, man.getPid());
value = RankManager.getInstance().getValue(req.rank, man.getPid());
} 
int times = ((WorshipFeature)player.getFeature(WorshipFeature.class)).getTimes(req.rank.ordinal());
request.response(new Response(times, rank, value, records));
}
}

