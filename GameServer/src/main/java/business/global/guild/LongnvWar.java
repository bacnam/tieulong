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
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.FightResult;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Maps;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.*;
import core.database.game.bo.LongnvApplyBO;
import core.database.game.bo.LongnvResultBO;
import core.database.game.bo.LongnvWarResultBO;
import core.database.game.bo.LongnvwarpuppetBO;
import core.network.proto.Fight;
import core.network.proto.LongnvWarFightProtol;
import core.network.proto.LongnvWarInfo;

import java.util.*;

public class LongnvWar {
    public final Map<Long, List<LongnvwarpuppetBO>> puppets;
    public Map<Long, List<Double>> playersHP;
    public List<LongnvResultBO> fightResult;
    public Map<Long, Fight.Battle> fightBattle;
    public List<Player> joinPlayers;
    Guild guild;
    LongnvWarResultBO bo;
    List<GuildWarMgr.Road> warRoad;
    int fightTime;
    int total;
    int enemyTotal;
    Map<Long, Integer> playerPuppet;
    private int road;

    public LongnvWar(Guild guild) {
        this.warRoad = new ArrayList<>();

        this.road = 3;

        this.playersHP = Maps.newConcurrentHashMap();

        this.fightResult = new ArrayList<>();

        this.fightBattle = Maps.newConcurrentHashMap();

        this.puppets = Maps.newConcurrentHashMap();

        this.joinPlayers = new ArrayList<>();

        this.playerPuppet = new HashMap<>();
        this.guild = guild;
        init();
    }

    public void init() {
        List<LongnvApplyBO> applys = BM.getBM(LongnvApplyBO.class).findAll("guildId", Long.valueOf(this.guild.getGuildId()));
        for (LongnvApplyBO apply : applys) {
            if (apply.getApplyTime() < CommTime.getTodayZeroClockS()) {
                apply.del();
                continue;
            }
            Player player = PlayerMgr.getInstance().getPlayer(apply.getPid());
            this.joinPlayers.add(player);
        }

        List<LongnvwarpuppetBO> puppets = BM.getBM(LongnvwarpuppetBO.class).findAll();
        for (LongnvwarpuppetBO puppet : puppets) {
            Guild guild = GuildMgr.getInstance().getGuild(puppet.getGuildId());
            if (guild == null) {
                puppet.del();
                continue;
            }
            if (puppet.getApplyTime() < CommTime.getTodayZeroClockS()) {
                puppet.del();
                continue;
            }
            if (this.puppets.get(Long.valueOf(puppet.getPid())) != null) {
                ((List<LongnvwarpuppetBO>) this.puppets.get(Long.valueOf(puppet.getPid()))).add(puppet);
                continue;
            }
            List<LongnvwarpuppetBO> list = new ArrayList<>();
            list.add(puppet);
            this.puppets.put(Long.valueOf(puppet.getPid()), list);
        }

        List<LongnvWarResultBO> results = BM.getBM(LongnvWarResultBO.class).findAll("guildId", Long.valueOf(this.guild.getGuildId()));
        for (LongnvWarResultBO result : results) {
            if (result.getChallengeTime() < CommTime.getTodayZeroClockS()) {
                result.del();
                continue;
            }
            this.bo = result;
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

        List<Player> defpuppets = new ArrayList<>();

        RefLongnvWarLevel ref = (RefLongnvWarLevel) RefDataMgr.get(RefLongnvWarLevel.class, Integer.valueOf(this.guild.bo.getLnwarLevel() + 1));
        if (ref.RobotLevel != 0) {
            List<Player> players = RobotManager.getInstance().getLvlPlayers(ref.RobotLevel);

            if (players == null) {
                players = RobotManager.getInstance().getLvlPlayers(38);
            }
            atkList.addAll(players.subList(0, ref.Amount));
        } else {

            long guildid = RankManager.getInstance().getPlayerId(RankType.Guild, 1);
            List<Long> members = GuildMgr.getInstance().getGuild(guildid).getMembers();
            List<Record> record = RankManager.getInstance().getRankList(RankType.Power, ref.Amount);
            for (Record r : record) {
                if (r == null) {
                    continue;
                }
                members.add(Long.valueOf(r.getPid()));
            }
            for (Long pid : members) {
                Player player = PlayerMgr.getInstance().getPlayer(pid.longValue());
                atkList.add(player);
            }
        }

        defList.addAll(this.joinPlayers);
        defpuppets = createPuppets();

        for (Map.Entry<Long, List<LongnvwarpuppetBO>> entry : this.puppets.entrySet()) {
            this.playerPuppet.put(entry.getKey(), Integer.valueOf(((List) entry.getValue()).size()));
        }

        this.total = defList.size() + defpuppets.size();
        this.enemyTotal = atkList.size();

        int j;
        for (j = 0; j < defList.size(); j++) {
            int index = j % this.road;
            ((GuildWarMgr.Road) this.warRoad.get(index)).getDefplayers().add(defList.get(j));
        }

        for (j = 0; j < defpuppets.size(); j++) {
            int index = j % this.road;
            ((GuildWarMgr.Road) this.warRoad.get(index)).getDefplayers().add(defpuppets.get(j));
        }

        for (j = 0; j < atkList.size(); j++) {
            int index = j % this.road;
            ((GuildWarMgr.Road) this.warRoad.get(index)).getAtkplayers().add(atkList.get(j));
        }
    }

    public void fight() {
        prepareFight();
        beginFight();
    }

    public void Start() {
        if (this.guild.getLevel() < RefDataMgr.getFactor("LongnvUnlockLv", 5)) {
            return;
        }
        NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.LongnvUnderAttack, this.guild, new String[0]);
        dailyRefresh();
        NPCchallenge();
        SyncTaskManager.schedule(LongnvWarConfig.challengeCD() * 1000, () -> {
            fight();
            return false;
        });
    }

    public void Restart() {
        if (this.bo == null) {
            return;
        }
        if (this.bo.getResult() != 0 || this.bo.getChallengeTime() < CommTime.getTodayZeroClockS()) {
            return;
        }
        int time = this.bo.getChallengeTime() + LongnvWarConfig.challengeCD() - CommTime.nowSecond();
        SyncTaskManager.schedule(Math.max(0, time) * 1000, () -> {
            try {
                fight();
            } catch (Exception e) {
                return false;
            }
            return false;
        });
    }

    public void beginFight() {
        this.fightTime = CommTime.nowSecond();
        for (int i = 0; i < this.road; i++) {
            GuildWarMgr.Road road = this.warRoad.get(i);
            road.setBegin(CommTime.nowSecond());
            if (road.getAtkplayers().size() == 0 || road.getDefplayers().size() == 0) {
                road.setOverTime(CommTime.nowSecond());
            }
            SyncTaskManager.task(() -> SyncTaskManager.schedule(LongnvWarConfig.oneFightTime(), () -> {
                System.out.println("Chiến đấu kết thúc sau thời gian quy định.");
                return false;
            }));
        }
    }

    private boolean roadFight(GuildWarMgr.Road road) {
        synchronized (this) {
            road.setBegin(CommTime.nowSecond());

            if (atkResult(road)) {
                return false;
            }

            if (timeOver(road)) {
                return false;
            }

            if (road.getWinner() != 0L) {
                return false;
            }
            Player atkPlayer = null;
            Player defPlayer = null;
            if (road.getAtkplayers().size() > 0) {
                atkPlayer = road.getAtkplayers().get(0);
            }
            if (road.getDefplayers().size() > 0) {
                defPlayer = road.getDefplayers().get(0);
            }
            if (atkPlayer == null || defPlayer == null) {
                if (road.getOverTime() != 0 && CommTime.nowSecond() - road.getOverTime() > LongnvWarConfig.overTime()) {
                    if (atkPlayer == null) {
                        road.setWinner(2L);
                        Optional<GuildWarMgr.Road> find = this.warRoad.stream().parallel().filter(x -> (x != paramRoad1 && x.getWinner() == 0L)).findAny();
                        if (find.isPresent()) {
                            ((GuildWarMgr.Road) find.get()).getDefplayers().addAll(road.getDefplayers());
                            road.getDefplayers().clear();
                        }
                    } else {
                        road.setWinner(1L);
                        Optional<GuildWarMgr.Road> find = this.warRoad.stream().parallel().filter(x -> (x != paramRoad1 && x.getWinner() == 0L)).findAny();
                        if (find.isPresent()) {
                            ((GuildWarMgr.Road) find.get()).getAtkplayers().addAll(road.getAtkplayers());
                            road.getAtkplayers().clear();
                        }
                    }
                    return false;
                }
                if (road.getOverTime() == 0) {
                    road.setOverTime(CommTime.nowSecond());
                }
            } else {

                SimulatBattle battle = new SimulatBattle(atkPlayer, defPlayer);
                battle.initHp(this.playersHP.get(Long.valueOf(atkPlayer.getPid())), this.playersHP.get(Long.valueOf(defPlayer.getPid())));
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
                this.fightBattle.put(Long.valueOf(resultbo.getId()), battle.proto());
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

                    this.playersHP.put(Long.valueOf(winner.getPid()), battle.getWinnerHp());

                    if (GuildWarConfig.puppetPlayer.class.isInstance(lose)) {
                        removePuppet(lose);
                    }
                }

                if (road.getDefplayers().size() == 0 || road.getAtkplayers().size() == 0) {
                    road.setOverTime(CommTime.nowSecond());
                }
            }
        }

        return true;
    }

    public void removePuppet(Player puppet) {
        List<LongnvwarpuppetBO> puppets = this.puppets.get(Long.valueOf(puppet.getPid()));
        LongnvwarpuppetBO find = null;
        if (puppets != null) {
            GuildWarConfig.puppetPlayer puppetplayer = (GuildWarConfig.puppetPlayer) puppet;
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
            }
            int atknum = getleftAtk();
            int defnum = getleftDef();
            if (atknum == 0 || defnum == 0) {
                this.bo.setFightTime(CommTime.nowSecond());
                if (atknum == 0) {
                    this.bo.setResult(FightResult.Win.ordinal());
                    RefLongnvWarLevel ref = (RefLongnvWarLevel) RefDataMgr.get(RefLongnvWarLevel.class, Integer.valueOf(this.guild.bo.getLnwarLevel() + 1));
                    NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.LongnvWin, new String[]{this.guild.getName(), ref.GuildName});
                }
                if (defnum == 0) {
                    this.bo.setResult(FightResult.Lost.ordinal());
                }
                this.bo.saveAll();

                dealWarFinish();
                return true;
            }
        }

        return false;
    }

    public boolean timeOver(GuildWarMgr.Road road) {
        synchronized (this) {
            if (road.isOver) {
                return true;
            }

            if (CommTime.nowSecond() - this.fightTime > LongnvWarConfig.fightTime()) {
                this.bo.setFightTime(CommTime.nowSecond());
                this.bo.setResult(FightResult.Win.ordinal());
                this.bo.saveAll();
                dealWarFinish();
                return true;
            }
        }
        return false;
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
            personReward(this.fightResult);

            this.playersHP.clear();
            this.guild.broadcast("warFinish", "");
            roportAll();
        }
    }

    public void roportAll() {
        String myGuild = this.guild.getName();

        String total = String.valueOf(this.total);

        String dead = String.valueOf(getdeadDef());

        String enemyTotal = String.valueOf(this.enemyTotal);

        String enemyDead = String.valueOf(getdeadAtk());

        SyncTaskManager.task(() -> {
            RefLongnvWarLevel ref = (RefLongnvWarLevel) RefDataMgr.get(RefLongnvWarLevel.class, Integer.valueOf(this.guild.bo.getLnwarLevel() + 1));

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
                        puppet = ((Integer) this.playerPuppet.get(pid)).intValue();
                    }
                } catch (Exception exception) {
                }
                kill = getKillNum(pid.longValue());
                MailCenter.getInstance().sendMail(pid.longValue(), mailId, new String[]{paramString1, paramString2, paramString3, EnemyGuild, paramString4, paramString5, (new StringBuilder(String.valueOf(puppet))).toString(), (new StringBuilder(String.valueOf(kill))).toString(), result});
            }
        });
    }

    public void personReward(List<LongnvResultBO> list) {
        List<GuildWarMgr.Road> roads = this.warRoad;

        int defwin = 0;
        for (GuildWarMgr.Road road : roads) {
            if (road.getWinner() == 0L)
                continue;
            if (road.getDefplayers().size() != 0) {
                defwin++;
            }
        }

        Map<Long, List<LongnvResultBO>> map = Maps.newConcurrentHashMap();
        for (LongnvResultBO bo : list) {
            if (bo.getResult() == FightResult.Lost.ordinal()) {
                if (map.get(Long.valueOf(bo.getDefpid())) != null) {
                    ((List<LongnvResultBO>) map.get(Long.valueOf(bo.getDefpid()))).add(bo);
                    continue;
                }
                List<LongnvResultBO> tmp_list = new ArrayList<>();
                tmp_list.add(bo);
                map.put(Long.valueOf(bo.getDefpid()), tmp_list);
            }
        }

        for (Map.Entry<Long, List<LongnvResultBO>> entry : map.entrySet()) {
            int num = 1;
            Player player = PlayerMgr.getInstance().getPlayer(((Long) entry.getKey()).longValue());
            num = Math.max(num, defwin);
            Reward rewardall = new Reward();
            Reward reward = RefLongnvWarPersonReward.getReward(((List) entry.getValue()).size());
            for (int i = 0; i < num; i++) {
                rewardall.combine(reward);
            }
            MailCenter.getInstance().sendMail(player.getPid(), "GM", "守卫龙女个人奖励", "在龙女守卫战中你奋勇杀敌，获得以下奖励", rewardall, new String[0]);
        }
    }

    public List<Player> createPuppets() {
        List<Player> puppets = new ArrayList<>();
        for (Map.Entry<Long, List<LongnvwarpuppetBO>> entry : this.puppets.entrySet()) {
            Player player = PlayerMgr.getInstance().getPlayer(((Long) entry.getKey()).longValue());
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
        }
        int time = this.bo.getChallengeTime() + LongnvWarConfig.challengeCD() - CommTime.nowSecond();
        if (time < 0) {
            throw new WSException(ErrorCode.Longnv_NotFoundFight, "报名时间已过");
        }
        if (!this.joinPlayers.contains(player)) {
            LongnvApplyBO apply = new LongnvApplyBO();
            apply.setPid(player.getPid());
            apply.setGuildId(this.guild.getGuildId());
            apply.setApplyTime(CommTime.nowSecond());
            apply.insert();
            this.joinPlayers.add(player);
        }
    }

    public LongnvWarInfo getWarInfo(Player player) throws WSException {
        if (this.bo == null) {
            return null;
        }

        LongnvWarInfo info = new LongnvWarInfo();
        info.level = this.bo.getLevel();
        info.robotLevel = this.guild.bo.getLnwarLevel();
        info.leftTime = Math.max(0, this.bo.getChallengeTime() + LongnvWarConfig.challengeCD() - CommTime.nowSecond());
        info.isApply = this.joinPlayers.contains(player);
        info.pickTimes = ((GuildMemberFeature) player.getFeature(GuildMemberFeature.class)).bo.getLongnvPickReward();
        info.result = this.bo.getResult();
        info.puppet = getPuppetInfo(player);
        info.leftRebirthTime = 86400 - CommTime.nowSecond() - this.bo.getChallengeTime();
        return info;
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
        if (overRoad == this.road) {
            return true;
        }

        return false;
    }

    public LongnvWarFightProtol getFightInfo(Player player) throws WSException {
        LongnvWarFightProtol info = new LongnvWarFightProtol();
        if (this.warRoad.size() == 0) {
            throw new WSException(ErrorCode.GuildWar_NotFoundFight, "战斗未开始");
        }
        if (isWarOver()) {
            throw new WSException(ErrorCode.GuildWar_NotFoundFight, "战斗已结束");
        }

        Guild.GuildSummary summary = new Guild.GuildSummary();
        summary.setGuildId(-1L);
        summary.setGuildName("神秘帮会");
        info.setAtk(summary);

        info.setDef(new Guild.GuildSummary(this.guild));

        List<LongnvWarFightProtol.GuildInfo> guildInfo = new ArrayList<>();
        guildInfo.add(new LongnvWarFightProtol.GuildInfo(info.getAtk().getGuildName(), getleftAtk(), getDeadAtk(), getAtkPuppetNum()));
        guildInfo.add(new LongnvWarFightProtol.GuildInfo(info.getDef().getGuildName(), getleftDef(), getDeadDef(), getDefPuppetNum()));
        info.setGuildInfo(guildInfo);
        List<LongnvWarFightProtol.ResultRecord> result = new ArrayList<>();
        for (LongnvResultBO bo : this.fightResult) {
            result.add(new LongnvWarFightProtol.ResultRecord(bo));
        }
        info.setResultInfo(result);
        info.setEndTime(nextRefreshTime());
        info.setKillnum(getKillNum(player.getPid()));
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
        info.setRebirthTime(((PlayerRecord) player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.LongnvRebirth));
        return info;
    }

    public int nextRefreshTime() {
        int time = CommTime.getTodayZeroClockS();
        time = time + 39600 + LongnvWarConfig.challengeCD();
        time += GuildWarConfig.fightTime();
        return time;
    }

    public int getKillNum(long pid) {
        int num = 0;
        List<LongnvResultBO> list = this.fightResult;
        for (LongnvResultBO bo : list) {
            if (bo.getDefpid() == pid && bo.getResult() == FightResult.Lost.ordinal()) {
                num++;
            }
        }
        return num;
    }

    public Guild.rebirth rebirth(Player player) throws WSException {
        if (!this.joinPlayers.contains(player)) {
            throw new WSException(ErrorCode.Longnv_NotApply, "玩家未报名");
        }
        int time = this.bo.getChallengeTime() + LongnvWarConfig.challengeCD() - CommTime.nowSecond();
        if (time < 0) {
            rebirthFighting(player);
            PlayerRecord playerRecord = (PlayerRecord) player.getFeature(PlayerRecord.class);
            return new Guild.rebirth(getTotalPuppet(), getPersonPuppet(player.getPid()), playerRecord.getValue(ConstEnum.DailyRefresh.LongnvRebirth));
        }
        PlayerCurrency currency = (PlayerCurrency) player.getFeature(PlayerCurrency.class);
        PlayerRecord recorder = (PlayerRecord) player.getFeature(PlayerRecord.class);

        int curTimes = recorder.getValue(ConstEnum.DailyRefresh.LongnvRebirth);
        RefCrystalPrice prize = RefCrystalPrice.getPrize(curTimes);
        if (!currency.check(PrizeType.Crystal, prize.LongnvRebirth)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "玩家召唤傀儡需要钻石%s", new Object[]{Integer.valueOf(prize.LongnvRebirth)});
        }
        currency.consume(PrizeType.Crystal, prize.LongnvRebirth, ItemFlow.Longnv_Rebirth);
        recorder.addValue(ConstEnum.DailyRefresh.LongnvRebirth);
        LongnvwarpuppetBO bo = new LongnvwarpuppetBO();
        bo.setPid(player.getPid());
        bo.setGuildId(this.guild.getGuildId());
        bo.setPuppetId(curTimes);
        bo.setApplyTime(CommTime.nowSecond());
        bo.insert();
        if (this.puppets.get(Long.valueOf(player.getPid())) != null) {
            ((List<LongnvwarpuppetBO>) this.puppets.get(Long.valueOf(player.getPid()))).add(bo);
        } else {
            List<LongnvwarpuppetBO> list = new ArrayList<>();
            list.add(bo);
            this.puppets.put(Long.valueOf(player.getPid()), list);
        }

        this.guild.broadcast("beforeLongnvPuppet", Integer.valueOf(getTotalPuppet()), player.getPid());

        return new Guild.rebirth(getTotalPuppet(), getPersonPuppet(player.getPid()), recorder.getValue(ConstEnum.DailyRefresh.LongnvRebirth));
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
        List<LongnvwarpuppetBO> list = this.puppets.get(Long.valueOf(pid));
        if (list != null) {
            num = list.size();
        }
        return num;
    }

    public LongnvWarFightProtol rebirthFighting(Player player) throws WSException {
        if (isWarOver()) {
            throw new WSException(ErrorCode.Longnv_NotFoundFight, "战斗已结束");
        }

        PlayerCurrency currency = (PlayerCurrency) player.getFeature(PlayerCurrency.class);
        PlayerRecord recorder = (PlayerRecord) player.getFeature(PlayerRecord.class);

        int curTimes = recorder.getValue(ConstEnum.DailyRefresh.LongnvRebirth);
        RefCrystalPrice prize = RefCrystalPrice.getPrize(curTimes);
        if (!currency.check(PrizeType.Crystal, prize.LongnvRebirth)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "玩家复活需要钻石%s", new Object[]{Integer.valueOf(prize.LongnvRebirth)});
        }
        currency.consume(PrizeType.Crystal, prize.LongnvRebirth, ItemFlow.Longnv_Rebirth);
        recorder.addValue(ConstEnum.DailyRefresh.LongnvRebirth);
        List<GuildWarMgr.Road> list = this.warRoad;
        List<GuildWarMgr.Road> tmp_list = new ArrayList<>(list);
        Collections.shuffle(tmp_list);
        Optional<GuildWarMgr.Road> find = tmp_list.stream().filter(x -> (x.getWinner() == 0L)).findAny();
        if (find.isPresent()) {
            GuildWarConfig.puppetPlayer p_player = new GuildWarConfig.puppetPlayer(player.getPlayerBO());
            p_player.setPuppet_id(curTimes);
            p_player.setIs_puppet(true);
            ((GuildWarMgr.Road) find.get()).getDefplayers().add(p_player);
        }
        LongnvwarpuppetBO bo = new LongnvwarpuppetBO();
        bo.setPid(player.getPid());
        bo.setGuildId(this.guild.getGuildId());
        bo.setPuppetId(curTimes);
        bo.setApplyTime(CommTime.nowSecond());
        bo.insert();
        if (this.puppets.get(Long.valueOf(player.getPid())) != null) {
            ((List<LongnvwarpuppetBO>) this.puppets.get(Long.valueOf(player.getPid()))).add(bo);
        } else {
            List<LongnvwarpuppetBO> listp = new ArrayList<>();
            listp.add(bo);
            this.puppets.put(Long.valueOf(player.getPid()), listp);
        }

        this.total++;

        int num = 0;
        if (this.playerPuppet.get(Long.valueOf(player.getPid())) != null) {
            num = ((Integer) this.playerPuppet.get(Long.valueOf(player.getPid()))).intValue();
        }
        num++;
        this.playerPuppet.put(Long.valueOf(player.getPid()), Integer.valueOf(num));

        SyncTaskManager.task(() -> {
            for (Long pid : this.guild.getMembers()) {
                Player player_tmp = PlayerMgr.getInstance().getPlayer(pid.longValue());
                if (player_tmp == paramPlayer)
                    continue;
                try {
                    player_tmp.pushProto("longnvRebirth", getFightInfo(player_tmp));
                } catch (WSException e) {
                    e.printStackTrace();
                }
            }
        });

        return getFightInfo(player);
    }

    public void dailyRefresh() {
        if (this.bo == null) {
            return;
        }
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

    public Guild.rebirth getPuppetInfo(Player player) {
        PlayerRecord recorder = (PlayerRecord) player.getFeature(PlayerRecord.class);
        return new Guild.rebirth(getTotalPuppet(), getPersonPuppet(player.getPid()), recorder.getValue(ConstEnum.DailyRefresh.LongnvRebirth));
    }

    public Reward pickReward(Player player) throws WSException {
        if (!LongnvWarConfig.pickRewardTime()) {
            throw new WSException(ErrorCode.Longnv_NotFoundFight, "不在领奖时间内");
        }
        if (this.bo == null || this.bo.getResult() != 1) {
            throw new WSException(ErrorCode.Longnv_NotWin, "没有成功守卫龙女");
        }
        int picktime = ((GuildMemberFeature) player.getFeature(GuildMemberFeature.class)).bo.getLongnvPickReward();
        if (picktime != 0) {
            throw new WSException(ErrorCode.Already_Picked, "奖励已领取");
        }
        ((GuildMemberFeature) player.getFeature(GuildMemberFeature.class)).bo.saveLongnvPickReward(picktime + 1);
        RefLongnvLevel reflevel = (RefLongnvLevel) RefDataMgr.get(RefLongnvLevel.class, Integer.valueOf(this.bo.getLevel()));
        RefReward ref = (RefReward) RefDataMgr.get(RefReward.class, Integer.valueOf(reflevel.RewardId));
        Reward reward = ref.genReward();
        ((PlayerItem) player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Longnv_Win);
        return reward;
    }
}

