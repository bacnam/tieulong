/*    */ package core.network.proto;
/*    */ 
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerBase;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefRedPacket;
/*    */ import core.database.game.bo.RedPacketBO;
/*    */ 
/*    */ public class RedPacketInfo
/*    */ {
/*    */   public long id;
/*    */   public Player.Summary summary;
/*    */   public String packetName;
/*    */   public int packetValue;
/*    */   public int leftMoney;
/*    */   public int maxMoney;
/*    */   public int alreadyPick;
/*    */   public int maxPick;
/*    */   public int createtime;
/*    */   public int status;
/*    */   
/*    */   public RedPacketInfo(RedPacketBO bo) {
/* 23 */     if (bo != null) {
/* 24 */       this.id = bo.getId();
/* 25 */       this.summary = ((PlayerBase)PlayerMgr.getInstance().getPlayer(bo.getPid()).getFeature(PlayerBase.class)).summary();
/* 26 */       RefRedPacket refRedPacket = (RefRedPacket)RefDataMgr.get(RefRedPacket.class, Integer.valueOf(bo.getPacketTypeId()));
/* 27 */       this.packetName = refRedPacket.Name;
/* 28 */       this.packetValue = refRedPacket.Money;
/* 29 */       this.leftMoney = bo.getLeftMoney();
/* 30 */       this.maxMoney = bo.getMaxMoney();
/* 31 */       this.alreadyPick = bo.getAlreadyPick();
/* 32 */       this.maxPick = bo.getMaxPick();
/* 33 */       this.createtime = bo.getTime();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/RedPacketInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */