package core.network.proto;

import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefWorldBoss;
import core.database.game.bo.WorldBossBO;

public class WorldBossInfo
{
long id;
long bossId;
long bossHp;
long bossMaxHp;
long bossLevel;
boolean isDead;
long deadTime;
long reviveTime;
long lastKillCid;
boolean canChalleng;

public WorldBossInfo() {}

public WorldBossInfo(WorldBossBO bo) {
this.id = bo.getId();
this.bossId = bo.getBossId();
this.bossHp = bo.getBossHp();
this.bossMaxHp = bo.getBossMaxHp();
this.bossLevel = bo.getBossLevel();
this.isDead = bo.getIsDead();
this.deadTime = bo.getDeadTime();
this.reviveTime = bo.getReviveTime();
this.lastKillCid = bo.getLastKillCid();

RefWorldBoss ref = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf((int)bo.getBossId()));
this.canChalleng = ref.isInOpenHour();
}
}

