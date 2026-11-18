package business.global.guild;

import BaseTask.SyncTask.SyncTaskManager;
import business.global.battle.SimulatBattle;
import business.global.gmmail.MailCenter;
import business.global.notice.NoticeMgr;
import business.global.rank.RankManager;
import business.global.rank.Record;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.RobotManager;
import business.player.feature.PlayerCurrency;
import business.player.feature.PlayerItem;
import business.player.feature.features.PlayerRecord;
import business.player.feature.guild.GuildMemberFeature;
import business.player.item.Reward;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.FightResult;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.common.enums.ConstEnum.UniverseMessageType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Maps;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCrystalPrice;
import core.config.refdata.ref.RefLongnvLevel;
import core.config.refdata.ref.RefLongnvWarLevel;
import core.config.refdata.ref.RefLongnvWarPersonReward;
import core.config.refdata.ref.RefReward;
import core.database.game.bo.LongnvApplyBO;
import core.database.game.bo.LongnvResultBO;
import core.database.game.bo.LongnvWarResultBO;
import core.database.game.bo.LongnvwarpuppetBO;
import core.network.proto.Fight;
import core.network.proto.LongnvWarFightProtol;
import core.network.proto.LongnvWarInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

public class LongnvWar {
    Guild guild;
    LongnvWarResultBO bo;
    List<GuildWarMgr.Road> warRoad = new ArrayList<>();
    private int road = 3;
    int fightTime;
    public Map<Long, List<Double>> playersHP = Maps.newConcurrentHashMap();
    public List<LongnvResultBO> fightResult = new ArrayList<>();
    public Map<Long, Fight.Battle> fightBattle = Maps.newConcurrentHashMap();
    public final Map<Long, List<LongnvwarpuppetBO>> puppets = Maps.newConcurrentHashMap();
    public List<Player> joinPlayers = new ArrayList<>();
    int total;
    int enemyTotal;
    Map<Long, Integer> playerPuppet = new HashMap<>();

    public LongnvWar(Guild guild) {
        this.guild = guild;
        this.init();
    }

    public void init() {
        for (LongnvApplyBO apply : BM.getBM(LongnvApplyBO.class).findAll("guildId", this.guild.getGuildId())) {
            if (apply.getApplyTime() < CommTime.getTodayZeroClockS()) {
                apply.del();
            } else {
                Player player = PlayerMgr.getInstance().getPlayer(apply.getPid());
                this.joinPlayers.add(player);
            }
        }

        for (LongnvwarpuppetBO puppet : BM.getBM(LongnvwarpuppetBO.class).findAll()) {
            Guild guild = GuildMgr.getInstance().getGuild(puppet.getGuildId());
            if (guild == null) {
                puppet.del();
            } else if (puppet.getApplyTime() < CommTime.getTodayZeroClockS()) {
                puppet.del();
            } else if (this.puppets.get(puppet.getPid()) != null) {
                this.puppets.get(puppet.getPid()).add(puppet);
            } else {
                List<LongnvwarpuppetBO> list = new ArrayList<>();
                list.add(puppet);
                this.puppets.put(puppet.getPid(), list);
            }
        }

        for (LongnvWarResultBO result : BM.getBM(LongnvWarResultBO.class).findAll("guildId", this.guild.getGuildId())) {
            if (result.getChallengeTime() < CommTime.getTodayZeroClockS()) {
                result.del();
            } else {
                this.bo = result;
            }
        }
    }

    public void NPCchallenge() {
        LongnvWarResultBO bo = new LongnvWarResultBO();
        bo.setGuildId(this.guild.getGuildId());
        bo.setLevel(this.guild.getLongnvLevel());
        bo.setChallengeTime(CommTime.nowSecond());
        bo.insert();
        this.bo = bo;
    }

    public void prepareFight() {
        this.warRoad.clear();

        for (int i = 0; i < this.road; i++) {
            this.warRoad.add(new GuildWarMgr.Road());
        }

        List<Player> defList = new ArrayList<>();
        List<Player> atkList = new ArrayList<>();
        new ArrayList();
        RefLongnvWarLevel ref = RefDataMgr.get(RefLongnvWarLevel.class, this.guild.bo.getLnwarLevel() + 1);
        if (ref.RobotLevel != 0) {
            List<Player> players = RobotManager.getInstance().getLvlPlayers(ref.RobotLevel);
            if (players == null) {
                players = RobotManager.getInstance().getLvlPlayers(38);
            }

            atkList.addAll(players.subList(0, ref.Amount));
        } else {
            long guildid = RankManager.getInstance().getPlayerId(RankType.Guild, 1);
            List<Long> members = GuildMgr.getInstance().getGuild(guildid).getMembers();

            for (Record r : RankManager.getInstance().getRankList(RankType.Power, ref.Amount)) {
                if (r != null) {
                    members.add(r.getPid());
                }
            }

            for (Long pid : members) {
                Player player = PlayerMgr.getInstance().getPlayer(pid);
                atkList.add(player);
            }
        }

        defList.addAll(this.joinPlayers);
        List<Player> defpuppets = this.createPuppets();

        for (Entry<Long, List<LongnvwarpuppetBO>> entry : this.puppets.entrySet()) {
            this.playerPuppet.put(entry.getKey(), entry.getValue().size());
        }

        this.total = defList.size() + defpuppets.size();
        this.enemyTotal = atkList.size();

        for (int i = 0; i < defList.size(); i++) {
            int index = i % this.road;
            this.warRoad.get(index).getDefplayers().add(defList.get(i));
        }

        for (int i = 0; i < defpuppets.size(); i++) {
            int index = i % this.road;
            this.warRoad.get(index).getDefplayers().add((Player)defpuppets.get(i));
        }

        for (int i = 0; i < atkList.size(); i++) {
            int index = i % this.road;
            this.warRoad.get(index).getAtkplayers().add(atkList.get(i));
        }
    }

    public void fight() {
        this.prepareFight();
        this.beginFight();
    }

    public void Start() {
        if (this.guild.getLevel() >= RefDataMgr.getFactor("LongnvUnlockLv", 5)) {
            NoticeMgr.getInstance().sendMarquee(UniverseMessageType.LongnvUnderAttack, this.guild);
            this.dailyRefresh();
            this.NPCchallenge();
            SyncTaskManager.schedule(LongnvWarConfig.challengeCD() * 1000, () -> {
                this.fight();
                return false;
            });
        }
    }

    public void Restart() {
        if (this.bo != null) {
            if (this.bo.getResult() == 0 && this.bo.getChallengeTime() >= CommTime.getTodayZeroClockS()) {
                int time = this.bo.getChallengeTime() + LongnvWarConfig.challengeCD() - CommTime.nowSecond();
                SyncTaskManager.schedule(Math.max(0, time) * 1000, () -> {
                    try {
                        this.fight();
                        return false;
                    } catch (Exception var2) {
                        return false;
                    }
                });
            }
        }
    }

    public void beginFight() {
        this.fightTime = CommTime.nowSecond();

        for (int i = 0; i < this.road; i++) {
            GuildWarMgr.Road road = this.warRoad.get(i);
            road.setBegin(CommTime.nowSecond());
            if (road.getAtkplayers().size() == 0 || road.getDefplayers().size() == 0) {
                road.setOverTime(CommTime.nowSecond());
            }

            SyncTaskManager.task(() -> SyncTaskManager.schedule(LongnvWarConfig.oneFightTime(), () -> this.roadFight(road)));
        }
    }

    private boolean roadFight(GuildWarMgr.Road road) {
        synchronized (this) {
            road.setBegin(CommTime.nowSecond());
            if (this.atkResult(road)) {
                return false;
            } else if (this.timeOver(road)) {
                return false;
            } else if (road.getWinner() != 0L) {
                return false;
            } else {
                Player atkPlayer = null;
                Player defPlayer = null;
                if (road.getAtkplayers().size() > 0) {
                    atkPlayer = road.getAtkplayers().get(0);
                }

                if (road.getDefplayers().size() > 0) {
                    defPlayer = road.getDefplayers().get(0);
                }

                if (atkPlayer != null && defPlayer != null) {
                    SimulatBattle battle = new SimulatBattle(atkPlayer, defPlayer);
                    battle.initHp(this.playersHP.get(atkPlayer.getPid()), this.playersHP.get(defPlayer.getPid()));
                    battle.initInspire();
                    battle.fight();
                    FightResult result = battle.getResult();
                    LongnvResultBO resultbo = new LongnvResultBO();
                    resultbo.setAtkpid(atkPlayer.getPid());
                    resultbo.setDefpid(defPlayer.getPid());
                    resultbo.setResult(result.ordinal());
                    resultbo.setFightTime(CommTime.nowSecond());
                    resultbo.insert();
                    this.fightResult.add(resultbo);
                    this.fightBattle.put(resultbo.getId(), battle.proto());
                    Player lose = null;
                    Player winner = null;
                    if (result == FightResult.Win) {
                        lose = road.getDefplayers().remove(0);
                        winner = atkPlayer;
                        road.getDeaddefplayers().add(lose);
                    }

                    if (result == FightResult.Lost) {
                        lose = road.getAtkplayers().remove(0);
                        winner = defPlayer;
                        road.getDeadatkplayers().add(lose);
                    }

                    if (result == FightResult.Draw) {
                        Player lose1 = road.getDefplayers().remove(0);
                        road.getDeaddefplayers().add(lose1);
                        Player lose2 = road.getAtkplayers().remove(0);
                        road.getDeadatkplayers().add(lose2);
                    }

                    if (lose != null && winner != null) {
                        this.playersHP.put(winner.getPid(), battle.getWinnerHp());
                        if (GuildWarConfig.puppetPlayer.class.isInstance(lose)) {
                            this.removePuppet(lose);
                        }
                    }

                    if (road.getDefplayers().size() == 0 || road.getAtkplayers().size() == 0) {
                        road.setOverTime(CommTime.nowSecond());
                    }
                } else {
                    if (road.getOverTime() != 0 && CommTime.nowSecond() - road.getOverTime() > LongnvWarConfig.overTime()) {
                        if (atkPlayer == null) {
                            road.setWinner(2L);
                            Optional<GuildWarMgr.Road> find = this.warRoad.stream().parallel().filter(x -> x != road && x.getWinner() == 0L).findAny();
                            if (find.isPresent()) {
                                find.get().getDefplayers().addAll(road.getDefplayers());
                                road.getDefplayers().clear();
                            }
                        } else {
                            road.setWinner(1L);
                            Optional<GuildWarMgr.Road> find = this.warRoad.stream().parallel().filter(x -> x != road && x.getWinner() == 0L).findAny();
                            if (find.isPresent()) {
                                find.get().getAtkplayers().addAll(road.getAtkplayers());
                                road.getAtkplayers().clear();
                            }
                        }

                        return false;
                    }

                    if (road.getOverTime() == 0) {
                        road.setOverTime(CommTime.nowSecond());
                    }
                }

                return true;
            }
        }
    }

    public void removePuppet(Player puppet) {
        List<LongnvwarpuppetBO> puppets = this.puppets.get(puppet.getPid());
        LongnvwarpuppetBO find = null;
        if (puppets != null) {
            GuildWarConfig.puppetPlayer puppetplayer = (GuildWarConfig.puppetPlayer)puppet;

            for (LongnvwarpuppetBO bo : puppets) {
                if (bo.getPuppetId() == puppetplayer.getPuppet_id()) {
                    find = bo;
                    break;
                }
            }
        }

        if (find != null) {
            puppets.remove(find);
        }
    }

    public boolean atkResult(GuildWarMgr.Road road) {
        synchronized (this) {
            if (road.isOver) {
                return true;
            } else {
                int atknum = this.getleftAtk();
                int defnum = this.getleftDef();
                if (atknum != 0 && defnum != 0) {
                    return false;
                } else {
                    this.bo.setFightTime(CommTime.nowSecond());
                    if (atknum == 0) {
                        this.bo.setResult(FightResult.Win.ordinal());
                        RefLongnvWarLevel ref = RefDataMgr.get(RefLongnvWarLevel.class, this.guild.bo.getLnwarLevel() + 1);
                        NoticeMgr.getInstance().sendMarquee(UniverseMessageType.LongnvWin, this.guild.getName(), ref.GuildName);
                    }

                    if (defnum == 0) {
                        this.bo.setResult(FightResult.Lost.ordinal());
                    }

                    this.bo.saveAll();
                    this.dealWarFinish();
                    return true;
                }
            }
        }
    }

    public boolean timeOver(GuildWarMgr.Road road) {
        synchronized (this) {
            if (road.isOver) {
                return true;
            } else if (CommTime.nowSecond() - this.fightTime > LongnvWarConfig.fightTime()) {
                this.bo.setFightTime(CommTime.nowSecond());
                this.bo.setResult(FightResult.Win.ordinal());
                this.bo.saveAll();
                this.dealWarFinish();
                return true;
            } else {
                return false;
            }
        }
    }

    public int getleftAtk() {
        int num = 0;

        for (int i = 0; i < this.road; i++) {
            GuildWarMgr.Road road = this.warRoad.get(i);
            num += road.getAtkplayers().size();
        }

        return num;
    }

    public int getDeadAtk() {
        int num = 0;

        for (int i = 0; i < this.road; i++) {
            GuildWarMgr.Road road = this.warRoad.get(i);
            num += road.getDeadatkplayers().size();
        }

        return num;
    }

    public int getleftDef() {
        int num = 0;

        for (int i = 0; i < this.road; i++) {
            GuildWarMgr.Road road = this.warRoad.get(i);
            num += road.getDefplayers().size();
        }

        return num;
    }

    public int getDeadDef() {
        int num = 0;

        for (int i = 0; i < this.road; i++) {
            GuildWarMgr.Road road = this.warRoad.get(i);
            num += road.getDeaddefplayers().size();
        }

        return num;
    }

    public int getdeadAtk() {
        int num = 0;

        for (int i = 0; i < this.road; i++) {
            GuildWarMgr.Road road = this.warRoad.get(i);
            num += road.getDeadatkplayers().size();
        }

        return num;
    }

    public int getdeadDef() {
        int num = 0;

        for (int i = 0; i < this.road; i++) {
            GuildWarMgr.Road road = this.warRoad.get(i);
            num += road.getDeaddefplayers().size();
        }

        return num;
    }

    private void dealWarFinish() {
        synchronized (this) {
            for (int i = 0; i < this.road; i++) {
                GuildWarMgr.Road road1 = this.warRoad.get(i);
                road1.isOver = true;
            }

            this.personReward(this.fightResult);
            this.playersHP.clear();
            this.guild.broadcast("warFinish", "");
            this.roportAll();
        }
    }

    public void roportAll() {
        String myGuild = this.guild.getName();
        String total = String.valueOf(this.total);
        String dead = String.valueOf(this.getdeadDef());
        String enemyTotal = String.valueOf(this.enemyTotal);
        String enemyDead = String.valueOf(this.getdeadAtk());
        SyncTaskManager.task(
            () -> {
                RefLongnvWarLevel ref = RefDataMgr.get(RefLongnvWarLevel.class, this.guild.bo.getLnwarLevel() + 1);
                String EnemyGuild = ref.GuildName;
                int mailId = LongnvWarConfig.ResultMail();
                String result = "";
                if (this.bo.getResult() == FightResult.Win.ordinal()) {
                    result = "胜利";
                } else if (this.bo.getResult() == FightResult.Lost.ordinal()) {
                    result = "失败";
                }

                for (Long pid : this.guild.getMembers()) {
                    int puppet = 0;
                    int kill = 0;

                    try {
                        if (this.playerPuppet.get(pid) != null) {
                            puppet = this.playerPuppet.get(pid);
                        }
                    } catch (Exception var15) {
                    }

                    kill = this.getKillNum(pid);
                    MailCenter.getInstance()
                        .sendMail(pid, mailId, myGuild, total, dead, EnemyGuild, enemyTotal, enemyDead, String.valueOf(puppet), String.valueOf(kill), result);
                }
            }
        );
    }

    public void personReward(List<LongnvResultBO> list) {
        List<GuildWarMgr.Road> roads = this.warRoad;
        int defwin = 0;

        for (GuildWarMgr.Road road : roads) {
            if (road.getWinner() != 0L && road.getDefplayers().size() != 0) {
                defwin++;
            }
        }

        Map<Long, List<LongnvResultBO>> map = Maps.newConcurrentHashMap();

        for (LongnvResultBO bo : list) {
            if (bo.getResult() == FightResult.Lost.ordinal()) {
                if (map.get(bo.getDefpid()) != null) {
                    map.get(bo.getDefpid()).add(bo);
                } else {
                    List<LongnvResultBO> tmp_list = new ArrayList<>();
                    tmp_list.add(bo);
                    map.put(bo.getDefpid(), tmp_list);
                }
            }
        }

        for (Entry<Long, List<LongnvResultBO>> entry : map.entrySet()) {
            int num = 1;
            Player player = PlayerMgr.getInstance().getPlayer(entry.getKey());
            num = Math.max(num, defwin);
            Reward rewardall = new Reward();
            Reward reward = RefLongnvWarPersonReward.getReward(entry.getValue().size());

            for (int i = 0; i < num; i++) {
                rewardall.combine(reward);
            }

            MailCenter.getInstance().sendMail(player.getPid(), "GM", "守卫龙女个人奖励", "在龙女守卫战中你奋勇杀敌，获得以下奖励", rewardall);
        }
    }

    public List<Player> createPuppets() {
        List<Player> puppets = new ArrayList<>();

        for (Entry<Long, List<LongnvwarpuppetBO>> entry : this.puppets.entrySet()) {
            Player player = PlayerMgr.getInstance().getPlayer(entry.getKey());

            for (LongnvwarpuppetBO bo : entry.getValue()) {
                GuildWarConfig.puppetPlayer p_player = new GuildWarConfig.puppetPlayer(player.getPlayerBO());
                p_player.setPuppet_id(bo.getPuppetId());
                p_player.setIs_puppet(true);
                puppets.add(p_player);
            }
        }

        return puppets;
    }

    public void applyLongnvWar(Player player) throws WSException {
        if (this.bo == null) {
            throw new WSException(ErrorCode.Longnv_NotFoundFight, "尚未有帮派宣战");
        } else {
            int time = this.bo.getChallengeTime() + LongnvWarConfig.challengeCD() - CommTime.nowSecond();
            if (time < 0) {
                throw new WSException(ErrorCode.Longnv_NotFoundFight, "报名时间已过");
            } else {
                if (!this.joinPlayers.contains(player)) {
                    LongnvApplyBO apply = new LongnvApplyBO();
                    apply.setPid(player.getPid());
                    apply.setGuildId(this.guild.getGuildId());
                    apply.setApplyTime(CommTime.nowSecond());
                    apply.insert();
                    this.joinPlayers.add(player);
                }
            }
        }
    }

    public LongnvWarInfo getWarInfo(Player player) throws WSException {
        if (this.bo == null) {
            return null;
        } else {
            LongnvWarInfo info = new LongnvWarInfo();
            info.level = this.bo.getLevel();
            info.robotLevel = this.guild.bo.getLnwarLevel();
            info.leftTime = Math.max(0, this.bo.getChallengeTime() + LongnvWarConfig.challengeCD() - CommTime.nowSecond());
            info.isApply = this.joinPlayers.contains(player);
            info.pickTimes = player.getFeature(GuildMemberFeature.class).bo.getLongnvPickReward();
            info.result = this.bo.getResult();
            info.puppet = this.getPuppetInfo(player);
            info.leftRebirthTime = 86400 - (CommTime.nowSecond() - this.bo.getChallengeTime());
            return info;
        }
    }

    public int getAtkPuppetNum() {
        int num = 0;

        for (int i = 0; i < this.road; i++) {
            GuildWarMgr.Road road = this.warRoad.get(i);

            for (Player player : road.atkplayers) {
                if (GuildWarConfig.puppetPlayer.class.isInstance(player)) {
                    num++;
                }
            }
        }

        return num;
    }

    public int getDefPuppetNum() {
        int num = 0;

        for (int i = 0; i < this.road; i++) {
            GuildWarMgr.Road road = this.warRoad.get(i);

            for (Player player : road.defplayers) {
                if (GuildWarConfig.puppetPlayer.class.isInstance(player)) {
                    num++;
                }
            }
        }

        return num;
    }

    public boolean isWarOver() {
        int overRoad = 0;

        for (GuildWarMgr.Road road : this.warRoad) {
            if (road.isOver) {
                overRoad++;
            }
        }

        return overRoad == this.road;
    }

    public LongnvWarFightProtol getFightInfo(Player player) throws WSException {
        LongnvWarFightProtol info = new LongnvWarFightProtol();
        if (this.warRoad.size() == 0) {
            throw new WSException(ErrorCode.GuildWar_NotFoundFight, "战斗未开始");
        } else if (this.isWarOver()) {
            throw new WSException(ErrorCode.GuildWar_NotFoundFight, "战斗已结束");
        } else {
            Guild.GuildSummary summary = new Guild.GuildSummary();
            summary.setGuildId(-1L);
            summary.setGuildName("神秘帮会");
            info.setAtk(summary);
            info.setDef(new Guild.GuildSummary(this.guild));
            List<LongnvWarFightProtol.GuildInfo> guildInfo = new ArrayList<>();
            guildInfo.add(new LongnvWarFightProtol.GuildInfo(info.getAtk().getGuildName(), this.getleftAtk(), this.getDeadAtk(), this.getAtkPuppetNum()));
            guildInfo.add(new LongnvWarFightProtol.GuildInfo(info.getDef().getGuildName(), this.getleftDef(), this.getDeadDef(), this.getDefPuppetNum()));
            info.setGuildInfo(guildInfo);
            List<LongnvWarFightProtol.ResultRecord> result = new ArrayList<>();

            for (LongnvResultBO bo : this.fightResult) {
                result.add(new LongnvWarFightProtol.ResultRecord(bo));
            }

            info.setResultInfo(result);
            info.setEndTime(this.nextRefreshTime());
            info.setKillnum(this.getKillNum(player.getPid()));
            List<LongnvWarFightProtol.RoadSummry> summarylist = new ArrayList<>();

            for (GuildWarMgr.Road road : this.warRoad) {
                summarylist.add(new LongnvWarFightProtol.RoadSummry(road));
            }

            info.setRoad(summarylist);

            for (GuildWarMgr.Road road : this.warRoad) {
                if (road.getDeadatkplayers().contains(player) || road.getDeaddefplayers().contains(player)) {
                    info.setDead(true);
                    break;
                }
            }

            info.setRebirthTime(player.getFeature(PlayerRecord.class).getValue(DailyRefresh.LongnvRebirth));
            return info;
        }
    }

    public int nextRefreshTime() {
        int time = CommTime.getTodayZeroClockS();
        time = time + 39600 + LongnvWarConfig.challengeCD();
        return time + GuildWarConfig.fightTime();
    }

    public int getKillNum(long pid) {
        int num = 0;

        for (LongnvResultBO bo : this.fightResult) {
            if (bo.getDefpid() == pid && bo.getResult() == FightResult.Lost.ordinal()) {
                num++;
            }
        }

        return num;
    }

    public Guild.rebirth rebirth(Player player) throws WSException {
        if (!this.joinPlayers.contains(player)) {
            throw new WSException(ErrorCode.Longnv_NotApply, "玩家未报名");
        } else {
            int time = this.bo.getChallengeTime() + LongnvWarConfig.challengeCD() - CommTime.nowSecond();
            if (time < 0) {
                this.rebirthFighting(player);
                PlayerRecord recorder = player.getFeature(PlayerRecord.class);
                return new Guild.rebirth(this.getTotalPuppet(), this.getPersonPuppet(player.getPid()), recorder.getValue(DailyRefresh.LongnvRebirth));
            } else {
                PlayerCurrency currency = player.getFeature(PlayerCurrency.class);
                PlayerRecord recorder = player.getFeature(PlayerRecord.class);
                int curTimes = recorder.getValue(DailyRefresh.LongnvRebirth);
                RefCrystalPrice prize = RefCrystalPrice.getPrize(curTimes);
                if (!currency.check(PrizeType.Crystal, prize.LongnvRebirth)) {
                    throw new WSException(ErrorCode.NotEnough_Crystal, "玩家召唤傀儡需要钻石%s", prize.LongnvRebirth);
                } else {
                    currency.consume(PrizeType.Crystal, prize.LongnvRebirth, ItemFlow.Longnv_Rebirth);
                    recorder.addValue(DailyRefresh.LongnvRebirth);
                    LongnvwarpuppetBO bo = new LongnvwarpuppetBO();
                    bo.setPid(player.getPid());
                    bo.setGuildId(this.guild.getGuildId());
                    bo.setPuppetId(curTimes);
                    bo.setApplyTime(CommTime.nowSecond());
                    bo.insert();
                    if (this.puppets.get(player.getPid()) != null) {
                        this.puppets.get(player.getPid()).add(bo);
                    } else {
                        List<LongnvwarpuppetBO> list = new ArrayList<>();
                        list.add(bo);
                        this.puppets.put(player.getPid(), list);
                    }

                    this.guild.broadcast("beforeLongnvPuppet", this.getTotalPuppet(), player.getPid());
                    return new Guild.rebirth(this.getTotalPuppet(), this.getPersonPuppet(player.getPid()), recorder.getValue(DailyRefresh.LongnvRebirth));
                }
            }
        }
    }

    public int getTotalPuppet() {
        int num = 0;

        for (List<LongnvwarpuppetBO> list : this.puppets.values()) {
            num += list.size();
        }

        return num;
    }

    public int getPersonPuppet(long pid) {
        int num = 0;
        List<LongnvwarpuppetBO> list = this.puppets.get(pid);
        if (list != null) {
            num = list.size();
        }

        return num;
    }

    public LongnvWarFightProtol rebirthFighting(Player player) throws WSException {
        if (this.isWarOver()) {
            throw new WSException(ErrorCode.Longnv_NotFoundFight, "战斗已结束");
        } else {
            PlayerCurrency currency = player.getFeature(PlayerCurrency.class);
            PlayerRecord recorder = player.getFeature(PlayerRecord.class);
            int curTimes = recorder.getValue(DailyRefresh.LongnvRebirth);
            RefCrystalPrice prize = RefCrystalPrice.getPrize(curTimes);
            if (!currency.check(PrizeType.Crystal, prize.LongnvRebirth)) {
                throw new WSException(ErrorCode.NotEnough_Crystal, "玩家复活需要钻石%s", prize.LongnvRebirth);
            } else {
                currency.consume(PrizeType.Crystal, prize.LongnvRebirth, ItemFlow.Longnv_Rebirth);
                recorder.addValue(DailyRefresh.LongnvRebirth);
                List<GuildWarMgr.Road> list = this.warRoad;
                List<GuildWarMgr.Road> tmp_list = new ArrayList<>(list);
                Collections.shuffle(tmp_list);
                Optional<GuildWarMgr.Road> find = tmp_list.stream().filter(x -> x.getWinner() == 0L).findAny();
                if (find.isPresent()) {
                    GuildWarConfig.puppetPlayer p_player = new GuildWarConfig.puppetPlayer(player.getPlayerBO());
                    p_player.setPuppet_id(curTimes);
                    p_player.setIs_puppet(true);
                    find.get().getDefplayers().add(p_player);
                }

                LongnvwarpuppetBO bo = new LongnvwarpuppetBO();
                bo.setPid(player.getPid());
                bo.setGuildId(this.guild.getGuildId());
                bo.setPuppetId(curTimes);
                bo.setApplyTime(CommTime.nowSecond());
                bo.insert();
                if (this.puppets.get(player.getPid()) != null) {
                    this.puppets.get(player.getPid()).add(bo);
                } else {
                    List<LongnvwarpuppetBO> listp = new ArrayList<>();
                    listp.add(bo);
                    this.puppets.put(player.getPid(), listp);
                }

                this.total++;
                int num = 0;
                if (this.playerPuppet.get(player.getPid()) != null) {
                    num = this.playerPuppet.get(player.getPid());
                }

                this.playerPuppet.put(player.getPid(), ++num);
                SyncTaskManager.task(() -> {
                    for (Long pid : this.guild.getMembers()) {
                        Player player_tmp = PlayerMgr.getInstance().getPlayer(pid);
                        if (player_tmp != player) {
                            try {
                                player_tmp.pushProto("longnvRebirth", this.getFightInfo(player_tmp));
                            } catch (WSException var6x) {
                                var6x.printStackTrace();
                            }
                        }
                    }
                });
                return this.getFightInfo(player);
            }
        }
    }

    public void dailyRefresh() {
        if (this.bo != null) {
            int level = this.guild.bo.getLnwarLevel();
            if (this.bo.getResult() == FightResult.Win.ordinal()) {
                int max = RefDataMgr.getAll(RefLongnvWarLevel.class).size();
                int now = Math.min(level + 1, max);
                this.guild.bo.saveLnwarLevel(now);
            }

            if (this.bo.getResult() == FightResult.Lost.ordinal()) {
                int now = Math.max(0, level - 1);
                this.guild.bo.saveLnwarLevel(now);
            }

            for (GuildMemberFeature feature : this.guild.getAllMemberFeatures()) {
                feature.bo.saveLongnvPickReward(0);
            }

            this.puppets.clear();
        }
    }

    public Guild.rebirth getPuppetInfo(Player player) {
        PlayerRecord recorder = player.getFeature(PlayerRecord.class);
        return new Guild.rebirth(this.getTotalPuppet(), this.getPersonPuppet(player.getPid()), recorder.getValue(DailyRefresh.LongnvRebirth));
    }

    public Reward pickReward(Player player) throws WSException {
        if (!LongnvWarConfig.pickRewardTime()) {
            throw new WSException(ErrorCode.Longnv_NotFoundFight, "不在领奖时间内");
        } else if (this.bo != null && this.bo.getResult() == 1) {
            int picktime = player.getFeature(GuildMemberFeature.class).bo.getLongnvPickReward();
            if (picktime != 0) {
                throw new WSException(ErrorCode.Already_Picked, "奖励已领取");
            } else {
                player.getFeature(GuildMemberFeature.class).bo.saveLongnvPickReward(picktime + 1);
                RefLongnvLevel reflevel = RefDataMgr.get(RefLongnvLevel.class, this.bo.getLevel());
                RefReward ref = RefDataMgr.get(RefReward.class, reflevel.RewardId);
                Reward reward = ref.genReward();
                player.getFeature(PlayerItem.class).gain(reward, ItemFlow.Longnv_Win);
                return reward;
            }
        } else {
            throw new WSException(ErrorCode.Longnv_NotWin, "没有成功守卫龙女");
        }
    }
}
