/*     */ package business.global.battle;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.zhonglian.server.common.enums.Attribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ public class Selector
/*     */ {
/*     */   private ITarget targetSelect;
/*     */   private ITarget castSelect;
/*     */   private IStrategy strategy;
/*     */   private IArea area;
/*     */   private AreaParam areaParam;
/*     */   
/*     */   public Selector(String selectTarget, String strategy, String castTarget, String area, String param) {
/*  19 */     this.targetSelect = target(selectTarget);
/*  20 */     this.castSelect = target(castTarget);
/*  21 */     this.strategy = strategy(strategy);
/*  22 */     this.area = area(area);
/*  23 */     this.areaParam = (AreaParam)(new Gson()).fromJson(param, AreaParam.class);
/*     */   }
/*     */   static class Result { Creature selected;
/*     */     
/*     */     Result(Creature selected, List<Creature> castOn) {
/*  28 */       this.selected = selected;
/*  29 */       this.castOn = castOn;
/*     */     }
/*     */ 
/*     */     
/*     */     List<Creature> castOn; }
/*     */ 
/*     */   
/*     */   public Result select(Creature me, List<Creature> all) {
/*  37 */     List<Creature> toSelectCast = new ArrayList<>();
/*  38 */     Creature target = null;
/*     */     
/*  40 */     for (Creature e : all) {
/*  41 */       if (e.isDead())
/*     */         continue; 
/*  43 */       if (this.castSelect.apply(me, e)) {
/*  44 */         toSelectCast.add(e);
/*     */       }
/*  46 */       if (this.targetSelect.apply(me, e) && me.inRange(e)) {
/*  47 */         target = (target == null) ? e : this.strategy.apply(me, target, e);
/*     */       }
/*     */     } 
/*  50 */     return new Result(target, this.area.apply(target, toSelectCast, this.areaParam));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ITarget target(String name) {
/*     */     String str;
/*  58 */     switch ((str = name).hashCode()) { case 2573164: if (!str.equals("Self"))
/*     */           break; 
/*  60 */         return (me, e) -> (me == e);
/*     */       case 63350320: if (!str.equals("Alias"))
/*  62 */           break;  return (me, e) -> (me != e && me.isFriend(e));
/*     */       case 67100520: if (!str.equals("Enemy")); break;
/*     */       case 69076575:
/*     */         if (!str.equals("Group"))
/*  66 */           break;  return (me, e) -> !(me != e && me.group != e.group);
/*     */       case 681064005:
/*     */         if (!str.equals("SelfAndAlias"))
/*  69 */           break;  return (me, e) -> !(me != e && !me.isFriend(e)); }  return (me, e) -> (me != e && !me.isFriend(e));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static IStrategy strategy(String name) {
/*     */     String str;
/*  79 */     switch ((str = name).hashCode()) { case -1763776967: if (!str.equals("Closest")); break;case 69907466: if (!str.equals("HpMin"))
/*     */           break; 
/*  81 */         return (me, tar1, tar2) -> (tar1.hp / tar1.attr(Attribute.MaxHP).doubleValue() < tar2.hp / tar2.attr(Attribute.MaxHP).doubleValue()) ? tar1 : tar2; }
/*     */ 
/*     */ 
/*     */     
/*  85 */     return (me, tar1, tar2) -> (distance0(me, tar1) < distance0(me, tar2)) ? tar1 : tar2;
/*     */   }
/*     */ 
/*     */   
/*     */   static class AreaParam
/*     */   {
/*     */     double radius;
/*     */   }
/*     */   
/*     */   private static interface IArea
/*     */   {
/*     */     default List<Creature> apply(Creature me, List<Creature> all, Selector.AreaParam param) {
/*  97 */       if (me == null) {
/*  98 */         return null;
/*     */       }
/* 100 */       return apply0(me, all, param);
/*     */     }
/*     */     
/*     */     List<Creature> apply0(Creature param1Creature, List<Creature> param1List, Selector.AreaParam param1AreaParam); }
/*     */   
/*     */   private static IArea area(String name) {
/*     */     String str;
/* 107 */     switch ((str = name).hashCode()) { case -1818398616: if (!str.equals("Single")); break;case 65921: if (!str.equals("All"))
/*     */           break; 
/* 109 */         return (target, all, param) -> all;
/*     */       case 2018617584: if (!str.equals("Circle"))
/* 111 */           break;  return (target, all, param) -> {
/*     */             double d = param.radius * param.radius;
/*     */             return (List)all.stream().filter(()).collect(Collectors.toList());
/*     */           }; }
/*     */ 
/*     */     
/* 117 */     return (target, all, param) -> {
/*     */         List<Creature> rtn = new ArrayList<>();
/*     */         rtn.add(target);
/*     */         return rtn;
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   private static double distance0(Creature c1, Creature c2) {
/* 126 */     double dx = c1.x - c2.x;
/* 127 */     double dy = c1.y - c2.y;
/* 128 */     return dx * dx + dy * dy;
/*     */   }
/*     */   
/*     */   private static interface IStrategy {
/*     */     Creature apply(Creature param1Creature1, Creature param1Creature2, Creature param1Creature3);
/*     */   }
/*     */   
/*     */   private static interface ITarget {
/*     */     boolean apply(Creature param1Creature1, Creature param1Creature2);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/battle/Selector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */