/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import business.player.feature.character.PowerUtils;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class RefAttribute
/*    */   extends RefBaseGame {
/*    */   @RefField(iskey = true)
/*    */   public String id;
/*    */   public int charId;
/*    */   public int lv;
/*    */   public int MaxHP;
/*    */   public int ATK;
/*    */   public int DEF;
/*    */   public int RGS;
/*    */   public int Hit;
/*    */   public int Dodge;
/*    */   public int Critical;
/*    */   public int Tenacity;
/*    */   @RefField(isfield = false)
/* 25 */   public int Power = 0;
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 29 */     Map<Attribute, Integer> attr = new HashMap<>();
/* 30 */     attr.put(Attribute.MaxHP, Integer.valueOf(this.MaxHP));
/* 31 */     attr.put(Attribute.ATK, Integer.valueOf(this.ATK));
/* 32 */     attr.put(Attribute.DEF, Integer.valueOf(this.DEF));
/* 33 */     attr.put(Attribute.RGS, Integer.valueOf(this.RGS));
/* 34 */     attr.put(Attribute.Hit, Integer.valueOf(this.Hit));
/* 35 */     attr.put(Attribute.Dodge, Integer.valueOf(this.Dodge));
/* 36 */     attr.put(Attribute.Critical, Integer.valueOf(this.Critical));
/* 37 */     attr.put(Attribute.Tenacity, Integer.valueOf(this.Tenacity));
/* 38 */     this.Power = PowerUtils.getPower(attr);
/* 39 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 44 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */