package business.global.activity.detail;

import business.global.activity.Activity;
import business.global.gmmail.MailCenter;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerItem;
import business.player.item.Reward;
import business.player.item.UniformItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.ConstEnum.LotteryType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Random;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import core.database.game.bo.LotteryBO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Lottery extends Activity {
    private Map<LotteryType, Lottery.Type> typeMap = new HashMap<>();
    private Map<Lottery.Type, List<LotteryBO>> typeList = new HashMap<>();
    private Map<Lottery.Type, List<LotteryBO>> rewardList = new HashMap<>();
    private Map<Lottery.Type, List<LotteryBO>> allList = new HashMap<>();

    public Lottery(ActivityBO bo) {
        super(bo);
    }

    @Override
    public void load(JsonObject json) throws WSException {
        for (JsonElement element : json.get("types").getAsJsonArray()) {
            JsonObject object = element.getAsJsonObject();
            Lottery.Type type = new Lottery.Type(object);
            this.typeMap.put(LotteryType.values()[type.type], type);
        }

        for (Lottery.Type type : this.typeMap.values()) {
            List<LotteryBO> list1 = new ArrayList<>();
            this.typeList.put(type, list1);
            List<LotteryBO> list2 = new ArrayList<>();
            this.rewardList.put(type, list2);
            List<LotteryBO> list3 = new ArrayList<>();
            this.allList.put(type, list3);
        }

        for (LotteryBO bo : BM.getBM(LotteryBO.class).findAll()) {
            if (bo.getBuyday() == 0) {
                bo.saveBuyday(CommTime.nowSecond());
            }

            if (bo.getBuyday() >= CommTime.getTodayZeroClockS()) {
                if (LotteryType.values()[bo.getType()] == LotteryType.normal) {
                    Lottery.Type type = this.typeMap.get(LotteryType.normal);
                    if (this.bo.getExtInt(0) == bo.getNum()) {
                        this.typeList.get(type).add(bo);
                    }

                    if (bo.getRewardday() != 0) {
                        this.rewardList.get(type).add(bo);
                    }

                    this.allList.get(type).add(bo);
                } else if (LotteryType.values()[bo.getType()] == LotteryType.rich) {
                    Lottery.Type typex = this.typeMap.get(LotteryType.rich);
                    if (this.bo.getExtInt(2) == bo.getNum()) {
                        this.typeList.get(typex).add(bo);
                    }

                    if (bo.getRewardday() != 0) {
                        this.rewardList.get(typex).add(bo);
                    }

                    this.allList.get(typex).add(bo);
                }
            }
        }

        for (Lottery.Type typexx : this.typeMap.values()) {
            this.checkReward(typexx);
        }
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onEnd() {
    }

    @Override
    public void onClosed() {
    }

    @Override
    public ActivityType getType() {
        return ActivityType.Lottery;
    }

    public Lottery.LotteryInfo attend(Player player, LotteryType type, int times) throws WSException {
        if (!this.isOpen()) {
            throw new WSException(ErrorCode.Activity_Close, "活动已关闭");
        } else {
            Lottery.Type lotteryType = this.typeMap.get(type);
            if (lotteryType == null) {
                throw new WSException(ErrorCode.Activity_Close, "活动已关闭");
            } else if (CommTime.getTodaySecond() >= lotteryType.begin && CommTime.getTodaySecond() <= lotteryType.end) {
                synchronized (this) {
                    List<UniformItem> listitem = new ArrayList<>();

                    for (int i = 0; i < times; i++) {
                        listitem.add(lotteryType.cost);
                    }

                    if (!player.getFeature(PlayerItem.class).checkAndConsume(listitem, ItemFlow.Lottery)) {
                        throw new WSException(ErrorCode.Not_Enough, "资源不足");
                    } else {
                        if (type == LotteryType.normal) {
                            if (times < 1 || this.bo.getExtInt(1) + times > lotteryType.rewardnum) {
                                throw new WSException(ErrorCode.Lottery_Error, "次数超过最大人数");
                            }

                            ActivityRecordBO bo = this.getOrCreateRecord(player);

                            for (int i = 0; i < times; i++) {
                                LotteryBO lotteryBO = new LotteryBO();
                                lotteryBO.setPid(player.getPid());
                                lotteryBO.setNum(this.bo.getExtInt(0));
                                lotteryBO.setNumber(this.randomNumber(lotteryType));
                                lotteryBO.setType(type.ordinal());
                                lotteryBO.setBuyday(CommTime.nowSecond());
                                lotteryBO.insert();
                                this.bo.saveExtInt(1, this.bo.getExtInt(1) + 1);
                                bo.saveExtInt(0, bo.getExtInt(0) + 1);
                                List<LotteryBO> list = this.typeList.get(lotteryType);
                                if (list == null) {
                                    List<LotteryBO> var17 = new ArrayList();
                                    var17.add(lotteryBO);
                                } else {
                                    list.add(lotteryBO);
                                }

                                List<LotteryBO> listh = this.allList.get(lotteryType);
                                if (listh == null) {
                                    List<LotteryBO> var20 = new ArrayList();
                                    var20.add(lotteryBO);
                                } else {
                                    listh.add(lotteryBO);
                                }
                            }
                        }

                        if (type == LotteryType.rich) {
                            if (times < 1 || this.bo.getExtInt(3) + times > lotteryType.rewardnum) {
                                throw new WSException(ErrorCode.Lottery_Error, "次数超过最大人数");
                            }

                            ActivityRecordBO bo = this.getOrCreateRecord(player);

                            for (int i = 0; i < times; i++) {
                                LotteryBO lotteryBOx = new LotteryBO();
                                lotteryBOx.setPid(player.getPid());
                                lotteryBOx.setNum(this.bo.getExtInt(2));
                                lotteryBOx.setNumber(this.randomNumber(lotteryType));
                                lotteryBOx.setType(type.ordinal());
                                lotteryBOx.setBuyday(CommTime.nowSecond());
                                lotteryBOx.insert();
                                this.bo.saveExtInt(3, this.bo.getExtInt(3) + 1);
                                bo.saveExtInt(1, bo.getExtInt(1) + 1);
                                List<LotteryBO> listx = this.typeList.get(lotteryType);
                                if (listx == null) {
                                    List<LotteryBO> var19 = new ArrayList();
                                    var19.add(lotteryBOx);
                                } else {
                                    listx.add(lotteryBOx);
                                }

                                List<LotteryBO> listh = this.allList.get(lotteryType);
                                if (listh == null) {
                                    List<LotteryBO> var22 = new ArrayList();
                                    var22.add(lotteryBOx);
                                } else {
                                    listh.add(lotteryBOx);
                                }
                            }
                        }

                        this.checkReward(lotteryType);
                        return this.build(player, lotteryType);
                    }
                }
            } else {
                throw new WSException(ErrorCode.Activity_Close, "不在时间内");
            }
        }
    }

    private int randomNumber(Lottery.Type type) {
        int num;
        do {
            num = Random.nextInt(100000000);
        } while (this.typeList.get(type).contains(num));

        return num;
    }

    private void checkReward(Lottery.Type type) {
        List<LotteryBO> list = this.typeList.get(type);
        int size = list.size();
        if (type.rewardnum <= size) {
            int index = Random.nextInt(size);
            LotteryBO bo = list.get(index);
            bo.saveRewardday(CommTime.nowSecond());
            this.rewardList.get(type).add(bo);
            MailCenter.getInstance().sendMail(bo.getPid(), "GM", "夺宝奖励", "恭喜你在夺宝活动中成为幸运用户，请查收", type.reward);
            List<Player> tmp_list = new ArrayList<>();
            if (LotteryType.values()[type.type] == LotteryType.normal) {
                if (this.bo.getExtInt(0) >= type.totalnum - 1) {
                    return;
                }

                for (LotteryBO tmpbo : list) {
                    Player tmpplayer = PlayerMgr.getInstance().getPlayer(tmpbo.getPid());
                    ActivityRecordBO recordbo = this.getOrCreateRecord(tmpplayer);
                    recordbo.saveExtInt(0, 0);
                    if (!tmp_list.contains(tmpplayer)) {
                        tmp_list.add(tmpplayer);
                    }
                }

                this.bo.saveExtInt(0, this.bo.getExtInt(0) + 1);
                this.bo.saveExtInt(1, 0);
                this.typeList.get(type).clear();
            } else if (LotteryType.values()[type.type] == LotteryType.rich) {
                if (this.bo.getExtInt(2) >= type.totalnum - 1) {
                    return;
                }

                for (LotteryBO tmpbox : list) {
                    Player tmpplayer = PlayerMgr.getInstance().getPlayer(tmpbox.getPid());
                    ActivityRecordBO recordbo = this.getOrCreateRecord(tmpplayer);
                    recordbo.saveExtInt(1, 0);
                    if (!tmp_list.contains(tmpplayer)) {
                        tmp_list.add(tmpplayer);
                    }
                }

                this.bo.saveExtInt(2, this.bo.getExtInt(2) + 1);
                this.bo.saveExtInt(3, 0);
                this.typeList.get(type).clear();
            }

            for (Player tmpplayer : tmp_list) {
                tmpplayer.pushProto("LotteryInfo", this.build(tmpplayer, type));
            }
        }
    }

    public Lottery.LotteryInfo build(Player player, Lottery.Type type) {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        Lottery.LotteryInfo info = new Lottery.LotteryInfo();
        if (LotteryType.values()[type.type] == LotteryType.normal) {
            info.setNum(this.bo.getExtInt(0));
            info.setPeople(this.bo.getExtInt(1));
            info.setMytime(bo.getExtInt(0));
        } else if (LotteryType.values()[type.type] == LotteryType.rich) {
            info.setNum(this.bo.getExtInt(2));
            info.setPeople(this.bo.getExtInt(3));
            info.setMytime(bo.getExtInt(1));
        }

        info.setBegin(type.begin);
        info.setEnd(type.end);
        info.setMaxnum(type.totalnum);
        info.setValue(type.value);
        info.setReward(type.reward);
        info.setCost(type.cost);
        info.setMaxpeople(type.rewardnum);
        info.setType(LotteryType.values()[type.type]);
        return info;
    }

    public List<Lottery.LotteryInfo> loadLotteryInfo(Player player, LotteryType type1) {
        List<Lottery.LotteryInfo> list = new ArrayList<>();
        Lottery.Type typetmp = this.typeMap.get(type1);
        if (typetmp != null) {
            list.add(this.build(player, typetmp));
            return list;
        } else {
            for (Lottery.Type type : this.typeMap.values()) {
                list.add(this.build(player, type));
            }

            return list;
        }
    }

    public Lottery.LotteryRewardInfo loadLotteryRwardInfo(Player player, LotteryType type1) {
        Lottery.Type type = this.typeMap.get(type1);
        Lottery.LotteryRewardInfo rewardlist = null;
        List<Lottery.RewardResult> list = new ArrayList<>();

        for (LotteryBO listbo : this.rewardList.get(type)) {
            String name = PlayerMgr.getInstance().getPlayer(listbo.getPid()).getName();
            list.add(new Lottery.RewardResult(listbo.getNum(), listbo.getNumber(), name));
        }

        if (LotteryType.values()[type.type] == LotteryType.normal) {
            rewardlist = new Lottery.LotteryRewardInfo(this.bo.getExtInt(0), type.totalnum, LotteryType.normal, list);
        } else if (LotteryType.values()[type.type] == LotteryType.rich) {
            rewardlist = new Lottery.LotteryRewardInfo(this.bo.getExtInt(2), type.totalnum, LotteryType.rich, list);
        }

        return rewardlist;
    }

    public List<Lottery.NumberInfo> loadNumber(Player player, LotteryType type1) {
        Lottery.Type type = this.typeMap.get(type1);
        Map<Integer, List<LotteryBO>> map = new HashMap<>();

        for (LotteryBO bo : this.allList.get(type)) {
            if (bo.getPid() == player.getPid()) {
                List<LotteryBO> list = map.get(bo.getNum());
                if (list == null) {
                    list = new ArrayList<>();
                }

                list.add(bo);
                map.put(bo.getNum(), list);
            }
        }

        List<Lottery.NumberInfo> list = new ArrayList<>();

        for (Entry<Integer, List<LotteryBO>> entry : map.entrySet()) {
            int rewardnum = 0;
            List<Integer> numbers = new ArrayList<>();

            for (LotteryBO box : entry.getValue()) {
                if (box.getRewardday() != 0) {
                    rewardnum = box.getNumber();
                }

                numbers.add(box.getNumber());
            }

            list.add(new Lottery.NumberInfo(entry.getKey(), type.totalnum, numbers, rewardnum));
        }

        return list;
    }

    public void dailyRefresh() {
        try {
            if (!this.isOpen()) {
                return;
            }

            for (Lottery.Type type : this.typeMap.values()) {
                if (LotteryType.values()[type.type] == LotteryType.normal) {
                    if (this.bo.getExtInt(1) < type.rewardnum) {
                        this.returnMoney(type);
                    }

                    this.bo.saveExtInt(0, 0);
                    this.bo.saveExtInt(1, 0);
                } else if (LotteryType.values()[type.type] == LotteryType.rich) {
                    if (this.bo.getExtInt(3) < type.rewardnum) {
                        this.returnMoney(type);
                    }

                    this.bo.saveExtInt(2, 0);
                    this.bo.saveExtInt(3, 0);
                }

                this.typeList.get(type).clear();
                this.rewardList.get(type).clear();
                this.allList.get(type).clear();
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    private void returnMoney(Lottery.Type type) {
        List<LotteryBO> listbo = this.typeList.get(type);
        List<Player> playerlist = new ArrayList<>();

        for (LotteryBO bo : listbo) {
            Player player = PlayerMgr.getInstance().getPlayer(bo.getPid());
            if (!playerlist.contains(player)) {
                playerlist.add(player);
            }
        }

        for (Player player : playerlist) {
            ActivityRecordBO recordbo = this.getOrCreateRecord(player);
            int count = 0;
            if (LotteryType.values()[type.type] == LotteryType.normal) {
                count = recordbo.getExtInt(0);
                recordbo.saveExtInt(0, 0);
            }

            if (LotteryType.values()[type.type] == LotteryType.rich) {
                count = recordbo.getExtInt(1);
                recordbo.saveExtInt(1, 0);
            }

            Reward reward = new Reward();

            for (int i = 0; i < count; i++) {
                reward.add(type.cost);
            }

            MailCenter.getInstance().sendMail(player.getPid(), "GM", "夺宝返还", "由于开奖人数不够，元宝已返还", reward);
        }
    }

    private static class LotteryInfo {
        int num;
        int maxnum;
        int value;
        int begin;
        int end;
        LotteryType type;
        Reward reward;
        UniformItem cost;
        int people;
        int maxpeople;
        int mytime;

        private LotteryInfo() {
        }

        public int getNum() {
            return this.num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getMaxnum() {
            return this.maxnum;
        }

        public void setMaxnum(int maxnum) {
            this.maxnum = maxnum;
        }

        public int getValue() {
            return this.value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public Reward getReward() {
            return this.reward;
        }

        public void setReward(Reward reward) {
            this.reward = reward;
        }

        public UniformItem getCost() {
            return this.cost;
        }

        public void setCost(UniformItem cost) {
            this.cost = cost;
        }

        public int getPeople() {
            return this.people;
        }

        public void setPeople(int people) {
            this.people = people;
        }

        public int getMaxpeople() {
            return this.maxpeople;
        }

        public void setMaxpeople(int maxpeople) {
            this.maxpeople = maxpeople;
        }

        public int getMytime() {
            return this.mytime;
        }

        public void setMytime(int mytime) {
            this.mytime = mytime;
        }

        public LotteryType getType() {
            return this.type;
        }

        public void setType(LotteryType type) {
            this.type = type;
        }

        public int getBegin() {
            return this.begin;
        }

        public void setBegin(int begin) {
            this.begin = begin;
        }

        public int getEnd() {
            return this.end;
        }

        public void setEnd(int end) {
            this.end = end;
        }
    }

    private static class LotteryRewardInfo {
        int num;
        int maxnum;
        LotteryType type;
        List<Lottery.RewardResult> list;

        private LotteryRewardInfo(int num, int maxnum, LotteryType type, List<Lottery.RewardResult> list) {
            this.num = num;
            this.maxnum = maxnum;
            this.type = type;
            this.list = list;
        }
    }

    private static class NumberInfo {
        int num;
        int maxnum;
        List<Integer> list;
        int rewardnum;

        private NumberInfo(int num, int maxnum, List<Integer> list, int rewardnum) {
            this.num = num;
            this.maxnum = maxnum;
            this.list = list;
            this.rewardnum = rewardnum;
        }
    }

    private static class RewardResult {
        int num;
        int number;
        String name;

        private RewardResult(int num, int number, String name) {
            this.num = num;
            this.number = number;
            this.name = name;
        }
    }

    private class Type {
        int id;
        int totalnum;
        int rewardnum;
        int type;
        int value;
        int begin;
        int end;
        UniformItem cost;
        Reward reward;

        private Type(JsonObject object) throws WSException {
            this.id = object.get("aid").getAsInt();
            this.totalnum = object.get("totalnum").getAsInt();
            this.rewardnum = object.get("rewardnum").getAsInt();
            this.type = object.get("type").getAsInt();
            this.value = object.get("value").getAsInt();
            this.begin = object.get("begin").getAsInt();
            this.end = object.get("end").getAsInt();
            this.cost = new UniformItem(object.get("costitem").getAsInt(), object.get("costcount").getAsInt());
            this.reward = new Reward(object.get("awards").getAsJsonArray());
        }
    }
}
