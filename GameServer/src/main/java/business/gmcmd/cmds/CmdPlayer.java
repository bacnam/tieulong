package business.gmcmd.cmds;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.DailySign;
import business.global.activity.detail.FirstRecharge;
import business.global.arena.ArenaManager;
import business.global.chat.ChatMgr;
import business.global.gmmail.MailCenter;
import business.global.notice.NoticeMgr;
import business.global.rank.RankManager;
import business.global.recharge.RechargeMgr;
import business.gmcmd.annotation.Command;
import business.gmcmd.annotation.Commander;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerItem;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.character.WarSpiritFeature;
import business.player.feature.pve.DungeonFeature;
import business.player.feature.pvp.DroiyanFeature;
import business.player.feature.pvp.StealGoldFeature;
import business.player.feature.task.TaskActivityFeature;
import business.player.feature.treasure.FindTreasureFeature;
import business.player.item.Reward;
import com.zhonglian.server.common.enums.ChatType;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDroiyanTreasure;
import core.config.refdata.ref.RefDungeon;
import core.config.refdata.ref.RefRecharge;
import core.config.refdata.ref.RefWarSpirit;
import core.database.game.bo.RechargeOrderBO;
import core.network.proto.AchieveInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Commander(name = "player", comment = "玩家相关命令")
public class CmdPlayer {
    @Command(comment = "获得物品[uniformId][数量]")
    public String gain(Player player, int item, int value) {
        ((PlayerItem) player.getFeature(PlayerItem.class)).gain(item, value, ItemFlow.Command);
        return "ok";
    }

    @Command(comment = "发送邮件")
    public String mail(Player player, int mailId, String param) {
        MailCenter.getInstance().sendMail(player.getPid(), mailId, new String[]{param});
        return "ok";
    }

    @Command(comment = "修正最后登出时间，参数为时间差")
    public String offline(Player player, int offset) {
        player.getPlayerBO().saveLastLogout(CommTime.nowSecond() - offset);
        ((DungeonFeature) player.getFeature(DungeonFeature.class)).calcOfflineReward();
        return "ok";
    }

    @Command(comment = "设置关卡等级")
    public String dungeon(Player player, int level) {
        if (level > RefDataMgr.getAll(RefDungeon.class).size())
            return "fail";
        player.getPlayerBO().saveDungeonLv(level);
        return "ok";
    }

    @Command(comment = "跑马灯")
    public String marquee(Player player, String parms) {
        NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.UnlockChar, new String[]{parms});
        return "ok";
    }

    @Command(comment = "寻宝")
    public Reward find(Player player, int times) {
        Reward reward = ((FindTreasureFeature) player.getFeature(FindTreasureFeature.class)).findTreasure(times);
        return reward;
    }

    @Command(comment = "充值传参商品id")
    public String recharge(Player player, String goodis) {
        if (RefDataMgr.get(RefRecharge.class, goodis) == null) {
            return "查无此商品" + goodis + "，请重新确定";
        }
        RechargeOrderBO bo = new RechargeOrderBO();
        bo.setCporderid("模拟cporderid" + CommTime.nowMS());
        bo.setCarrier("模拟carrier");
        bo.setPlatform("模拟platform");
        bo.setAdfrom("模拟 adfrom");
        bo.setAdfrom2("模拟 adfrom2");
        bo.setGameid("模拟 gameid");
        bo.setServerID("模拟 server_id");
        bo.setAppID("模拟 appid");
        bo.setOpenId(player.getPlayerBO().getOpenId());
        bo.setPid(player.getPid());
        bo.setAdfromOrderid("模拟  adfrom_orderid");
        bo.setQuantity(1);
        bo.setProductid(goodis);
        bo.setStatus(RechargeMgr.OrderStatus.Paid.toString());
        bo.insert();

        RechargeMgr.getInstance().sendPrize(bo);
        return "充值完毕";
    }

    @Command(comment = "清空排行榜")
    public String clearrank(Player player, RankType type) {
        RankManager.getInstance().clear(type);
        return "ok";
    }

    @Command(comment = "查看任务 信息")
    public String tasklist(Player player) {
        AchievementFeature achievementContainer = (AchievementFeature) player.getFeature(AchievementFeature.class);
        List<AchieveInfo> achieveInfoList = achievementContainer.loadAchieveList();
        TaskActivityFeature taskActivityFeature = (TaskActivityFeature) player.getFeature(TaskActivityFeature.class);
        taskActivityFeature.pushTaskActiveInfo();
        return achieveInfoList.toString();
    }

    @Command(comment = "完成任务")
    public String taskpick(Player player, int id) {
        AchievementFeature achievementContainer = (AchievementFeature) player.getFeature(AchievementFeature.class);
        List<AchieveInfo> achieveInfoList = achievementContainer.loadAchieveList();
        TaskActivityFeature taskActivityFeature = (TaskActivityFeature) player.getFeature(TaskActivityFeature.class);
        taskActivityFeature.pushTaskActiveInfo();
        return achieveInfoList.toString();
    }

    @Command(comment = "模拟世界聊天:内容")
    public String worldchat(Player player, String content) throws WSException {
        ChatMgr.getInstance().addChat(player, content, ChatType.CHATTYPE_WORLD, 0L);
        return "ok";
    }

    @Command(comment = "模拟首冲")
    public String firstrecharge(Player player) {
        ((FirstRecharge) ActivityMgr.getActivity(FirstRecharge.class)).sendFirstRechargeReward(player);
        return "ok";
    }

    @Command(comment = "每日签到刷新")
    public String dailySignInRefresh(Player player) {
        ((DailySign) ActivityMgr.getActivity(DailySign.class)).handDailyRefresh(player, 1);
        return "OK";
    }

    @Command(comment = "发放竞技场奖励")
    public String arenareward(Player player) {
        ArenaManager.getInstance().settle();
        return "OK";
    }

    @Command(comment = "发放决战奖励")
    public String droiyanreward(Player player) {
        RankManager.getInstance().settle(RankType.Droiyan, true);
        return "OK";
    }

    @Command(comment = "添加仇人")
    public String addEnemy(Player player) {
        int rank = RankManager.getInstance().getRank(RankType.Power, player.getPid());
        int Max = Math.min(rank + 20, RankManager.getInstance().getRankSize(RankType.Power));
        int begin = Math.max(rank + 1, 1);

        List<Long> rankList = new ArrayList<>();
        for (int i = begin; i < Max; i++) {
            long pid = RankManager.getInstance().getPlayerId(RankType.Power, i);
            rankList.add(Long.valueOf(pid));
        }

        Collections.shuffle(rankList);
        if (rankList.size() >= 1)
            ((DroiyanFeature) player.getFeature(DroiyanFeature.class)).addEnemy(((Long) rankList.get(0)).longValue());
        return "OK";
    }

    @Command(comment = "添加仇人")
    public String addDroiyan(Player player, long pid) {
        ((DroiyanFeature) player.getFeature(DroiyanFeature.class)).addDroiyan(pid);
        return "OK";
    }

    @Command(comment = "解锁战灵")
    public String unlockspirit(Player player) {
        for (RefWarSpirit ref : RefDataMgr.getAll(RefWarSpirit.class).values()) {
            try {
                WarSpiritFeature feature = (WarSpiritFeature) player.getFeature(WarSpiritFeature.class);
                if (feature.getWarSpirit(ref.id) == null)
                    ((WarSpiritFeature) player.getFeature(WarSpiritFeature.class)).unlockWarSpirit(ref.id);
            } catch (WSException e) {
                e.printStackTrace();
            }
        }
        return "OK";
    }

    @Command(comment = "决战宝图")
    public String droiyantreasure(Player player, int id) {
        ((DroiyanFeature) player.getFeature(DroiyanFeature.class)).addTreasure((RefDroiyanTreasure) RefDataMgr.get(RefDroiyanTreasure.class, Integer.valueOf(id)));
        return "OK";
    }

    @Command(comment = "刷新探金手玩家列表")
    public String refreshstealgold(Player player) {
        ((StealGoldFeature) player.getFeature(StealGoldFeature.class)).search();
        return "OK";
    }

    @Command(comment = "踢出所有人")
    public String kickall(Player player) {
        PlayerMgr.getInstance().kickoutAllPlayer();
        return "ok";
    }
}

