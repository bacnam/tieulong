package core.network.proto;

import core.database.game.bo.WorldBossChallengeBO;
import java.util.List;

public class WorldBossChalleng
{
long id;
long pid;
long teamLevel;
long challengeTimes;
List<Long> inspiringTimes;
List<Long> totalDamage;
List<Long> damageRank;
long beginFightTime;
long leaveFightTime;
long attackTimes;
long fightCD;

public WorldBossChalleng() {}

public WorldBossChalleng(WorldBossChallengeBO bo) {
this.id = bo.getId();
this.pid = bo.getPid();
this.teamLevel = bo.getTeamLevel();
this.challengeTimes = bo.getChallengeTimes();
this.inspiringTimes = bo.getInspiringTimesAll();
this.totalDamage = bo.getTotalDamageAll();
this.damageRank = bo.getDamageRankAll();
this.beginFightTime = bo.getBeginFightTime();
this.leaveFightTime = bo.getLeaveFightTime();
this.attackTimes = bo.getAttackTimes();
}
}

