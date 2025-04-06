/*    */ package business.player.feature.record;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.Feature;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.zhonglian.server.common.db.BM;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import core.database.game.bo.VipRecordBO;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ 
/*    */ public class VipRecord
/*    */   extends Feature
/*    */ {
/*    */   public VipRecordBO record;
/*    */   
/*    */   public VipRecord(Player owner) {
/* 19 */     super(owner);
/*    */   }
/*    */ 
/*    */   
/*    */   public void loadDB() {
/* 24 */     this.record = (VipRecordBO)BM.getBM(VipRecordBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
/*    */   }
/*    */ 
/*    */   
/*    */   public VipRecordBO getOrCreate() {
/* 29 */     VipRecordBO bo = this.record;
/* 30 */     if (bo != null) {
/* 31 */       return bo;
/*    */     }
/* 33 */     synchronized (this) {
/* 34 */       bo = this.record;
/* 35 */       if (bo != null) {
/* 36 */         return bo;
/*    */       }
/* 38 */       bo = new VipRecordBO();
/* 39 */       bo.setPid(this.player.getPid());
/* 40 */       bo.insert();
/* 41 */       this.record = bo;
/*    */     } 
/* 43 */     return bo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Boolean> getFetchGiftStatusList() {
/* 52 */     List<Integer> fetchList = Lists.newArrayList(getOrCreate().getLastFetchPrivateTimeAll());
/* 53 */     return (List<Boolean>)fetchList.stream().map(x -> (x.intValue() > 0) ? Boolean.valueOf(true) : Boolean.valueOf(false))
/*    */       
/* 55 */       .collect(Collectors.toList());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLastFetchPrivateTime(int level) {
/* 64 */     VipRecordBO bo = getOrCreate();
/* 65 */     bo.saveLastFetchPrivateTime(level, CommTime.nowSecond());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getLastFetchPrivateTime(int level) {
/* 75 */     return getOrCreate().getLastFetchPrivateTime(level);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/record/VipRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */