/*    */ package business.global.activity.detail;
/*    */ 
/*    */ import business.global.activity.Activity;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.zhonglian.server.common.enums.ActivityType;
/*    */ import com.zhonglian.server.http.server.RequestException;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import core.database.game.bo.ActivityBO;
/*    */ import core.server.OpenSeverTime;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MysteryStore
/*    */   extends Activity
/*    */ {
/*    */   private int begin;
/*    */   
/*    */   public MysteryStore(ActivityBO data) {
/* 20 */     super(data);
/*    */ 
/*    */     
/* 23 */     this.begin = 0;
/*    */   }
/*    */   
/*    */   public void load(JsonObject json) throws WSException {
/* 27 */     this.begin = json.get("begin").getAsInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public String check(JsonObject json) throws RequestException {
/* 32 */     return "ok";
/*    */   }
/*    */ 
/*    */   
/*    */   public ActivityType getType() {
/* 37 */     return ActivityType.MysteryStore;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onOpen() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEnd() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onClosed() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getBeginTime() {
/* 60 */     return OpenSeverTime.getInstance().getOpenZeroTime() + this.begin;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/MysteryStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */