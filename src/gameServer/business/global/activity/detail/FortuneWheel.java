/*     */ package business.global.activity.detail;
/*     */ public class FortuneWheel extends Activity { public List<WheelExtReward> extRewards; public List<Integer> weightList;
/*     */   List<RollReward> rollReward;
/*     */   public int recharge;
/*     */   public int doubleNum;
/*     */   public List<WheelRecord> records;
/*     */   
/*     */   public class WheelExtReward { private int awardId;
/*     */     private int times;
/*     */     private Reward prize;
/*     */     
/*     */     public int getAwardId() {
/*     */       return this.awardId;
/*     */     }
/*     */     
/*     */     public void setAwardId(int awardId) {
/*     */       this.awardId = awardId;
/*     */     }
/*     */     
/*     */     public int getTimes() {
/*     */       return this.times;
/*     */     }
/*     */     
/*     */     public void setTimes(int times) {
/*     */       this.times = times;
/*     */     }
/*     */     
/*     */     public Reward getPrize() {
/*     */       return this.prize;
/*     */     }
/*     */     
/*     */     public void setPrize(Reward prize) {
/*     */       this.prize = prize;
/*     */     } }
/*     */   
/*     */   public FortuneWheel(ActivityBO data) {
/*  37 */     super(data);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  82 */     this.records = new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class WheelRecord
/*     */   {
/*     */     private WheelRecord(String name, Reward reward) {
/*  90 */       this.name = name;
/*  91 */       this.reward = reward;
/*     */     }
/*     */     String name;
/*     */     Reward reward; }
/*     */   
/*     */   public void load(JsonObject json) throws WSException {
/*  97 */     this.extRewards = Lists.newArrayList();
/*  98 */     for (JsonElement element : json.get("awards").getAsJsonArray()) {
/*  99 */       JsonObject obj = element.getAsJsonObject();
/* 100 */       WheelExtReward builder = new WheelExtReward();
/* 101 */       builder.setAwardId(obj.get("aid").getAsInt());
/* 102 */       builder.setTimes(obj.get("times").getAsInt());
/* 103 */       builder.setPrize(new Reward(obj.get("items").getAsJsonArray()));
/* 104 */       this.extRewards.add(builder);
/*     */     } 
/* 106 */     this.weightList = Lists.newArrayList();
/* 107 */     this.rollReward = Lists.newArrayList();
/* 108 */     for (JsonElement element : json.get("roll_awards").getAsJsonArray()) {
/* 109 */       JsonObject obj = element.getAsJsonObject();
/* 110 */       this.weightList.add(Integer.valueOf(obj.get("weight").getAsInt()));
/* 111 */       Reward reward = new Reward(obj.get("items").getAsJsonArray());
/* 112 */       this.rollReward.add(new RollReward(obj.get("aid").getAsInt(), reward, null));
/*     */     } 
/* 114 */     this.recharge = json.get("recharge").getAsInt();
/* 115 */     this.doubleNum = json.get("double").getAsInt();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String check(JsonObject json) throws RequestException {
/* 121 */     return "ok";
/*     */   }
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/* 126 */     return ActivityType.FortuneWheel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClosed() {
/* 131 */     clearActRecord();
/*     */   }
/*     */   
/*     */   public void dailyRefresh(Player player) {
/* 135 */     if (getStatus() != ActivityStatus.Open) {
/*     */       return;
/*     */     }
/* 138 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 139 */     bo.saveExtInt(0, bo.getExtInt(0) + 1);
/*     */     
/* 141 */     player.pushProto("FortuneWheelInfo", accumRechargeProto(player));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handlePlayerChange(Player player, int money) {
/* 151 */     if (getStatus() != ActivityStatus.Open) {
/*     */       return;
/*     */     }
/* 154 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*     */     
/* 156 */     int now_money = bo.getExtInt(2);
/* 157 */     now_money += money;
/* 158 */     int times = 0;
/*     */     while (true) {
/* 160 */       if (now_money < this.recharge) {
/* 161 */         bo.saveExtInt(2, now_money);
/*     */         break;
/*     */       } 
/* 164 */       now_money -= this.recharge;
/* 165 */       times++;
/*     */     } 
/*     */ 
/*     */     
/* 169 */     bo.setExtInt(0, bo.getExtInt(0) + times);
/* 170 */     bo.saveAll();
/*     */     
/* 172 */     player.pushProto("FortuneWheelInfo", accumRechargeProto(player));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FortuneWheelInfo accumRechargeProto(Player player) {
/* 182 */     FortuneWheelInfo info = new FortuneWheelInfo(null);
/* 183 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 184 */     info.rechargeNum = this.recharge;
/* 185 */     info.doubleNum = this.doubleNum;
/* 186 */     info.timesReward = this.extRewards;
/* 187 */     info.rollReward = this.rollReward;
/* 188 */     info.haveTimes = bo.getExtInt(0);
/* 189 */     info.useTimes = bo.getExtInt(1);
/* 190 */     info.nowRecharge = bo.getExtInt(2);
/* 191 */     info.records = this.records;
/* 192 */     return info;
/*     */   }
/*     */   
/*     */   private class FortuneWheelInfo {
/*     */     int rechargeNum;
/*     */     int doubleNum;
/*     */     List<FortuneWheel.WheelExtReward> timesReward;
/*     */     List<FortuneWheel.RollReward> rollReward;
/*     */     int haveTimes;
/*     */     int useTimes;
/*     */     int nowRecharge;
/*     */     List<FortuneWheel.WheelRecord> records;
/*     */     
/*     */     private FortuneWheelInfo() {}
/*     */   }
/*     */   
/*     */   private class RollReward {
/*     */     int aid;
/*     */     Reward reward;
/*     */     
/*     */     private RollReward(int aid, Reward reward) {
/* 213 */       this.aid = aid;
/* 214 */       this.reward = reward;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RollReward roll(Player player) throws WSException {
/* 225 */     Reward finalreward = new Reward();
/* 226 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 227 */     if (bo.getExtInt(0) <= 0) {
/* 228 */       throw new WSException(ErrorCode.Wheel_NoTimes, "次数不足");
/*     */     }
/* 230 */     int index = CommMath.getRandomIndexByRate(this.weightList);
/* 231 */     Reward reward = ((RollReward)this.rollReward.get(index)).reward;
/* 232 */     int num = bo.getExtInt(1) / this.doubleNum + 1;
/* 233 */     for (int i = 0; i < num; i++) {
/* 234 */       finalreward.combine(reward);
/*     */     }
/* 236 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(finalreward, ItemFlow.FortuneWheel);
/* 237 */     bo.setExtInt(0, bo.getExtInt(0) - 1);
/* 238 */     bo.setExtInt(1, bo.getExtInt(1) + 1);
/* 239 */     bo.saveAll();
/* 240 */     checkTimes(player);
/* 241 */     synchronized (this) {
/* 242 */       if (this.records.size() >= 30) {
/* 243 */         this.records.remove(0);
/*     */       }
/* 245 */       this.records.add(new WheelRecord(player.getName(), finalreward, null));
/*     */     } 
/*     */     
/* 248 */     player.pushProto("FortuneWheelInfo", accumRechargeProto(player));
/* 249 */     RollReward reward2 = new RollReward(((RollReward)this.rollReward.get(index)).aid, finalreward, null);
/*     */     
/* 251 */     return reward2;
/*     */   }
/*     */   
/*     */   public void checkTimes(Player player) {
/* 255 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 256 */     List<Integer> extreward = StringUtils.string2Integer(bo.getExtStr(0));
/* 257 */     for (WheelExtReward ext : this.extRewards) {
/* 258 */       if (extreward.contains(Integer.valueOf(ext.awardId))) {
/*     */         continue;
/*     */       }
/* 261 */       if (bo.getExtInt(1) >= ext.times) {
/* 262 */         MailCenter.getInstance().sendMail(player.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, ext.prize, new String[] {
/* 263 */               (new StringBuilder(String.valueOf(bo.getExtInt(1)))).toString() });
/* 264 */         extreward.add(Integer.valueOf(ext.awardId));
/* 265 */         bo.saveExtStr(0, StringUtils.list2String(extreward));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onOpen() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEnd() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActivityRecordBO createPlayerActRecord(Player player) {
/* 290 */     ActivityRecordBO bo = new ActivityRecordBO();
/* 291 */     bo.setPid(player.getPid());
/* 292 */     bo.setAid(this.bo.getId());
/* 293 */     bo.setActivity(getType().toString());
/* 294 */     bo.saveExtInt(0, 1);
/* 295 */     bo.insert();
/* 296 */     return bo;
/*     */   } }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/FortuneWheel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */