/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.arena.ArenaManager;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import com.zhonglian.server.common.enums.FightResult;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.ArenaFightRecordBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class ArenaFightRecord
/*    */   extends PlayerHandler
/*    */ {
/* 20 */   static PlayerMgr playerManager = null;
/*    */   
/*    */   private static class Record
/*    */   {
/*    */     int time;
/*    */     String oppoName;
/*    */     FightResult rslt;
/*    */     int rank;
/*    */     boolean isAttack;
/*    */     
/*    */     public Record(long reader, ArenaFightRecordBO bo) {
/* 31 */       if (ArenaFightRecord.playerManager == null) {
/* 32 */         ArenaFightRecord.playerManager = PlayerMgr.getInstance();
/*    */       }
/* 34 */       this.time = bo.getEndTime();
/* 35 */       if (reader == bo.getAtkPid()) {
/* 36 */         this.rank = bo.getAtkRank();
/* 37 */         this.oppoName = ArenaFightRecord.playerManager.getPlayer(bo.getDefPid()).getName();
/* 38 */         this.rslt = FightResult.values()[bo.getResult()];
/* 39 */         this.isAttack = true;
/*    */       } else {
/* 41 */         this.rank = bo.getDefRank();
/* 42 */         this.oppoName = ArenaFightRecord.playerManager.getPlayer(bo.getAtkPid()).getName();
/* 43 */         this.rslt = (FightResult.values()[bo.getResult()] == FightResult.Win) ? FightResult.Lost : FightResult.Win;
/* 44 */         this.isAttack = false;
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 51 */     Collection<ArenaFightRecordBO> recods = ArenaManager.getInstance().getFightRecords(player.getPid());
/* 52 */     List<Record> rtn = new ArrayList<>();
/* 53 */     if (recods != null) {
/* 54 */       for (ArenaFightRecordBO r : recods) {
/* 55 */         rtn.add(new Record(player.getPid(), r));
/*    */       }
/*    */     }
/* 58 */     request.response(rtn);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/ArenaFightRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */