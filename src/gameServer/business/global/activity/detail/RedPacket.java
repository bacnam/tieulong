/*    */ package business.global.activity.detail;
/*    */ 
/*    */ import business.global.activity.Activity;
/*    */ import business.global.redpacket.RedPacketMgr;
/*    */ import business.player.Player;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.zhonglian.server.common.enums.ActivityStatus;
/*    */ import com.zhonglian.server.common.enums.ActivityType;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import core.database.game.bo.ActivityBO;
/*    */ import core.network.proto.RedPacketInfo;
/*    */ import core.network.proto.RedPacketPickInfo;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class RedPacket
/*    */   extends Activity
/*    */ {
/*    */   public RedPacket(ActivityBO bo) {
/* 20 */     super(bo);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void load(JsonObject json) throws WSException {}
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
/*    */   public ActivityType getType() {
/* 50 */     return ActivityType.RedPacket;
/*    */   }
/*    */   
/*    */   public List<RedPacketInfo> getPacketList(Player player) {
/* 54 */     return RedPacketMgr.getInstance().getList(player);
/*    */   }
/*    */   
/*    */   public List<RedPacketPickInfo> getPickList(long id) {
/* 58 */     return RedPacketMgr.getInstance().getPickInfo(id);
/*    */   }
/*    */   
/*    */   public int pick(long id, Player player) throws WSException {
/* 62 */     return RedPacketMgr.getInstance().snatchPacket(id, player);
/*    */   }
/*    */   
/*    */   public void handle(int money, Player player) {
/* 66 */     if (getStatus() != ActivityStatus.Open) {
/*    */       return;
/*    */     }
/* 69 */     RedPacketMgr.getInstance().handleRecharge(money, player);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/RedPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */