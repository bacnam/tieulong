/*    */ package core.network.proto;
/*    */ 
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefDress;
/*    */ import core.database.game.bo.DressBO;
/*    */ 
/*    */ public class DressInfo
/*    */ {
/*    */   DressBO dressbo;
/*    */   int dressLeftTime;
/*    */   
/*    */   public DressInfo(DressBO bo) {
/* 14 */     RefDress ref = (RefDress)RefDataMgr.get(RefDress.class, Integer.valueOf(bo.getDressId()));
/* 15 */     this.dressbo = bo;
/* 16 */     if (ref.TimeLimit > 0 && bo.getEquipTime() != 0) {
/* 17 */       this.dressLeftTime = Math.max(0, ref.TimeLimit - CommTime.nowSecond() - bo.getEquipTime());
/*    */     } else {
/* 19 */       this.dressLeftTime = -1;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/DressInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */