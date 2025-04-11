package business.global.activity.detail;

public class FortuneWheel extends Activity {
    public List<WheelExtReward> extRewards;
    public List<Integer> weightList;
    public int recharge;
    public int doubleNum;
    public List<WheelRecord> records;
    List<RollReward> rollReward;

    public FortuneWheel(ActivityBO data) {
        super(data);

        this.records = new ArrayList<>();
    }

    public void load(JsonObject json) throws WSException {
        this.extRewards = Lists.newArrayList();
        for (JsonElement element : json.get("awards").getAsJsonArray()) {
            JsonObject obj = element.getAsJsonObject();
            WheelExtReward builder = new WheelExtReward();
            builder.setAwardId(obj.get("aid").getAsInt());
            builder.setTimes(obj.get("times").getAsInt());
            builder.setPrize(new Reward(obj.get("items").getAsJsonArray()));
            this.extRewards.add(builder);
        }
        this.weightList = Lists.newArrayList();
        this.rollReward = Lists.newArrayList();
        for (JsonElement element : json.get("roll_awards").getAsJsonArray()) {
            JsonObject obj = element.getAsJsonObject();
            this.weightList.add(Integer.valueOf(obj.get("weight").getAsInt()));
            Reward reward = new Reward(obj.get("items").getAsJsonArray());
            this.rollReward.add(new RollReward(obj.get("aid").getAsInt(), reward, null));
        }
        this.recharge = json.get("recharge").getAsInt();
        this.doubleNum = json.get("double").getAsInt();
    }

    public String check(JsonObject json) throws RequestException {
        return "ok";
    }

    public ActivityType getType() {
        return ActivityType.FortuneWheel;
    }

    public void onClosed() {
        clearActRecord();
    }

    public void dailyRefresh(Player player) {
        if (getStatus() != ActivityStatus.Open) {
            return;
        }
        ActivityRecordBO bo = getOrCreateRecord(player);
        bo.saveExtInt(0, bo.getExtInt(0) + 1);

        player.pushProto("FortuneWheelInfo", accumRechargeProto(player));
    }

    public void handlePlayerChange(Player player, int money) {
        if (getStatus() != ActivityStatus.Open) {
            return;
        }
        ActivityRecordBO bo = getOrCreateRecord(player);

        int now_money = bo.getExtInt(2);
        now_money += money;
        int times = 0;
        while (true) {
            if (now_money < this.recharge) {
                bo.saveExtInt(2, now_money);
                break;
            }
            now_money -= this.recharge;
            times++;
        }

        bo.setExtInt(0, bo.getExtInt(0) + times);
        bo.saveAll();

        player.pushProto("FortuneWheelInfo", accumRechargeProto(player));
    }

    public FortuneWheelInfo accumRechargeProto(Player player) {
        FortuneWheelInfo info = new FortuneWheelInfo(null);
        ActivityRecordBO bo = getOrCreateRecord(player);
        info.rechargeNum = this.recharge;
        info.doubleNum = this.doubleNum;
        info.timesReward = this.extRewards;
        info.rollReward = this.rollReward;
        info.haveTimes = bo.getExtInt(0);
        info.useTimes = bo.getExtInt(1);
        info.nowRecharge = bo.getExtInt(2);
        info.records = this.records;
        return info;
    }

    public RollReward roll(Player player) throws WSException {
        Reward finalreward = new Reward();
        ActivityRecordBO bo = getOrCreateRecord(player);
        if (bo.getExtInt(0) <= 0) {
            throw new WSException(ErrorCode.Wheel_NoTimes, "次数不足");
        }
        int index = CommMath.getRandomIndexByRate(this.weightList);
        Reward reward = ((RollReward) this.rollReward.get(index)).reward;
        int num = bo.getExtInt(1) / this.doubleNum + 1;
        for (int i = 0; i < num; i++) {
            finalreward.combine(reward);
        }
        ((PlayerItem) player.getFeature(PlayerItem.class)).gain(finalreward, ItemFlow.FortuneWheel);
        bo.setExtInt(0, bo.getExtInt(0) - 1);
        bo.setExtInt(1, bo.getExtInt(1) + 1);
        bo.saveAll();
        checkTimes(player);
        synchronized (this) {
            if (this.records.size() >= 30) {
                this.records.remove(0);
            }
            this.records.add(new WheelRecord(player.getName(), finalreward, null));
        }

        player.pushProto("FortuneWheelInfo", accumRechargeProto(player));
        RollReward reward2 = new RollReward(((RollReward) this.rollReward.get(index)).aid, finalreward, null);

        return reward2;
    }

    public void checkTimes(Player player) {
        ActivityRecordBO bo = getOrCreateRecord(player);
        List<Integer> extreward = StringUtils.string2Integer(bo.getExtStr(0));
        for (WheelExtReward ext : this.extRewards) {
            if (extreward.contains(Integer.valueOf(ext.awardId))) {
                continue;
            }
            if (bo.getExtInt(1) >= ext.times) {
                MailCenter.getInstance().sendMail(player.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, ext.prize, new String[]{
                        (new StringBuilder(String.valueOf(bo.getExtInt(1)))).toString()});
                extreward.add(Integer.valueOf(ext.awardId));
                bo.saveExtStr(0, StringUtils.list2String(extreward));
            }
        }
    }

    public void onOpen() {
    }

    public void onEnd() {
    }

    public ActivityRecordBO createPlayerActRecord(Player player) {
        ActivityRecordBO bo = new ActivityRecordBO();
        bo.setPid(player.getPid());
        bo.setAid(this.bo.getId());
        bo.setActivity(getType().toString());
        bo.saveExtInt(0, 1);
        bo.insert();
        return bo;
    }

    private static class WheelRecord {
        String name;
        Reward reward;
        private WheelRecord(String name, Reward reward) {
            this.name = name;
            this.reward = reward;
        }
    }

    public class WheelExtReward {
        private int awardId;
        private int times;
        private Reward prize;

        public int getAwardId() {
            return this.awardId;
        }

        public void setAwardId(int awardId) {
            this.awardId = awardId;
        }

        public int getTimes() {
            return this.times;
        }

        public void setTimes(int times) {
            this.times = times;
        }

        public Reward getPrize() {
            return this.prize;
        }

        public void setPrize(Reward prize) {
            this.prize = prize;
        }
    }

    private class FortuneWheelInfo {
        int rechargeNum;
        int doubleNum;
        List<FortuneWheel.WheelExtReward> timesReward;
        List<FortuneWheel.RollReward> rollReward;
        int haveTimes;
        int useTimes;
        int nowRecharge;
        List<FortuneWheel.WheelRecord> records;

        private FortuneWheelInfo() {
        }
    }

    private class RollReward {
        int aid;
        Reward reward;

        private RollReward(int aid, Reward reward) {
            this.aid = aid;
            this.reward = reward;
        }
    }
}

