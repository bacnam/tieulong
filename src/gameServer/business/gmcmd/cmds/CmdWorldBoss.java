package business.gmcmd.cmds;

import business.global.rank.RankManager;
import business.global.worldboss.WorldBossMgr;
import business.gmcmd.annotation.Command;
import business.gmcmd.annotation.Commander;
import business.player.Player;
import business.player.feature.worldboss.WorldBossFeature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.RankType;
import core.database.game.bo.WorldBossBO;

@Commander(name = "worldboss", comment = "世界BOSS相关命令")
public class CmdWorldBoss
{
@Command(comment = "刷新玩家挑战信息")
public String refreshplayer(Player player) {
((WorldBossFeature)player.getFeature(WorldBossFeature.class)).dailyRefresh();
return String.format("刷新玩家参战信息成功", new Object[0]);
}

@Command(comment = "刷新世界BOSS")
public String refresh(Player player, int bossId) {
WorldBossMgr.getInstance().dailyRefreshWorldBoss(bossId);
return String.format("刷新世界BOSS" + bossId + "成功", new Object[0]);
}

@Command(comment = "清空世界BOSS排行榜")
public String clearrank(Player player, int bossId) {
RankType type = null;
switch (bossId) {
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

RankManager.getInstance().clear(type);
return String.format("清空世界BOSS" + bossId + "排行榜成功", new Object[0]);
}

@Command(comment = "设置BOSS最大血量并刷新")
public String sethp(Player player, int bossId, int bossMaxHp) {
WorldBossBO boss = WorldBossMgr.getInstance().getBO(bossId);
boss.saveBossMaxHp(bossMaxHp);
WorldBossMgr.getInstance().dailyRefreshWorldBoss(bossId);
return String.format("设置BOSS" + bossId + "最大血量并刷新", new Object[0]);
}

@Command(comment = "删除所有世界BOSS重新读表")
public String delall(Player player) {
BM.getBM(WorldBossBO.class).delAll();
WorldBossMgr.getInstance().init();
return String.format("删除所有世界BOSS", new Object[0]);
}

@Command(comment = "设置BOSS当前血量")
public String nowhp(Player player, int bossId, int Hp) {
WorldBossBO boss = WorldBossMgr.getInstance().getBO(bossId);
boss.saveBossHp(Hp);
return String.format("设置BOSS" + bossId + "当前血量", new Object[0]);
}

@Command(comment = "发送世界BOSS奖励")
public String mail(Player player, int bossId) {
WorldBossMgr.getInstance().sendRankReward(bossId, true);
return String.format("删除所有世界BOSS", new Object[0]);
}

@Command(comment = "自动挑战世界BOSS")
public String auto(Player player, int bossId) {
WorldBossMgr.getInstance().autoFight(bossId);
return String.format("自动挑战世界BOSS", new Object[0]);
}
}

