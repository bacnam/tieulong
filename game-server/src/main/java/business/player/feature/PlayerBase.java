package business.player.feature;

import BaseCommon.CommLog;
import business.global.activity.ActivityMgr;
import business.global.activity.detail.LevelReward;
import business.global.activity.detail.RankLevel;
import business.global.fight.Fighter;
import business.global.guild.Guild;
import business.global.guild.GuildWarConfig;
import business.global.guild.GuildWarMgr;
import business.global.notice.NoticeMgr;
import business.global.rank.RankManager;
import business.global.worldboss.WorldBossMgr;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import business.player.feature.character.DressFeature;
import business.player.feature.character.Equip;
import business.player.feature.character.WarSpiritFeature;
import business.player.feature.features.MailFeature;
import business.player.feature.features.PlayerRecord;
import business.player.feature.features.RechargeFeature;
import business.player.feature.guild.GuildMemberFeature;
import business.player.feature.marry.MarryFeature;
import business.player.feature.player.LingBaoFeature;
import business.player.feature.player.NewTitleFeature;
import business.player.feature.pve.DungeonFeature;
import business.player.feature.pve.InstanceFeature;
import business.player.feature.store.PlayerStore;
import business.player.feature.store.StoreFeature;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.DressType;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.InstanceType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.StoreType;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.common.enums.ConstEnum.UniverseMessageType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.http.client.IResponseHandler;
import com.zhonglian.server.http.server.GMParam;
import com.zhonglian.server.http.server.HttpUtils;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefGuildJobInfo;
import core.config.refdata.ref.RefRobot;
import core.config.refdata.ref.RefVIP;
import core.database.game.bo.CharacterBO;
import core.database.game.bo.DressBO;
import core.database.game.bo.InstanceInfoBO;
import core.database.game.bo.PlayerBO;
import core.database.game.bo.WorldBossBO;
import core.network.proto.WarSpiritInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import proto.gamezone.Player.PlayerInfo;

public class PlayerBase extends Feature {
    public int chatTime = 0;
    public int worldChatTime = 0;
    public int gmChatTime = 0;

    public PlayerBase(Player data) {
        super(data);
    }

    public void onCreate(int selected) throws WSException {
        this.player.getFeature(CharFeature.class).unlockChar(selected, ItemFlow.PlayerCreate);
    }

    public void onCreate(RefRobot init) throws WSException {
        List<Integer> simulateTeam = init.randTeam();
        CharFeature feature = this.player.getFeature(CharFeature.class);
        int leader = 0;

        for (Integer simulateId : simulateTeam) {
            int cardid = feature.simulate(simulateId);
            if (leader == 0) {
                leader = cardid;
            }
        }

        this.player.getPlayerBO().saveIcon(leader);
        feature.updateRobotPower();
    }

    public void bannedLogin(int duration) {
        if (duration > 0) {
            duration += CommTime.nowSecond();
        }

        this.player.getPlayerBO().saveBannedLoginExpiredTime(duration);
        if (duration != 0) {
            this.player.getPlayerBO().saveBannedTimes(this.player.getPlayerBO().getBannedTimes() + 1);
            this.player.getFeature(PlayerBase.class).onBanned(this.player);
            this.kickout();
        }
    }

    public void kickout() {
        this.player.pushProto("kickout", " ");
        if (this.player.getClientSession() != null) {
            this.player.getClientSession().close();
        }
    }

    public void onLevelUp(int nowLvl) {
        this.player.getFeature(CharFeature.class).updateCharPower();
        RankManager.getInstance().update(RankType.Level, this.player.getPid(), nowLvl);
        AchievementFeature achievement = this.player.getFeature(AchievementFeature.class);
        achievement.updateMax(AchievementType.PlayerLevel, nowLvl);
        StoreFeature feature = this.player.getFeature(StoreFeature.class);
        PlayerStore playerStore = feature.getOrCreate(StoreType.ArenaStore);
        playerStore.doAutoRefresh();
        ActivityMgr.getActivity(RankLevel.class).UpdateMaxRequire_cost(this.player, nowLvl);
        ActivityMgr.getActivity(LevelReward.class).handLevelChange(this.player);
        this.NoticeGmPlayerInfo();
        PlayerMgr.getInstance().tryNotify(this.player);
    }

    public void onVipLevelUp(int nowLvl, int oldLvl) {
        RefVIP oldRef = RefDataMgr.get(RefVIP.class, oldLvl);
        RefVIP nowRef = RefDataMgr.get(RefVIP.class, nowLvl);
        int addPackage = nowRef.AddPackage - oldRef.AddPackage;
        this.player.getPlayerBO().saveExtPackage(this.player.getPlayerBO().getExtPackage() + addPackage);
        this.player.pushProto("extPackage", this.player.getPlayerBO().getExtPackage());
        InstanceInfoBO instanceBO = this.player.getFeature(InstanceFeature.class).getOrCreate();

        InstanceType[] var10;
        for (InstanceType type : var10 = InstanceType.values()) {
            if (type == InstanceType.EquipInstance) {
                int addNum = nowRef.EquipInstanceTimes - oldRef.EquipInstanceTimes;
                instanceBO.setChallengTimes(type.ordinal(), instanceBO.getChallengTimes(type.ordinal()) + addNum);
            }

            if (type == InstanceType.MeridianInstance) {
                int addNum = nowRef.MeridianInstanceTimes - oldRef.MeridianInstanceTimes;
                instanceBO.setChallengTimes(type.ordinal(), instanceBO.getChallengTimes(type.ordinal()) + addNum);
            }

            if (type == InstanceType.GemInstance) {
                int addNum = nowRef.GemInstanceTimes - oldRef.GemInstanceTimes;
                instanceBO.setChallengTimes(type.ordinal(), instanceBO.getChallengTimes(type.ordinal()) + addNum);
            }
        }

        instanceBO.saveAll();
        this.NoticeGmPlayerInfo();
    }

    public void onBanned(Player player) {
        RankManager.getInstance().clearPlayerData(player);
    }

    public void onConnect() {
        this.player.getDailyRefreshFeature().reload();
        this.player.getDailyRefreshFeature().process(CommTime.nowSecond());
        this.player.getFeature(MailFeature.class).onConnect();
        this.player.getFeature(DungeonFeature.class).calcOfflineReward();
        if (this.player.getVipLevel() >= RefDataMgr.getFactor("VIP_Marque", 6)) {
            NoticeMgr.getInstance().sendMarquee(UniverseMessageType.VipLogin, "" + this.player.getVipLevel(), this.player.getName());
        }

        if (RankManager.getInstance().getRank(RankType.Power, this.player.getPid()) == 1) {
            NoticeMgr.getInstance().sendMarquee(UniverseMessageType.PowerLogin, this.player.getName());
        }

        this.NoticeGmPlayerInfo();
        this.player.getPlayerBO().saveOnlineUpdate(CommTime.nowSecond());
        PlayerMgr.getInstance().tryNotify(this.player);
    }

    public void onDisconnect() {
        this.player.getPlayerBO().saveLastLogout(CommTime.nowSecond());

        for (WorldBossBO bo : WorldBossMgr.getInstance().getBOList()) {
            WorldBossMgr.getInstance().doLeaveWorldBoss(this.player, (int)bo.getBossId());
        }

        this.calOnlineTime();
    }

    public void sendMsg(String key) {
    }

    public void sendMsg(String key, List<String> msgList) {
    }

    public void sendSysMessage(String msg) {
    }

    public void sendPopupMessage(String msg) {
    }

    public core.network.proto.Player.FullInfo fullInfo() {
        core.network.proto.Player.FullInfo info = new core.network.proto.Player.FullInfo();
        PlayerBO bo = this.player.getPlayerBO();
        info.pid = bo.getId();
        info.name = bo.getName();
        info.icon = bo.getIcon();
        info.createTime = bo.getCreateTime();
        info.vipLv = bo.getVipLevel();
        info.vipExp = bo.getVipExp();
        info.lv = bo.getLv();
        info.exp = bo.getExp();
        info.dungeonlv = bo.getDungeonLv();
        info.gold = bo.getGold();
        info.crystal = bo.getCrystal();
        info.strengthenMaterial = bo.getStrengthenMaterial();
        info.gemMaterial = bo.getGemMaterial();
        info.starMaterial = bo.getStarMaterial();
        info.merMaterial = bo.getMerMaterial();
        info.wingMaterial = bo.getWingMaterial();
        info.arenaToken = bo.getArenaToken();
        info.equipInstanceMaterial = bo.getEquipInstanceMaterial();
        info.gemInstanceMaterial = bo.getGemInstanceMaterial();
        info.meridianInstanceMaterial = bo.getMeridianInstanceMaterial();
        info.redPiece = bo.getRedPiece();
        info.extPackage = bo.getExtPackage();
        info.artificeMaterial = bo.getArtificeMaterial();
        info.warspiritTalentMaterial = bo.getWarspiritTalentMaterial();
        info.warspiritLv = bo.getWarspiritLv();
        info.warspiritExp = bo.getWarspiritExp();
        info.warspiritTalent = bo.getWarspiritTalent();
        info.lottery = bo.getLottery();
        info.dressMaterial = bo.getDressMaterial();
        GuildMemberFeature guildMemberFeature = this.player.getFeature(GuildMemberFeature.class);
        if (guildMemberFeature.getGuild() != null) {
            info.guildSkill = guildMemberFeature.getGuildSkillList();
        }

        PlayerRecord recorder = this.player.getFeature(PlayerRecord.class);
        int curTimes = recorder.getValue(DailyRefresh.PackageBuyTimes);
        info.buyPackageTimes = curTimes;
        info.dresses = this.player.getFeature(DressFeature.class).getAllDressInfo();
        info.characters = this.player.getFeature(CharFeature.class).getAllInfo();
        info.warspirits = this.player.getFeature(WarSpiritFeature.class).getAllInfo();
        info.sex = this.player.getFeature(MarryFeature.class).bo.getSex();
        return info;
    }

    public core.network.proto.Player.FightInfo fightInfo() {
        core.network.proto.Player.FightInfo info = new core.network.proto.Player.FightInfo();
        PlayerBO bo = this.player.getPlayerBO();
        info.pid = bo.getId();
        info.name = bo.getName();
        info.icon = bo.getIcon();
        info.vipLv = bo.getVipLevel();
        info.vipExp = bo.getVipExp();
        info.lv = bo.getLv();
        info.warspiritLv = bo.getWarspiritLv();
        info.warspiritTalent = bo.getWarspiritTalent();
        info.characters = this.player.getFeature(CharFeature.class).getAllInfo();
        if (this.player.getFeature(WarSpiritFeature.class).getWarSpiritNow() != null) {
            info.warspirit = new WarSpiritInfo(this.player.getFeature(WarSpiritFeature.class).getWarSpiritNow());
        } else {
            info.warspirit = null;
        }

        info.title = this.player.getFeature(NewTitleFeature.class).getAllTitleInfo();
        info.LingBaoLevel = this.player.getFeature(LingBaoFeature.class).getLevel();
        GuildMemberFeature guildfeature = this.player.getFeature(GuildMemberFeature.class);
        Guild guild = guildfeature.getGuild();
        if (guild != null) {
            RefGuildJobInfo job = guildfeature.getJobRef();
            info.guildName = guild.getName();
            info.guildJob = job.JobName;
        }

        return info;
    }

    public core.network.proto.Player.Summary summary() {
        core.network.proto.Player.Summary summary = new core.network.proto.Player.Summary();
        PlayerBO bo = this.player.getPlayerBO();
        summary.pid = bo.getId();
        summary.name = bo.getName();
        summary.lv = bo.getLv();
        summary.icon = bo.getIcon();
        summary.vipLv = bo.getVipLevel();
        summary.power = this.player.getFeature(CharFeature.class).getPower();
        summary.sex = this.player.getFeature(MarryFeature.class).bo.getSex();
        summary.is_married = this.player.getFeature(MarryFeature.class).isMarried();
        RechargeFeature rechargeFeature = this.player.getFeature(RechargeFeature.class);
        int monthNum = rechargeFeature.getRebateRemains(AchievementType.MonthCardCrystal);
        summary.MonthCard = monthNum > 0;
        int yearNum = rechargeFeature.getRebateRemains(AchievementType.YearCardCrystal);
        summary.YearCard = yearNum == -1;
        return summary;
    }

    public core.network.proto.Player.showModle modle() {
        core.network.proto.Player.showModle modle = new core.network.proto.Player.showModle();
        PlayerBO bo = this.player.getPlayerBO();
        modle.pid = bo.getId();
        modle.name = bo.getName();
        modle.icon = bo.getIcon();
        List<Character> characters = new ArrayList<>(this.player.getFeature(CharFeature.class).getAll().values());
        characters.sort((left, right) -> left.getBo().getId() < right.getBo().getId() ? -1 : 1);
        Character character = characters.get(0);
        CharacterBO bocha = character.getBo();
        Equip equip = character.getEquip(EquipPos.Body);
        int equipid = equip == null ? 0 : equip.getEquipId();
        modle.model = equipid;
        modle.wing = bocha.getWing();
        DressBO dressbody = character.getDress(DressType.Role);
        modle.modelDress = dressbody == null ? 0 : dressbody.getDressId();
        DressBO dresswing = character.getDress(DressType.Wing);
        modle.wingDress = dresswing == null ? 0 : dresswing.getDressId();
        if (GuildWarConfig.puppetPlayer.class.isInstance(this.player)) {
            GuildWarConfig.puppetPlayer p_player = (GuildWarConfig.puppetPlayer)this.player;
            modle.puppet_id = p_player.getPuppet_id();
            modle.is_puppet = p_player.isIs_puppet();
        }

        Map<Integer, Fighter> fighters = this.player.getFeature(CharFeature.class).getFighters();
        List<Double> maxhp = new ArrayList<>();

        for (Entry<Integer, Fighter> pair : fighters.entrySet()) {
            Fighter fighter = pair.getValue();
            maxhp.add(fighter.attr(Attribute.MaxHP));
        }

        modle.maxhp = maxhp;
        List<Double> hp = GuildWarMgr.getInstance().playersHP.get(this.player.getPid());
        if (hp == null) {
            hp = maxhp;
        }

        modle.hp = hp;
        return modle;
    }

    @Override
    public void loadDB() {
    }

    public PlayerInfo zoneProto() {
        PlayerInfo info = new PlayerInfo();
        PlayerBO bo = this.player.getPlayerBO();
        info.pid = bo.getId();
        info.name = bo.getName();
        info.icon = bo.getIcon();
        info.vipLv = bo.getVipLevel();
        info.lv = bo.getLv();
        info.serverId = bo.getSid();
        info.maxPower = bo.getMaxFightPower();
        Guild guild = this.player.getFeature(GuildMemberFeature.class).getGuild();
        if (guild != null) {
            info.guildID = guild.getGuildId();
            info.guildName = guild.getName();
        } else {
            info.guildID = 0L;
            info.guildName = "";
        }

        return info;
    }

    public void NoticeGmPlayerInfo() {
        GMParam params = new GMParam();
        params.put("open_id", this.player.getOpenId());
        params.put("name", this.player.getName());
        params.put("level", this.player.getLv());
        params.put("icon", this.player.getPlayerBO().getIcon());
        params.put("vip", this.player.getVipLevel());
        params.put("world_id", System.getProperty("world_sid", "0"));
        params.put("server_id", this.player.getSid());
        String baseurl = System.getProperty("downConfUrl");
        HttpUtils.RequestGM("http://" + baseurl + "/gm/user!info", params, new IResponseHandler() {
            @Override
            public void compeleted(String response) {
                try {
                    JsonObject json = new JsonParser().parse(response).getAsJsonObject();
                    if (json.get("state").getAsInt() != 1000) {
                        CommLog.error("发送GM用户信息失败" + json.get("state").getAsInt());
                    }
                } catch (Exception var3) {
                }
            }

            @Override
            public void failed(Exception exception) {
                CommLog.error("发送GM用户信息失败");
            }
        });
    }

    public void calOnlineTime() {
        int time = CommTime.nowSecond() - this.player.getPlayerBO().getOnlineUpdate();
        this.player.getFeature(PlayerRecord.class).addValue(DailyRefresh.OnlineSecond, time);
        this.player.getPlayerBO().saveOnlineUpdate(CommTime.nowSecond());
    }
}
