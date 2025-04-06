/*    */ package business.global.broadcast;
/*    */ 
/*    */ import business.player.Player;
/*    */ import java.util.LinkedList;
/*    */ 
/*    */ 
/*    */ public class BroadcastRecord
/*    */ {
/*    */   public LinkedList<Player> players;
/*    */   public BroadcastTask task;
/*    */   
/*    */   public BroadcastRecord(LinkedList<Player> players, BroadcastTask task) {
/* 13 */     this.players = players;
/* 14 */     this.task = task;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/broadcast/BroadcastRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */