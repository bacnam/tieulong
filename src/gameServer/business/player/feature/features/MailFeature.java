/*     */ package business.player.feature.features;
/*     */ 
/*     */ import business.global.gmmail.MailCenter;
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.item.Reward;
/*     */ import com.zhonglian.server.common.utils.CommString;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Lists;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.database.game.bo.GlobalMailBO;
/*     */ import core.database.game.bo.PlayerMailBO;
/*     */ import core.network.proto.MailInfo;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MailFeature
/*     */   extends Feature
/*     */ {
/*     */   public final Map<Long, PlayerMailBO> mailMap;
/*     */   
/*     */   public MailFeature(Player owner) {
/*  35 */     super(owner);
/*     */ 
/*     */     
/*  38 */     this.mailMap = new ConcurrentHashMap<>();
/*     */   }
/*     */   
/*     */   public void loadDB() {
/*  42 */     this.mailMap.clear();
/*  43 */     MailCenter logic = MailCenter.getInstance();
/*  44 */     List<PlayerMailBO> boList = logic.loadPrivateMail(this.player.getPid());
/*  45 */     for (PlayerMailBO bo : boList) {
/*  46 */       this.mailMap.put(Long.valueOf(bo.getId()), bo);
/*     */     }
/*  48 */     logic.delPrivateMail(this.player.getPid());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onConnect() {
/*  55 */     achieveGlobalMail();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void achieveGlobalMail() {
/*  62 */     synchronized (this) {
/*     */       
/*  64 */       int curTime = CommTime.nowSecond();
/*     */       
/*  66 */       int playerCreateTime = this.player.getPlayerBO().getCreateTime();
/*     */       
/*  68 */       long oldCheckId = this.player.getPlayerBO().getGmMailCheckId();
/*     */       
/*  70 */       long newCheckId = oldCheckId;
/*     */       
/*  72 */       List<GlobalMailBO> gms = MailCenter.getInstance().getGlobalMailList();
/*     */       
/*  74 */       for (int index = gms.size() - 1; index >= 0; index--) {
/*  75 */         GlobalMailBO bo = gms.get(index);
/*     */         
/*  77 */         if (bo.getId() <= oldCheckId) {
/*     */           break;
/*     */         }
/*     */         
/*  81 */         if (bo.getCreateTime() >= playerCreateTime)
/*     */         {
/*     */ 
/*     */           
/*  85 */           if (bo.getCreateTime() + bo.getExistTime() >= curTime) {
/*     */ 
/*     */ 
/*     */             
/*  89 */             PlayerMailBO pmail = new PlayerMailBO();
/*  90 */             pmail.setPid(this.player.getPid());
/*  91 */             pmail.setSenderName(bo.getSenderName());
/*  92 */             pmail.setTitle(bo.getTitle());
/*  93 */             pmail.setContent(bo.getContent());
/*  94 */             pmail.setCreateTime(bo.getCreateTime());
/*  95 */             pmail.setExistTime(bo.getExistTime());
/*  96 */             pmail.setPickUpExistTime(bo.getPickUpExistTime());
/*  97 */             pmail.setUniformIDList(bo.getUniformIDList());
/*  98 */             pmail.setUniformCountList(bo.getUniformCountList());
/*  99 */             pmail.setGlobalMailID(bo.getId());
/*     */             
/* 101 */             pmail.insert();
/*     */             
/* 103 */             addMail(pmail);
/*     */             
/* 105 */             newCheckId = Math.max(newCheckId, pmail.getGlobalMailID());
/*     */           }  } 
/* 107 */       }  if (oldCheckId != newCheckId) {
/* 108 */         ((PlayerCurrency)this.player.getFeature(PlayerCurrency.class)).updateMaxGlobalMailID(newCheckId);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getExistTime(PlayerMailBO bo) {
/* 120 */     if (bo.getPickUpTime() == 0) {
/* 121 */       return bo.getCreateTime() + bo.getExistTime();
/*     */     }
/* 123 */     return bo.getPickUpTime() + bo.getPickUpExistTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMail(PlayerMailBO bo) {
/* 132 */     this.mailMap.put(Long.valueOf(bo.getId()), bo);
/* 133 */     this.player.pushProto("addMail", parseToProto(bo));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeMail(PlayerMailBO bo) {
/* 142 */     this.mailMap.remove(Long.valueOf(bo.getId()));
/* 143 */     bo.del();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MailInfo> cmd_getMailList() {
/* 152 */     List<PlayerMailBO> mailList = Lists.newArrayList();
/* 153 */     List<PlayerMailBO> overdueMailList = Lists.newArrayList();
/*     */     
/* 155 */     int curTime = CommTime.nowSecond();
/*     */     
/* 157 */     for (PlayerMailBO bo : this.mailMap.values()) {
/* 158 */       int maxExistTime = getExistTime(bo);
/* 159 */       if (maxExistTime < curTime) {
/* 160 */         overdueMailList.add(bo);
/*     */         continue;
/*     */       } 
/* 163 */       mailList.add(bo);
/*     */     } 
/*     */     
/* 166 */     Collections.sort(mailList, (o1, o2) -> {
/*     */           if (o2.getPickUpTime() == 0 && o1.getPickUpTime() != 0) {
/*     */             return 1;
/*     */           }
/*     */           if (o2.getPickUpTime() != 0 && o1.getPickUpTime() == 0) {
/*     */             return -1;
/*     */           }
/*     */           int o1Max = getExistTime(o1);
/*     */           int o2Max = getExistTime(o2);
/*     */           return o1Max - o2Max;
/*     */         });
/* 177 */     int MaxMailCount = RefDataMgr.getFactor("MaxMailCount", 50);
/* 178 */     if (mailList.size() >= MaxMailCount) {
/* 179 */       mailList = mailList.subList(0, MaxMailCount);
/*     */     }
/*     */     
/* 182 */     for (PlayerMailBO bo : overdueMailList) {
/* 183 */       removeMail(bo);
/*     */     }
/*     */     
/* 186 */     List<MailInfo> protoList = Lists.newArrayList();
/* 187 */     for (PlayerMailBO bo : mailList) {
/* 188 */       protoList.add(parseToProto(bo));
/*     */     }
/* 190 */     return protoList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pickUpMail(long mailId, WebSocketRequest request) {
/* 200 */     PlayerMailBO PlayerMailBO = this.mailMap.get(Long.valueOf(mailId));
/* 201 */     if (PlayerMailBO == null) {
/* 202 */       request.error(ErrorCode.InvalidParam, "没有找到邮件，邮件id:" + mailId, new Object[0]);
/*     */       return;
/*     */     } 
/* 205 */     if (PlayerMailBO.getPickUpTime() != 0) {
/* 206 */       request.error(ErrorCode.InvalidParam, "邮件已领取，邮件id:%s,领取时间%s", new Object[] { Long.valueOf(mailId), Integer.valueOf(PlayerMailBO.getPickUpTime()) });
/*     */       
/*     */       return;
/*     */     } 
/* 210 */     request.response(pickUp(mailId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pickUpAllReward(WebSocketRequest request) {
/* 219 */     if (this.mailMap.isEmpty()) {
/* 220 */       request.error(ErrorCode.InvalidParam, "邮箱里没有邮件", new Object[0]);
/*     */       return;
/*     */     } 
/* 223 */     List<MailInfo> protoList = Lists.newArrayList();
/* 224 */     for (PlayerMailBO bo : this.mailMap.values()) {
/*     */ 
/*     */ 
/*     */       
/* 228 */       if (bo.getPickUpTime() != 0) {
/*     */         continue;
/*     */       }
/* 231 */       protoList.add(pickUp(bo.getId()));
/*     */     } 
/* 233 */     request.response(protoList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MailInfo pickUp(long mailId) {
/* 242 */     PlayerMailBO bo = this.mailMap.get(Long.valueOf(mailId));
/* 243 */     MailInfo mailInfo = new MailInfo(bo);
/*     */ 
/*     */     
/* 246 */     List<Integer> idList = CommString.getIntegerList(bo.getUniformIDList(), ";");
/* 247 */     List<Integer> countList = CommString.getIntegerList(bo.getUniformCountList(), ";");
/*     */     
/* 249 */     Reward reward = null;
/* 250 */     if (idList.size() != 0) {
/* 251 */       reward = new Reward(idList, countList);
/*     */     }
/* 253 */     if (reward != null) {
/* 254 */       ((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Mail);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 259 */     if (bo.getPickUpExistTime() == 0) {
/* 260 */       removeMail(bo);
/* 261 */       mailInfo.setLeftTime(0);
/*     */     } else {
/* 263 */       int nowSecond = CommTime.nowSecond();
/* 264 */       bo.savePickUpTime(nowSecond);
/* 265 */       int leftTime = getExistTime(bo) - nowSecond;
/* 266 */       mailInfo.setPickUpTime(nowSecond);
/* 267 */       mailInfo.setLeftTime(Math.max(0, leftTime));
/*     */     } 
/*     */ 
/*     */     
/* 271 */     return mailInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MailInfo parseToProto(PlayerMailBO bo) {
/* 281 */     MailInfo mailInfo = new MailInfo(bo);
/* 282 */     int leftTime = getExistTime(bo) - CommTime.nowSecond();
/* 283 */     mailInfo.setLeftTime(leftTime);
/*     */     
/* 285 */     return mailInfo;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/features/MailFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */