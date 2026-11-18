package business.player.feature.worldboss;

import business.global.gmmail.MailCenter;
import business.global.rank.RankManager;
import business.global.worldboss.WorldBossMgr;
import business.player.Player;
import business.player.feature.Feature;
import business.player.feature.character.CharFeature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefMail;
import core.config.refdata.ref.RefReward;
import core.config.refdata.ref.RefWorldBoss;
import core.database.game.bo.WorldBossBO;
import core.database.game.bo.WorldBossChallengeBO;
import java.util.LinkedList;
import java.util.Queue;

public class WorldBossFeature extends Feature {
    public WorldBossChallengeBO challenge;
    private Queue<Long> lasttime = new LinkedList<>();
    private double maxdamage = 0.0;
    private static int fight_interval = 300;

    public WorldBossFeature(Player owner) {
        super(owner);
    }

    @Override
    public void loadDB() {
        this.challenge = BM.getBM(WorldBossChallengeBO.class).findOne("pid", this.player.getPid());
    }

    public WorldBossChallengeBO getOrCreate() {
        WorldBossChallengeBO bo = this.challenge;
        if (bo != null) {
            return bo;
        } else {
            synchronized (this) {
                bo = this.challenge;
                if (bo != null) {
                    return bo;
                } else {
                    bo = new WorldBossChallengeBO();
                    bo.setPid(this.player.getPid());
                    bo.setTeamLevel(1L);
                    bo.setInspiringTimesAll(0L);
                    bo.setChallengeTimes(RefDataMgr.getFactor("WorldBossChallengeTimes", 10));
                    bo.insert();
                    this.challenge = bo;
                    return bo;
                }
            }
        }
    }

    public void buyChallengeTimes(int times) {
        WorldBossChallengeBO bo = this.getOrCreate();
        bo.saveChallengeTimes(bo.getChallengeTimes() + times);
    }

    public void challengTimesRefresh() {
        WorldBossChallengeBO bo = this.getOrCreate();
        bo.saveChallengeTimes(RefDataMgr.getFactor("WorldBossChallengeTimes", 10));
    }

    public void dailyRefresh() {
        try {
            WorldBossChallengeBO bo = this.getOrCreate();
            bo.setTeamLevel(this.player.getPlayerBO().getLv());
            bo.setInspiringTimesAll(0L);
            bo.setChallengeTimes(RefDataMgr.getFactor("WorldBossChallengeTimes", 10));
            bo.setTotalDamageAll(0L);
            bo.setLeaveFightTime(0L);
            bo.setAttackTimes(0L);
            bo.setResurrection(0);
            bo.saveAll();
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    public void updateChallengeDamage(WorldBossChallengeBO bo, long damage, int bossId) {
        long totalDamage = bo.getTotalDamage(bossId - 1) + damage;
        if (totalDamage > WorldBossMgr.getInstance().getBO(bossId).getBossMaxHp()) {
            bo.saveTotalDamage(bossId - 1, WorldBossMgr.getInstance().getBO(bossId).getBossMaxHp());
        } else {
            bo.saveTotalDamage(bossId - 1, totalDamage);
        }

        RankType type = WorldBossMgr.getInstance().getRankType(bossId);
        RankManager.getInstance().update(type, this.player.getPid(), bo.getTotalDamage(bossId - 1));
    }

    public WorldBossChallengeBO beginFightWorldBoss(int bossId) throws WSException {
        WorldBossMgr worldBossMgr = WorldBossMgr.getInstance();
        RefWorldBoss ref = RefDataMgr.get(RefWorldBoss.class, bossId);
        if (!ref.isInOpenHour()) {
            throw new WSException(ErrorCode.WorldBoss_NotOpen, "世界boss尚未开放，无法挑战");
        } else {
            WorldBossBO boss = worldBossMgr.getBO(bossId);
            if (boss.getIsDead()) {
                throw new WSException(ErrorCode.WorldBoss_IsDeath, "当前世界Boss已死，无法挑战");
            } else {
                WorldBossChallengeBO bo = this.challenge;
                int fightCD = RefDataMgr.getFactor("WorldBossAttackCD", 30);
                if (CommTime.nowSecond() - bo.getLeaveFightTime() < fightCD) {
                    throw new WSException(ErrorCode.WorldBoss_FightInCD, "玩家处于CD状态");
                } else {
                    worldBossMgr.doEnterWorldBoss(this.player, boss);
                    bo.setBeginFightTime(CommTime.nowSecond());
                    bo.setAttackTimes(bo.getAttackTimes() + 1L);
                    bo.saveAll();
                    this.maxdamage = this.player.getFeature(CharFeature.class).getMaxDamage(ref.BossId);
                    return bo;
                }
            }
        }
    }

    public boolean hurtWorldBoss(int bossId, long damage) throws WSException {
        long now = CommTime.nowMS();
        if (this.lasttime.size() >= this.player.getFeature(CharFeature.class).size()) {
            long last = this.lasttime.poll() + fight_interval;
            if (now < last) {
                throw new WSException(ErrorCode.Fight_CheckFailed, "伤害CD中");
            }
        }

        this.lasttime.add(now);
        if (damage > 100L && damage > this.maxdamage) {
            throw new WSException(ErrorCode.Fight_CheckFailed, "伤害异常");
        } else {
            RefWorldBoss refBoss = RefDataMgr.get(RefWorldBoss.class, bossId);
            if (!refBoss.isInOpenHour()) {
                throw new WSException(ErrorCode.WorldBoss_NotOpen, "世界boss尚未开放，无法挑战");
            } else {
                boolean isKill = WorldBossMgr.getInstance().doHurtWorldBoss(this.player, damage, bossId);
                this.dealKillBoss(isKill, bossId);
                this.player.getPlayerBO().setDungeonTime(CommTime.nowSecond());
                return false;
            }
        }
    }

    public void dealKillBoss(boolean isKill, int bossId) {
        if (isKill) {
            RefWorldBoss refBoss = RefDataMgr.get(RefWorldBoss.class, bossId);
            RefMail refMail = RefDataMgr.get(RefMail.class, refBoss.MailId);
            RefReward refReward = RefDataMgr.get(RefReward.class, refMail.RewardId);
            WorldBossMgr.getInstance().addWorldBossKillRecord(bossId, this.player, refReward.genReward());
            MailCenter.getInstance().sendMail(this.player.getPid(), refBoss.MailId);
        }
    }

    public WorldBossChallengeBO LeaveWorldBoss(int bossId) throws WSException {
        this.challenge.saveLeaveFightTime(CommTime.nowSecond());
        WorldBossMgr.getInstance().doLeaveWorldBoss(this.player, bossId);
        return this.challenge;
    }

    public int playerDamageRank(int bossId) {
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
        }

        return RankManager.getInstance().getRank(type, this.player.getPid());
    }

    public long playerDamageNum(int bossId) {
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
        }

        return RankManager.getInstance().getValue(type, this.player.getPid());
    }
}
