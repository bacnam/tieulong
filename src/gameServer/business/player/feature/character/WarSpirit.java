/*    */ package business.player.feature.character;
/*    */ 
/*    */ import business.player.Player;
/*    */ import core.database.game.bo.WarSpiritBO;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WarSpirit
/*    */ {
/*    */   Player player;
/*    */   private WarSpiritBO bo;
/*    */   Integer power;
/*    */   
/*    */   WarSpirit(Player player, WarSpiritBO bo) {
/* 36 */     this.power = null; this.player = player;
/*    */     this.bo = bo;
/*    */   }
/* 39 */   public Player getPlayer() { return this.player; } public void setPlayer(Player player) { this.player = player; } public int getPower() { if (this.power == null) {
/* 40 */       this.power = Integer.valueOf((new SpiritAttrCalculator(this)).getPower());
/*    */     }
/* 42 */     return this.power.intValue(); }
/*    */   public WarSpiritBO getBo() { return this.bo; }
/*    */   public void setBo(WarSpiritBO bo) { this.bo = bo; }
/*    */   public int getSpiritId() { return this.bo.getSpiritId(); } public SpiritAttrCalculator getAttr() {
/* 46 */     return new SpiritAttrCalculator(this);
/*    */   }
/*    */   
/*    */   public void onAttrChanged() {
/* 50 */     this.power = Integer.valueOf((new SpiritAttrCalculator(this)).getPower());
/* 51 */     ((CharFeature)this.player.getFeature(CharFeature.class)).updatePower();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/character/WarSpirit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */