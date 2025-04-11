package business.player.feature.marry;

import business.global.notice.NoticeMgr;
import business.global.rank.RankManager;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.Feature;
import business.player.feature.PlayerBase;
import business.player.feature.PlayerCurrency;
import business.player.feature.PlayerItem;
import business.player.feature.character.CharFeature;
import business.player.feature.features.PlayerRecord;
import business.player.item.Reward;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.StringUtils;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.*;
import core.database.game.bo.MarryApplyBO;
import core.database.game.bo.MarryBO;
import core.database.game.bo.MarryDivorceApplyBO;
import core.network.proto.LoverInfo;
import core.network.proto.MarryApplyInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarryFeature
        extends Feature {
    public MarryBO bo;
    public MarryApplyBO sendApplys;
    public List<MarryApplyBO> receiveApplys;
    public MarryDivorceApplyBO divorceApplys;
    public List<MarryDivorceApplyBO> receiveDivorceApplys;
    private int Max_List_Size;
    private int receive_apply_size;
    public MarryFeature(Player player) {
        super(player);

        this.Max_List_Size = RefDataMgr.getFactor("MarryList", 4);
        this.receive_apply_size = RefDataMgr.getFactor("MarryReceiveApply", 3);
    }

    public void loadDB() {
        this.bo = (MarryBO) BM.getBM(MarryBO.class).findOne("pid", Long.valueOf(getPid()));
        this.sendApplys = (MarryApplyBO) BM.getBM(MarryApplyBO.class).findOne("from_pid", Long.valueOf(getPid()));
        this.receiveApplys = BM.getBM(MarryApplyBO.class).findAll("pid", Long.valueOf(getPid()));
        this.receiveDivorceApplys = BM.getBM(MarryDivorceApplyBO.class).findAll("pid", Long.valueOf(getPid()));
        if (this.bo == null) {
            this.bo = new MarryBO();
            this.bo.setPid(getPid());
            this.bo.insert();
        }
    }

    public List<Player> getMarryList() throws WSException {
        if (this.bo.getSex() == 0) {
            throw new WSException(ErrorCode.Marry_SexNot, "玩家没选择性别");
        }
        List<Long> except = new ArrayList<>();
        except.add(Long.valueOf(getPid()));
        List<Player> list = PlayerMgr.getInstance().randomLoadSexPlayers(getSex(), this.Max_List_Size, except);
        return list;
    }

    public List<MarryApplyBO> getApplyList() throws WSException {
        List<MarryApplyBO> remove = new ArrayList<>();
        this.receiveApplys.stream().filter(x -> !checkApply(x)).forEach(x -> paramList.add(x));

        this.receiveApplys.removeAll(remove);
        return this.receiveApplys;
    }

    public List<Player> search(String name) throws WSException {
        List<Player> players = new ArrayList<>();

        Player one = (Player) (PlayerMgr.getInstance()).sexPlayers.get(name);
        if (one != null &&
                ((MarryFeature) one.getFeature(MarryFeature.class)).bo.getSex() != this.bo.getSex()) {
            players.add(one);
            return players;
        }

        for (Player player : (PlayerMgr.getInstance()).sexPlayers.values()) {
            if (((MarryFeature) player.getFeature(MarryFeature.class)).bo.getSex() == this.bo.getSex()) {
                continue;
            }
            if (player.getName().contains(name)) {
                players.add(player);
            }
            if (players.size() >= this.Max_List_Size) {
                break;
            }
        }
        Collections.shuffle(players);
        return players;
    }

    public void marryApply(long pid) throws WSException {
        if (this.sendApplys != null) {
            throw new WSException(ErrorCode.Marry_ApplyFull, "玩家已求婚");
        }

        Player toPlayer = PlayerMgr.getInstance().getPlayer(pid);
        MarryFeature to_feature = (MarryFeature) toPlayer.getFeature(MarryFeature.class);

        List<MarryApplyBO> remove = new ArrayList<>();
        to_feature.receiveApplys.stream().filter(x -> !checkApply(x)).forEach(x -> paramList.add(x));

        to_feature.receiveApplys.removeAll(remove);
        if (to_feature.receiveApplys.size() >= to_feature.receive_apply_size) {
            throw new WSException(ErrorCode.Marry_ApplyFull, "玩家申请已满");
        }

        if (!((PlayerItem) this.player.getFeature(PlayerItem.class)).check(MarryConfig.getMarryApplyCostId(), MarryConfig.getMarryApplyCostCount())) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "玩家缺少结婚戒指");
        }

        MarryApplyBO bo = to_feature.receiveApply(this.player);
        marryApply(bo);
    }

    public MarryApplyBO receiveApply(Player applyPlayer) throws WSException {
        synchronized (this) {
            MarryApplyBO applybo = new MarryApplyBO();
            applybo.setPid(getPid());
            applybo.setFromPid(applyPlayer.getPid());
            applybo.setApplyTime(CommTime.nowSecond());
            applybo.insert();
            this.receiveApplys.add(applybo);
            MarryApplyInfo info = new MarryApplyInfo();
            info.setSummary(((PlayerBase) applyPlayer.getFeature(PlayerBase.class)).summary());
            info.setLeftTime(getLeftTime(applybo));
            this.player.pushProto("newMarryApply", info);
            return applybo;
        }
    }

    public void marryApply(MarryApplyBO applybo) {
        this.sendApplys = applybo;
    }

    public MarryApplyBO getApply(long pid) {
        for (MarryApplyBO applybo : this.receiveApplys) {
            if (applybo.getFromPid() == pid) {
                return applybo;
            }
        }
        return null;
    }

    public MarryDivorceApplyBO getDivorceApply(long pid) {
        for (MarryDivorceApplyBO bo : this.receiveDivorceApplys) {
            if (bo.getFromPid() == pid) {
                return bo;
            }
        }
        return null;
    }

    public boolean checkApply(MarryApplyBO bo) {
        boolean check = (CommTime.nowSecond() - bo.getApplyTime() < RefDataMgr.getFactor("MarryApplyTime", 1800));
        if (!check) {
            Player toPlayer = PlayerMgr.getInstance().getPlayer(bo.getFromPid());
            MarryFeature to_feature = (MarryFeature) toPlayer.getFeature(MarryFeature.class);
            to_feature.removeApply(bo);
        }

        return check;
    }

    public boolean checkApply(MarryDivorceApplyBO bo) {
        boolean check = (CommTime.nowSecond() - bo.getApplyTime() < RefDataMgr.getFactor("MarryApplyTime", 1800));
        if (!check) {
            Player toPlayer = PlayerMgr.getInstance().getPlayer(bo.getFromPid());
            MarryFeature to_feature = (MarryFeature) toPlayer.getFeature(MarryFeature.class);
            to_feature.removeApply(bo);
        }

        return check;
    }

    public void agreedMarry(long pid) throws WSException {
        MarryApplyBO bo = getApply(pid);
        if (bo == null || !checkApply(bo)) {
            throw new WSException(ErrorCode.Marry_ApplyFail, "玩家申请失效");
        }

        Player toPlayer = PlayerMgr.getInstance().getPlayer(pid);
        MarryFeature to_feature = (MarryFeature) toPlayer.getFeature(MarryFeature.class);

        if (to_feature.isMarried()) {
            throw new WSException(ErrorCode.Marry_Already, "玩家已结婚");
        }

        if (!((PlayerItem) toPlayer.getFeature(PlayerItem.class)).checkAndConsume(MarryConfig.getMarryApplyCostId(), MarryConfig.getMarryApplyCostCount(),
                ItemFlow.MarryApply)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "对方缺少结婚戒指");
        }

        to_feature.beLoverWith(getPid());
        to_feature.sendApplys.del();
        to_feature.sendApplys = null;
        toPlayer.pushProto("beAgreed", to_feature.getLoveInfo());

        this.receiveApplys.remove(bo);
        beLoverWith(pid);

        Player man = null;
        if (this.bo.getSex() == ConstEnum.SexType.Man.ordinal()) {
            man = this.player;
        } else {
            man = toPlayer;
        }

        RankManager.getInstance().update(RankType.Lovers, man.getPid(), ((
                (CharFeature) this.player.getFeature(CharFeature.class)).getPower() + ((CharFeature) toPlayer.getFeature(CharFeature.class)).getPower()));

        broadcast();
    }

    public void broadcast() {
        broadcastProtol pro = new broadcastProtol(null);
        pro.husbend = (getLoveInfo().getHusband()).name;
        pro.wife = (getLoveInfo().getWife()).name;
        pro.reward = ((RefReward) RefDataMgr.get(RefReward.class, Integer.valueOf(RefDataMgr.getFactor("MarryAllReward", 500001)))).genReward();

        for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
            ((PlayerItem) player.getFeature(PlayerItem.class)).gain(pro.reward, ItemFlow.MarryAllReward);
            player.pushProto("NewMarry", pro);
        }

        NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.NewMarry, new String[]{pro.husbend, pro.wife});
    }

    public void beLoverWith(long pid) {
        this.bo.saveLoverPid(pid);
        this.bo.saveMarried(ConstEnum.MarryType.Married.ordinal());

        RefReward ref = (RefReward) RefDataMgr.get(RefReward.class, Integer.valueOf(MarryConfig.getMarryReward()));
        if (ref != null) {
            ((PlayerItem) this.player.getFeature(PlayerItem.class)).gain(ref.genReward(), ItemFlow.MarryReward);
        }
    }

    public void disagreedMarry(long pid) throws WSException {
        MarryApplyBO bo = getApply(pid);
        if (bo == null || !checkApply(bo)) {
            throw new WSException(ErrorCode.Marry_ApplyFail, "玩家申请失效");
        }
        bo.del();
        Player toPlayer = PlayerMgr.getInstance().getPlayer(pid);
        MarryFeature to_feature = (MarryFeature) toPlayer.getFeature(MarryFeature.class);
        to_feature.removeApply(bo);
        this.receiveApplys.remove(bo);
    }

    public void removeApply(MarryApplyBO bo) {
        synchronized (this) {
            if (this.sendApplys == null) {
                return;
            }
            this.sendApplys = null;
            bo.del();
            this.player.pushProto("beReject", "");
        }
    }

    public void removeApply(MarryDivorceApplyBO bo) {
        synchronized (this) {
            if (this.divorceApplys == null || this.divorceApplys != bo) {
                return;
            }
            this.divorceApplys = null;
            bo.del();
            this.player.pushProto("beDivorceReject", "");
        }
    }

    public void sendFlower() throws WSException {
        PlayerRecord record = (PlayerRecord) this.player.getFeature(PlayerRecord.class);
        int times = record.getValue(ConstEnum.DailyRefresh.LoversSend);

        PlayerCurrency playerCurrency = (PlayerCurrency) this.player.getFeature(PlayerCurrency.class);
        if (!playerCurrency.checkAndConsume(PrizeType.Crystal, (RefCrystalPrice.getPrize(times)).LoversSend, ItemFlow.MarryApply)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "玩家元宝不足");
        }
        record.addValue(ConstEnum.DailyRefresh.LoversSend);
        Player toPlayer = getLover();
        MarryFeature to_feature = (MarryFeature) toPlayer.getFeature(MarryFeature.class);
        to_feature.gainExp(RefDataMgr.getFactor("sendFlowerExp", 100));
        toPlayer.pushProto("flower", to_feature.bo);
        gainExp(RefDataMgr.getFactor("sendFlowerExp", 100));
        this.player.pushProto("flower", this.bo);
    }

    public Player getLover() {
        return PlayerMgr.getInstance().getPlayer(this.bo.getLoverPid());
    }

    public MarryFeature getLoverFeature() {
        return (MarryFeature) PlayerMgr.getInstance().getPlayer(this.bo.getLoverPid()).getFeature(MarryFeature.class);
    }

    public int gainExp(int Exp) {
        RefMarryLevel nextLeveLinfo = (RefMarryLevel) RefDataMgr.get(RefMarryLevel.class, Integer.valueOf(this.bo.getLevel() + 1));
        if (nextLeveLinfo == null) {
            return 0;
        }
        this.bo.saveExp(this.bo.getExp() + Exp);
        RefMarryLevel levelinfo = (RefMarryLevel) RefDataMgr.get(RefMarryLevel.class, Integer.valueOf(this.bo.getLevel()));
        if (this.bo.getExp() < levelinfo.NeedExp) {
            return Exp;
        }
        this.bo.setExp(this.bo.getExp() - levelinfo.NeedExp);
        this.bo.setLevel(this.bo.getLevel() + 1);
        if (RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(this.bo.getLevel() + 1)) == null) {
            this.bo.setExp(0);
        }
        this.bo.saveAll();

        return Exp;
    }

    public void divorce() {
        if (this.bo.getSex() == ConstEnum.SexType.Man.ordinal()) {
            RankManager.getInstance().clearPlayerData(this.player, RankType.Lovers);
        }
        this.bo.setLoverPid(0L);
        this.bo.setMarried(0);
        this.bo.setExp(0);
        this.bo.setLevel(0);
        this.bo.setSignin(0);
        this.bo.setSignReward("");
        this.bo.setLevelReward("");
        this.bo.setIsSign(false);
        this.bo.saveAll();
    }

    public void marryApply(MarryDivorceApplyBO applybo) {
        this.divorceApplys = applybo;
    }

    public MarryDivorceApplyBO marryDivorceApply(long pid) throws WSException {
        if (this.divorceApplys != null) {
            throw new WSException(ErrorCode.Marry_ApplyFull, "玩家已发送过离婚");
        }
        if (!((PlayerItem) this.player.getFeature(PlayerItem.class)).check(MarryConfig.getMarryDivorceApplyCostId(), MarryConfig.getMarryDivorceApplyCostCount())) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "玩家缺少离婚许可");
        }

        Player toPlayer = PlayerMgr.getInstance().getPlayer(pid);
        MarryFeature to_feature = (MarryFeature) toPlayer.getFeature(MarryFeature.class);
        MarryDivorceApplyBO bo = to_feature.receiveDivorceApply(this.player);
        marryApply(bo);
        return bo;
    }

    public MarryDivorceApplyBO receiveDivorceApply(Player applyPlayer) throws WSException {
        synchronized (this) {
            MarryDivorceApplyBO applybo = new MarryDivorceApplyBO();
            applybo.setPid(getPid());
            applybo.setFromPid(applyPlayer.getPid());
            applybo.setApplyTime(CommTime.nowSecond());
            applybo.insert();
            this.receiveDivorceApplys.add(applybo);
            MarryApplyInfo info = new MarryApplyInfo();
            info.setSummary(((PlayerBase) applyPlayer.getFeature(PlayerBase.class)).summary());
            info.setLeftTime(getDivorceLeftTime(applybo));
            this.player.pushProto("newDivorceApply", info);
            return applybo;
        }
    }

    public void disagreedDivorce(long pid) throws WSException {
        MarryDivorceApplyBO bo = getDivorceApply(pid);
        if (bo == null || !checkApply(bo)) {
            throw new WSException(ErrorCode.Marry_ApplyFail, "玩家申请失效");
        }
        bo.del();
        Player toPlayer = PlayerMgr.getInstance().getPlayer(pid);
        MarryFeature to_feature = (MarryFeature) toPlayer.getFeature(MarryFeature.class);
        to_feature.removeApply(bo);
        this.receiveDivorceApplys.remove(bo);
    }

    public void agreedDivorce(long pid) throws WSException {
        MarryDivorceApplyBO bo = getDivorceApply(pid);
        if (bo == null) {
            throw new WSException(ErrorCode.Marry_ApplyFail, "玩家申请失效");
        }

        Player toPlayer = PlayerMgr.getInstance().getPlayer(pid);
        MarryFeature to_feature = (MarryFeature) toPlayer.getFeature(MarryFeature.class);

        if (!to_feature.isMarried()) {
            throw new WSException(ErrorCode.Marry_DivorceAlready, "玩家已离婚");
        }

        if (!((PlayerItem) toPlayer.getFeature(PlayerItem.class)).checkAndConsume(MarryConfig.getMarryDivorceApplyCostId(), MarryConfig.getMarryDivorceApplyCostCount(),
                ItemFlow.MarryApply)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "对方缺少离婚许可");
        }

        to_feature.divorce();
        bo.del();
        to_feature.divorceApplys = null;
        toPlayer.pushProto("beDivorce", to_feature.getLoveInfo());
        divorce();

        removeApply(this.divorceApplys);
        this.receiveDivorceApplys.remove(bo);
    }

    public int getLeftTime(MarryApplyBO bo) {
        return Math.max(0, RefDataMgr.getFactor("MarryApplyTime", 1800) - CommTime.nowSecond() - bo.getApplyTime());
    }

    public int getDivorceLeftTime(MarryDivorceApplyBO bo) {
        return Math.max(0, RefDataMgr.getFactor("MarryApplyTime", 1800) - CommTime.nowSecond() - bo.getApplyTime());
    }

    public LoverInfo getLoveInfo() {
        if (this.bo.getSex() == 0 || this.bo.getLoverPid() == 0L) {
            return null;
        }

        LoverInfo info = new LoverInfo();
        if (this.bo.getSex() == ConstEnum.SexType.Man.ordinal()) {
            info.setHusband(((PlayerBase) this.player.getFeature(PlayerBase.class)).summary());
            info.setWife(((PlayerBase) PlayerMgr.getInstance().getPlayer(this.bo.getLoverPid()).getFeature(PlayerBase.class)).summary());
        } else {
            info.setHusband(((PlayerBase) PlayerMgr.getInstance().getPlayer(this.bo.getLoverPid()).getFeature(PlayerBase.class)).summary());
            info.setWife(((PlayerBase) this.player.getFeature(PlayerBase.class)).summary());
        }

        info.setLevel(this.bo.getLevel());
        info.setExp(this.bo.getExp());

        PlayerRecord record = (PlayerRecord) this.player.getFeature(PlayerRecord.class);
        int times = record.getValue(ConstEnum.DailyRefresh.LoversSend);
        info.setFlowerTimes(times);

        info.setAlreadyPick(StringUtils.string2Integer(this.bo.getLevelReward()));
        return info;
    }

    public boolean isMarried() {
        return (this.bo.getMarried() != 0);
    }

    public int getSex() {
        return this.bo.getSex();
    }

    public void setSex(ConstEnum.SexType sex) throws WSException {
        if (this.bo.getSex() != 0) {
            throw new WSException(ErrorCode.Marry_SexExit, "玩家已有性别");
        }
        this.bo.saveSex(sex.ordinal());
        (PlayerMgr.getInstance()).sexPlayers.put(this.player.getName(), this.player);
    }

    public int signIn() throws WSException {
        if (!isMarried()) {
            throw new WSException(ErrorCode.Marry_NotYet, "尚未结婚");
        }

        if (this.bo.getIsSign()) {
            throw new WSException(ErrorCode.Marry_SignAlready, "已签到");
        }

        this.bo.saveSignin(getSignin() + 1);
        this.bo.saveIsSign(true);
        getLover().pushProto("LoverSign", Integer.valueOf(getSignin()));
        return getSignin();
    }

    public int getSignin() {
        return this.bo.getSignin();
    }

    public Reward pickSignInReward(int signId) throws WSException {
        RefSignIn ref = (RefSignIn) RefDataMgr.get(RefSignIn.class, Integer.valueOf(signId));
        if (ref == null) {
            throw new WSException(ErrorCode.Marry_SignNotEnough, "签到未找到");
        }

        if (ref.Day > getSignin() || ref.Day > getLoverFeature().getSignin()) {
            throw new WSException(ErrorCode.Marry_SignNotEnough, "签到天数不足");
        }
        List<Integer> pickReward = StringUtils.string2Integer(this.bo.getSignReward());
        if (pickReward.contains(Integer.valueOf(signId))) {
            throw new WSException(ErrorCode.Marry_SignAlreadyPick, "签到奖励已领取");
        }
        Reward reward = ((PlayerItem) this.player.getFeature(PlayerItem.class)).gain(ref.UniformitemId, ref.Count, ItemFlow.MarrySign);
        pickReward.add(Integer.valueOf(signId));
        this.bo.saveSignReward(StringUtils.list2String(pickReward));
        return reward;
    }

    public Reward pickLevelReward(int level) throws WSException {
        RefMarryLevel ref = (RefMarryLevel) RefDataMgr.get(RefMarryLevel.class, Integer.valueOf(level));
        if (ref == null) {
            throw new WSException(ErrorCode.Marry_LevelNotFound, "等级未找到");
        }
        if (ref.id > this.bo.getLevel()) {
            throw new WSException(ErrorCode.Marry_LevelNotEnough, "恩爱等级不足");
        }
        List<Integer> pickReward = StringUtils.string2Integer(this.bo.getLevelReward());
        if (pickReward.contains(Integer.valueOf(level))) {
            throw new WSException(ErrorCode.Marry_SignAlreadyPick, "奖励已领取");
        }
        Reward reward = ((PlayerItem) this.player.getFeature(PlayerItem.class)).gain(((RefReward) RefDataMgr.get(RefReward.class, Integer.valueOf(ref.RewardId))).genReward(), ItemFlow.MarrySign);
        pickReward.add(Integer.valueOf(level));
        this.bo.saveLevelReward(StringUtils.list2String(pickReward));
        return reward;
    }

    public void dailyRefresh() {
        try {
            this.bo.saveIsSign(false);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static class broadcastProtol {
        String husbend;
        String wife;
        Reward reward;

        private broadcastProtol() {
        }
    }
}

