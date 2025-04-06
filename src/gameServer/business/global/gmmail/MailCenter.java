/*     */ package business.global.gmmail;
/*     */ 
/*     */ import BaseTask.SyncTask.SyncTaskManager;
/*     */ import BaseThread.BaseMutexObject;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.RobotManager;
/*     */ import business.player.feature.features.MailFeature;
/*     */ import business.player.item.Reward;
/*     */ import business.player.item.UniformItem;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.StringUtils;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefMail;
/*     */ import core.config.refdata.ref.RefReward;
/*     */ import core.database.game.bo.GlobalMailBO;
/*     */ import core.database.game.bo.PlayerMailBO;
/*     */ import core.logger.flow.FlowLogger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
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
/*     */ public class MailCenter
/*     */ {
/*     */   public MailCenter() {
/*  45 */     this.allBOs = Lists.newArrayList();
/*     */     
/*  47 */     this.offLineMailCached = new HashMap<>();
/*     */ 
/*     */     
/*  50 */     this.m_mutex = new BaseMutexObject();
/*     */   } private static MailCenter instance = new MailCenter(); public List<GlobalMailBO> allBOs;
/*     */   public void lock() {
/*  53 */     this.m_mutex.lock();
/*     */   } public HashMap<Long, List<PlayerMailBO>> offLineMailCached; protected BaseMutexObject m_mutex; public static MailCenter getInstance() {
/*     */     return instance;
/*     */   } public void unlock() {
/*  57 */     this.m_mutex.unlock();
/*     */   }
/*     */   
/*     */   public void init() {
/*  61 */     this.allBOs = BM.getBM(GlobalMailBO.class).findAll();
/*     */     
/*  63 */     Collections.sort(this.allBOs, new Comparator<GlobalMailBO>()
/*     */         {
/*     */           public int compare(GlobalMailBO o1, GlobalMailBO o2) {
/*  66 */             return (int)(o1.getId() - o2.getId());
/*     */           }
/*     */         });
/*     */     
/*  70 */     SyncTaskManager.schedule(600000, () -> {
/*     */           clearGlobalMail();
/*     */           return true;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<PlayerMailBO> loadPrivateMail(long cid) {
/*  84 */     List<PlayerMailBO> mails = BM.getBM(PlayerMailBO.class).findAll("pid", Long.valueOf(cid));
/*  85 */     synchronized (this.offLineMailCached) {
/*  86 */       List<PlayerMailBO> cached = this.offLineMailCached.get(Long.valueOf(cid));
/*  87 */       if (cached != null && cached.size() > 0) {
/*  88 */         mails.addAll(cached);
/*     */       }
/*  90 */       return mails;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearGlobalMail() {
/*  99 */     if (this.allBOs.size() <= 0) {
/*     */       return;
/*     */     }
/* 102 */     lock();
/* 103 */     List<GlobalMailBO> removeList = new ArrayList<>();
/* 104 */     int curTime = CommTime.nowSecond();
/* 105 */     for (GlobalMailBO bo : this.allBOs.subList(0, this.allBOs.size() - 1)) {
/* 106 */       if (bo.getCreateTime() + bo.getExistTime() < curTime) {
/* 107 */         removeList.add(bo);
/*     */       }
/*     */     } 
/* 110 */     for (GlobalMailBO bo : removeList) {
/* 111 */       this.allBOs.remove(bo);
/* 112 */       bo.del();
/*     */     } 
/* 114 */     unlock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<GlobalMailBO> getGlobalMailList() {
/* 123 */     List<GlobalMailBO> list = null;
/* 124 */     lock();
/* 125 */     list = Lists.newArrayList(this.allBOs);
/* 126 */     unlock();
/* 127 */     return list;
/*     */   }
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
/*     */   public void sendGlobalMail(String senderName, String title, String content, int existTime, int pickUpExistTime, String uniformIDList, String uniformCountList) {
/* 143 */     if (existTime == 0) {
/* 144 */       existTime = RefDataMgr.getFactor("MailDefaultExistTime", 604800);
/*     */     }
/* 146 */     GlobalMailBO bo = new GlobalMailBO();
/* 147 */     bo.setSenderName(senderName);
/* 148 */     bo.setTitle(title);
/* 149 */     bo.setContent(content);
/* 150 */     bo.setCreateTime(CommTime.nowSecond());
/* 151 */     bo.setExistTime(existTime);
/* 152 */     bo.setPickUpExistTime(pickUpExistTime);
/* 153 */     bo.setUniformIDList(uniformIDList);
/* 154 */     bo.setUniformCountList(uniformCountList);
/*     */     
/* 156 */     lock();
/* 157 */     bo.insert();
/* 158 */     this.allBOs.add(bo);
/* 159 */     unlock();
/*     */ 
/*     */     
/* 162 */     Collection<Player> players = PlayerMgr.getInstance().getOnlinePlayers();
/* 163 */     for (Player p : players) {
/* 164 */       SyncTaskManager.task(() -> ((MailFeature)paramPlayer.getFeature(MailFeature.class)).achieveGlobalMail());
/*     */     }
/*     */   }
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
/*     */   public void sendMail(long cid, String senderName, String title, String content, Reward prize, String... param) {
/* 178 */     for (int i = 0; i < param.length; i++) {
/* 179 */       content = content.replace("{" + i + "}", param[i]);
/*     */     }
/*     */     
/* 182 */     String uniformIds = "", counts = "";
/* 183 */     for (UniformItem item : prize) {
/* 184 */       uniformIds = String.valueOf(uniformIds) + item.getUniformId() + ";";
/* 185 */       counts = String.valueOf(counts) + item.getCount() + ";";
/*     */     } 
/* 187 */     sendMail(cid, senderName, title, content, uniformIds, counts);
/*     */   }
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
/*     */   public void sendMail(long cid, String senderName, String title, String content, List<Integer> uniformIdList, List<Integer> countList) {
/* 201 */     int existTime = RefDataMgr.getFactor("MailDefaultExistTime", 604800);
/* 202 */     int pickUpExistTime = 0;
/* 203 */     if (uniformIdList == null || uniformIdList.size() == 0) {
/* 204 */       pickUpExistTime = RefDataMgr.getFactor("MailDefaultPickExistTime", 259200);
/*     */     }
/* 206 */     sendMail(cid, senderName, title, content, existTime, pickUpExistTime, uniformIdList, countList);
/*     */   }
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
/*     */   public void sendMail(long cid, String senderName, String title, String content, String uniformIds, String counts) {
/* 220 */     int existTime = RefDataMgr.getFactor("MailDefaultExistTime", 604800);
/* 221 */     int pickUpExistTime = 0;
/*     */     
/* 223 */     pickUpExistTime = RefDataMgr.getFactor("MailDefaultPickExistTime", 259200);
/*     */     
/* 225 */     sendMail(cid, senderName, title, content, existTime, pickUpExistTime, uniformIds, counts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendMail(long cid, int mailId, String... param) {
/* 236 */     RefMail refMail = (RefMail)RefDataMgr.get(RefMail.class, Integer.valueOf(mailId));
/* 237 */     RefReward refReward = (RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(refMail.RewardId));
/* 238 */     String content = refMail.Content;
/* 239 */     for (int i = 0; i < param.length; i++) {
/* 240 */       content = content.replace("{" + i + "}", param[i]);
/*     */     }
/* 242 */     List<Integer> uniformIdList = null, countList = null;
/* 243 */     if (refReward != null) {
/* 244 */       uniformIdList = Lists.newArrayList();
/* 245 */       countList = Lists.newArrayList();
/* 246 */       for (UniformItem item : refReward.genReward()) {
/* 247 */         uniformIdList.add(Integer.valueOf(item.getUniformId()));
/* 248 */         countList.add(Integer.valueOf(item.getCount()));
/*     */       } 
/*     */     } 
/* 251 */     sendMail(cid, "", refMail.Title, content, refMail.ExistTime, refMail.PickUpExistTime, uniformIdList, countList);
/*     */   }
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
/*     */   public void sendMail(long cid, int mailId, int uniformId, int count, String... param) {
/* 264 */     List<Integer> uniformIdList = Lists.newArrayList(), countList = Lists.newArrayList();
/* 265 */     uniformIdList.add(Integer.valueOf(uniformId));
/* 266 */     countList.add(Integer.valueOf(count));
/* 267 */     sendMail(cid, mailId, uniformIdList, countList, param);
/*     */   }
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
/*     */   public void sendMail(long cid, int mailId, List<Integer> extUniformIdList, List<Integer> extCountList, String... param) {
/* 280 */     RefMail refMail = (RefMail)RefDataMgr.get(RefMail.class, Integer.valueOf(mailId));
/* 281 */     RefReward refReward = (RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(refMail.RewardId));
/* 282 */     String content = refMail.Content;
/* 283 */     for (int i = 0; i < param.length; i++) {
/* 284 */       content = content.replace("{" + i + "}", param[i]);
/*     */     }
/* 286 */     List<Integer> uniformIdList = Lists.newArrayList(), countList = Lists.newArrayList();
/* 287 */     if (refReward != null) {
/* 288 */       for (UniformItem uniform : refReward.genReward()) {
/* 289 */         uniformIdList.add(Integer.valueOf(uniform.getUniformId()));
/* 290 */         countList.add(Integer.valueOf(uniform.getCount()));
/*     */       } 
/*     */     }
/* 293 */     if (extUniformIdList != null && extUniformIdList.size() > 0) {
/* 294 */       uniformIdList.addAll(extUniformIdList);
/* 295 */       countList.addAll(extCountList);
/*     */     } 
/* 297 */     sendMail(cid, "", refMail.Title, content, refMail.ExistTime, refMail.PickUpExistTime, uniformIdList, countList);
/*     */   }
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
/*     */   public void sendMail(long cid, int mailId, String extrasUniformIds, String extrasCounts, String params) {
/* 311 */     RefMail refMail = (RefMail)RefDataMgr.get(RefMail.class, Integer.valueOf(mailId));
/*     */     
/* 313 */     String content = refMail.Content;
/* 314 */     if (params != null && params.length() > 0) {
/* 315 */       String[] array = params.split(";");
/* 316 */       for (int i = 0; i < array.length; i++) {
/* 317 */         content = content.replace("{" + i + "}", array[i]);
/*     */       }
/*     */     } 
/*     */     
/* 321 */     StringBuilder itemId = new StringBuilder();
/* 322 */     StringBuilder counts = new StringBuilder();
/*     */     
/* 324 */     RefReward reward = (RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(refMail.RewardId));
/* 325 */     if (reward != null) {
/* 326 */       for (UniformItem item : reward.genReward()) {
/* 327 */         itemId.append(item.getUniformId()).append(';');
/* 328 */         counts.append(item.getCount()).append(';');
/*     */       } 
/*     */     }
/*     */     
/* 332 */     if (extrasUniformIds != "" && extrasUniformIds.length() > 0) {
/* 333 */       itemId.append(extrasUniformIds);
/* 334 */       counts.append(extrasCounts);
/*     */     } 
/* 336 */     if (itemId.toString().endsWith(";")) {
/* 337 */       itemId.deleteCharAt(itemId.length() - 1);
/*     */     }
/* 339 */     if (counts.toString().endsWith(";")) {
/* 340 */       counts.deleteCharAt(counts.length() - 1);
/*     */     }
/* 342 */     sendMail(cid, "", refMail.Title, content, refMail.ExistTime, refMail.PickUpExistTime, itemId.toString(), counts.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addCachedPrivateMail(long cid, PlayerMailBO mail) {
/* 352 */     synchronized (this.offLineMailCached) {
/* 353 */       List<PlayerMailBO> mails = this.offLineMailCached.get(Long.valueOf(cid));
/* 354 */       if (mails == null) {
/* 355 */         this.offLineMailCached.put(Long.valueOf(cid), mails = new ArrayList<>());
/*     */       }
/* 357 */       mails.add(mail);
/* 358 */       this.offLineMailCached.put(Long.valueOf(cid), mails);
/*     */     } 
/*     */   }
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
/*     */   private void sendMail(long cid, String senderName, String title, String content, int existTime, int pickUpExistTime, List<Integer> uniformIdList, List<Integer> countList) {
/* 377 */     String uniformIds = "", counts = "";
/* 378 */     if (uniformIdList != null && uniformIdList.size() > 0) {
/* 379 */       uniformIds = StringUtils.list2String(uniformIdList);
/* 380 */       counts = StringUtils.list2String(countList);
/*     */     } 
/* 382 */     sendMail(cid, senderName, title, content, existTime, pickUpExistTime, uniformIds, counts);
/*     */   }
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
/*     */   private void sendMail(long cid, String senderName, String title, String content, int existTime, int pickUpExistTime, String uniformIDList, String uniformCountList) {
/* 399 */     if (RobotManager.getInstance().isRobot(cid)) {
/*     */       return;
/*     */     }
/* 402 */     PlayerMailBO bo = new PlayerMailBO();
/* 403 */     bo.setPid(cid);
/* 404 */     bo.setSenderName(senderName);
/* 405 */     bo.setTitle(title);
/* 406 */     bo.setContent(content);
/* 407 */     bo.setCreateTime(CommTime.nowSecond());
/* 408 */     bo.setExistTime(existTime);
/* 409 */     bo.setPickUpExistTime(pickUpExistTime);
/* 410 */     bo.setUniformIDList(uniformIDList);
/* 411 */     bo.setUniformCountList(uniformCountList);
/* 412 */     bo.insert();
/*     */     
/* 414 */     Player player = PlayerMgr.getInstance().getPlayer(cid);
/* 415 */     if (player == null) {
/*     */       return;
/*     */     }
/*     */     
/* 419 */     if (player.isLoaded(MailFeature.class)) {
/* 420 */       ((MailFeature)player.getFeature(MailFeature.class)).addMail(bo);
/*     */     }
/*     */     else {
/*     */       
/* 424 */       addCachedPrivateMail(cid, bo);
/*     */     } 
/*     */     
/* 427 */     FlowLogger.mail(player.getPid(), player.getOpenId(), player.getVipLevel(), player.getLv(), 
/* 428 */         bo.getId(), title, uniformIDList, uniformCountList, senderName, "new");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delPrivateMail(long cid) {
/* 438 */     synchronized (this.offLineMailCached) {
/* 439 */       this.offLineMailCached.remove(Long.valueOf(cid));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/gmmail/MailCenter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */