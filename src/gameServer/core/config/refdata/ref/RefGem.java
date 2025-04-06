/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefGem
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int Material;
/*    */   public int ATK;
/*    */   public int MaxHP;
/*    */   public int DEF;
/*    */   public int RGS;
/*    */   public int Hit;
/*    */   public int Dodge;
/*    */   public int Critical;
/*    */   public int Tenacity;
/*    */   
/*    */   public int getValue(Attribute attr) {
/* 25 */     switch (attr) {
/*    */       case null:
/* 27 */         return this.ATK;
/*    */       case MaxHP:
/* 29 */         return this.MaxHP;
/*    */       case DEF:
/* 31 */         return this.DEF;
/*    */       case RGS:
/* 33 */         return this.RGS;
/*    */       case Hit:
/* 35 */         return this.Hit;
/*    */       case Dodge:
/* 37 */         return this.Dodge;
/*    */       case Critical:
/* 39 */         return this.Critical;
/*    */       case Tenacity:
/* 41 */         return this.Tenacity;
/*    */     } 
/* 43 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 54 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefGem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */