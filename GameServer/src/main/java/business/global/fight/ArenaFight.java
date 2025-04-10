package business.global.fight;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.RankArena;
import business.global.arena.ArenaConfig;
import business.global.arena.ArenaManager;
import business.global.arena.Competitor;
import business.global.notice.NoticeMgr;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerItem;
import business.player.feature.character.CharFeature;
import business.player.feature.features.PlayerRecord;
import business.player.item.Reward;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.FightResult;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.logger.flow.ItemFlow;
import core.database.game.bo.ArenaFightRecordBO;
import java.util.Map;

public class ArenaFight
extends Fight<Reward>
{
Competitor atk;
Competitor def;
Map<Integer, Fighter> deffender;
Map<Integer, Fighter> attacker;

public ArenaFight(Competitor atk, Competitor def) {
this.atk = atk;
this.def = def;

Player atk0 = PlayerMgr.getInstance().getPlayer(atk.getPid());
this.attacker = ((CharFeature)atk0.getFeature(CharFeature.class)).getFighters();

Player def0 = PlayerMgr.getInstance().getPlayer(def.getPid());
this.deffender = ((CharFeature)def0.getFeature(CharFeature.class)).getFighters();
}

protected void beforeSettle(FightResult result) {}

protected void onCheckError() {
((PlayerRecord)PlayerMgr.getInstance().getPlayer(this.atk.getPid())
.getFeature(PlayerRecord.class)).addValue(ConstEnum.DailyRefresh.ArenaChallenge, -1);
}

public long getAtkPid() {
return this.atk.getPid();
}

protected Reward onLost() {
Reward reward = new Reward();
reward.add(PrizeType.ArenaToken, ArenaConfig.loserToken());

save(FightResult.Lost);
Player player = PlayerMgr.getInstance().getPlayer(this.atk.getPid());
((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.ArenaFight);
return reward;
}

protected Reward onWin() {
Reward reward = new Reward();
if (this.atk.getRank() > this.def.getRank()) {
Player playerAta = PlayerMgr.getInstance().getPlayer(this.atk.getPid());
Player playerDef = PlayerMgr.getInstance().getPlayer(this.def.getPid());
if (this.def.getRank() == 1) {
NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.ArenaFirstChange, new String[] { playerAta.getName(), playerDef.getName() });
}
ArenaManager.getInstance().swithRank(this.atk.getRank(), this.def.getRank());

((RankArena)ActivityMgr.getActivity(RankArena.class)).UpdateMaxRequire_cost(playerAta, this.atk.getRank());
((RankArena)ActivityMgr.getActivity(RankArena.class)).UpdateMaxRequire_cost(playerDef, this.def.getRank());
} 
reward.add(PrizeType.ArenaToken, ArenaConfig.winnerToken());
reward.add(PrizeType.Gold, ArenaConfig.winnerGold());
save(FightResult.Win);
Player player = PlayerMgr.getInstance().getPlayer(this.atk.getPid());
((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.ArenaFight);
return reward;
}

protected Map<Integer, Fighter> getDeffenders() {
return this.deffender;
}

private void save(FightResult result) {
ArenaFightRecordBO bo = new ArenaFightRecordBO();
bo.setAtkPid(this.atk.getPid());
bo.setAtkRank(this.atk.getRank());
bo.setDefPid(this.def.getPid());
bo.setDefRank(this.def.getRank());
bo.setBeginTime(this.beginTime);
bo.setResult(result.ordinal());
bo.setEndTime(CommTime.nowSecond());
bo.insert();
ArenaManager.getInstance().addFightRecord(bo);
}

protected Map<Integer, Fighter> getAttackers() {
return this.attacker;
}

public int fightTime() {
return ArenaConfig.fightTime();
}
}

