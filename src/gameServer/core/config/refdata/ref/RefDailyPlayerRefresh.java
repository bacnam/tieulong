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
/*    */ public class RefDailyPlayerRefresh
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
/*    */   public int getIndex() {
/* 27 */     return this.Index;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getComment() {
/* 32 */     return this.Comment;
/*    */   }
/*    */ 
/*    */   
/*    */   public IDailyRefreshRef.StartRefer getStartRefer() {
/* 37 */     return this.StartRefer;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getFirstSec() {
/* 42 */     return this.FirstSec;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getInterval() {
/* 47 */     return this.Interval;
/*    */   }
/*    */ 
/*    */   
/*    */   public IDailyRefreshRef.DailyRefreshEventType getEventTypes() {
/* 52 */     return this.EventTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Integer> getEventValue() {
/* 57 */     return this.EventValues;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 62 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 67 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefDailyPlayerRefresh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */