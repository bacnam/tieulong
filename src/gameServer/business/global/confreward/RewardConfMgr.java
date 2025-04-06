/*     */ package business.global.confreward;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.item.Reward;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.utils.CommString;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Maps;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefUniformItem;
/*     */ import core.database.game.bo.ConfRewardBO;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RewardConfMgr
/*     */ {
/*  22 */   private static RewardConfMgr instance = null;
/*     */   
/*     */   public static RewardConfMgr getInstance() {
/*  25 */     if (instance == null) {
/*  26 */       instance = new RewardConfMgr();
/*     */     }
/*  28 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Integer, ConfRewardBO> confRewardMap;
/*     */   
/*     */   public RewardConfMgr() {
/*  35 */     this.confRewardMap = Maps.newConcurrentMap();
/*     */   }
/*     */   public void init() {
/*  38 */     this.confRewardMap.clear();
/*  39 */     for (ConfRewardBO bo : BM.getBM(ConfRewardBO.class).findAll()) {
/*  40 */       this.confRewardMap.put(Integer.valueOf(bo.getRewardID()), bo);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfRewardBO getConfRewardBO(int rewardUniformId) {
/*  51 */     return this.confRewardMap.get(Integer.valueOf(rewardUniformId));
/*     */   }
/*     */   
/*     */   public String getConfRewardName(int rewardUniformId) {
/*  55 */     ConfRewardBO bo = getConfRewardBO(rewardUniformId);
/*  56 */     if (bo != null) {
/*  57 */       return bo.getItemsName();
/*     */     }
/*  59 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reward buyConfReward(int rewardUniformId, int count) {
/*  69 */     ConfRewardBO bo = this.confRewardMap.get(Integer.valueOf(rewardUniformId));
/*  70 */     if (bo == null) {
/*  71 */       return new Reward();
/*     */     }
/*  73 */     List<Integer> idList = CommString.getIntegerList(bo.getItemID(), ";");
/*  74 */     List<Integer> countList = (List<Integer>)CommString.getIntegerList(bo.getItemCount(), ";").stream().map(x -> Integer.valueOf(x.intValue() * paramInt)).collect(Collectors.toList());
/*  75 */     return new Reward(idList, countList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean gmConfRewardBO(Player player, int rewardUniformId, int price) throws WSException {
/*  86 */     if (RefDataMgr.get(RefUniformItem.class, Integer.valueOf(rewardUniformId)) != null) {
/*  87 */       throw new WSException(ErrorCode.InvalidParam, "非法的参数:rewardId=%s，rewardId不能为UniformItem表的ID", new Object[] { Integer.valueOf(rewardUniformId) });
/*     */     }
/*  89 */     ConfRewardBO bo = getConfRewardBO(rewardUniformId);
/*  90 */     if (bo != null) {
/*  91 */       throw new WSException(ErrorCode.InvalidParam, "非法的参数:rewardId=%s，礼包已存在", new Object[] { Integer.valueOf(rewardUniformId) });
/*     */     }
/*  93 */     bo = new ConfRewardBO();
/*  94 */     bo.setRewardID(rewardUniformId);
/*  95 */     bo.setPrice(price);
/*  96 */     bo.setCreateTime(CommTime.nowSecond());
/*  97 */     bo.setName("测试礼包");
/*  98 */     bo.setRewardDescribe("测试礼包");
/*  99 */     bo.setIconID("Gem_Earth");
/* 100 */     bo.setItemID("11001;11002");
/* 101 */     bo.setItemCount("1;2");
/* 102 */     bo.insert_sync();
/* 103 */     this.confRewardMap.put(Integer.valueOf(rewardUniformId), bo);
/* 104 */     player.pushProto("RequestConfRewardInfo", "");
/* 105 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/confreward/RewardConfMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */