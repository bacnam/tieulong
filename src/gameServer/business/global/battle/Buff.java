/*    */ package business.global.battle;
/*    */ 
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefBuff;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Buff
/*    */ {
/*    */   private Creature creature;
/*    */   int buffid;
/*    */   private RefBuff buffdata;
/*    */   private int lv;
/*    */   private double time;
/*    */   private Map<Attribute, Double> attrsPer;
/*    */   private Map<Attribute, Double> attrsFixed;
/*    */   
/*    */   public double attrPer(Attribute attr) {
/* 25 */     Double value = this.attrsPer.get(attr);
/* 26 */     return (value == null) ? 0.0D : value.doubleValue();
/*    */   }
/*    */   
/*    */   public double attrFixed(Attribute attr) {
/* 30 */     Double value = this.attrsFixed.get(attr);
/* 31 */     return (value == null) ? 0.0D : value.doubleValue();
/*    */   }
/*    */   
/*    */   public Buff(Creature tar, int buffid, int skilllv) {
/* 35 */     this.creature = tar;
/* 36 */     this.lv = skilllv;
/* 37 */     this.buffid = buffid;
/* 38 */     this.buffdata = (RefBuff)RefDataMgr.get(RefBuff.class, Integer.valueOf(buffid));
/*    */     
/* 40 */     this.attrsPer = new HashMap<>();
/* 41 */     this.attrsFixed = new HashMap<>();
/*    */     
/* 43 */     for (int i = 0; i < this.buffdata.AttrTypeList.size(); i++) {
/* 44 */       Attribute attr = this.buffdata.AttrTypeList.get(i);
/* 45 */       double value = ((Double)this.buffdata.AttrValueList.get(i)).doubleValue() + ((Double)this.buffdata.AttrIncList.get(i)).doubleValue() * this.lv;
/* 46 */       if (((Boolean)this.buffdata.AttrFixedList.get(i)).booleanValue()) {
/* 47 */         this.attrsFixed.put(attr, Double.valueOf(value));
/*    */       } else {
/* 49 */         this.attrsPer.put(attr, Double.valueOf(value / 100.0D));
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void update(double dt) {
/* 55 */     double pre = this.time;
/* 56 */     this.time += dt;
/* 57 */     if (this.time > this.buffdata.Time) {
/*    */       return;
/*    */     }
/* 60 */     Math.floor(pre); Math.floor(this.time);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isEffective() {
/* 66 */     if (this.time > this.buffdata.Time) {
/* 67 */       return false;
/*    */     }
/* 69 */     if (this.buffdata.HPCondition > 0) {
/* 70 */       return (this.creature.hp / this.creature.baseAttr(Attribute.MaxHP) < (this.buffdata.HPCondition / 100));
/*    */     }
/* 72 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isInCd() {
/* 76 */     return (this.time < this.buffdata.CD);
/*    */   }
/*    */   
/*    */   public int getId() {
/* 80 */     return this.buffid;
/*    */   }
/*    */   
/*    */   public int getLevel() {
/* 84 */     return this.lv;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/battle/Buff.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */