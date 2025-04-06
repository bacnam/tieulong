/*     */ package core.config.refdata.ref;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import com.zhonglian.server.common.data.RefAssert;
/*     */ import com.zhonglian.server.common.data.RefContainer;
/*     */ import com.zhonglian.server.common.data.RefField;
/*     */ import com.zhonglian.server.common.enums.UnlockType;
/*     */ import com.zhonglian.server.common.utils.Random;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class RefRobot
/*     */   extends RefBaseGame
/*     */ {
/*     */   @RefField(iskey = true)
/*     */   public int id;
/*     */   public int Count;
/*     */   public int VIP;
/*     */   public int Level;
/*     */   public int DungeonLevel;
/*     */   public List<Integer> Characters1;
/*     */   public List<Integer> Characters2;
/*     */   public List<Integer> Characters3;
/*     */   public List<Integer> Characters4;
/*     */   public List<Integer> Characters5;
/*     */   
/*     */   public List<Integer> randTeam() {
/*  42 */     List<List<Integer>> teams = new ArrayList<>();
/*  43 */     if (!this.Characters1.isEmpty()) {
/*  44 */       teams.add(this.Characters1);
/*     */     }
/*  46 */     if (!this.Characters2.isEmpty()) {
/*  47 */       teams.add(this.Characters2);
/*     */     }
/*  49 */     if (!this.Characters3.isEmpty()) {
/*  50 */       teams.add(this.Characters3);
/*     */     }
/*  52 */     if (!this.Characters4.isEmpty()) {
/*  53 */       teams.add(this.Characters4);
/*     */     }
/*  55 */     if (!this.Characters5.isEmpty())
/*  56 */       teams.add(this.Characters5); 
/*  57 */     return teams.get(Random.nextInt(teams.size()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean Assert() {
/*  62 */     if (!assertCharacters(this.Characters1)) {
/*  63 */       return false;
/*     */     }
/*  65 */     if (!assertCharacters(this.Characters2)) {
/*  66 */       return false;
/*     */     }
/*  68 */     if (!assertCharacters(this.Characters3)) {
/*  69 */       return false;
/*     */     }
/*  71 */     if (!assertCharacters(this.Characters4)) {
/*  72 */       return false;
/*     */     }
/*  74 */     if (!assertCharacters(this.Characters5)) {
/*  75 */       return false;
/*     */     }
/*  77 */     if (this.Characters1.size() == 0 && this.Characters2.size() == 0 && this.Characters3.size() == 0 && this.Characters4.size() == 0 && this.Characters5.size() == 0) {
/*  78 */       CommLog.error("至少需要留一支队伍给机器人");
/*  79 */       return false;
/*     */     } 
/*  81 */     return true;
/*     */   }
/*     */   
/*     */   private boolean assertCharacters(List<Integer> characters) {
/*  85 */     if (characters.size() == 0) {
/*  86 */       return true;
/*     */     }
/*  88 */     if (!RefAssert.inRef(characters, RefRobotCharacter.class, new Object[] { Integer.valueOf(0) })) {
/*  89 */       return false;
/*     */     }
/*  91 */     UnlockType type = UnlockType.valueOf("Character" + characters.size());
/*  92 */     if (type != UnlockType.Character1 && !RefUnlockFunction.checkUnlockSave(this.Level, this.VIP, type)) {
/*  93 */       CommLog.error("玩家 {}级 VIP:{} 不能拥有{}个角色", new Object[] { Integer.valueOf(this.Level), Integer.valueOf(this.VIP), Integer.valueOf(characters.size()) });
/*  94 */       return false;
/*     */     } 
/*  96 */     Set<Integer> charidSet = new HashSet<>();
/*  97 */     for (Integer simulateId : characters) {
/*  98 */       RefRobotCharacter character = (RefRobotCharacter)RefDataMgr.get(RefRobotCharacter.class, simulateId);
/*  99 */       charidSet.add(Integer.valueOf(character.CharId));
/* 100 */       for (Integer equipid : character.Equip) {
/* 101 */         if (equipid.intValue() == 0)
/*     */           continue; 
/* 103 */         if (((RefEquip)RefDataMgr.get(RefEquip.class, equipid)).Level > this.Level) {
/* 104 */           CommLog.error("玩家 {}级 配置的角色:{}, 不能穿戴装备:{}", new Object[] { Integer.valueOf(this.Level), simulateId, equipid });
/* 105 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/* 109 */     if (charidSet.size() != characters.size()) {
/* 110 */       CommLog.error("配置了相同的角色 ID[" + characters.toString() + "]");
/* 111 */       return false;
/*     */     } 
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean AssertAll(RefContainer<?> all) {
/* 118 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefRobot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */