package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.RankActivity;
import business.global.activity.detail.RankArena;
import business.global.activity.detail.RankArtifice;
import business.global.activity.detail.RankDroiyan;
import business.global.activity.detail.RankDungeon;
import business.global.activity.detail.RankGuild;
import business.global.activity.detail.RankGumu;
import business.global.activity.detail.RankLevel;
import business.global.activity.detail.RankPower;
import business.global.activity.detail.RankTianLong;
import business.global.activity.detail.RankWing;
import business.global.activity.detail.RankXiaoyao;
import business.global.guild.Guild;
import business.global.rank.RankManager;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.guild.GuildMemberFeature;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.VipAwardInfo;
import java.io.IOException;
import java.util.List;

public class RankRewardOpenServer
extends PlayerHandler
{
class Request
{
ConstEnum.RankRewardType type;
}

private static class RankReward
{
String firstName;
int firstVip;
int myRank;
Reward reward;
int cost;
int require;
boolean isPicked;
List<VipAwardInfo> vipawards;
List<RankActivity.RankAward> rankrewards;

private RankReward(String firstName, int firstVip, int myRank, Reward reward, int cost, int require, boolean isPicked, List<VipAwardInfo> vipawards, List<RankActivity.RankAward> rankrewards) {
this.firstName = firstName;
this.firstVip = firstVip;
this.myRank = myRank;
this.reward = reward;
this.cost = cost;
this.require = require;
this.isPicked = isPicked;
this.vipawards = vipawards;
this.rankrewards = rankrewards;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);

RankActivity rankActivity = null;
RankType type = null;

switch (req.type) {
case WingRank:
rankActivity = (RankActivity)ActivityMgr.getActivity(RankWing.class);
type = RankType.WingLevel;
break;
case DungeonRank:
rankActivity = (RankActivity)ActivityMgr.getActivity(RankDungeon.class);
type = RankType.Dungeon;
break;
case LevelRank:
rankActivity = (RankActivity)ActivityMgr.getActivity(RankLevel.class);
type = RankType.Level;
break;
case PowerRank:
rankActivity = (RankActivity)ActivityMgr.getActivity(RankPower.class);
type = RankType.Power;
break;
case DroiyanRank:
rankActivity = (RankActivity)ActivityMgr.getActivity(RankDroiyan.class);
type = RankType.Droiyan;
break;
case null:
rankActivity = (RankActivity)ActivityMgr.getActivity(RankArena.class);
type = RankType.Arena;
break;
case GumuRank:
rankActivity = (RankActivity)ActivityMgr.getActivity(RankGumu.class);
type = RankType.GuMuPower;
break;
case TianLongRank:
rankActivity = (RankActivity)ActivityMgr.getActivity(RankTianLong.class);
type = RankType.TianLongPower;
break;
case XiaoyaoRank:
rankActivity = (RankActivity)ActivityMgr.getActivity(RankXiaoyao.class);
type = RankType.XiaoYaoPower;
break;
case ArtificeRank:
rankActivity = (RankActivity)ActivityMgr.getActivity(RankArtifice.class);
type = RankType.Artifice;
break;
case GuildRank:
rankActivity = (RankActivity)ActivityMgr.getActivity(RankGuild.class);
type = RankType.Guild;
break;
} 

if (rankActivity.getStatus() == ActivityStatus.Close) {
throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[] { rankActivity.getType() });
}

String firstName = "";
int firstVip = 0;
long firstId = RankManager.getInstance().getPlayerId(type, 1);

if (PlayerMgr.getInstance().getPlayer(firstId) != null) {
firstName = PlayerMgr.getInstance().getPlayer(firstId).getName();
firstVip = PlayerMgr.getInstance().getPlayer(firstId).getVipLevel();
} 
int myRank = 0;
if (type == RankType.Guild) {
Guild guild = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuild();
if (guild != null)
myRank = RankManager.getInstance().getRank(type, guild.getGuildId()); 
} else {
myRank = RankManager.getInstance().getRank(type, player.getPid());
} 
Reward reward = rankActivity.getReward();
int cost = rankActivity.getCost(player);
int require = rankActivity.getRequire_cost();

request.response(new RankReward(firstName, firstVip, myRank, reward, cost, require, rankActivity.isPicked(player), rankActivity.vipAwardProto(player), 
rankActivity.rankrewardList, null));
}
}

