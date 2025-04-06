/*    */ package business.player.feature.features;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.Feature;
/*    */ import com.zhonglian.server.common.db.BM;
/*    */ import core.database.game.bo.ClientdataBO;
/*    */ import core.database.game.bo.ClientguideBO;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PlayerClientData
/*    */   extends Feature
/*    */ {
/*    */   private static final int FIELD_MAXLENGTH = 25000;
/*    */   public ClientdataBO clientData;
/*    */   public ClientguideBO ClientGuide;
/*    */   
/*    */   public PlayerClientData(Player owner) {
/* 21 */     super(owner);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void loadDB() {
/* 29 */     long cid = this.player.getPid();
/* 30 */     this.clientData = (ClientdataBO)BM.getBM(ClientdataBO.class).findOne("cid", Long.valueOf(cid));
/* 31 */     if (this.clientData == null) {
/* 32 */       this.clientData = new ClientdataBO();
/* 33 */       this.clientData.setPid(this.player.getPid());
/* 34 */       this.clientData.insert();
/*    */     } 
/*    */     
/* 37 */     this.ClientGuide = (ClientguideBO)BM.getBM(ClientguideBO.class).findOne("cid", Long.valueOf(cid));
/* 38 */     if (this.ClientGuide == null) {
/* 39 */       this.ClientGuide = new ClientguideBO();
/* 40 */       this.ClientGuide.setPid(this.player.getPid());
/* 41 */       this.ClientGuide.insert();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean setClientDataField(int idx, String val) {
/* 47 */     if ((val.toCharArray()).length > 25000) {
/* 48 */       return false;
/*    */     }
/* 50 */     this.clientData.saveVstr(idx, val);
/* 51 */     return true;
/*    */   }
/*    */   
/*    */   public String getClientDataField(int idx) {
/* 55 */     return this.clientData.getVstr(idx);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/features/PlayerClientData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */