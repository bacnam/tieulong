/*     */ package business.global.activity.detail;
/*     */ 
/*     */ import business.global.activity.Activity;
/*     */ import business.global.gmmail.MailCenter;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.item.Reward;
/*     */ import business.player.item.UniformItem;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.ActivityType;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Random;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.database.game.bo.ActivityRecordBO;
/*     */ import core.database.game.bo.LotteryBO;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Lottery
/*     */   extends Activity
/*     */ {
/*     */   private Map<ConstEnum.LotteryType, Type> typeMap;
/*     */   private Map<Type, List<LotteryBO>> typeList;
/*     */   private Map<Type, List<LotteryBO>> rewardList;
/*     */   private Map<Type, List<LotteryBO>> allList;
/*     */   
/*     */   public Lottery(ActivityBO bo) {
/*  43 */     super(bo);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  48 */     this.typeMap = new HashMap<>();
/*     */ 
/*     */     
/*  51 */     this.typeList = new HashMap<>();
/*     */ 
/*     */     
/*  54 */     this.rewardList = new HashMap<>();
/*     */ 
/*     */     
/*  57 */     this.allList = new HashMap<>();
/*     */   }
/*     */   
/*     */   private class Type {
/*     */     int id;
/*     */     int totalnum;
/*     */     int rewardnum;
/*     */     int type;
/*     */     int value;
/*     */     int begin;
/*     */     int end;
/*     */     UniformItem cost;
/*     */     Reward reward;
/*     */     
/*     */     private Type(JsonObject object) throws WSException {
/*  72 */       this.id = object.get("aid").getAsInt();
/*  73 */       this.totalnum = object.get("totalnum").getAsInt();
/*  74 */       this.rewardnum = object.get("rewardnum").getAsInt();
/*  75 */       this.type = object.get("type").getAsInt();
/*  76 */       this.value = object.get("value").getAsInt();
/*  77 */       this.begin = object.get("begin").getAsInt();
/*  78 */       this.end = object.get("end").getAsInt();
/*  79 */       this.cost = new UniformItem(object.get("costitem").getAsInt(), object.get("costcount").getAsInt());
/*  80 */       this.reward = new Reward(object.get("awards").getAsJsonArray());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(JsonObject json) throws WSException {
/*  86 */     JsonArray jsonarray = json.get("types").getAsJsonArray();
/*  87 */     for (JsonElement element : jsonarray) {
/*  88 */       JsonObject object = element.getAsJsonObject();
/*  89 */       Type type = new Type(object, null);
/*  90 */       this.typeMap.put(ConstEnum.LotteryType.values()[type.type], type);
/*     */     } 
/*     */     
/*  93 */     for (Type type : this.typeMap.values()) {
/*  94 */       List<LotteryBO> list1 = new ArrayList<>();
/*  95 */       this.typeList.put(type, list1);
/*  96 */       List<LotteryBO> list2 = new ArrayList<>();
/*  97 */       this.rewardList.put(type, list2);
/*  98 */       List<LotteryBO> list3 = new ArrayList<>();
/*  99 */       this.allList.put(type, list3);
/*     */     } 
/* 101 */     List<LotteryBO> allbo = BM.getBM(LotteryBO.class).findAll();
/* 102 */     for (LotteryBO bo : allbo) {
/* 103 */       if (bo.getBuyday() == 0) {
/* 104 */         bo.saveBuyday(CommTime.nowSecond());
/*     */       }
/* 106 */       if (bo.getBuyday() < CommTime.getTodayZeroClockS()) {
/*     */         continue;
/*     */       }
/* 109 */       if (ConstEnum.LotteryType.values()[bo.getType()] == ConstEnum.LotteryType.normal) {
/* 110 */         Type type = this.typeMap.get(ConstEnum.LotteryType.normal);
/* 111 */         if (this.bo.getExtInt(0) == bo.getNum()) {
/* 112 */           ((List<LotteryBO>)this.typeList.get(type)).add(bo);
/*     */         }
/* 114 */         if (bo.getRewardday() != 0) {
/* 115 */           ((List<LotteryBO>)this.rewardList.get(type)).add(bo);
/*     */         }
/* 117 */         ((List<LotteryBO>)this.allList.get(type)).add(bo); continue;
/*     */       } 
/* 119 */       if (ConstEnum.LotteryType.values()[bo.getType()] == ConstEnum.LotteryType.rich) {
/* 120 */         Type type = this.typeMap.get(ConstEnum.LotteryType.rich);
/* 121 */         if (this.bo.getExtInt(2) == bo.getNum()) {
/* 122 */           ((List<LotteryBO>)this.typeList.get(type)).add(bo);
/*     */         }
/* 124 */         if (bo.getRewardday() != 0) {
/* 125 */           ((List<LotteryBO>)this.rewardList.get(type)).add(bo);
/*     */         }
/* 127 */         ((List<LotteryBO>)this.allList.get(type)).add(bo);
/*     */       } 
/*     */     } 
/*     */     
/* 131 */     for (Type type : this.typeMap.values()) {
/* 132 */       checkReward(type);
/*     */     }
/*     */   }
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
/*     */   public void onEnd() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onClosed() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/* 157 */     return ActivityType.Lottery;
/*     */   }
/*     */   
/*     */   public LotteryInfo attend(Player player, ConstEnum.LotteryType type, int times) throws WSException {
/* 161 */     if (!isOpen()) {
/* 162 */       throw new WSException(ErrorCode.Activity_Close, "活动已关闭");
/*     */     }
/* 164 */     Type lotteryType = this.typeMap.get(type);
/* 165 */     if (lotteryType == null) {
/* 166 */       throw new WSException(ErrorCode.Activity_Close, "活动已关闭");
/*     */     }
/*     */     
/* 169 */     if (CommTime.getTodaySecond() < lotteryType.begin || CommTime.getTodaySecond() > lotteryType.end) {
/* 170 */       throw new WSException(ErrorCode.Activity_Close, "不在时间内");
/*     */     }
/*     */     
/* 173 */     synchronized (this) {
/* 174 */       List<UniformItem> listitem = new ArrayList<>();
/* 175 */       for (int i = 0; i < times; i++) {
/* 176 */         listitem.add(lotteryType.cost);
/*     */       }
/*     */       
/* 179 */       if (!((PlayerItem)player.getFeature(PlayerItem.class)).checkAndConsume(listitem, ItemFlow.Lottery)) {
/* 180 */         throw new WSException(ErrorCode.Not_Enough, "资源不足");
/*     */       }
/* 182 */       if (type == ConstEnum.LotteryType.normal) {
/* 183 */         if (times < 1 || this.bo.getExtInt(1) + times > lotteryType.rewardnum) {
/* 184 */           throw new WSException(ErrorCode.Lottery_Error, "次数超过最大人数");
/*     */         }
/*     */         
/* 187 */         ActivityRecordBO bo = getOrCreateRecord(player);
/* 188 */         for (int j = 0; j < times; j++) {
/* 189 */           LotteryBO lotteryBO = new LotteryBO();
/* 190 */           lotteryBO.setPid(player.getPid());
/* 191 */           lotteryBO.setNum(this.bo.getExtInt(0));
/* 192 */           lotteryBO.setNumber(randomNumber(lotteryType));
/* 193 */           lotteryBO.setType(type.ordinal());
/* 194 */           lotteryBO.setBuyday(CommTime.nowSecond());
/* 195 */           lotteryBO.insert();
/* 196 */           this.bo.saveExtInt(1, this.bo.getExtInt(1) + 1);
/* 197 */           bo.saveExtInt(0, bo.getExtInt(0) + 1);
/*     */ 
/*     */           
/* 200 */           List<LotteryBO> list = this.typeList.get(lotteryType);
/* 201 */           if (list == null) {
/* 202 */             list = new ArrayList<>();
/* 203 */             list.add(lotteryBO);
/*     */           } else {
/* 205 */             list.add(lotteryBO);
/*     */           } 
/*     */           
/* 208 */           List<LotteryBO> listh = this.allList.get(lotteryType);
/* 209 */           if (listh == null) {
/* 210 */             listh = new ArrayList<>();
/* 211 */             listh.add(lotteryBO);
/*     */           } else {
/* 213 */             listh.add(lotteryBO);
/*     */           } 
/*     */         } 
/* 216 */       }  if (type == ConstEnum.LotteryType.rich) {
/* 217 */         if (times < 1 || this.bo.getExtInt(3) + times > lotteryType.rewardnum) {
/* 218 */           throw new WSException(ErrorCode.Lottery_Error, "次数超过最大人数");
/*     */         }
/*     */         
/* 221 */         ActivityRecordBO bo = getOrCreateRecord(player);
/* 222 */         for (int j = 0; j < times; j++) {
/* 223 */           LotteryBO lotteryBO = new LotteryBO();
/* 224 */           lotteryBO.setPid(player.getPid());
/* 225 */           lotteryBO.setNum(this.bo.getExtInt(2));
/* 226 */           lotteryBO.setNumber(randomNumber(lotteryType));
/* 227 */           lotteryBO.setType(type.ordinal());
/* 228 */           lotteryBO.setBuyday(CommTime.nowSecond());
/* 229 */           lotteryBO.insert();
/* 230 */           this.bo.saveExtInt(3, this.bo.getExtInt(3) + 1);
/* 231 */           bo.saveExtInt(1, bo.getExtInt(1) + 1);
/*     */ 
/*     */           
/* 234 */           List<LotteryBO> list = this.typeList.get(lotteryType);
/* 235 */           if (list == null) {
/* 236 */             list = new ArrayList<>();
/* 237 */             list.add(lotteryBO);
/*     */           } else {
/* 239 */             list.add(lotteryBO);
/*     */           } 
/*     */           
/* 242 */           List<LotteryBO> listh = this.allList.get(lotteryType);
/* 243 */           if (listh == null) {
/* 244 */             listh = new ArrayList<>();
/* 245 */             listh.add(lotteryBO);
/*     */           } else {
/* 247 */             listh.add(lotteryBO);
/*     */           } 
/*     */         } 
/* 250 */       }  checkReward(lotteryType);
/* 251 */       return build(player, lotteryType);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int randomNumber(Type type) {
/*     */     while (true) {
/* 258 */       int num = Random.nextInt(100000000);
/* 259 */       if (!((List)this.typeList.get(type)).contains(Integer.valueOf(num))) {
/* 260 */         return num;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkReward(Type type) {
/* 266 */     List<LotteryBO> list = this.typeList.get(type);
/* 267 */     int size = list.size();
/* 268 */     if (type.rewardnum <= size) {
/* 269 */       int index = Random.nextInt(size);
/* 270 */       LotteryBO bo = list.get(index);
/* 271 */       bo.saveRewardday(CommTime.nowSecond());
/* 272 */       ((List<LotteryBO>)this.rewardList.get(type)).add(bo);
/*     */       
/* 274 */       MailCenter.getInstance().sendMail(bo.getPid(), "GM", "夺宝奖励", "恭喜你在夺宝活动中成为幸运用户，请查收", type.reward, new String[0]);
/* 275 */       List<Player> tmp_list = new ArrayList<>();
/* 276 */       if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.normal) {
/* 277 */         if (this.bo.getExtInt(0) >= type.totalnum - 1) {
/*     */           return;
/*     */         }
/*     */         
/* 281 */         for (LotteryBO tmpbo : list) {
/* 282 */           Player tmpplayer = PlayerMgr.getInstance().getPlayer(tmpbo.getPid());
/* 283 */           ActivityRecordBO recordbo = getOrCreateRecord(tmpplayer);
/* 284 */           recordbo.saveExtInt(0, 0);
/* 285 */           if (!tmp_list.contains(tmpplayer)) {
/* 286 */             tmp_list.add(tmpplayer);
/*     */           }
/*     */         } 
/* 289 */         this.bo.saveExtInt(0, this.bo.getExtInt(0) + 1);
/* 290 */         this.bo.saveExtInt(1, 0);
/* 291 */         ((List)this.typeList.get(type)).clear();
/*     */       }
/* 293 */       else if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.rich) {
/* 294 */         if (this.bo.getExtInt(2) >= type.totalnum - 1) {
/*     */           return;
/*     */         }
/*     */         
/* 298 */         for (LotteryBO tmpbo : list) {
/* 299 */           Player tmpplayer = PlayerMgr.getInstance().getPlayer(tmpbo.getPid());
/* 300 */           ActivityRecordBO recordbo = getOrCreateRecord(tmpplayer);
/* 301 */           recordbo.saveExtInt(1, 0);
/* 302 */           if (!tmp_list.contains(tmpplayer)) {
/* 303 */             tmp_list.add(tmpplayer);
/*     */           }
/*     */         } 
/* 306 */         this.bo.saveExtInt(2, this.bo.getExtInt(2) + 1);
/* 307 */         this.bo.saveExtInt(3, 0);
/* 308 */         ((List)this.typeList.get(type)).clear();
/*     */       } 
/* 310 */       for (Player tmpplayer : tmp_list)
/* 311 */         tmpplayer.pushProto("LotteryInfo", build(tmpplayer, type)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class LotteryInfo
/*     */   {
/*     */     int num;
/*     */     int maxnum;
/*     */     int value;
/*     */     int begin;
/*     */     int end;
/*     */     ConstEnum.LotteryType type;
/*     */     Reward reward;
/*     */     UniformItem cost;
/*     */     int people;
/*     */     int maxpeople;
/*     */     int mytime;
/*     */     
/*     */     private LotteryInfo() {}
/*     */     
/*     */     public int getNum() {
/* 332 */       return this.num;
/*     */     }
/*     */     
/*     */     public void setNum(int num) {
/* 336 */       this.num = num;
/*     */     }
/*     */     
/*     */     public int getMaxnum() {
/* 340 */       return this.maxnum;
/*     */     }
/*     */     
/*     */     public void setMaxnum(int maxnum) {
/* 344 */       this.maxnum = maxnum;
/*     */     }
/*     */     
/*     */     public int getValue() {
/* 348 */       return this.value;
/*     */     }
/*     */     
/*     */     public void setValue(int value) {
/* 352 */       this.value = value;
/*     */     }
/*     */     
/*     */     public Reward getReward() {
/* 356 */       return this.reward;
/*     */     }
/*     */     
/*     */     public void setReward(Reward reward) {
/* 360 */       this.reward = reward;
/*     */     }
/*     */     
/*     */     public UniformItem getCost() {
/* 364 */       return this.cost;
/*     */     }
/*     */     
/*     */     public void setCost(UniformItem cost) {
/* 368 */       this.cost = cost;
/*     */     }
/*     */     
/*     */     public int getPeople() {
/* 372 */       return this.people;
/*     */     }
/*     */     
/*     */     public void setPeople(int people) {
/* 376 */       this.people = people;
/*     */     }
/*     */     
/*     */     public int getMaxpeople() {
/* 380 */       return this.maxpeople;
/*     */     }
/*     */     
/*     */     public void setMaxpeople(int maxpeople) {
/* 384 */       this.maxpeople = maxpeople;
/*     */     }
/*     */     
/*     */     public int getMytime() {
/* 388 */       return this.mytime;
/*     */     }
/*     */     
/*     */     public void setMytime(int mytime) {
/* 392 */       this.mytime = mytime;
/*     */     }
/*     */     
/*     */     public ConstEnum.LotteryType getType() {
/* 396 */       return this.type;
/*     */     }
/*     */     
/*     */     public void setType(ConstEnum.LotteryType type) {
/* 400 */       this.type = type;
/*     */     }
/*     */     
/*     */     public int getBegin() {
/* 404 */       return this.begin;
/*     */     }
/*     */     
/*     */     public void setBegin(int begin) {
/* 408 */       this.begin = begin;
/*     */     }
/*     */     
/*     */     public int getEnd() {
/* 412 */       return this.end;
/*     */     }
/*     */     
/*     */     public void setEnd(int end) {
/* 416 */       this.end = end;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public LotteryInfo build(Player player, Type type) {
/* 422 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 423 */     LotteryInfo info = new LotteryInfo(null);
/* 424 */     if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.normal) {
/* 425 */       info.setNum(this.bo.getExtInt(0));
/* 426 */       info.setPeople(this.bo.getExtInt(1));
/* 427 */       info.setMytime(bo.getExtInt(0));
/* 428 */     } else if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.rich) {
/* 429 */       info.setNum(this.bo.getExtInt(2));
/* 430 */       info.setPeople(this.bo.getExtInt(3));
/* 431 */       info.setMytime(bo.getExtInt(1));
/*     */     } 
/* 433 */     info.setBegin(type.begin);
/* 434 */     info.setEnd(type.end);
/* 435 */     info.setMaxnum(type.totalnum);
/* 436 */     info.setValue(type.value);
/* 437 */     info.setReward(type.reward);
/* 438 */     info.setCost(type.cost);
/* 439 */     info.setMaxpeople(type.rewardnum);
/* 440 */     info.setType(ConstEnum.LotteryType.values()[type.type]);
/* 441 */     return info;
/*     */   }
/*     */   
/*     */   public List<LotteryInfo> loadLotteryInfo(Player player, ConstEnum.LotteryType type1) {
/* 445 */     List<LotteryInfo> list = new ArrayList<>();
/* 446 */     Type typetmp = this.typeMap.get(type1);
/* 447 */     if (typetmp != null) {
/* 448 */       list.add(build(player, typetmp));
/* 449 */       return list;
/*     */     } 
/*     */     
/* 452 */     for (Type type : this.typeMap.values()) {
/* 453 */       list.add(build(player, type));
/*     */     }
/*     */     
/* 456 */     return list;
/*     */   }
/*     */   
/*     */   private static class LotteryRewardInfo
/*     */   {
/*     */     int num;
/*     */     int maxnum;
/*     */     ConstEnum.LotteryType type;
/*     */     List<Lottery.RewardResult> list;
/*     */     
/*     */     private LotteryRewardInfo(int num, int maxnum, ConstEnum.LotteryType type, List<Lottery.RewardResult> list) {
/* 467 */       this.num = num;
/* 468 */       this.maxnum = maxnum;
/* 469 */       this.type = type;
/* 470 */       this.list = list;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class RewardResult
/*     */   {
/*     */     int num;
/*     */     
/*     */     int number;
/*     */     String name;
/*     */     
/*     */     private RewardResult(int num, int number, String name) {
/* 483 */       this.num = num;
/* 484 */       this.number = number;
/* 485 */       this.name = name;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public LotteryRewardInfo loadLotteryRwardInfo(Player player, ConstEnum.LotteryType type1) {
/* 491 */     Type type = this.typeMap.get(type1);
/* 492 */     LotteryRewardInfo rewardlist = null;
/* 493 */     List<RewardResult> list = new ArrayList<>();
/* 494 */     for (LotteryBO listbo : this.rewardList.get(type)) {
/* 495 */       String name = PlayerMgr.getInstance().getPlayer(listbo.getPid()).getName();
/* 496 */       list.add(new RewardResult(listbo.getNum(), listbo.getNumber(), name, null));
/*     */     } 
/*     */     
/* 499 */     if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.normal) {
/* 500 */       rewardlist = new LotteryRewardInfo(this.bo.getExtInt(0), type.totalnum, ConstEnum.LotteryType.normal, list, null);
/* 501 */     } else if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.rich) {
/* 502 */       rewardlist = new LotteryRewardInfo(this.bo.getExtInt(2), type.totalnum, ConstEnum.LotteryType.rich, list, null);
/*     */     } 
/* 504 */     return rewardlist;
/*     */   }
/*     */   
/*     */   public List<NumberInfo> loadNumber(Player player, ConstEnum.LotteryType type1) {
/* 508 */     Type type = this.typeMap.get(type1);
/*     */     
/* 510 */     Map<Integer, List<LotteryBO>> map = new HashMap<>();
/*     */     
/* 512 */     for (LotteryBO bo : this.allList.get(type)) {
/* 513 */       if (bo.getPid() == player.getPid()) {
/* 514 */         List<LotteryBO> list1 = map.get(Integer.valueOf(bo.getNum()));
/* 515 */         if (list1 == null)
/* 516 */           list1 = new ArrayList<>(); 
/* 517 */         list1.add(bo);
/* 518 */         map.put(Integer.valueOf(bo.getNum()), list1);
/*     */       } 
/*     */     } 
/*     */     
/* 522 */     List<NumberInfo> list = new ArrayList<>();
/* 523 */     for (Map.Entry<Integer, List<LotteryBO>> entry : map.entrySet()) {
/* 524 */       int rewardnum = 0;
/* 525 */       List<Integer> numbers = new ArrayList<>();
/* 526 */       for (LotteryBO bo : entry.getValue()) {
/* 527 */         if (bo.getRewardday() != 0) {
/* 528 */           rewardnum = bo.getNumber();
/*     */         }
/* 530 */         numbers.add(Integer.valueOf(bo.getNumber()));
/*     */       } 
/* 532 */       list.add(new NumberInfo(((Integer)entry.getKey()).intValue(), type.totalnum, numbers, rewardnum, null));
/*     */     } 
/*     */     
/* 535 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class NumberInfo
/*     */   {
/*     */     int num;
/*     */     int maxnum;
/*     */     List<Integer> list;
/*     */     int rewardnum;
/*     */     
/*     */     private NumberInfo(int num, int maxnum, List<Integer> list, int rewardnum) {
/* 547 */       this.num = num;
/* 548 */       this.maxnum = maxnum;
/* 549 */       this.list = list;
/* 550 */       this.rewardnum = rewardnum;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void dailyRefresh() {
/*     */     try {
/* 557 */       if (!isOpen()) {
/*     */         return;
/*     */       }
/* 560 */       for (Type type : this.typeMap.values()) {
/* 561 */         if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.normal) {
/*     */           
/* 563 */           if (this.bo.getExtInt(1) < type.rewardnum)
/*     */           {
/* 565 */             returnMoney(type);
/*     */           }
/* 567 */           this.bo.saveExtInt(0, 0);
/* 568 */           this.bo.saveExtInt(1, 0);
/* 569 */         } else if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.rich) {
/*     */           
/* 571 */           if (this.bo.getExtInt(3) < type.rewardnum)
/*     */           {
/* 573 */             returnMoney(type);
/*     */           }
/* 575 */           this.bo.saveExtInt(2, 0);
/* 576 */           this.bo.saveExtInt(3, 0);
/*     */         } 
/*     */         
/* 579 */         ((List)this.typeList.get(type)).clear();
/* 580 */         ((List)this.rewardList.get(type)).clear();
/* 581 */         ((List)this.allList.get(type)).clear();
/*     */       }
/*     */     
/* 584 */     } catch (Exception e) {
/*     */       
/* 586 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void returnMoney(Type type) {
/* 591 */     List<LotteryBO> listbo = this.typeList.get(type);
/*     */     
/* 593 */     List<Player> playerlist = new ArrayList<>();
/*     */     
/* 595 */     for (LotteryBO bo : listbo) {
/* 596 */       Player player = PlayerMgr.getInstance().getPlayer(bo.getPid());
/* 597 */       if (!playerlist.contains(player)) {
/* 598 */         playerlist.add(player);
/*     */       }
/*     */     } 
/*     */     
/* 602 */     for (Player player : playerlist) {
/* 603 */       ActivityRecordBO recordbo = getOrCreateRecord(player);
/* 604 */       int count = 0;
/* 605 */       if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.normal) {
/* 606 */         count = recordbo.getExtInt(0);
/* 607 */         recordbo.saveExtInt(0, 0);
/*     */       } 
/* 609 */       if (ConstEnum.LotteryType.values()[type.type] == ConstEnum.LotteryType.rich) {
/* 610 */         count = recordbo.getExtInt(1);
/* 611 */         recordbo.saveExtInt(1, 0);
/*     */       } 
/* 613 */       Reward reward = new Reward();
/* 614 */       for (int i = 0; i < count; i++) {
/* 615 */         reward.add(type.cost);
/*     */       }
/*     */       
/* 618 */       MailCenter.getInstance().sendMail(player.getPid(), "GM", "夺宝返还", "由于开奖人数不够，元宝已返还", reward, new String[0]);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/Lottery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */