/*    */ package core.network.proto;
/*    */ 
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerBase;
/*    */ import core.database.game.bo.RedPacketPickBO;
/*    */ 
/*    */ public class RedPacketPickInfo
/*    */ {
/*    */   public long id;
/*    */   public Player.Summary summary;
/*    */   public int pickMoney;
/*    */   
/*    */   public RedPacketPickInfo(RedPacketPickBO bo) {
/* 14 */     this.id = bo.getId();
/* 15 */     this.summary = ((PlayerBase)PlayerMgr.getInstance().getPlayer(bo.getPid()).getFeature(PlayerBase.class)).summary();
/* 16 */     this.pickMoney = bo.getMoney();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/RedPacketPickInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */