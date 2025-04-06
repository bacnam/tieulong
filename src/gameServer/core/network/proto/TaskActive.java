/*    */ package core.network.proto;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TaskActive
/*    */ {
/*    */   int id;
/*    */   int value;
/* 12 */   public List<Integer> stepStatus = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public int getId() {
/* 16 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(int id) {
/* 20 */     this.id = id;
/*    */   }
/*    */   
/*    */   public int getValue() {
/* 24 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(int value) {
/* 28 */     this.value = value;
/*    */   }
/*    */   
/*    */   public List<Integer> getStepStatus() {
/* 32 */     return this.stepStatus;
/*    */   }
/*    */   
/*    */   public void setStepStatus(List<Integer> stepStatus) {
/* 36 */     this.stepStatus = stepStatus;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/TaskActive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */