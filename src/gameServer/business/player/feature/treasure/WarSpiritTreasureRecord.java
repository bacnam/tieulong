/*    */ package business.player.feature.treasure;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import com.zhonglian.server.common.db.BM;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefWarSpirit;
/*    */ import core.database.game.bo.WarSpiritTreasureRecordBO;
/*    */ import java.util.ArrayList;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WarSpiritTreasureRecord
/*    */ {
/* 17 */   private static WarSpiritTreasureRecord instance = new WarSpiritTreasureRecord();
/*    */   
/*    */   public static WarSpiritTreasureRecord getInstance() {
/* 20 */     return instance;
/*    */   }
/*    */   
/* 23 */   List<WarSpiritTreasureRecordBO> records = new LinkedList<>();
/*    */   
/* 25 */   int recordSize = RefDataMgr.getFactor("WarspiritTreasureSize", 3);
/*    */   
/*    */   public void init() {
/* 28 */     List<WarSpiritTreasureRecordBO> list = BM.getBM(WarSpiritTreasureRecordBO.class).findAllBySort("time", false, this.recordSize);
/* 29 */     this.records = list;
/*    */   }
/*    */   
/*    */   public void add(WarSpiritTreasureRecordBO bo) {
/* 33 */     synchronized (this) {
/* 34 */       if (this.records.size() >= this.recordSize) {
/* 35 */         WarSpiritTreasureRecordBO remove = this.records.remove(0);
/* 36 */         remove.del();
/*    */       } 
/* 38 */       this.records.add(bo);
/* 39 */       for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
/* 40 */         player.pushProto("newWarspiritTreasure", new WarSpiritRecordInfo(bo));
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public List<WarSpiritRecordInfo> getRecords() {
/* 46 */     List<WarSpiritRecordInfo> list = new ArrayList<>();
/* 47 */     for (WarSpiritTreasureRecordBO bo : this.records) {
/* 48 */       list.add(new WarSpiritRecordInfo(bo));
/*    */     }
/*    */     
/* 51 */     return list;
/*    */   }
/*    */   
/*    */   public static class WarSpiritRecordInfo {
/*    */     String name;
/*    */     String warspiritName;
/*    */     
/*    */     public WarSpiritRecordInfo(WarSpiritTreasureRecordBO bo) {
/* 59 */       Player player = PlayerMgr.getInstance().getPlayer(bo.getPid());
/* 60 */       RefWarSpirit ref = (RefWarSpirit)RefDataMgr.get(RefWarSpirit.class, Integer.valueOf(bo.getSpiritId()));
/* 61 */       this.name = player.getName();
/* 62 */       this.warspiritName = ref.Name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/treasure/WarSpiritTreasureRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */