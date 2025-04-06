/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.mgr.daily.IDailyRefreshRef;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefDailyWorldRefresh
/*    */   extends RefBaseGame
/*    */   implements IDailyRefreshRef
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int Index;
/*    */   public String Comment;
/*    */   public IDailyRefreshRef.StartRefer StartRefer;
/*    */   public int FirstSec;
/*    */   public int Interval;
/*    */   public IDailyRefreshRef.DailyRefreshEventType EventTypes;
/*    */   public ArrayList<Integer> EventValues;
/*    */   
/*    */   public boolean checkFieldValue() {
/* 31 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getIndex() {
/* 36 */     return this.Index;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getComment() {
/* 41 */     return this.Comment;
/*    */   }
/*    */ 
/*    */   
/*    */   public IDailyRefreshRef.StartRefer getStartRefer() {
/* 46 */     return this.StartRefer;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getFirstSec() {
/* 51 */     return this.FirstSec;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getInterval() {
/* 56 */     return this.Interval;
/*    */   }
/*    */ 
/*    */   
/*    */   public IDailyRefreshRef.DailyRefreshEventType getEventTypes() {
/* 61 */     return this.EventTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Integer> getEventValue() {
/* 66 */     return this.EventValues;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 71 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 76 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefDailyWorldRefresh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */