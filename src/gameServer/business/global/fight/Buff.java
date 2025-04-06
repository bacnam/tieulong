/*    */ package business.global.fight;
/*    */ 
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import core.config.refdata.ref.RefBuff;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Buff
/*    */ {
/*    */   int id;
/*    */   long time;
/*    */   RefBuff ref;
/*    */   int lv;
/*    */   long cd;
/*    */   boolean isExpired = true;
/*    */   private Map<Attribute, Double> attrsPer;
/*    */   private Map<Attribute, Double> attrsFixed;
/*    */   
/*    */   public double attrPer(Attribute attr) {
/* 23 */     Double value = this.attrsPer.get(attr);
/* 24 */     return (value == null) ? 0.0D : value.doubleValue();
/*    */   }
/*    */   
/*    */   public double attrFixed(Attribute attr) {
/* 28 */     Double value = this.attrsFixed.get(attr);
/* 29 */     return (value == null) ? 0.0D : value.doubleValue();
/*    */   }
/*    */   
/*    */   public Buff(RefBuff ref, int bufflv) {
/* 33 */     this.id = ref.id;
/* 34 */     this.ref = ref;
/* 35 */     this.lv = bufflv;
/*    */     
/* 37 */     this.attrsPer = new HashMap<>();
/* 38 */     this.attrsFixed = new HashMap<>();
/*    */     
/* 40 */     for (int i = 0; i < this.ref.AttrTypeList.size(); i++) {
/* 41 */       Attribute attr = this.ref.AttrTypeList.get(i);
/* 42 */       double value = ((Double)this.ref.AttrValueList.get(i)).doubleValue() + ((Double)this.ref.AttrIncList.get(i)).doubleValue() * this.lv;
/* 43 */       if (((Boolean)this.ref.AttrFixedList.get(i)).booleanValue()) {
/* 44 */         this.attrsFixed.put(attr, Double.valueOf(value));
/*    */       } else {
/* 46 */         this.attrsPer.put(attr, Double.valueOf(value / 100.0D));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/fight/Buff.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */