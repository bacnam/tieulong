package business.global.activity.detail;

import business.global.activity.Activity;
import business.global.gmmail.MailCenter;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerItem;
import business.player.item.Reward;
import business.player.item.UniformItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.ConstEnum;
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

public class Lottery
        extends Activity {
    private Map<ConstEnum.LotteryType, Type> typeMap;
    private Map<Type, List<LotteryBO>> typeList;
    private Map<Type, List<LotteryBO>> rewardList;
    private Map<Type, List<LotteryBO>> allList;

    public Lottery(ActivityBO bo) {
        super(bo);

        this.typeMap = new HashMap<>();

        this.typeList = new HashMap<>();

        this.rewardList = new HashMap<>();

        this.allList = new HashMap<>();
    }

    public void load(JsonObject json) throws WSException {
        JsonArray jsonarray = json.get("types").getAsJsonArray();
        for (JsonElement element : jsonarray) {
            JsonObject object = element.getAsJsonObject();
            Type type = new Type(object, null);
            this.typeMap.put(ConstEnum.LotteryType.values()[type.type], type);
        }

        for (Type type : this.typeMap.values()) {
            List<LotteryBO> list1 = new ArrayList<>();
            this.typeList.put(type, list1);
            List<LotteryBO> list2 = new ArrayList<>();
            this.rewardList.put(type, list2);
            List<LotteryBO> list3 = new ArrayList<>();
            this.allList.put(type, list3);
        }
        List<LotteryBO> allbo = BM.getBM(LotteryBO.class).findAll();
        for (LotteryBO bo : allbo) {
            if (bo.getBuyday() == 0) {
                bo.saveBuyday(CommTime.nowSecond());
            }
            if (bo.getBuyday() < CommTime.getTodayZeroClockS()) {
                continue;
            }
            if (ConstEnum.LotteryType.values()[bo.getType()] == ConstEnum.LotteryType.normal) {
                Type type = this.typeMap.get(ConstEnum.LotteryType.normal);
                if (this.bo.getExtInt(0) == bo.getNum()) {
                    ((List<LotteryBO>) this.typeList.get(type)).add(bo);
                }
                if (bo.getRewardday() != 0) {
                    ((List<LotteryBO>) this.rewardList.get(type)).add(bo);
                }
                ((List<LotteryBO>) this.allList.get(type)).add(bo);
                continue;
            }
            if (ConstEnum.LotteryType.values()[bo.getType()] == ConstEnum.LotteryType.rich) {
                Type type = this.typeMap.get(ConstEnum.LotteryType.rich);
                if (this.bo.getExtInt(2) == bo.getNum()) {
                    ((List<LotteryBO>) this.typeList.get(type)).add(bo);
                }
                if (bo.getRewardday() != 0) {
                    ((List<LotteryBO>) this.rewardList.get(type)).add(bo);
                }
                ((List<LotteryBO>) this.allList.get(type)).add(bo);
            }
        }

        for (Type type : this.typeMap.values()) {
            checkReward(type);
        }
    }

    public void onOpen() {
    }

    public void onEnd() {
    }

    public void onClosed() {
    }

    public ActivityType getType() {
        return ActivityType.Lottery;
    }

    public LotteryInfo attend(Player player, ConstEnum.LotteryType type, int times) throws WSException {
        if (!isOpen()) {
            throw new WSException(ErrorCode.Activity_Close, "活动已关闭");
        }
        Type lotteryType = this.typeMap.get(type);
        if (lotteryType == null) {
            throw new WSException(ErrorCode.Activity_Close, "活动已关闭");
        }

        if (CommTime.getTodaySecond() < lotteryType.begin || CommTime.getTodaySecond() > lotteryType.end) {
            throw new WSException(ErrorCode.Activity_Close, "不在时间内");
        }

        synchronized (this) {
            List<UniformItem> listitem = new ArrayList<>();
            for (int i = 0; i < times; i++) {
                listitem.add(lotteryType.cost);
            }

            if (!((PlayerItem) player.getFeature(PlayerItem.class)).checkAndConsume(listitem, ItemFlow.Lottery)) {
                throw new WSException(ErrorCode.Not_Enough, "资源不足");
            }
            if (type == ConstEnum.LotteryType.normal) {
                if (times < 1 || this.bo.getExtInt(1) + times > lotteryType.rewardnum) {
                    throw new WSException(ErrorCode.Lottery_Error, "次数超过最大人数");
                }

                ActivityRecordBO bo = getOrCreateRecord(player);
                for (int j = 0; j < times; j++) {
                    LotteryBO lotteryBO = new LotteryBO();
                    lotteryBO.setPid(player.getPid());
                    lotteryBO.setNum(this.bo.getExtInt(0));
                    lotteryBO.setNumber(randomNumber(lotteryType));
                    lotteryBO.setType(type.ordinal());
                    lotteryBO.setBuyday(CommTime.nowSecond());
                    lotteryBO.insert();
                    this.bo.saveExtInt(1, this.bo.getExtInt(1) + 1);
                    bo.saveExtInt(0, bo.getExtInt(0) + 1);

                    List<LotteryBO> list = this.typeList.get(lotteryType);
                    if (list == null) {
                        list = new ArrayList<>();
                        list.add(lotteryBO);
                    } else {
                        list.add(lotteryBO);
                    }

                    List<LotteryBO> listh = this.allList.get(lotteryType);
                    if (listh == null) {
                        listh = new ArrayList<>();
                        listh.add(lotteryBO);
                    } else {
                        listh.add(lotteryBO);
                    }
                }
            }
            if (type == ConstEnum.LotteryType.rich) {
                if (times < 1 || this.bo.getExtInt(3) + times > lotteryType.rewardnum) {
                    throw new WSException(ErrorCode.Lottery_Error, "次数超过最大人数");
                }

                ActivityRecordBO bo = getOrCreateRecord(player);
                for (int j = 0; j < times; j++) {
                    LotteryBO lotteryBO = new LotteryBO();
                    lotteryBO.setPid(player.getPid());
                    lotteryBO.setNum(this.bo.getExtInt(2));
                    lotteryBO.setNumber(randomNumber(lotteryType));
                    lotteryBO.setType(type.ordinal());
                    lotteryBO.setBuyday(CommTime.nowSecond());
                    lotteryBO.insert();
                    this.bo.saveExtInt(3, this.bo.getExtInt(3) + 1);
                    bo.saveExtInt(1, bo.getExtInt(1) + 1);

                    List<LotteryBO> list = this.typeList.get(lotteryType);
                    if (list == null) {
                        list = new ArrayList<>();
                        list.add(lotteryBO);
                    } else {
                        list.add(lotteryBO);
                    }

                    List<LotteryBO> listh = this.allList.get(lotteryType);
                    if (listh == null) {
                        listh = new ArrayList<>();
                        listh.add(lotteryBO);
                    } else {
                        listh.add(lotteryBO);
                    }
                }
            }
            checkReward(lotteryType);
            return build(player, lotteryType);
        }
    }

    private int randomNumber(Type type) {
        while (true) {
            int num = Random.nextInt(100000000);
            if (!((List) this.typeList.get(type)).contains(Integer.valueOf(num))) {
                return num;
            }
        }
    }

    private void checkReward(Type type) {
        List<LotteryBO> list = this.typeList.get(type);
        int size = list.size();
        if (type.rewardnum <= size) {
            int index = Random.nextInt(size);
            LotteryBO bo = list.get(index);
            bo.saveRewardday(CommTime.nowSecond());
            ((List<LotteryBO>) this.rewardList.get(type)).add(bo);

            MailCenter.getInstance().sendMail(bo.getPid(), "GM", "夺宝奖励", "恭喜你在夺宝活动中成为幸运用户，请查收", type.reward, new String[0]);
            List<Player> tmp_list = new ArrayList<>();
            if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.normal) {
                if (this.bo.getExtInt(0) >= type.totalnum - 1) {
                    return;
                }

                for (LotteryBO tmpbo : list) {
                    Player tmpplayer = PlayerMgr.getInstance().getPlayer(tmpbo.getPid());
                    ActivityRecordBO recordbo = getOrCreateRecord(tmpplayer);
                    recordbo.saveExtInt(0, 0);
                    if (!tmp_list.contains(tmpplayer)) {
                        tmp_list.add(tmpplayer);
                    }
                }
                this.bo.saveExtInt(0, this.bo.getExtInt(0) + 1);
                this.bo.saveExtInt(1, 0);
                ((List) this.typeList.get(type)).clear();
            } else if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.rich) {
                if (this.bo.getExtInt(2) >= type.totalnum - 1) {
                    return;
                }

                for (LotteryBO tmpbo : list) {
                    Player tmpplayer = PlayerMgr.getInstance().getPlayer(tmpbo.getPid());
                    ActivityRecordBO recordbo = getOrCreateRecord(tmpplayer);
                    recordbo.saveExtInt(1, 0);
                    if (!tmp_list.contains(tmpplayer)) {
                        tmp_list.add(tmpplayer);
                    }
                }
                this.bo.saveExtInt(2, this.bo.getExtInt(2) + 1);
                this.bo.saveExtInt(3, 0);
                ((List) this.typeList.get(type)).clear();
            }
            for (Player tmpplayer : tmp_list)
                tmpplayer.pushProto("LotteryInfo", build(tmpplayer, type));
        }
    }

    public LotteryInfo build(Player player, Type type) {
        ActivityRecordBO bo = getOrCreateRecord(player);
        LotteryInfo info = new LotteryInfo(null);
        if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.normal) {
            info.setNum(this.bo.getExtInt(0));
            info.setPeople(this.bo.getExtInt(1));
            info.setMytime(bo.getExtInt(0));
        } else if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.rich) {
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
        info.setType(ConstEnum.LotteryType.values()[type.type]);
        return info;
    }

    public List<LotteryInfo> loadLotteryInfo(Player player, ConstEnum.LotteryType type1) {
        List<LotteryInfo> list = new ArrayList<>();
        Type typetmp = this.typeMap.get(type1);
        if (typetmp != null) {
            list.add(build(player, typetmp));
            return list;
        }

        for (Type type : this.typeMap.values()) {
            list.add(build(player, type));
        }

        return list;
    }

    public LotteryRewardInfo loadLotteryRwardInfo(Player player, ConstEnum.LotteryType type1) {
        Type type = this.typeMap.get(type1);
        LotteryRewardInfo rewardlist = null;
        List<RewardResult> list = new ArrayList<>();
        for (LotteryBO listbo : this.rewardList.get(type)) {
            String name = PlayerMgr.getInstance().getPlayer(listbo.getPid()).getName();
            list.add(new RewardResult(listbo.getNum(), listbo.getNumber(), name, null));
        }

        if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.normal) {
            rewardlist = new LotteryRewardInfo(this.bo.getExtInt(0), type.totalnum, ConstEnum.LotteryType.normal, list, null);
        } else if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.rich) {
            rewardlist = new LotteryRewardInfo(this.bo.getExtInt(2), type.totalnum, ConstEnum.LotteryType.rich, list, null);
        }
        return rewardlist;
    }

    public List<NumberInfo> loadNumber(Player player, ConstEnum.LotteryType type1) {
        Type type = this.typeMap.get(type1);

        Map<Integer, List<LotteryBO>> map = new HashMap<>();

        for (LotteryBO bo : this.allList.get(type)) {
            if (bo.getPid() == player.getPid()) {
                List<LotteryBO> list1 = map.get(Integer.valueOf(bo.getNum()));
                if (list1 == null)
                    list1 = new ArrayList<>();
                list1.add(bo);
                map.put(Integer.valueOf(bo.getNum()), list1);
            }
        }

        List<NumberInfo> list = new ArrayList<>();
        for (Map.Entry<Integer, List<LotteryBO>> entry : map.entrySet()) {
            int rewardnum = 0;
            List<Integer> numbers = new ArrayList<>();
            for (LotteryBO bo : entry.getValue()) {
                if (bo.getRewardday() != 0) {
                    rewardnum = bo.getNumber();
                }
                numbers.add(Integer.valueOf(bo.getNumber()));
            }
            list.add(new NumberInfo(((Integer) entry.getKey()).intValue(), type.totalnum, numbers, rewardnum, null));
        }

        return list;
    }

    public void dailyRefresh() {
        try {
            if (!isOpen()) {
                return;
            }
            for (Type type : this.typeMap.values()) {
                if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.normal) {

                    if (this.bo.getExtInt(1) < type.rewardnum) {
                        returnMoney(type);
                    }
                    this.bo.saveExtInt(0, 0);
                    this.bo.saveExtInt(1, 0);
                } else if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.rich) {

                    if (this.bo.getExtInt(3) < type.rewardnum) {
                        returnMoney(type);
                    }
                    this.bo.saveExtInt(2, 0);
                    this.bo.saveExtInt(3, 0);
                }

                ((List) this.typeList.get(type)).clear();
                ((List) this.rewardList.get(type)).clear();
                ((List) this.allList.get(type)).clear();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void returnMoney(Type type) {
        List<LotteryBO> listbo = this.typeList.get(type);

        List<Player> playerlist = new ArrayList<>();

        for (LotteryBO bo : listbo) {
            Player player = PlayerMgr.getInstance().getPlayer(bo.getPid());
            if (!playerlist.contains(player)) {
                playerlist.add(player);
            }
        }

        for (Player player : playerlist) {
            ActivityRecordBO recordbo = getOrCreateRecord(player);
            int count = 0;
            if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.normal) {
                count = recordbo.getExtInt(0);
                recordbo.saveExtInt(0, 0);
            }
            if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.rich) {
                count = recordbo.getExtInt(1);
                recordbo.saveExtInt(1, 0);
            }
            Reward reward = new Reward();
            for (int i = 0; i < count; i++) {
                reward.add(type.cost);
            }

            MailCenter.getInstance().sendMail(player.getPid(), "GM", "夺宝返还", "由于开奖人数不够，元宝已返还", reward, new String[0]);
        }
    }

    private static class LotteryInfo {
        int num;
        int maxnum;
        int value;
        int begin;
        int end;
        ConstEnum.LotteryType type;
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

        public ConstEnum.LotteryType getType() {
            return this.type;
        }

        public void setType(ConstEnum.LotteryType type) {
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
        ConstEnum.LotteryType type;
        List<Lottery.RewardResult> list;

        private LotteryRewardInfo(int num, int maxnum, ConstEnum.LotteryType type, List<Lottery.RewardResult> list) {
            this.num = num;
            this.maxnum = maxnum;
            this.type = type;
            this.list = list;
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

