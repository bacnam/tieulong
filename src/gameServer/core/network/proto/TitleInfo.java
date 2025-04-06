/*    */ package core.network.proto;
/*    */ 
/*    */ import core.database.game.bo.NewtitleBO;
/*    */ 
/*    */ public class TitleInfo {
/*    */   int titleId;
/*    */   int level;
/*    */   boolean isUsing;
/*    */   
/*    */   public TitleInfo(NewtitleBO bo) {
/* 11 */     this.titleId = bo.getTitleId();
/* 12 */     this.level = bo.getLevel();
/* 13 */     this.isUsing = bo.getIsUsing();
/*    */   }
/*    */   
/*    */   public int getTitleId() {
/* 17 */     return this.titleId;
/*    */   }
/*    */   
/*    */   public void setTitleId(int titleId) {
/* 21 */     this.titleId = titleId;
/*    */   }
/*    */   
/*    */   public int getLevel() {
/* 25 */     return this.level;
/*    */   }
/*    */   
/*    */   public void setLevel(int level) {
/* 29 */     this.level = level;
/*    */   }
/*    */   
/*    */   public boolean isUsing() {
/* 33 */     return this.isUsing;
/*    */   }
/*    */   
/*    */   public void setUsing(boolean isUsing) {
/* 37 */     this.isUsing = isUsing;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/TitleInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */