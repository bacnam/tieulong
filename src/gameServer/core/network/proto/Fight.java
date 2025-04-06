/*     */ package core.network.proto;
/*     */ 
/*     */ import business.global.battle.Creature;
/*     */ import com.zhonglian.server.common.enums.Attribute;
/*     */ import com.zhonglian.server.common.enums.FightResult;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ public class Fight
/*     */ {
/*     */   public static class Begin
/*     */   {
/*     */     public int fightId;
/*     */     
/*     */     public Begin(int id) {
/*  18 */       this.fightId = id;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class End
/*     */   {
/*     */     public int fightId;
/*     */     public FightResult result;
/*     */     public List<Fight.CheckData> checks;
/*     */   }
/*     */   
/*     */   public static class Settle
/*     */   {
/*     */     public int victim;
/*     */     public int vgroup;
/*     */     public double value;
/*     */     
/*     */     public Settle(int victim, int vgroup, double value) {
/*  36 */       this.victim = victim;
/*  37 */       this.vgroup = vgroup;
/*  38 */       this.value = value;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class CheckData
/*     */   {
/*     */     public int time;
/*     */     public int attacker;
/*     */     public int agroup;
/*     */     public int skill;
/*     */     public Fight.Settle[] settles;
/*     */     
/*     */     public CheckData(int time, int attacker, int agroup, int skill, Fight.Settle[] settles) {
/*  52 */       this.time = time;
/*  53 */       this.attacker = attacker;
/*  54 */       this.agroup = agroup;
/*  55 */       this.skill = skill;
/*  56 */       this.settles = settles;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class Skill
/*     */   {
/*     */     int id;
/*     */     
/*     */     int level;
/*     */   }
/*     */   
/*     */   public static class Fighter
/*     */   {
/*     */     int type;
/*     */     int id;
/*     */     int level;
/*     */     int Model;
/*     */     int Wing;
/*     */     double inithp;
/*     */     double MaxHP;
/*     */     double ATK;
/*     */     double DEF;
/*     */     double RGS;
/*     */     double Hit;
/*     */     double Dodge;
/*     */     double Critical;
/*     */     double Tenacity;
/*     */     double RNG;
/*     */     double SPD;
/*     */     List<Fight.Skill> skills;
/*     */     
/*     */     public Fighter(Creature c) {
/*  89 */       this.type = c.getType();
/*  90 */       this.id = c.getId();
/*  91 */       this.level = c.getLevel();
/*  92 */       this.Model = c.getModel();
/*  93 */       this.Wing = c.getWing();
/*     */       
/*  95 */       this.inithp = c.initHp;
/*  96 */       this.MaxHP = c.baseAttr(Attribute.MaxHP);
/*  97 */       this.ATK = c.baseAttr(Attribute.ATK);
/*  98 */       this.DEF = c.baseAttr(Attribute.DEF);
/*  99 */       this.RGS = c.baseAttr(Attribute.RGS);
/* 100 */       this.Hit = c.baseAttr(Attribute.Hit);
/* 101 */       this.Dodge = c.baseAttr(Attribute.Dodge);
/* 102 */       this.Critical = c.baseAttr(Attribute.Critical);
/* 103 */       this.Tenacity = c.baseAttr(Attribute.Tenacity);
/*     */       
/* 105 */       this.RNG = c.getRange();
/* 106 */       this.SPD = c.getSpeed();
/*     */       
/* 108 */       this.skills = new ArrayList<>();
/* 109 */       for (business.global.battle.Skill s : c.getSkills()) {
/* 110 */         Fight.Skill s0 = new Fight.Skill();
/* 111 */         s0.id = s.getId();
/* 112 */         s0.level = s.getLevel();
/* 113 */         this.skills.add(s0);
/*     */       } 
/*     */     } }
/*     */   
/*     */   public static class Battle {
/*     */     int id;
/*     */     
/*     */     public Battle(int id, String map, List<Creature> team, List<Creature> opponents) {
/* 121 */       this.id = id;
/* 122 */       this.map = map;
/* 123 */       this.team = (List<Fight.Fighter>)team.stream().map(c -> new Fight.Fighter(c)).collect(Collectors.toList());
/* 124 */       this.oppos = (List<Fight.Fighter>)opponents.stream().map(c -> new Fight.Fighter(c)).collect(Collectors.toList());
/*     */     }
/*     */     
/*     */     String map;
/*     */     List<Fight.Fighter> team;
/*     */     List<Fight.Fighter> oppos;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/Fight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */