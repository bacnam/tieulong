/*     */ package business.global.battle;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.feature.character.CharAttrCalculator;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.character.Character;
/*     */ import business.player.feature.character.Equip;
/*     */ import business.player.feature.character.SpiritAttrCalculator;
/*     */ import business.player.feature.character.WarSpirit;
/*     */ import business.player.feature.character.WarSpiritFeature;
/*     */ import com.zhonglian.server.common.enums.Attribute;
/*     */ import com.zhonglian.server.common.enums.EquipPos;
/*     */ import com.zhonglian.server.common.enums.FightResult;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefCharacter;
/*     */ import core.config.refdata.ref.RefMonster;
/*     */ import core.config.refdata.ref.RefSkill;
/*     */ import core.config.refdata.ref.RefWarSpirit;
/*     */ import core.database.game.bo.CharacterBO;
/*     */ import core.database.game.bo.WarSpiritBO;
/*     */ import core.network.proto.Fight;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ public abstract class Battle
/*     */ {
/*  30 */   private static int nextFightId = 1;
/*     */   
/*  32 */   int fightId = nextFightId++;
/*  33 */   long seed = this.fightId; protected List<Creature> all; protected List<Creature> team; protected List<Creature> opponents; protected List<Bullet> bullets;
/*     */   
/*     */   double random() {
/*  36 */     this.seed = (this.seed * 9301L + 49297L) % 233280L;
/*  37 */     return this.seed / 233280.0D;
/*     */   }
/*     */   protected boolean stopped; FightResult result; Player me; Player opponent; protected double time;
/*     */   public int getFightId() {
/*  41 */     return this.fightId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Battle(Player me, Player opponent) {
/*  57 */     this.team = getTeam(me);
/*  58 */     this.team.forEach(c -> { 
/*  59 */         }); this.opponents = getTeam(opponent);
/*  60 */     this.opponents.forEach(c -> {
/*     */         
/*  62 */         }); this.all = new ArrayList<>();
/*  63 */     this.all.addAll(this.team);
/*  64 */     this.all.addAll(this.opponents);
/*     */     
/*  66 */     this.bullets = new ArrayList<>();
/*  67 */     this.me = me;
/*  68 */     this.opponent = opponent;
/*     */   }
/*     */   
/*     */   public Battle(Player me, int monsterId) {
/*  72 */     this.team = getTeam(me);
/*  73 */     this.team.forEach(c -> { 
/*  74 */         }); this.opponents = getMonster(monsterId);
/*  75 */     this.opponents.forEach(c -> {
/*     */         
/*  77 */         }); this.all = new ArrayList<>();
/*  78 */     this.all.addAll(this.team);
/*  79 */     this.all.addAll(this.opponents);
/*  80 */     this.bullets = new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Fight.Battle proto() {
/*  89 */     return new Fight.Battle(this.fightId, getMap(), this.team, this.opponents);
/*     */   }
/*     */   
/*     */   List<Creature> getTeam(Player player) {
/*  93 */     List<Creature> creatures = new ArrayList<>();
/*     */     
/*  95 */     List<Character> characters = 
/*  96 */       new ArrayList<>(((CharFeature)player.getFeature(CharFeature.class)).getAll().values());
/*  97 */     characters.sort((left, right) -> (left.getBo().getId() < right.getBo().getId()) ? -1 : 1);
/*  98 */     for (Character character : characters) {
/*  99 */       int charid = character.getCharId();
/* 100 */       CharacterBO bo = character.getBo();
/*     */       
/* 102 */       Equip equip = character.getEquip(EquipPos.Body);
/* 103 */       int equipid = (equip == null) ? 0 : equip.getEquipId();
/*     */       
/* 105 */       CharAttrCalculator charAttrCalculator = character.getAttr();
/* 106 */       Creature fighter = new Creature(this, charid, equipid, bo.getWing(), charAttrCalculator.getAttrs());
/* 107 */       fighter.level = player.getLv();
/* 108 */       fighter.RoleType = RoleType.Character;
/*     */       
/* 110 */       RefCharacter refChar = (RefCharacter)RefDataMgr.get(RefCharacter.class, Integer.valueOf(charid));
/* 111 */       fighter.SPD = refChar.SPD;
/* 112 */       fighter.RNG = refChar.RNG;
/* 113 */       for (int i = 0; i < refChar.SkillList.size(); i++) {
/* 114 */         int skillid = ((Integer)refChar.SkillList.get(i)).intValue();
/* 115 */         RefSkill skill = (RefSkill)RefDataMgr.get(RefSkill.class, Integer.valueOf(skillid));
/* 116 */         if (player.getLv() >= skill.Require)
/*     */         {
/*     */           
/* 119 */           fighter.skillLauncher.addSkill(new Skill(fighter, skill, Math.max(bo.getSkill(i), 1))); } 
/*     */       } 
/* 121 */       creatures.add(fighter);
/*     */     } 
/*     */     
/* 124 */     WarSpirit spirit = ((WarSpiritFeature)player.getFeature(WarSpiritFeature.class)).getWarSpiritNow();
/* 125 */     if (spirit != null) {
/* 126 */       WarSpiritBO bo = spirit.getBo();
/* 127 */       RefWarSpirit refSpirit = (RefWarSpirit)RefDataMgr.get(RefWarSpirit.class, Integer.valueOf(bo.getSpiritId()));
/*     */       
/* 129 */       SpiritAttrCalculator calculator = spirit.getAttr();
/* 130 */       Creature fighter = new Creature(this, spirit.getSpiritId(), spirit.getSpiritId(), 0, calculator.getAttrs());
/* 131 */       fighter.level = player.getPlayerBO().getWarspiritLv();
/* 132 */       fighter.RoleType = RoleType.WarSpirit;
/*     */       
/* 134 */       fighter.SPD = refSpirit.SPD;
/* 135 */       fighter.RNG = refSpirit.RNG;
/* 136 */       for (int i = 0; i < refSpirit.SkillList.size(); i++) {
/* 137 */         int skillid = ((Integer)refSpirit.SkillList.get(i)).intValue();
/* 138 */         RefSkill skill = (RefSkill)RefDataMgr.get(RefSkill.class, Integer.valueOf(skillid));
/* 139 */         if (skillid == 0) {
/* 140 */           fighter.skillLauncher.addSkill(new Skill(fighter, skill, bo.getSkill()));
/*     */         } else {
/* 142 */           fighter.skillLauncher.addSkill(new Skill(fighter, skill, 0));
/*     */         } 
/*     */       } 
/* 145 */       creatures.add(fighter);
/*     */     } 
/* 147 */     return creatures;
/*     */   }
/*     */   
/*     */   List<Creature> getMonster(int monsterId) {
/* 151 */     List<Creature> creatures = new ArrayList<>();
/*     */     
/* 153 */     RefMonster refMonster = (RefMonster)RefDataMgr.get(RefMonster.class, Integer.valueOf(monsterId));
/*     */     
/* 155 */     Map<Attribute, Integer> attrs = new HashMap<>();
/* 156 */     attrs.put(Attribute.MaxHP, Integer.valueOf(refMonster.MaxHP));
/* 157 */     attrs.put(Attribute.ATK, Integer.valueOf(refMonster.ATK));
/* 158 */     attrs.put(Attribute.DEF, Integer.valueOf(refMonster.DEF));
/* 159 */     attrs.put(Attribute.RGS, Integer.valueOf(refMonster.RGS));
/* 160 */     attrs.put(Attribute.Hit, Integer.valueOf(refMonster.Hit));
/* 161 */     attrs.put(Attribute.Dodge, Integer.valueOf(refMonster.Dodge));
/* 162 */     attrs.put(Attribute.Critical, Integer.valueOf(refMonster.Critical));
/* 163 */     attrs.put(Attribute.Tenacity, Integer.valueOf(refMonster.Tenacity));
/* 164 */     Creature fighter = new Creature(this, monsterId, 0, 0, attrs);
/* 165 */     fighter.level = refMonster.Level;
/* 166 */     fighter.RoleType = RoleType.Monster;
/*     */     
/* 168 */     fighter.SPD = refMonster.SPD;
/* 169 */     fighter.RNG = refMonster.RNG;
/* 170 */     for (int i = 0; i < refMonster.SkillList.size(); i++) {
/* 171 */       int skillid = ((Integer)refMonster.SkillList.get(i)).intValue();
/* 172 */       RefSkill skill = (RefSkill)RefDataMgr.get(RefSkill.class, Integer.valueOf(skillid));
/* 173 */       if (fighter.level >= skill.Require)
/*     */       {
/*     */         
/* 176 */         fighter.skillLauncher.addSkill(new Skill(fighter, skill, Math.max(0, 1))); } 
/*     */     } 
/* 178 */     creatures.add(fighter);
/*     */     
/* 180 */     return creatures;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void win() {
/* 193 */     this.result = FightResult.Win;
/* 194 */     onWin();
/*     */   }
/*     */   
/*     */   protected void lost() {
/* 198 */     this.result = FightResult.Lost;
/* 199 */     onLost();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fight() {
/* 210 */     initLoc();
/*     */     
/* 212 */     this.time = 0.0D;
/* 213 */     this.stopped = false;
/* 214 */     int maxTime = fightTime();
/*     */     
/* 216 */     while (this.time < maxTime && !this.stopped) {
/* 217 */       double dt = 0.016666666666666666D;
/*     */       
/* 219 */       List<Bullet> toremove = new ArrayList<>();
/* 220 */       for (int i = this.bullets.size() - 1; i >= 0; i--) {
/* 221 */         Bullet bullet = this.bullets.get(i);
/* 222 */         bullet.update(dt);
/* 223 */         if (!bullet.alive())
/* 224 */           toremove.add(bullet); 
/*     */       } 
/* 226 */       this.bullets.removeAll(toremove);
/*     */ 
/*     */       
/* 229 */       Creature leader = getLeader(this.team);
/* 230 */       if (leader == null) {
/* 231 */         lost();
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 236 */       for (int j = 0; j < this.all.size(); j++) {
/* 237 */         Creature creature = this.all.get(j);
/* 238 */         creature.update(dt);
/* 239 */         if (!creature.isDead()) {
/*     */ 
/*     */           
/* 242 */           Creature enemy = getOpponent(creature);
/* 243 */           if (enemy != null) {
/*     */             
/* 245 */             creature.castSkill();
/*     */             
/* 247 */             creature.stepTo(enemy, dt);
/*     */           } 
/*     */         } 
/* 250 */       }  this.time += dt;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Creature getOpponent(Creature c) {
/* 255 */     if (c == null) {
/* 256 */       return null;
/*     */     }
/* 258 */     Creature closest = null;
/* 259 */     double d = Double.MAX_VALUE;
/* 260 */     for (int i = 0; i < this.all.size(); i++) {
/* 261 */       Creature e = this.all.get(i);
/* 262 */       if (!e.isDead())
/*     */       {
/*     */         
/* 265 */         if (!c.isFriend(e)) {
/*     */ 
/*     */           
/* 268 */           double d2 = distance(e, c);
/* 269 */           if (d2 < d) {
/* 270 */             d = d2;
/* 271 */             closest = e;
/*     */           } 
/*     */         }  } 
/* 274 */     }  return closest;
/*     */   }
/*     */   
/*     */   private double distance(Creature c1, Creature c2) {
/* 278 */     double dx = c1.x - c2.x;
/* 279 */     double dy = c1.y - c2.y;
/* 280 */     return Math.sqrt(dx * dx + dy * dy);
/*     */   }
/*     */   
/*     */   protected Creature getLeader(List<Creature> team) {
/* 284 */     for (int i = 0; i < team.size(); i++) {
/* 285 */       Creature m = team.get(i);
/* 286 */       if (!m.isDead() && m.RoleType != RoleType.WarSpirit) {
/* 287 */         return m;
/*     */       }
/*     */     } 
/* 290 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initLoc(List<Creature> fighters, int initx, int inity) {
/* 296 */     List<Point> list = new ArrayList<>();
/*     */     
/* 298 */     int size = fighters.size();
/* 299 */     for (int i = -size; i < size; i++) {
/* 300 */       for (int j = -size; j < size; j++) {
/* 301 */         list.add(new Point(initx + tiledWidth() * i, inity + tiledHeight() * j));
/*     */       }
/*     */     } 
/* 304 */     shuffle(list);
/* 305 */     int idx = 0;
/* 306 */     for (Creature f : fighters) {
/* 307 */       Point point = list.get(idx++);
/* 308 */       f.x = point.x;
/* 309 */       f.y = point.y;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected <T> void shuffle(List<T> inputList) {
/* 314 */     for (int i = inputList.size() - 1; i >= 0; i--) {
/* 315 */       int randomIndex = (int)Math.floor(random() * (i + 1));
/* 316 */       T itemAtIndex = inputList.get(randomIndex);
/*     */       
/* 318 */       inputList.set(randomIndex, inputList.get(i));
/* 319 */       inputList.set(i, itemAtIndex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onDie(Creature creature) {
/* 324 */     if (getLeader(this.team) == null) {
/* 325 */       lost();
/* 326 */     } else if (getLeader(this.opponents) == null) {
/* 327 */       win();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addBullet(Bullet bullet) {
/* 332 */     this.bullets.add(bullet);
/*     */   }
/*     */   
/*     */   public Battle() {}
/*     */   
/*     */   protected abstract String getMap();
/*     */   
/*     */   protected abstract int fightTime();
/*     */   
/*     */   protected abstract int tiledWidth();
/*     */   
/*     */   protected abstract int tiledHeight();
/*     */   
/*     */   protected abstract void onLost();
/*     */   
/*     */   protected abstract void onWin();
/*     */   
/*     */   protected abstract void initLoc();
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/battle/Battle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */