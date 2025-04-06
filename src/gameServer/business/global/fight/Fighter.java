/*    */ package business.global.fight;
/*    */ 
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import core.config.refdata.ref.RefSkill;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Fighter
/*    */ {
/*    */   public int id;
/* 16 */   public Map<Integer, RefSkill> skills = new HashMap<>();
/* 17 */   public Map<Integer, Integer> skillsLv = new HashMap<>();
/* 18 */   public Map<Attribute, Double> attrs = new HashMap<>();
/*    */   
/*    */   public boolean isMain = true;
/*    */   
/*    */   public double hp;
/*    */   
/* 24 */   Map<Integer, Integer> skillsCD = new HashMap<>();
/* 25 */   int fighttimes = 0;
/* 26 */   Map<Integer, Buff> buffs = new HashMap<>();
/*    */   
/*    */   public int x;
/*    */   public int y;
/*    */   
/*    */   public double attr(Attribute attr) {
/* 32 */     Double base = this.attrs.get(attr);
/* 33 */     if (base == null) {
/* 34 */       base = Double.valueOf(0.0D);
/*    */     }
/* 36 */     double per = 0.0D;
/* 37 */     double fixed = 0.0D;
/* 38 */     for (Buff b : this.buffs.values()) {
/* 39 */       if (b.isExpired) {
/*    */         continue;
/*    */       }
/* 42 */       if (b.ref.HPCondition != 0 && this.hp / ((Double)this.attrs.get(Attribute.MaxHP)).doubleValue() > b.ref.HPCondition / 100.0D) {
/*    */         continue;
/*    */       }
/* 45 */       per += b.attrPer(attr);
/* 46 */       fixed += b.attrFixed(attr);
/*    */     } 
/* 48 */     return base.doubleValue() * (1.0D + per) + fixed;
/*    */   }
/*    */   
/*    */   public void checkBuff(long time) {
/* 52 */     List<Integer> buff2remove = new ArrayList<>();
/* 53 */     for (Buff buff : this.buffs.values()) {
/* 54 */       buff.isExpired = (buff.time < time);
/* 55 */       if (buff.time < time && buff.cd < time) {
/* 56 */         buff2remove.add(Integer.valueOf(buff.id));
/*    */       }
/*    */     } 
/* 59 */     for (Iterator<Integer> iterator = buff2remove.iterator(); iterator.hasNext(); ) { int buff = ((Integer)iterator.next()).intValue();
/* 60 */       this.buffs.remove(Integer.valueOf(buff)); }
/*    */   
/*    */   }
/*    */   
/*    */   public void action(long time) {
/* 65 */     this.fighttimes++;
/*    */   }
/*    */   
/*    */   public void putAllAttr(Map<Attribute, Integer> attrs) {
/* 69 */     for (Map.Entry<Attribute, Integer> attr : attrs.entrySet()) {
/* 70 */       this.attrs.put(attr.getKey(), new Double(((Integer)attr.getValue()).intValue()));
/*    */     }
/*    */   }
/*    */   
/*    */   public void putAttr(Attribute attr, double value) {
/* 75 */     this.attrs.put(attr, new Double(value));
/*    */   }
/*    */   
/*    */   public void removeBuffs(List<Integer> clearBuffList) {
/* 79 */     for (Integer id : clearBuffList) {
/* 80 */       this.buffs.remove(id);
/*    */     }
/*    */     
/* 83 */     this.hp = Math.min(this.hp, attr(Attribute.MaxHP));
/*    */   }
/*    */   
/*    */   public boolean isMain() {
/* 87 */     return this.isMain;
/*    */   }
/*    */   
/*    */   public void initBuff(int time) {
/* 91 */     for (Buff buff : this.buffs.values()) {
/* 92 */       buff.cd += time;
/* 93 */       buff.time += time;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/fight/Fighter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */