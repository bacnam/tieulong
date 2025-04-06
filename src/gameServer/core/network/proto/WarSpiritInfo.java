/*    */ package core.network.proto;
/*    */ 
/*    */ import business.player.feature.character.WarSpirit;
/*    */ 
/*    */ 
/*    */ public class WarSpiritInfo
/*    */ {
/*    */   public long sid;
/*    */   public int spiritId;
/*    */   public int skill;
/*    */   public boolean selected;
/*    */   public int lv;
/*    */   public int talent;
/*    */   public int star;
/*    */   
/*    */   public WarSpiritInfo() {}
/*    */   
/*    */   public WarSpiritInfo(WarSpirit warspirit) {
/* 19 */     if (warspirit != null) {
/* 20 */       this.sid = warspirit.getBo().getId();
/* 21 */       this.spiritId = warspirit.getSpiritId();
/* 22 */       this.skill = warspirit.getBo().getSkill();
/* 23 */       this.selected = warspirit.getBo().getIsSelected();
/* 24 */       this.lv = warspirit.getPlayer().getPlayerBO().getWarspiritLv();
/* 25 */       this.talent = warspirit.getPlayer().getPlayerBO().getWarspiritTalent();
/* 26 */       this.star = warspirit.getBo().getStar();
/*    */     } 
/*    */   }
/*    */   
/*    */   public long getSid() {
/* 31 */     return this.sid;
/*    */   }
/*    */   
/*    */   public void setSid(long sid) {
/* 35 */     this.sid = sid;
/*    */   }
/*    */   
/*    */   public int getSpiritId() {
/* 39 */     return this.spiritId;
/*    */   }
/*    */   
/*    */   public void setSpiritId(int spiritId) {
/* 43 */     this.spiritId = spiritId;
/*    */   }
/*    */   
/*    */   public int getSkill() {
/* 47 */     return this.skill;
/*    */   }
/*    */   
/*    */   public void setSkill(int skill) {
/* 51 */     this.skill = skill;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/WarSpiritInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */