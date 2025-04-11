package business.global.fight;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.RankDroiyan;
import business.global.arena.ArenaConfig;
import business.global.notice.NoticeMgr;
import business.global.pvp.EncouterManager;
import business.global.rank.RankManager;
import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.character.CharFeature;
import business.player.feature.pvp.DroiyanFeature;
import business.player.item.Reward;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.FightResult;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Random;
import com.zhonglian.server.logger.flow.ItemFlow;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDroiyanTreasure;
import core.database.game.bo.DroiyanBO;
import core.database.game.bo.DroiyanRecordBO;
import core.database.game.bo.DroiyanTreasureBO;
import core.network.client2game.handler.pvp.DroiyanTreasures;

import java.util.Map;

public class DroiyanFight
        extends Fight<DroiyanRecordBO> {
    DroiyanFeature atk;
    DroiyanFeature def;
    Map<Integer, Fighter> deffender;
    Map<Integer, Fighter> attacker;
    boolean isRevenge = false;

    public DroiyanFight(DroiyanFeature atk, DroiyanFeature def, boolean revenge) {
        this.atk = atk;
        this.def = def;
        this.isRevenge = revenge;

        this.deffender = ((CharFeature) def.getPlayer().getFeature(CharFeature.class)).getFighters();
        this.attacker = ((CharFeature) atk.getPlayer().getFeature(CharFeature.class)).getFighters();
    }

    protected void onCheckError() {
        if (this.isRevenge) {
            this.atk.addEnemy(this.def.getPid());
        } else {
            this.atk.addDroiyan(this.def.getPid());
        }
    }

    public long getAtkPid() {
        return this.atk.getPid();
    }

    public int getAtkRed() {
        return this.atk.getBo().getRed();
    }

    protected DroiyanRecordBO onLost() {
        this.def.damage();
        this.atk.reborn();

        int time = CommTime.nowSecond();
        DroiyanRecordBO recordBO = new DroiyanRecordBO();
        recordBO.setPid(this.atk.getPid());
        recordBO.setTarget(this.def.getPid());
        recordBO.setResult(FightResult.Lost.ordinal());
        recordBO.setTargetName(this.def.getPlayerName());
        recordBO.setTime(time);

        DroiyanBO loserbo = this.atk.getBo();
        int red = loserbo.getRed() - RefDataMgr.getFactor("Droiyan_LostRed", 20);
        loserbo.setRed(Math.max(red, 0));

        loserbo.setWinTimes(0);

        if (this.isRevenge) {
            recordBO.setRevenged(true);
            this.def.setRevenged(this.atk.getPid());
        } else {

            recordBO.setRevenged(true);
        }
        loserbo.saveAll();
        recordBO.insert();
        this.atk.addRecored(recordBO);
        return recordBO;
    }

    protected DroiyanRecordBO onWin() {
        Player winner = this.atk.getPlayer();
        DroiyanBO winnerbo = this.atk.getBo();

        this.atk.damage();
        this.def.reborn();

        int time = CommTime.nowSecond();
        DroiyanRecordBO recordBO = new DroiyanRecordBO();
        recordBO.setPid(this.atk.getPid());
        recordBO.setTarget(this.def.getPid());
        recordBO.setResult(FightResult.Win.ordinal());
        recordBO.setTargetName(this.def.getPlayerName());
        recordBO.setTime(time);

        int red = winnerbo.getRed() + RefDataMgr.getFactor("Droiyan_WinRed", 15);
        red = Math.min(red, RefDataMgr.getFactor("Droiyan_MaxRed", 100));
        winnerbo.setRed(red);
        if (!this.isRevenge) {

            int dlevel = this.def.getPlayer().getLv() - winner.getLv();
            int point = RefDataMgr.getFactor("Droiyan_WinRed", 15) + Math.max(dlevel, 0) + 2 * winnerbo.getWinTimes();
            winnerbo.setPoint(winnerbo.getPoint() + point);
            recordBO.setPoint(point);
            RankManager.getInstance().update(RankType.Droiyan, winnerbo.getPid(), winnerbo.getPoint());

            ((RankDroiyan) ActivityMgr.getActivity(RankDroiyan.class)).UpdateMaxRequire_cost(winner, winnerbo.getPoint());

            winnerbo.setWinTimes(winnerbo.getWinTimes() + 1);
        }

        DroiyanTreasureBO robTreasure = null;
        int robRate = 0;

        if (this.def.isRed()) {
            robRate = RefDataMgr.getFactor("Droiyan_RedRobRate", 8000);
        }

        if (this.isRevenge) {
            robRate = RefDataMgr.getFactor("Droiyan_RevengeRobRate", 10000);
        } else {

            robRate = RefDataMgr.getFactor("Droiyan_RobRate", 3000);
        }

        if (Random.nextInt(10000) < robRate) {
            if (this.isRevenge) {
                robTreasure = this.def.beRevengeRobbed();
            } else if (!this.isRevenge) {
                robTreasure = this.def.beRobbed();
            }
            if (robTreasure != null) {
                NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.RobTreasure, new String[]{this.atk.getPlayerName()});
            }
        }
        if (robTreasure != null && !this.atk.isTreasureFull()) {
            robTreasure.setPid(this.atk.getPid());
            robTreasure.setGainTime(time);
            RefDroiyanTreasure ref = (RefDroiyanTreasure) RefDataMgr.get(RefDroiyanTreasure.class, Integer.valueOf(robTreasure.getTreasureId()));
            robTreasure.setExpireTime(ref.Time + time);
            this.atk.addTreasure(robTreasure);
            recordBO.setRob(robTreasure.getTreasureId());
            EncouterManager.getInstance().addNews(EncouterManager.NewsType.Drop, this.atk.getPlayerName(), robTreasure.getTreasureId());
            robTreasure.saveAll();
            pushTreasure(robTreasure);
        }

        RefDroiyanTreasure refTreasure = RefDroiyanTreasure.findTreature(Random.nextInt(10000));
        if (refTreasure != null && !this.atk.isTreasureFull()) {
            recordBO.setTreasure(refTreasure.id);
            DroiyanTreasureBO treasure = this.atk.addTreasure(refTreasure);
            EncouterManager.getInstance().addNews(EncouterManager.NewsType.Drop, this.atk.getPlayerName(), refTreasure.id);
            pushTreasure(treasure);

            NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.DropTreasure, new String[]{this.atk.getPlayerName()});
        }

        Reward reward = new Reward();
        recordBO.setGold(RefDataMgr.getFactor("Droiyan_WinnerGold", 10000));
        recordBO.setExp(RefDataMgr.getFactor("Droiyan_WinnerExp", 5000));
        reward.add(PrizeType.Gold, recordBO.getGold());
        reward.add(PrizeType.Exp, recordBO.getExp());
        ((PlayerItem) winner.getFeature(PlayerItem.class)).gain(reward, ItemFlow.DroiyanFight);
        if (this.isRevenge) {
            recordBO.setRevenged(true);
            this.def.setRevenged(this.atk.getPid());
        } else {

            recordBO.setRevenged(false);
            this.def.addEnemy(this.atk.getPid());
        }

        RankManager.getInstance().update(RankType.Droiyan, getAtkPid(), winnerbo.getPoint());

        if (winnerbo.getRed() >= RefDataMgr.getFactor("Droiyan_MaxRed", 100)) {
            NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.DroiyanRedName, new String[]{this.atk.getPlayer().getName()});
        }

        winnerbo.saveAll();
        recordBO.insert();
        this.atk.addRecored(recordBO);
        return recordBO;
    }

    private void pushTreasure(DroiyanTreasureBO bo) {
        this.atk.getPlayer().pushProto("addTreasure", new DroiyanTreasures.Treasure(bo));
    }

    protected Map<Integer, Fighter> getDeffenders() {
        return this.deffender;
    }

    protected Map<Integer, Fighter> getAttackers() {
        return this.attacker;
    }

    public int fightTime() {
        return ArenaConfig.fightTime();
    }
}

