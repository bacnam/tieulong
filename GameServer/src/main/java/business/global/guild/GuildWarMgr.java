package business.global.guild;

import BaseTask.SyncTask.SyncTaskManager;
import business.global.battle.SimulatBattle;
import business.global.gmmail.MailCenter;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.RobotManager;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import business.player.feature.guild.GuildMemberFeature;
import business.player.item.Reward;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.FightResult;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Maps;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCrystalPrice;
import core.config.refdata.ref.RefGuildWarCenter;
import core.config.refdata.ref.RefGuildWarPersonReward;
import core.database.game.bo.GuildwarGuildResultBO;
import core.database.game.bo.GuildwarResultBO;
import core.database.game.bo.GuildwarapplyBO;
import core.database.game.bo.GuildwarpuppetBO;
import core.network.proto.Fight;
import core.network.proto.GuildWarCenterInfo;
import core.network.proto.GuildWarFightProtol;

import java.util.*;

public class GuildWarMgr {
    private static GuildWarMgr instance = new GuildWarMgr();
    public Map<Integer, List<Guild>> guildWarApplyer = Maps.newConcurrentHashMap();
    public Map<Integer, Guild> guildWarCenterOwner = Maps.newConcurrentHashMap();
    public Map<Integer, List<Guild>> guildWarAtk = Maps.newConcurrentHashMap();
    public Map<Long, Fight.Battle> fightBattle = Maps.newConcurrentHashMap();
    public Map<Long, List<GuildwarResultBO>> guildAllResulet = Maps.newConcurrentHashMap();
    public Map<Integer, List<Player>> watchPlayers = Maps.newConcurrentHashMap();
    public Map<Long, List<Double>> playersHP = Maps.newConcurrentHashMap();
    public Map<Integer, Guild> historyWinner = Maps.newConcurrentHashMap();
    public Map<Integer, List<Road>> guildWarRoad = Maps.newConcurrentHashMap();
    Map<Integer, Map<Long, Report>> reports = new HashMap<>();
    private int road = 3;
    private Map<Integer, Integer> fightTime = Maps.newConcurrentHashMap();
    private Map<Integer, List<GuildwarResultBO>> fightResult = Maps.newConcurrentHashMap();
    private Map<Integer, List<GuildwarGuildResultBO>> guildFightResult = Maps.newConcurrentHashMap();
    private Map<Integer, Center> fighters = Maps.newConcurrentHashMap();

    public static GuildWarMgr getInstance() {
        return instance;
    }

    public void dailyRefresh() {
        try {
            for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
                this.guildWarApplyer.put(Integer.valueOf(ref.id), new LinkedList<>());
                this.guildWarAtk.put(Integer.valueOf(ref.id), new LinkedList<>());

                this.guildWarRoad.put(Integer.valueOf(ref.id), new LinkedList<>());

                this.watchPlayers.put(Integer.valueOf(ref.id), new LinkedList<>());

                for (int i = 0; i < this.road; i++) {
                    ((List<Road>) this.guildWarRoad.get(Integer.valueOf(ref.id))).add(new Road());
                }
            }
            this.guildFightResult.clear();
            this.fightResult.clear();
            this.guildAllResulet.clear();
            this.fightBattle.clear();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void init() {
        for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
            this.guildWarApplyer.put(Integer.valueOf(ref.id), new LinkedList<>());
            this.guildWarAtk.put(Integer.valueOf(ref.id), new LinkedList<>());

            this.guildWarRoad.put(Integer.valueOf(ref.id), new LinkedList<>());

            this.watchPlayers.put(Integer.valueOf(ref.id), new LinkedList<>());

            for (int i = 0; i < this.road; i++) {
                ((List<Road>) this.guildWarRoad.get(Integer.valueOf(ref.id))).add(new Road());
            }
        }

        List<GuildwarapplyBO> guildwarapplys = BM.getBM(GuildwarapplyBO.class).findAllBySort("applyTime", true, 0);
        for (GuildwarapplyBO guildwarapply : guildwarapplys) {
            Guild guild = GuildMgr.getInstance().getGuild(guildwarapply.getGuildId());
            if (guild == null) {
                guildwarapply.del();
                continue;
            }
            if (guildwarapply.getWinCenterId() != 0) {
                this.historyWinner.put(Integer.valueOf(guildwarapply.getWinCenterId()), guild);
            }
            if (guildwarapply.getApplyTime() > CommTime.getTodayZeroClockS()) {
                guild.guildwarCenter = guildwarapply;
                ((List<Guild>) this.guildWarApplyer.get(Integer.valueOf(guildwarapply.getCenterId()))).add(guild);
            }
        }

        for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {

            if (this.historyWinner.get(Integer.valueOf(ref.id)) != null) {
                this.guildWarCenterOwner.put(Integer.valueOf(ref.id), this.historyWinner.get(Integer.valueOf(ref.id)));
            }
            int centerId = ref.id;
            Guild owner = this.guildWarCenterOwner.get(Integer.valueOf(centerId));
            if (owner == null) {

                ((List) this.guildWarAtk.get(Integer.valueOf(ref.id))).addAll(this.guildWarApplyer.get(Integer.valueOf(ref.id)));
                continue;
            }
            boolean flag = false;
            for (Guild tmp_guild : this.guildWarApplyer.get(Integer.valueOf(centerId))) {
                if (owner != tmp_guild && !flag) {
                    continue;
                }
                if (owner == tmp_guild) {
                    flag = true;
                    continue;
                }
                ((List<Guild>) this.guildWarAtk.get(Integer.valueOf(ref.id))).add(tmp_guild);
            }
        }

        if (CommTime.getTodayHour() >= 19) {
            Start();
        }
    }

    public Guild getAtkGuild(int centerId) {
        return (((List) this.guildWarAtk.get(Integer.valueOf(centerId))).size() > 0) ? ((List<Guild>) this.guildWarAtk.get(Integer.valueOf(centerId))).get(0) : null;
    }

    public void applyGuildWar(int centerId, Guild guild) throws WSException {
        if (((List) this.guildWarApplyer.get(Integer.valueOf(centerId))).size() >= RefDataMgr.getFactor("MaxGuildWarApply", 6)) {
            throw new WSException(ErrorCode.GuildWar_AlreadyApply, "据点申请已满");
        }
        synchronized (this) {
            ((List<Guild>) this.guildWarApplyer.get(Integer.valueOf(centerId))).add(guild);
            ((List<Guild>) this.guildWarAtk.get(Integer.valueOf(centerId))).add(guild);
        }
    }

    public void prepareFight() {
        for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
            if (!ref.isOpenTime()) {
                continue;
            }
            if (getAtkGuild(ref.id) == null) {
                continue;
            }
            int centerId = ref.id;

            Guild owner = this.guildWarCenterOwner.get(Integer.valueOf(centerId));
            List<Player> defList = new ArrayList<>();
            List<Player> atkList = new ArrayList<>();
            List<Player> defpuppets = new ArrayList<>();
            List<Player> atkpuppets = new ArrayList<>();

            this.guildWarRoad.put(Integer.valueOf(ref.id), new LinkedList<>());

            for (int i = 0; i < this.road; i++) {
                ((List<Road>) this.guildWarRoad.get(Integer.valueOf(ref.id))).add(new Road());
            }

            this.fightResult.put(Integer.valueOf(centerId), new ArrayList<>());

            this.fighters.put(Integer.valueOf(centerId), new Center(null));

            if (owner == null) {

                defList = RobotManager.getInstance().getRandomPlayers(14);
            } else {

                defList = owner.getGuildWarPlayer();
                defpuppets = owner.createPuppets();
            }

            Guild atkGuild = getAtkGuild(ref.id);
            while (atkGuild.getGuildWarPlayer().size() == 0) {

                ((List) this.guildWarApplyer.get(Integer.valueOf(ref.id))).remove(atkGuild);
                ((List) this.guildWarAtk.get(Integer.valueOf(ref.id))).remove(atkGuild);
                atkGuild = getAtkGuild(ref.id);
                if (atkGuild == null) {
                    break;
                }
            }
            if (atkGuild == null) {
                continue;
            }

            atkList = atkGuild.getGuildWarPlayer();

            atkpuppets = atkGuild.createPuppets();

            Map<Long, Report> reportmap = new HashMap<>();
            Report defreport = new Report(null);
            defreport.guild = owner;
            defreport.total = defList.size() + defpuppets.size();
            if (owner != null) {
                for (Map.Entry<Long, List<GuildwarpuppetBO>> entry : owner.puppets.entrySet()) {
                    defreport.puppet.put(entry.getKey(), Integer.valueOf(((List) entry.getValue()).size()));
                }
                reportmap.put(Long.valueOf(owner.getGuildId()), defreport);
            } else {
                reportmap.put(Long.valueOf(0L), defreport);
            }

            Report atkreport = new Report(null);
            atkreport.guild = atkGuild;
            atkreport.total = atkList.size() + atkpuppets.size();
            for (Map.Entry<Long, List<GuildwarpuppetBO>> entry : atkGuild.puppets.entrySet()) {
                atkreport.puppet.put(entry.getKey(), Integer.valueOf(((List) entry.getValue()).size()));
            }
            reportmap.put(Long.valueOf(atkGuild.getGuildId()), atkreport);
            this.reports.put(Integer.valueOf(centerId), reportmap);

            this.fighters.put(Integer.valueOf(centerId), new Center(atkList, atkGuild, defList, owner, null));

            int j;
            for (j = 0; j < defList.size(); j++) {
                int index = j % this.road;
                ((Road) ((List<Road>) this.guildWarRoad.get(Integer.valueOf(ref.id))).get(index)).getDefplayers().add(defList.get(j));
            }

            for (j = 0; j < defpuppets.size(); j++) {
                int index = j % this.road;
                ((Road) ((List<Road>) this.guildWarRoad.get(Integer.valueOf(ref.id))).get(index)).getDefplayers().add(defpuppets.get(j));
            }

            for (j = 0; j < atkList.size(); j++) {
                int index = j % this.road;
                ((Road) ((List<Road>) this.guildWarRoad.get(Integer.valueOf(ref.id))).get(index)).getAtkplayers().add(atkList.get(j));
            }

            for (j = 0; j < atkpuppets.size(); j++) {
                int index = j % this.road;
                ((Road) ((List<Road>) this.guildWarRoad.get(Integer.valueOf(ref.id))).get(index)).getAtkplayers().add(atkpuppets.get(j));
            }
        }
    }

    public void beginFightByCenterId(int centerId) {
        this.fightTime.put(Integer.valueOf(centerId), Integer.valueOf(CommTime.nowSecond()));
        for (int i = 0; i < this.road; i++) {
            Road road = ((List<Road>) this.guildWarRoad.get(Integer.valueOf(centerId))).get(i);
            road.setBegin(CommTime.nowSecond());
            if (road.getAtkplayers().size() == 0 || road.getDefplayers().size() == 0) {
                road.setOverTime(CommTime.nowSecond());
            }
            SyncTaskManager.task(() -> SyncTaskManager.schedule(GuildWarConfig.oneFightTime(), () -> {
                return false;
            }));
        }
    }

    private boolean roadFight(Road road, int centerId) {
        synchronized (this) {
            road.setBegin(CommTime.nowSecond());

            if (atkResult(centerId, road)) {
                return false;
            }

            if (timeOver(centerId, road)) {
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
                if (road.getOverTime() != 0 && CommTime.nowSecond() - road.getOverTime() + 2 > GuildWarConfig.overTime()) {
                    if (atkPlayer == null) {
                        if (RobotManager.getInstance().isRobot(defPlayer.getPid())) {
                            road.setWinner(-1L);
                        } else {
                            road.setWinner(((GuildMemberFeature) defPlayer.getFeature(GuildMemberFeature.class)).getGuildID());
                        }
                        Optional<Road> find = ((List<Road>) this.guildWarRoad.get(Integer.valueOf(centerId))).stream().parallel().filter(x -> (x != paramRoad1 && x.getWinner() == 0L)).findAny();
                        if (find.isPresent()) {
                            ((Road) find.get()).getDefplayers().addAll(road.getDefplayers());
                            road.getDefplayers().clear();
                        }
                    } else {

                        road.setWinner(((GuildMemberFeature) atkPlayer.getFeature(GuildMemberFeature.class)).getGuildID());
                        Optional<Road> find = ((List<Road>) this.guildWarRoad.get(Integer.valueOf(centerId))).stream().parallel().filter(x -> (x != paramRoad1 && x.getWinner() == 0L)).findAny();
                        if (find.isPresent()) {
                            ((Road) find.get()).getAtkplayers().addAll(road.getAtkplayers());
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
                GuildwarResultBO resultbo = new GuildwarResultBO();
                resultbo.setAtkpid(atkPlayer.getPid());
                resultbo.setDefpid(defPlayer.getPid());
                resultbo.setResult(result.ordinal());
                resultbo.setCenterId(centerId);
                resultbo.setFightTime(CommTime.nowSecond());
                resultbo.insert();
                ((List<GuildwarResultBO>) this.fightResult.get(Integer.valueOf(centerId))).add(resultbo);
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
                        Guild guild = ((GuildMemberFeature) lose.getFeature(GuildMemberFeature.class)).getGuild();
                        guild.removePuppet(lose);
                    }
                }

                if (road.getDefplayers().size() == 0 || road.getAtkplayers().size() == 0) {
                    road.setOverTime(CommTime.nowSecond());
                }
            }
        }

        return true;
    }

    private void dealWarFinish(int centerId, GuildwarGuildResultBO result) {
        synchronized (this) {
            for (int i = 0; i < this.road; i++) {
                Road road1 = ((List<Road>) this.guildWarRoad.get(Integer.valueOf(centerId))).get(i);
                road1.isOver = true;
            }
            personReward(this.fightResult.get(Integer.valueOf(centerId)), centerId);
            this.fighters.remove(Integer.valueOf(centerId));
            this.playersHP.clear();

            for (Player player : this.watchPlayers.get(Integer.valueOf(centerId))) {
                player.pushProto("warFinish", "");
            }
            checkReward(centerId);
            roportAll(centerId, result);
        }
    }

    public void roportAll(int centerId, GuildwarGuildResultBO result) {
        Map<Long, Report> map = this.reports.get(Integer.valueOf(centerId));
        RefGuildWarCenter ref = (RefGuildWarCenter) RefDataMgr.get(RefGuildWarCenter.class, Integer.valueOf(centerId));

        long defId = result.getDefGuildId();
        if (defId != 0L) {
            Report report1 = map.get(Long.valueOf(defId));
            Guild guild = GuildMgr.getInstance().getGuild(defId);

            String str1 = guild.getName();

            String str2 = String.valueOf(((Report) map.get(Long.valueOf(defId))).total);

            String str3 = String.valueOf(getdeadDef(centerId));

            String EnemyGuild = GuildMgr.getInstance().getGuild(result.getAtkGuildId()).getName();

            String str4 = String.valueOf(((Report) map.get(Long.valueOf(result.getAtkGuildId()))).total);

            String str5 = String.valueOf(getdeadAtk(centerId));

            SyncTaskManager.task(() -> {
                int mailId = 0;
                if (paramGuildwarGuildResultBO.getResult() == FightResult.Win.ordinal()) {
                    mailId = paramRefGuildWarCenter.FailMail;
                } else if (paramGuildwarGuildResultBO.getResult() == FightResult.Lost.ordinal()) {
                    mailId = paramRefGuildWarCenter.MailId;
                }
                for (Long pid : paramGuild.getMembers()) {
                    int puppet = 0;
                    int kill = 0;
                    try {
                        if (paramReport.puppet.get(pid) != null) {
                            puppet = ((Integer) paramReport.puppet.get(pid)).intValue();
                        }
                    } catch (Exception exception) {
                    }

                    kill = getKillNum(paramInt, pid.longValue());

                    MailCenter.getInstance().sendMail(pid.longValue(), mailId, new String[]{paramString1, paramString2, paramString3, paramString4, paramString5, paramString6, (new StringBuilder(String.valueOf(puppet))).toString(), (new StringBuilder(String.valueOf(kill))).toString()});
                }
            });
        }

        long guildId = result.getAtkGuildId();

        Report report = map.get(Long.valueOf(guildId));

        Guild defGuild = GuildMgr.getInstance().getGuild(guildId);

        String myGuild = defGuild.getName();

        String total = String.valueOf(((Report) map.get(Long.valueOf(guildId))).total);

        String dead = String.valueOf(getdeadAtk(centerId));

        String enemyTotal = String.valueOf(((Report) map.get(Long.valueOf(result.getDefGuildId()))).total);

        String enemyDead = String.valueOf(getdeadDef(centerId));

        SyncTaskManager.task(() -> {
            String EnemyGuild = "";

            if (GuildMgr.getInstance().getGuild(paramGuildwarGuildResultBO.getDefGuildId()) == null) {
                EnemyGuild = "神秘帮派";
            } else {
                EnemyGuild = GuildMgr.getInstance().getGuild(paramGuildwarGuildResultBO.getDefGuildId()).getName();
            }
            int mailId = 0;
            if (paramGuildwarGuildResultBO.getResult() == FightResult.Win.ordinal()) {
                mailId = paramRefGuildWarCenter.MailId;
            } else if (paramGuildwarGuildResultBO.getResult() == FightResult.Lost.ordinal()) {
                mailId = paramRefGuildWarCenter.FailMail;
            }
            for (Long pid : paramGuild.getMembers()) {
                int puppet = 0;
                int kill = 0;
                try {
                    if (paramReport.puppet.get(pid) != null) {
                        puppet = ((Integer) paramReport.puppet.get(pid)).intValue();
                    }
                } catch (Exception exception) {
                }
                kill = getKillNum(paramInt, pid.longValue());
                MailCenter.getInstance().sendMail(pid.longValue(), mailId, new String[]{paramString1, paramString2, paramString3, EnemyGuild, paramString4, paramString5, (new StringBuilder(String.valueOf(puppet))).toString(), (new StringBuilder(String.valueOf(kill))).toString()});
            }
        });
    }

    public boolean atkResult(int centerId, Road road) {
        synchronized (this) {
            if (road.isOver) {
                return true;
            }
            int atknum = getleftAtk(centerId);
            int defnum = getleftDef(centerId);
            if (atknum == 0 || defnum == 0) {
                GuildwarGuildResultBO bo = new GuildwarGuildResultBO();
                if (getAtkGuild(centerId) != null)
                    bo.setAtkGuildId(getAtkGuild(centerId).getGuildId());
                if (this.guildWarCenterOwner.get(Integer.valueOf(centerId)) != null)
                    bo.setDefGuildId(((Guild) this.guildWarCenterOwner.get(Integer.valueOf(centerId))).getGuildId());
                bo.setCenterId(centerId);
                bo.setFightTime(CommTime.nowSecond());
                if (atknum == 0) {
                    bo.setResult(FightResult.Lost.ordinal());
                }
                if (defnum == 0) {
                    bo.setResult(FightResult.Win.ordinal());
                }
                bo.insert();

                this.guildAllResulet.put(Long.valueOf(bo.getId()), new ArrayList<>(this.fightResult.get(Integer.valueOf(centerId))));

                if (this.guildFightResult.get(Integer.valueOf(centerId)) == null) {
                    List<GuildwarGuildResultBO> list = new ArrayList<>();
                    list.add(bo);
                    this.guildFightResult.put(Integer.valueOf(centerId), list);
                } else {
                    ((List<GuildwarGuildResultBO>) this.guildFightResult.get(Integer.valueOf(centerId))).add(bo);
                }

                Guild guild = ((List<Guild>) this.guildWarAtk.get(Integer.valueOf(centerId))).remove(0);
                if (defnum == 0) {
                    this.guildWarCenterOwner.put(Integer.valueOf(centerId), guild);
                }
                dealWarFinish(centerId, bo);
                return true;
            }
        }

        return false;
    }

    public boolean timeOver(int centerId, Road road) {
        synchronized (this) {
            if (road.isOver) {
                return true;
            }

            if (CommTime.nowSecond() - ((Integer) this.fightTime.get(Integer.valueOf(centerId))).intValue() > GuildWarConfig.fightTime() - GuildWarConfig.restTime()) {
                GuildwarGuildResultBO bo = new GuildwarGuildResultBO();
                bo.setAtkGuildId(getAtkGuild(centerId).getGuildId());
                if (this.guildWarCenterOwner.get(Integer.valueOf(centerId)) != null)
                    bo.setDefGuildId(((Guild) this.guildWarCenterOwner.get(Integer.valueOf(centerId))).getGuildId());
                bo.setCenterId(centerId);
                bo.setFightTime(CommTime.nowSecond());
                bo.setResult(FightResult.Lost.ordinal());
                bo.insert();
                if (this.guildFightResult.get(Integer.valueOf(centerId)) == null) {
                    List<GuildwarGuildResultBO> list = new ArrayList<>();
                    list.add(bo);
                    this.guildFightResult.put(Integer.valueOf(centerId), list);
                } else {
                    ((List<GuildwarGuildResultBO>) this.guildFightResult.get(Integer.valueOf(centerId))).add(bo);
                }
                ((List) this.guildWarAtk.get(Integer.valueOf(centerId))).remove(0);
                dealWarFinish(centerId, bo);
                return true;
            }
        }

        return false;
    }

    public boolean fight() {
        int guilds = 0;
        for (List<Guild> list : this.guildWarAtk.values()) {
            guilds += list.size();
        }
        if (guilds == 0) {
            return false;
        }

        prepareFight();
        for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
            if (!ref.isOpenTime()) {
                continue;
            }
            if (getAtkGuild(ref.id) == null) {
                continue;
            }

            beginFightByCenterId(ref.id);
        }

        return true;
    }

    public void Start() {
        try {
            fight();
            SyncTaskManager.schedule(GuildWarConfig.fightTime() * 1000, () -> fight());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void guildWarEvent() {
        try {
            checkTakeReward();
            Start();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void checkTakeReward() {
        for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
            if (!ref.isOpenTime()) {
                continue;
            }
            if (getAtkGuild(ref.id) != null) {
                continue;
            }

            Guild owner = this.guildWarCenterOwner.get(Integer.valueOf(ref.id));
            if (owner == null) {
                continue;
            }

            for (Long pid : owner.getMembers()) {
                MailCenter.getInstance().sendMail(pid.longValue(), ref.TakeMail, new String[0]);
            }
        }
    }

    public List<GuildWarCenterInfo> getCenterInfo() {
        List<GuildWarCenterInfo> listInfo = new ArrayList<>();
        for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
            listInfo.add(centerInfo(ref));
        }
        return listInfo;
    }

    public GuildWarCenterInfo centerInfo(RefGuildWarCenter ref) {
        int centerId = ref.id;
        GuildWarCenterInfo info = new GuildWarCenterInfo();

        info.setCenterId(centerId);

        info.setOpen(ref.isOpenTime());

        List<Guild.GuildSummary> list = new ArrayList<>();
        for (Guild guild : this.guildWarApplyer.get(Integer.valueOf(centerId))) {
            Guild.GuildSummary summary = new Guild.GuildSummary();
            summary.setGuildId(guild.getGuildId());
            summary.setGuildName(guild.getName());
            list.add(summary);
        }
        info.setAtkGuild(list);

        Guild guildowner = this.guildWarCenterOwner.get(Integer.valueOf(centerId));

        if (guildowner != null && guildowner.guildwarCenter != null && guildowner.guildwarCenter.getWinCenterId() == centerId) {
            info.setWinner(new Guild.GuildSummary(guildowner));
        }

        if (guildowner != null) {
            info.setOwner(new Guild.GuildSummary(guildowner));
        } else {
            Guild.GuildSummary summary = new Guild.GuildSummary();
            summary.setGuildName("神秘帮会");
            Player.showModle modle = new Player.showModle();
            modle.name = "神秘帮主";
            summary.setPresident(modle);
            info.setOwner(summary);
        }

        List<GuildWarCenterInfo.GuildwarGuildResultRecord> resultlist = new ArrayList<>();
        if (this.guildFightResult.get(Integer.valueOf(centerId)) != null) {
            for (GuildwarGuildResultBO bo : this.guildFightResult.get(Integer.valueOf(centerId))) {
                resultlist.add(new GuildWarCenterInfo.GuildwarGuildResultRecord(bo));
            }
        }
        info.setResult(resultlist);

        info.setAtkingGuild(new Guild.GuildSummary(getAtkGuild(centerId)));
        return info;
    }

    public GuildWarFightProtol getFightInfo(int centerId, Player player) throws WSException {
        GuildWarFightProtol info = new GuildWarFightProtol();
        info.setCenterId(centerId);
        Guild guildatk = getAtkGuild(centerId);
        if (this.fighters.get(Integer.valueOf(centerId)) == null) {
            throw new WSException(ErrorCode.GuildWar_NotFoundFight, "战斗未开始或已结束");
        }
        if (guildatk != null) {
            info.setAtk(new Guild.GuildSummary(guildatk));
        } else {
            Guild.GuildSummary summary = new Guild.GuildSummary();
            summary.setGuildId(0L);
            summary.setGuildName("");
            info.setAtk(summary);
        }

        Guild guilddef = this.guildWarCenterOwner.get(Integer.valueOf(centerId));
        if (guilddef != null) {
            info.setDef(new Guild.GuildSummary(guilddef));
        } else {
            Guild.GuildSummary summary = new Guild.GuildSummary();
            summary.setGuildId(-1L);
            summary.setGuildName("神秘帮会");
            info.setDef(summary);
        }
        List<GuildWarFightProtol.GuildInfo> guildInfo = new ArrayList<>();
        guildInfo.add(new GuildWarFightProtol.GuildInfo(info.getAtk().getGuildName(), getleftAtk(centerId),
                ((Center) this.fighters.get(Integer.valueOf(centerId))).atkplayers.size() - getleftAtk(centerId), getAtkPuppetNum(centerId)));
        guildInfo.add(new GuildWarFightProtol.GuildInfo(info.getDef().getGuildName(), getleftDef(centerId),
                ((Center) this.fighters.get(Integer.valueOf(centerId))).defplayers.size() - getleftDef(centerId), getDefPuppetNum(centerId)));
        info.setGuildInfo(guildInfo);
        List<GuildWarFightProtol.ResultRecord> result = new ArrayList<>();
        for (GuildwarResultBO bo : this.fightResult.get(Integer.valueOf(centerId))) {
            result.add(new GuildWarFightProtol.ResultRecord(bo));
        }
        info.setResultInfo(result);
        info.setEndTime(nextRefreshTime());
        info.setKillnum(getKillNum(centerId, player.getPid()));
        List<GuildWarFightProtol.RoadSummry> summarylist = new ArrayList<>();
        for (Road road : this.guildWarRoad.get(Integer.valueOf(centerId))) {
            summarylist.add(new GuildWarFightProtol.RoadSummry(road));
        }
        info.setRoad(summarylist);
        for (Road road : this.guildWarRoad.get(Integer.valueOf(centerId))) {
            if (road.getDeadatkplayers().contains(player) || road.getDeaddefplayers().contains(player)) {
                info.setDead(true);
                break;
            }
        }
        info.setRebirthTime(((PlayerRecord) player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.GuildWarRebirth));
        List<Player> listplayer = this.watchPlayers.get(Integer.valueOf(centerId));
        if (!listplayer.contains(player)) {
            listplayer.add(player);
        }

        return info;
    }

    public int nextRefreshTime() {
        int time = CommTime.getTodayZeroClockS();
        time += 68400;
        while (true) {
            time += GuildWarConfig.fightTime();
            if (time > CommTime.nowSecond()) {
                return time - GuildWarConfig.restTime();
            }
        }
    }

    public int getleftAtk(int centerId) {
        int num = 0;
        for (int i = 0; i < this.road; i++) {
            Road road = ((List<Road>) this.guildWarRoad.get(Integer.valueOf(centerId))).get(i);
            num += road.getAtkplayers().size();
        }
        return num;
    }

    public int getleftDef(int centerId) {
        int num = 0;
        for (int i = 0; i < this.road; i++) {
            Road road = ((List<Road>) this.guildWarRoad.get(Integer.valueOf(centerId))).get(i);
            num += road.getDefplayers().size();
        }
        return num;
    }

    public int getdeadAtk(int centerId) {
        int num = 0;
        for (int i = 0; i < this.road; i++) {
            Road road = ((List<Road>) this.guildWarRoad.get(Integer.valueOf(centerId))).get(i);
            num += road.getDeadatkplayers().size();
        }
        return num;
    }

    public int getdeadDef(int centerId) {
        int num = 0;
        for (int i = 0; i < this.road; i++) {
            Road road = ((List<Road>) this.guildWarRoad.get(Integer.valueOf(centerId))).get(i);
            num += road.getDeaddefplayers().size();
        }
        return num;
    }

    public int getKillNum(int centerId, long pid) {
        int num = 0;
        List<GuildwarResultBO> list = this.fightResult.get(Integer.valueOf(centerId));
        for (GuildwarResultBO bo : list) {
            if (bo.getAtkpid() == pid && bo.getResult() == FightResult.Win.ordinal()) {
                num++;
            }
        }
        return num;
    }

    public int getAtkPuppetNum(int centerId) {
        int num = 0;
        for (Player player : ((Center) this.fighters.get(Integer.valueOf(centerId))).atkplayers) {
            if (GuildWarConfig.puppetPlayer.class.isInstance(player)) {
                num++;
            }
        }
        return num;
    }

    public int getDefPuppetNum(int centerId) {
        int num = 0;
        for (Player player : ((Center) this.fighters.get(Integer.valueOf(centerId))).defplayers) {
            if (GuildWarConfig.puppetPlayer.class.isInstance(player)) {
                num++;
            }
        }
        return num;
    }

    public void checkReward(int centerId) {
        Guild guild = this.guildWarCenterOwner.get(Integer.valueOf(centerId));
        if (guild != null && ((List) this.guildWarAtk.get(Integer.valueOf(centerId))).size() == 0) {
            if (guild.guildwarCenter.getWinCenterId() != 0) {
                return;
            }
            RefGuildWarCenter ref = (RefGuildWarCenter) RefDataMgr.get(RefGuildWarCenter.class, Integer.valueOf(centerId));
            for (Long pid : guild.getMembers()) {
                MailCenter.getInstance().sendMail(pid.longValue(), ref.TakeMail, new String[0]);
            }
            guild.guildwarCenter.saveWinCenterId(centerId);
            this.historyWinner.put(Integer.valueOf(centerId), guild);
        }
    }

    public void personReward(List<GuildwarResultBO> list, int centerId) {
        List<Road> roads = this.guildWarRoad.get(Integer.valueOf(centerId));
        int atkwin = 0;
        int defwin = 0;
        for (Road road : roads) {
            if (road.getWinner() == 0L)
                continue;
            if (road.getAtkplayers().size() != 0) {
                atkwin++;
                continue;
            }
            if (road.getDefplayers().size() != 0) {
                defwin++;
            }
        }

        Map<Long, List<GuildwarResultBO>> map = Maps.newConcurrentHashMap();
        for (GuildwarResultBO bo : list) {
            if (bo.getResult() == FightResult.Win.ordinal()) {
                if (map.get(Long.valueOf(bo.getAtkpid())) != null) {
                    ((List<GuildwarResultBO>) map.get(Long.valueOf(bo.getAtkpid()))).add(bo);
                    continue;
                }
                List<GuildwarResultBO> tmp_list = new ArrayList<>();
                tmp_list.add(bo);
                map.put(Long.valueOf(bo.getAtkpid()), tmp_list);
            }
        }

        for (Map.Entry<Long, List<GuildwarResultBO>> entry : map.entrySet()) {
            int num = 1;
            Player player = PlayerMgr.getInstance().getPlayer(((Long) entry.getKey()).longValue());
            if (((Center) this.fighters.get(Integer.valueOf(centerId))).atkplayers.contains(player)) {
                num = Math.max(num, atkwin);
            } else if (((Center) this.fighters.get(Integer.valueOf(centerId))).defplayers.contains(player)) {
                num = Math.max(num, defwin);
            }
            Reward rewardall = new Reward();
            Reward reward = RefGuildWarPersonReward.getReward(((List) entry.getValue()).size());
            for (int i = 0; i < num; i++) {
                rewardall.combine(reward);
            }
            MailCenter.getInstance().sendMail(player.getPid(), "GM", "帮派战个人奖励", "在帮派战中你奋勇杀敌，获得以下奖励", rewardall, new String[0]);
        }
    }

    public GuildWarFightProtol rebirth(Player player, int centerId) throws WSException {
        Center center = this.fighters.get(Integer.valueOf(centerId));

        if (center == null) {
            throw new WSException(ErrorCode.GuildWar_NotFoundFight, "战斗已结束");
        }

        PlayerCurrency currency = (PlayerCurrency) player.getFeature(PlayerCurrency.class);
        PlayerRecord recorder = (PlayerRecord) player.getFeature(PlayerRecord.class);

        int curTimes = recorder.getValue(ConstEnum.DailyRefresh.GuildWarRebirth);
        RefCrystalPrice prize = RefCrystalPrice.getPrize(curTimes);
        if (!currency.check(PrizeType.Crystal, prize.GuildWarRebirth)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "玩家复活需要钻石%s", new Object[]{Integer.valueOf(prize.GuildWarRebirth)});
        }
        currency.consume(PrizeType.Crystal, prize.GuildWarRebirth, ItemFlow.GuildWar_Rebirth);
        recorder.addValue(ConstEnum.DailyRefresh.GuildWarRebirth);
        Guild guild = ((GuildMemberFeature) player.getFeature(GuildMemberFeature.class)).getGuild();
        GuildwarpuppetBO bo = new GuildwarpuppetBO();
        bo.setPid(player.getPid());
        bo.setGuildId(guild.getGuildId());
        bo.savePuppetId(curTimes);
        bo.setApplyTime(CommTime.nowSecond());
        bo.insert();
        if (guild.puppets.get(Long.valueOf(player.getPid())) != null) {
            ((List<GuildwarpuppetBO>) guild.puppets.get(Long.valueOf(player.getPid()))).add(bo);
        } else {
            List<GuildwarpuppetBO> list1 = new ArrayList<>();
            list1.add(bo);
            guild.puppets.put(Long.valueOf(player.getPid()), list1);
        }

        Report report = (Report) ((Map) this.reports.get(Integer.valueOf(centerId))).get(Long.valueOf(guild.getGuildId()));
        report.total++;
        int num = 0;
        if (report.puppet.get(Long.valueOf(player.getPid())) != null) {
            num = ((Integer) report.puppet.get(Long.valueOf(player.getPid()))).intValue();
        }
        num++;
        report.puppet.put(Long.valueOf(player.getPid()), Integer.valueOf(num));

        List<Road> list = this.guildWarRoad.get(Integer.valueOf(centerId));
        List<Road> tmp_list = new ArrayList<>(list);
        Collections.shuffle(tmp_list);
        if (center.defplayers.contains(player)) {
            Optional<Road> find = tmp_list.stream().filter(x -> (x.getWinner() == 0L)).findAny();
            if (find.isPresent()) {
                GuildWarConfig.puppetPlayer p_player = new GuildWarConfig.puppetPlayer(player.getPlayerBO());
                p_player.setPuppet_id(curTimes);
                p_player.setIs_puppet(true);
                ((Road) find.get()).getDefplayers().add(p_player);
            }
        }
        if (center.atkplayers.contains(player)) {
            Optional<Road> find = tmp_list.stream().filter(x -> (x.getWinner() == 0L)).findAny();
            if (find.isPresent()) {
                GuildWarConfig.puppetPlayer p_player = new GuildWarConfig.puppetPlayer(player.getPlayerBO());
                p_player.setPuppet_id(curTimes);
                p_player.setIs_puppet(true);
                ((Road) find.get()).getAtkplayers().add(p_player);
            }
        }

        SyncTaskManager.task(() -> {
            for (Player player_tmp : this.watchPlayers.get(Integer.valueOf(paramInt))) {
                if (player_tmp == paramPlayer)
                    continue;
                try {
                    player_tmp.pushProto("guildwarRebirth", getFightInfo(paramInt, player_tmp));
                } catch (WSException e) {
                    e.printStackTrace();
                }
            }
        });

        return getFightInfo(centerId, player);
    }

    public int isFighting(Guild guild) {
        for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
            int centerId = 0;
            if (this.fighters.get(Integer.valueOf(ref.id)) != null) {
                centerId = ref.id;
            }
            if (centerId == 0) {
                continue;
            }
            Guild Def = this.guildWarCenterOwner.get(Integer.valueOf(centerId));
            Guild Atk = getAtkGuild(centerId);

            if (Def != null && Def == guild) {
                return centerId;
            }
            if (Atk != null && Atk == guild) {
                return centerId;
            }
        }

        return 0;
    }

    private static class Report {
        Guild guild;
        Map<Long, Integer> puppet = new HashMap<>();
        int total;

        private Report() {
        }
    }

    private static class Center {
        Guild atkGuild;
        Guild defGuild;
        List<Player> atkplayers = new ArrayList<>();
        List<Player> defplayers = new ArrayList<>();

        private Center(List<Player> atkplayers, Guild atkGuild, List<Player> defplayers, Guild defGuild) {
            this.atkplayers = atkplayers;
            this.atkGuild = atkGuild;
            this.defplayers = defplayers;
            this.defGuild = defGuild;
        }

        private Center() {
        }
    }

    public static class Road {
        List<Player> atkplayers;
        List<Player> defplayers;
        List<Player> deadatkplayers;
        List<Player> deaddefplayers;
        long Winner = 0L;
        int overTime;
        boolean isOver = false;
        int begin;

        public Road() {
            this.atkplayers = new Vector<>();
            this.defplayers = new Vector<>();
            this.deadatkplayers = new Vector<>();
            this.deaddefplayers = new Vector<>();
        }

        public long getWinner() {
            return this.Winner;
        }

        public void setWinner(long winner) {
            this.Winner = winner;
        }

        public List<Player> getAtkplayers() {
            return this.atkplayers;
        }

        public void setAtkplayers(List<Player> atkplayers) {
            this.atkplayers = atkplayers;
        }

        public List<Player> getDefplayers() {
            return this.defplayers;
        }

        public void setDefplayers(List<Player> defplayers) {
            this.defplayers = defplayers;
        }

        public int getOverTime() {
            return this.overTime;
        }

        public void setOverTime(int overTime) {
            this.overTime = overTime;
        }

        public List<Player> getDeadatkplayers() {
            return this.deadatkplayers;
        }

        public void setDeadatkplayers(List<Player> deadatkplayers) {
            this.deadatkplayers = deadatkplayers;
        }

        public List<Player> getDeaddefplayers() {
            return this.deaddefplayers;
        }

        public void setDeaddefplayers(List<Player> deaddefplayers) {
            this.deaddefplayers = deaddefplayers;
        }

        public boolean isOver() {
            return this.isOver;
        }

        public void setOver(boolean isOver) {
            this.isOver = isOver;
        }

        public int getBegin() {
            return this.begin;
        }

        public void setBegin(int begin) {
            this.begin = begin;
        }
    }
}

