package business.global.worldboss;

import BaseTask.SyncTask.SyncTaskManager;
import business.global.battle.detail.WorldbossBattle;
import business.global.gmmail.MailCenter;
import business.global.notice.NoticeMgr;
import business.global.rank.RankManager;
import business.global.rank.Record;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.RobotManager;
import business.player.feature.PlayerBase;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import business.player.feature.worldboss.WorldBossConfig;
import business.player.feature.worldboss.WorldBossFeature;
import business.player.item.Reward;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.common.utils.Random;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCrystalPrice;
import core.config.refdata.ref.RefLanguage;
import core.config.refdata.ref.RefWorldBoss;
import core.config.refdata.ref.RefWorldBossDamageReward;
import core.database.game.bo.WorldBossBO;
import core.database.game.bo.WorldBossChallengeBO;
import core.database.game.bo.WorldBossKillRecordBO;
import core.network.proto.WorldBossInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class WorldBossMgr
{
private static WorldBossMgr instance = new WorldBossMgr();

public static WorldBossMgr getInstance() {
return instance;
}

public List<WorldBossBO> boList;

public List<WorldBossKillRecordBO> killRecords = Lists.newConcurrentList();

public Map<WorldBossBO, List<Player>> joinPlayers = new HashMap<>();

public Map<WorldBossBO, List<Player>> fightingPlayers = new HashMap<>();

public List<Player> robotPlayers = new ArrayList<>();

public List<Long> autoPlayers = new Vector<>();

boolean robotauto = false;

public void init() {
this.boList = null;
this.killRecords.clear();
this.joinPlayers.clear();
this.fightingPlayers.clear();

List<WorldBossBO> bossList = BM.getBM(WorldBossBO.class).findAll();
if (bossList != null && bossList.size() > 0) {
this.boList = bossList;
}

if (this.boList == null) {
this.boList = new ArrayList<>();
for (RefWorldBoss ref : RefDataMgr.getAll(RefWorldBoss.class).values()) {
WorldBossBO bo = new WorldBossBO();
bo.setBossId(ref.id);
bo.setBossHp(ref.MaxHP);
bo.setBossMaxHp(ref.MaxHP);
bo.setIsDead(false);
bo.setBossLevel(1L);
bo.setReviveTime(0L);
bo.insert_sync();
this.boList.add(bo);
} 
} 

for (WorldBossKillRecordBO bo : BM.getBM(WorldBossKillRecordBO.class).findAll()) {
this.killRecords.add(bo);
}

this.robotPlayers = RobotManager.getInstance().getRandomPlayers(RefDataMgr.getFactor("MaxWorldBossRobot", 15));

for (WorldBossChallengeBO bo : BM.getBM(WorldBossChallengeBO.class).findAll()) {
if (!bo.getAutoChallenge()) {
continue;
}
this.autoPlayers.add(Long.valueOf(bo.getPid()));
} 

for (WorldBossBO bo : this.boList) {
this.joinPlayers.put(bo, new ArrayList<>());
this.fightingPlayers.put(bo, new ArrayList<>());
} 
}

public WorldBossBO getBO(int bossId) {
for (WorldBossBO bo : this.boList) {
if (bo.getBossId() == bossId) {
return bo;
}
} 
return null;
}

public WorldBossKillRecordBO getKillRecord(long bossId) {
WorldBossKillRecordBO killRecordBO = null;
for (WorldBossKillRecordBO bo : this.killRecords) {
if (bo.getBossId() == bossId && CommTime.nowSecond() - bo.getDeathTime() < 86400) {
killRecordBO = bo;
}
} 
return killRecordBO;
}

public List<WorldBossBO> getBOList() {
return this.boList;
}

public List<Player> getPlayerList(WorldBossBO boss) {
return this.fightingPlayers.get(boss);
}

public List<Player> getJoinPlayerList(WorldBossBO boss) {
return this.joinPlayers.get(boss);
}

public int killBossNum() {
int killNum = 0;
synchronized (this.killRecords) {
killNum = this.killRecords.size();
} 
return killNum;
}

public void clearKillRecords(int bossId) {
for (WorldBossKillRecordBO bo : this.killRecords) {
if (bo.getBossId() != bossId) {
continue;
}
this.killRecords.remove(bo);
if (CommTime.nowSecond() - bo.getDeathTime() > 259200) {
bo.del();
}
} 
}

public void clearPlayers(int bossId) {
WorldBossBO boss = getBO(bossId);
List<Player> joinPlayers = getJoinPlayerList(boss);
List<Player> fightPlayers = getPlayerList(boss);
if (joinPlayers != null)
joinPlayers.clear(); 
if (fightPlayers != null) {
fightPlayers.clear();
}
}

public void limitHP(WorldBossBO bo) {
if (bo.getBossHp() == bo.getBossMaxHp()) {
return;
}

RefWorldBoss ref = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf((int)bo.getBossId()));
WorldBossKillRecordBO killRecord = getKillRecord(bo.getBossId());
if (killRecord != null)
{ if (killRecord.getFightTime() > RefDataMgr.getFactor("WorldBossFightTime", 1200)) {
bo.setBossMaxHp((long)((float)bo.getBossMaxHp() / ref.DownMultiple));
} else if (killRecord.getFightTime() < RefDataMgr.getFactor("WorldBossFightTime", 600)) {
if ((float)bo.getBossMaxHp() > 9.223372E18F / ref.UpMultiple) {
bo.setBossMaxHp(Long.MAX_VALUE);
} else {
bo.setBossMaxHp((long)((float)bo.getBossMaxHp() * ref.UpMultiple));
} 
}  }
else { bo.setBossMaxHp((long)((float)bo.getBossMaxHp() / ref.DownMultiple)); }

long hp = bo.getBossMaxHp();
hp = hp / 1000000L * 1000000L;
hp = Math.max(hp, RefDataMgr.getFactor("WorldBossMinHp", 100000));
bo.setBossMaxHp(hp);
}

public void dailyRefreshWorldBoss(int bossId) {
try {
int index = bossId - 1;
WorldBossBO bo = getBOList().get(index);

limitHP(bo);

bo.setBossHp(bo.getBossMaxHp());
bo.setBossLevel(1L);
bo.setIsDead(false);
bo.setReviveTime(CommTime.nowSecond());
bo.setLastKillCid(0L);
bo.saveAll();

clearKillRecords(bossId);

RankType type = getRankType(bossId);
RankManager.getInstance().clear(type);

clearPlayers(bossId);

for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
player.pushProto("bossRefresh", bo);
}
} catch (Exception e) {

e.printStackTrace();
} 
}

public RankType getRankType(int bossId) {
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

return type;
}

public void sendRankReward(int bossId, boolean isKill) {
try {
WorldBossBO bo = getBO(bossId);
if (bo.getIsDead() && !isKill) {
return;
}
RankType type = getRankType(bossId);
List<Record> records = RankManager.getInstance().getRankList(type, 999999);
for (Record record : records) {
if (record == null)
continue; 
RefWorldBossDamageReward ref = RefWorldBossDamageReward.getReward(bossId, record.getRank());
if (ref != null) {
MailCenter.getInstance().sendMail(record.getPid(), ref.MailId, new String[] { String.valueOf(record.getRank()) });
}
} 
} catch (Exception e) {

e.printStackTrace();
} 
}

public void doEnterWorldBoss(Player player, WorldBossBO worldBoss) {
List<Player> players = this.joinPlayers.get(worldBoss);
if (players == null) {
List<Player> playerL = new ArrayList<>();
playerL.add(player);
this.joinPlayers.put(worldBoss, playerL);
}
else if (!players.contains(player)) {
players.add(player);
this.joinPlayers.put(worldBoss, players);
} 

List<Player> fightPlayers = this.fightingPlayers.get(worldBoss);
if (fightPlayers == null) {
List<Player> playerL = new ArrayList<>();
playerL.add(player);
this.fightingPlayers.put(worldBoss, playerL);
}
else if (!fightPlayers.contains(player)) {
fightPlayers.add(player);
this.fightingPlayers.put(worldBoss, fightPlayers);
} 

for (Player tmpPlayer : this.fightingPlayers.get(worldBoss)) {
if (tmpPlayer != null && tmpPlayer != player) {
tmpPlayer.pushProto("newWorldBossPlayer", ((PlayerBase)player.getFeature(PlayerBase.class)).fightInfo());
}
} 
}

public void doLeaveWorldBoss(Player player, int bossId) {
WorldBossBO boss = getBO(bossId);
if (this.fightingPlayers.get(boss) != null) {
((List)this.fightingPlayers.get(boss)).remove(player);
}
}

public void broadCastProtoToPlayers(WorldBossBO boss) {
for (Player player : this.joinPlayers.get(boss)) {
player.pushProto("worldbossDead", new WorldBossInfo(boss));
}
}

public void addWorldBossKillRecord(int bossId, Player killPlayer, Reward reward) {
WorldBossBO bo = getBO(bossId);
RefWorldBoss ref = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(bossId));

long beginTime = CommTime.getTodayHourMS(ref.BeginTime) / 1000L;

WorldBossKillRecordBO kill = new WorldBossKillRecordBO();
kill.setBossId(bo.getBossId());
kill.setBossLevel(bo.getBossLevel());
kill.setKillerPid(killPlayer.getPid());
kill.setKillerName(killPlayer.getName());
kill.setKillerLevel(killPlayer.getLv());
kill.setKillerIcon(killPlayer.getPlayerBO().getIcon());
kill.setRewardItemId(reward.uniformItemIds());
kill.saveRewardItemCount(reward.uniformItemCounts());
kill.setDeathTime((int)bo.getDeadTime());
kill.setFightTime(bo.getDeadTime() - beginTime);
kill.insert();
this.killRecords.add(kill);
}

public boolean doHurtWorldBoss(Player player, long damage, int bossId) throws WSException {
boolean isKill = false;
synchronized (this) {
WorldBossBO bo = getBO(bossId);
if (bo.getBossHp() == bo.getBossMaxHp() && !this.robotauto) {
this.robotauto = true;
robotFight(bossId);
} 

if (bo.getIsDead()) {
throw new WSException(ErrorCode.WorldBoss_IsDeath, "Boss已被消灭");
}
long leftHp = bo.getBossHp();
bo.setBossHp(leftHp - damage);
if (bo.getBossHp() <= 0L) {
damage = leftHp;
bo.setDeadTime(CommTime.nowSecond());
bo.setBossHp(0L);
bo.setIsDead(true);
bo.setLastKillCid(player.getPid());
isKill = true;
} 
bo.saveAll();

WorldBossFeature feature = (WorldBossFeature)player.getFeature(WorldBossFeature.class);
feature.updateChallengeDamage(feature.getOrCreate(), damage, bossId);

if (isKill) {
broadCastProtoToPlayers(bo);
RefWorldBoss boss = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(bossId));
RefLanguage reflan = (RefLanguage)RefDataMgr.get(RefLanguage.class, boss.Name);
NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.KillWorldBoss, new String[] { player.getName(), reflan.CN });
sendRankReward(bossId, isKill);
} 
} 
return isKill;
}

public void robotFight(int bossId) {
SyncTaskManager.schedule(30000, () -> robotDamage(paramInt));
}

public boolean robotDamage(int bossId) {
RefWorldBoss ref = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(bossId));
WorldBossBO bo = getBO(bossId);
double limit = bo.getBossMaxHp() * 0.05D;
if (!ref.isInOpenHour() || bo.getIsDead() || bo.getBossHp() < limit) {
this.robotauto = false;
return false;
} 
for (Player player : this.robotPlayers) {
try {
long damage = Random.nextLong(10000L);
doHurtWorldBoss(player, damage, bossId);
} catch (WSException e) {
e.printStackTrace();
} 
} 
return true;
}

public void applyAuto(Player player) throws WSException {
synchronized (this) {
if (this.autoPlayers.contains(Long.valueOf(player.getPid()))) {
throw new WSException(ErrorCode.WorldBoss_AlreadyAuto, "自动挑战已申请");
}
((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate().saveAutoChallenge(true);
this.autoPlayers.add(Long.valueOf(player.getPid()));
} 
}

public void cancelAuto(Player player) throws WSException {
synchronized (this) {
if (!this.autoPlayers.contains(Long.valueOf(player.getPid()))) {
throw new WSException(ErrorCode.WorldBoss_NotAuto, "尚未申请过");
}
((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate().saveAutoChallenge(false);
this.autoPlayers.remove(Long.valueOf(player.getPid()));
} 
}

public void autoFight(int bossId) {
for (Long pid : this.autoPlayers) {
Player player = PlayerMgr.getInstance().getPlayer(pid.longValue());
((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate().saveTotalDamage(bossId - 1, 0L);
} 
SyncTaskManager.schedule(WorldBossConfig.getFightCD() * 2 * 1000, () -> {
RefWorldBoss ref = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(paramInt));
WorldBossBO bo = getBO(paramInt);
if (!ref.isInOpenHour() || bo.getIsDead())
return false; 
checkAuto();
for (Long pid : this.autoPlayers) {
Player player = PlayerMgr.getInstance().getPlayer(pid.longValue());
autoDamage(player, paramInt);
} 
return true;
});
}

public void checkAuto() {
Iterator<Long> it = this.autoPlayers.iterator();
while (it.hasNext()) {
long pid = ((Long)it.next()).longValue();
Player player = PlayerMgr.getInstance().getPlayer(pid);
if (!((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate().getAutoChallenge()) {
it.remove();
}
} 
}

public boolean autoDamage(Player player, int bossId) {
RefWorldBoss ref = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(bossId));
WorldBossBO bo = getBO(bossId);
if (!ref.isInOpenHour() || bo.getIsDead())
return false; 
try {
PlayerRecord record = (PlayerRecord)player.getFeature(PlayerRecord.class);
int times = record.getValue(ConstEnum.DailyRefresh.AutoFightWorldboss);
int price = (RefCrystalPrice.getPrize(times)).AutoFightWorldboss;
if (!((PlayerCurrency)player.getFeature(PlayerCurrency.class)).checkAndConsume(PrizeType.Gold, price, ItemFlow.AutoFightWorldboss)) {
((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate().saveAutoChallenge(false);
return false;
} 
WorldbossBattle battle = new WorldbossBattle(player, ref.BossId);
battle.init(bossId);
battle.fight();
long damage = battle.getDamage();

boolean isKill = doHurtWorldBoss(player, damage, bossId);

((WorldBossFeature)player.getFeature(WorldBossFeature.class)).dealKillBoss(isKill, bossId);
record.addValue(ConstEnum.DailyRefresh.AutoFightWorldboss);
((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate().saveLeaveFightTime((CommTime.nowSecond() + WorldBossConfig.getFightCD()));
} catch (WSException e) {
e.printStackTrace();
} 
return true;
}
}

