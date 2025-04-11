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
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.DressType;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.InstanceType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.StoreType;
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
import core.network.proto.Player;
import core.network.proto.WarSpiritInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import proto.gamezone.Player;

public class PlayerBase
        extends Feature {
    public int chatTime;
    public int worldChatTime;
    public int gmChatTime;

    public PlayerBase(Player data) {
        super(data);

        this.chatTime = 0;
        this.worldChatTime = 0;
        this.gmChatTime = 0;
    }

    public void onCreate(int selected) throws WSException {
        ((CharFeature) this.player.getFeature(CharFeature.class)).unlockChar(selected, ItemFlow.PlayerCreate);
    }

    public void onCreate(RefRobot init) throws WSException {
        List<Integer> simulateTeam = init.randTeam();
        CharFeature feature = (CharFeature) this.player.getFeature(CharFeature.class);
        int leader = 0;
        for (Integer simulateId : simulateTeam) {
            int cardid = feature.simulate(simulateId);
            if (leader == 0)
                leader = cardid;
        }
        this.player.getPlayerBO().saveIcon(leader);
        feature.updateRobotPower();
    }

    public void bannedLogin(int duration) {
        if (duration > 0) {
            duration = CommTime.nowSecond() + duration;
        }
        int expiredTime = duration;
        this.player.getPlayerBO().saveBannedLoginExpiredTime(expiredTime);
        if (expiredTime != 0) {
            this.player.getPlayerBO().saveBannedTimes(this.player.getPlayerBO().getBannedTimes() + 1);
            ((PlayerBase) this.player.getFeature(PlayerBase.class)).onBanned(this.player);
            kickout();
        }
    }

    public void kickout() {
        this.player.pushProto("kickout", " ");
        if (this.player.getClientSession() != null) {
            this.player.getClientSession().close();
        }
    }

    public void onLevelUp(int nowLvl) {
        ((CharFeature) this.player.getFeature(CharFeature.class)).updateCharPower();

        RankManager.getInstance().update(RankType.Level, this.player.getPid(), nowLvl);

        AchievementFeature achievement = (AchievementFeature) this.player.getFeature(AchievementFeature.class);

        achievement.updateMax(Achievement.AchievementType.PlayerLevel, Integer.valueOf(nowLvl));

        StoreFeature feature = (StoreFeature) this.player.getFeature(StoreFeature.class);
        PlayerStore playerStore = feature.getOrCreate(StoreType.ArenaStore);
        playerStore.doAutoRefresh();

        ((RankLevel) ActivityMgr.getActivity(RankLevel.class)).UpdateMaxRequire_cost(this.player, nowLvl);

        ((LevelReward) ActivityMgr.getActivity(LevelReward.class)).handLevelChange(this.player);

        NoticeGmPlayerInfo();

        PlayerMgr.getInstance().tryNotify(this.player);
    }

    public void onVipLevelUp(int nowLvl, int oldLvl) {
        RefVIP oldRef = (RefVIP) RefDataMgr.get(RefVIP.class, Integer.valueOf(oldLvl));
        RefVIP nowRef = (RefVIP) RefDataMgr.get(RefVIP.class, Integer.valueOf(nowLvl));
        int addPackage = nowRef.AddPackage - oldRef.AddPackage;
        this.player.getPlayerBO().saveExtPackage(this.player.getPlayerBO().getExtPackage() + addPackage);
        this.player.pushProto("extPackage", Integer.valueOf(this.player.getPlayerBO().getExtPackage()));

        InstanceInfoBO instanceBO = ((InstanceFeature) this.player.getFeature(InstanceFeature.class)).getOrCreate();
        byte b;
        int i;
        InstanceType[] arrayOfInstanceType;
        for (i = (arrayOfInstanceType = InstanceType.values()).length, b = 0; b < i; ) {
            InstanceType type = arrayOfInstanceType[b];
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
            b++;
        }

        instanceBO.saveAll();

        NoticeGmPlayerInfo();
    }

    public void onBanned(Player player) {
        RankManager.getInstance().clearPlayerData(player);
    }

    public void onConnect() {
        this.player.getDailyRefreshFeature().reload();

        this.player.getDailyRefreshFeature().process(CommTime.nowSecond());

        ((MailFeature) this.player.getFeature(MailFeature.class)).onConnect();

        ((DungeonFeature) this.player.getFeature(DungeonFeature.class)).calcOfflineReward();

        if (this.player.getVipLevel() >= RefDataMgr.getFactor("VIP_Marque", 6)) {
            NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.VipLogin, new String[]{this.player.getVipLevel(), this.player.getName()});
        }
        if (RankManager.getInstance().getRank(RankType.Power, this.player.getPid()) == 1) {
            NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.PowerLogin, new String[]{this.player.getName()});
        }

        NoticeGmPlayerInfo();

        this.player.getPlayerBO().saveOnlineUpdate(CommTime.nowSecond());

        PlayerMgr.getInstance().tryNotify(this.player);
    }

    public void onDisconnect() {
        this.player.getPlayerBO().saveLastLogout(CommTime.nowSecond());
        for (WorldBossBO bo : WorldBossMgr.getInstance().getBOList()) {
            WorldBossMgr.getInstance().doLeaveWorldBoss(this.player, (int) bo.getBossId());
        }

        calOnlineTime();
    }

    public void sendMsg(String key) {
    }

    public void sendMsg(String key, List<String> msgList) {
    }

    public void sendSysMessage(String msg) {
    }

    public void sendPopupMessage(String msg) {
    }

    public Player.FullInfo fullInfo() {
        Player.FullInfo info = new Player.FullInfo();
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

        GuildMemberFeature guildMemberFeature = (GuildMemberFeature) this.player.getFeature(GuildMemberFeature.class);
        if (guildMemberFeature.getGuild() != null) {
            info.guildSkill = guildMemberFeature.getGuildSkillList();
        }

        PlayerRecord recorder = (PlayerRecord) this.player.getFeature(PlayerRecord.class);
        int curTimes = recorder.getValue(ConstEnum.DailyRefresh.PackageBuyTimes);
        info.buyPackageTimes = curTimes;
        info.dresses = ((DressFeature) this.player.getFeature(DressFeature.class)).getAllDressInfo();
        info.characters = ((CharFeature) this.player.getFeature(CharFeature.class)).getAllInfo();
        info.warspirits = ((WarSpiritFeature) this.player.getFeature(WarSpiritFeature.class)).getAllInfo();
        info.sex = ((MarryFeature) this.player.getFeature(MarryFeature.class)).bo.getSex();
        return info;
    }

    public Player.FightInfo fightInfo() {
        Player.FightInfo info = new Player.FightInfo();
        PlayerBO bo = this.player.getPlayerBO();
        info.pid = bo.getId();
        info.name = bo.getName();
        info.icon = bo.getIcon();
        info.vipLv = bo.getVipLevel();
        info.vipExp = bo.getVipExp();
        info.lv = bo.getLv();
        info.warspiritLv = bo.getWarspiritLv();
        info.warspiritTalent = bo.getWarspiritTalent();

        info.characters = ((CharFeature) this.player.getFeature(CharFeature.class)).getAllInfo();
        if (((WarSpiritFeature) this.player.getFeature(WarSpiritFeature.class)).getWarSpiritNow() != null) {
            info.warspirit = new WarSpiritInfo(((WarSpiritFeature) this.player.getFeature(WarSpiritFeature.class)).getWarSpiritNow());
        } else {
            info.warspirit = null;
        }
        info.title = ((NewTitleFeature) this.player.getFeature(NewTitleFeature.class)).getAllTitleInfo();
        info.LingBaoLevel = ((LingBaoFeature) this.player.getFeature(LingBaoFeature.class)).getLevel();
        GuildMemberFeature guildfeature = (GuildMemberFeature) this.player.getFeature(GuildMemberFeature.class);
        Guild guild = guildfeature.getGuild();

        if (guild != null) {
            RefGuildJobInfo job = guildfeature.getJobRef();
            info.guildName = guild.getName();
            info.guildJob = job.JobName;
        }
        return info;
    }

    public Player.Summary summary() {
        Player.Summary summary = new Player.Summary();
        PlayerBO bo = this.player.getPlayerBO();
        summary.pid = bo.getId();
        summary.name = bo.getName();
        summary.lv = bo.getLv();
        summary.icon = bo.getIcon();
        summary.vipLv = bo.getVipLevel();
        summary.power = ((CharFeature) this.player.getFeature(CharFeature.class)).getPower();
        summary.sex = ((MarryFeature) this.player.getFeature(MarryFeature.class)).bo.getSex();
        summary.is_married = ((MarryFeature) this.player.getFeature(MarryFeature.class)).isMarried();

        RechargeFeature rechargeFeature = (RechargeFeature) this.player.getFeature(RechargeFeature.class);
        int monthNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.MonthCardCrystal);
        summary.MonthCard = (monthNum > 0);
        int yearNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.YearCardCrystal);
        summary.YearCard = (yearNum == -1);

        return summary;
    }

    public Player.showModle modle() {
        Player.showModle modle = new Player.showModle();
        PlayerBO bo = this.player.getPlayerBO();
        modle.pid = bo.getId();
        modle.name = bo.getName();
        modle.icon = bo.getIcon();
        List<Character> characters =
                new ArrayList<>(((CharFeature) this.player.getFeature(CharFeature.class)).getAll().values());
        characters.sort((left, right) -> (left.getBo().getId() < right.getBo().getId()) ? -1 : 1);
        Character character = characters.get(0);
        CharacterBO bocha = character.getBo();

        Equip equip = character.getEquip(EquipPos.Body);
        int equipid = (equip == null) ? 0 : equip.getEquipId();
        modle.model = equipid;
        modle.wing = bocha.getWing();

        DressBO dressbody = character.getDress(DressType.Role);
        modle.modelDress = (dressbody == null) ? 0 : dressbody.getDressId();

        DressBO dresswing = character.getDress(DressType.Wing);
        modle.wingDress = (dresswing == null) ? 0 : dresswing.getDressId();

        if (GuildWarConfig.puppetPlayer.class.isInstance(this.player)) {
            GuildWarConfig.puppetPlayer p_player = (GuildWarConfig.puppetPlayer) this.player;
            modle.puppet_id = p_player.getPuppet_id();
            modle.is_puppet = p_player.isIs_puppet();
        }

        Map<Integer, Fighter> fighters = ((CharFeature) this.player.getFeature(CharFeature.class)).getFighters();
        List<Double> maxhp = new ArrayList<>();
        for (Map.Entry<Integer, Fighter> pair : fighters.entrySet()) {
            Fighter fighter = pair.getValue();
            maxhp.add(Double.valueOf(fighter.attr(Attribute.MaxHP)));
        }
        modle.maxhp = maxhp;

        List<Double> hp = (List<Double>) (GuildWarMgr.getInstance()).playersHP.get(Long.valueOf(this.player.getPid()));
        if (hp == null) {
            hp = maxhp;
        }
        modle.hp = hp;
        return modle;
    }

    public void loadDB() {
    }

    public Player.PlayerInfo zoneProto() {
        Player.PlayerInfo info = new Player.PlayerInfo();
        PlayerBO bo = this.player.getPlayerBO();

        info.pid = bo.getId();
        info.name = bo.getName();
        info.icon = bo.getIcon();
        info.vipLv = bo.getVipLevel();
        info.lv = bo.getLv();
        info.serverId = bo.getSid();
        info.maxPower = bo.getMaxFightPower();

        Guild guild = ((GuildMemberFeature) this.player.getFeature(GuildMemberFeature.class)).getGuild();
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
        params.put("level", Integer.valueOf(this.player.getLv()));
        params.put("icon", Integer.valueOf(this.player.getPlayerBO().getIcon()));
        params.put("vip", Integer.valueOf(this.player.getVipLevel()));
        params.put("world_id", System.getProperty("world_sid", "0"));
        params.put("server_id", Integer.valueOf(this.player.getSid()));

        String baseurl = System.getProperty("downConfUrl");
        HttpUtils.RequestGM(baseurl, new IResponseHandler() {
            public void compeleted(String response) {
                try {
                    JsonObject json = new JsonParser().parse(response).getAsJsonObject();
                    if (json.get("state").getAsInt() != 1000) {
                        CommLog.error("发送GM用户信息失败" + json.get("state").getAsInt());
                    }
                } catch (Exception exception) {
                    // Log nếu cần
                }
            }

            public void failed(Exception exception) {
                CommLog.error("发送GM用户信息失败");
            }
        });
    }

    public void calOnlineTime() {
        int time = CommTime.nowSecond() - this.player.getPlayerBO().getOnlineUpdate();
        ((PlayerRecord) this.player.getFeature(PlayerRecord.class)).addValue(ConstEnum.DailyRefresh.OnlineSecond, time);
        this.player.getPlayerBO().saveOnlineUpdate(CommTime.nowSecond());
    }
}

