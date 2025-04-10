package core.network.client2game.handler.pvc;

import business.global.rank.RankManager;
import business.global.worldboss.WorldBossMgr;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import business.player.feature.features.PlayerRecord;
import business.player.feature.worldboss.WorldBossFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.database.game.bo.WorldBossBO;
import core.database.game.bo.WorldBossChallengeBO;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Player;
import java.io.IOException;

public class WorldBossCheck
extends PlayerHandler
{
public static class Request
{
int bossId;
}

public static class BossInfo
{
WorldBossBO boss;
Player.Summary summary;
long firstDamage;
WorldBossChallengeBO challengBO;
int myRank;
long myDamage;
int fightCD;
Player.Summary LastKillPlayer;
boolean isAuto;
int autoTimes;

public BossInfo(WorldBossBO boss, Player.Summary summary, long firstDamage, WorldBossChallengeBO challengBO, int myRank, long myDamage, int fightCD, Player.Summary LastKillPlayer, boolean isAuto, int autoTimes) {
this.boss = boss;
this.summary = summary;
this.firstDamage = firstDamage;
this.challengBO = challengBO;
this.myRank = myRank;
this.myDamage = myDamage;
this.fightCD = fightCD;
this.LastKillPlayer = LastKillPlayer;
this.isAuto = isAuto;
this.autoTimes = autoTimes;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Player.Summary summary;
Request req = (Request)(new Gson()).fromJson(message, Request.class);
RankType type = null;
switch (req.bossId) {
case 1:
type = RankType.WorldBoss1;
break;
case 2:
type = RankType.WorldBoss2;
break;
case 3:
type = RankType.WorldBoss3;
break;
case 4:
type = RankType.WorldBoss4;
break;
} 

WorldBossBO worldBoss = WorldBossMgr.getInstance().getBO(req.bossId);
long pid = RankManager.getInstance().getPlayerId(type, 1);
Player tar = PlayerMgr.getInstance().getPlayer(pid);

if (tar != null) {
summary = ((PlayerBase)tar.getFeature(PlayerBase.class)).summary();
} else {
summary = null;
} 
long damage = RankManager.getInstance().getValue(type, pid);
int myRank = ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).playerDamageRank(req.bossId);
long myDamage = ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).playerDamageNum(req.bossId);
WorldBossChallengeBO challengBO = ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate();
int fightCD = (int)(CommTime.nowSecond() - challengBO.getLeaveFightTime());
if (fightCD > RefDataMgr.getFactor("WorldBossAttackCD", 30)) {
fightCD = 0;
} else {
fightCD = RefDataMgr.getFactor("WorldBossAttackCD", 30) - fightCD;
}  long lastKillPid = worldBoss.getLastKillCid();
Player lastKillPlayer = PlayerMgr.getInstance().getPlayer(lastKillPid);
Player.Summary LastKillPlayersum = null;
if (lastKillPlayer != null) {
LastKillPlayersum = ((PlayerBase)lastKillPlayer.getFeature(PlayerBase.class)).summary();
}
boolean isAuto = ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate().getAutoChallenge();
int autoTimes = ((PlayerRecord)player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.AutoFightWorldboss);

request.response(new BossInfo(worldBoss, summary, damage, challengBO, myRank, myDamage, fightCD, LastKillPlayersum, isAuto, autoTimes));
}
}

