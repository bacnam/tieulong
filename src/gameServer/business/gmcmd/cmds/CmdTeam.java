/*     */ package business.gmcmd.cmds;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import business.global.fight.Fighter;
/*     */ import business.gmcmd.annotation.Command;
/*     */ import business.gmcmd.annotation.Commander;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerBase;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.character.Character;
/*     */ import com.zhonglian.server.common.enums.Attribute;
/*     */ import core.config.refdata.ref.RefRebirth;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ @Commander(name = "team", comment = "队伍设置相关")
/*     */ public class CmdTeam
/*     */ {
/*     */   @Command(comment = "输出玩家队伍属性")
/*     */   public String power(Player player) {
/*  22 */     Map<Integer, Fighter> fighters = ((CharFeature)player.getFeature(CharFeature.class)).getFighters();
/*  23 */     for (Map.Entry<Integer, Fighter> pair : fighters.entrySet()) {
/*  24 */       CommLog.info("============分割线============");
/*  25 */       Fighter fighter = pair.getValue();
/*  26 */       CommLog.info("角色ID：{}", Integer.valueOf(fighter.id));
/*  27 */       CommLog.info("生命：{}", Double.valueOf(fighter.attr(Attribute.MaxHP)));
/*  28 */       CommLog.info("攻击：{}", Double.valueOf(fighter.attr(Attribute.ATK)));
/*  29 */       CommLog.info("防御：{}", Double.valueOf(fighter.attr(Attribute.DEF)));
/*  30 */       CommLog.info("法防：{}", Double.valueOf(fighter.attr(Attribute.RGS)));
/*  31 */       CommLog.info("命中：{}", Double.valueOf(fighter.attr(Attribute.Hit)));
/*  32 */       CommLog.info("闪避：{}", Double.valueOf(fighter.attr(Attribute.Dodge)));
/*  33 */       CommLog.info("暴击：{}", Double.valueOf(fighter.attr(Attribute.Critical)));
/*  34 */       CommLog.info("暴抗：{}", Double.valueOf(fighter.attr(Attribute.Tenacity)));
/*  35 */       CommLog.info("============分割线============");
/*     */     } 
/*  37 */     return "查看服务端控制台输出";
/*     */   }
/*     */   
/*     */   @Command(comment = "设置人物等级")
/*     */   public String level(Player player, int level) {
/*  42 */     player.getPlayerBO().saveLv(level);
/*  43 */     if (level > 100) {
/*  44 */       int temp1 = level % 100;
/*  45 */       int temp2 = temp1 / 10;
/*  46 */       int rebirth = ((RefRebirth)((List)RefRebirth.sameLevel.get(Integer.valueOf(temp2))).get(0)).id;
/*  47 */       Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
/*  48 */       for (Character chars : characters.values()) {
/*  49 */         chars.getBo().saveRebirth(rebirth);
/*  50 */         chars.onAttrChanged();
/*     */       } 
/*     */     } 
/*     */     
/*  54 */     ((PlayerBase)player.getFeature(PlayerBase.class)).onLevelUp(level);
/*  55 */     player.pushProperties("lv", player.getPlayerBO().getLv());
/*  56 */     return "ok";
/*     */   }
/*     */   
/*     */   @Command(comment = "设置翅膀等级")
/*     */   public String wing(Player player, int level) {
/*  61 */     Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
/*  62 */     for (Character chars : characters.values()) {
/*  63 */       chars.getBo().saveWing(level);
/*  64 */       chars.onAttrChanged();
/*     */     } 
/*  66 */     return "ok";
/*     */   }
/*     */   
/*     */   @Command(comment = "设置转生等级")
/*     */   public String rebirth(Player player, int level) {
/*  71 */     Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
/*  72 */     for (Character chars : characters.values()) {
/*  73 */       chars.getBo().saveRebirth(level);
/*  74 */       chars.onAttrChanged();
/*     */     } 
/*  76 */     return "ok";
/*     */   }
/*     */   
/*     */   @Command(comment = "设置经脉等级")
/*     */   public String meridian(Player player, int level) {
/*  81 */     Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
/*  82 */     for (Character chars : characters.values()) {
/*  83 */       chars.getBo().saveMeridian(level);
/*  84 */       chars.onAttrChanged();
/*     */     } 
/*  86 */     return "ok";
/*     */   }
/*     */   
/*     */   @Command(comment = "设置技能等级")
/*     */   public String skill(Player player, int level) {
/*  91 */     Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
/*  92 */     for (Character chars : characters.values()) {
/*  93 */       chars.getBo().saveSkillAll(level);
/*  94 */       chars.onAttrChanged();
/*     */     } 
/*  96 */     return "ok";
/*     */   }
/*     */   
/*     */   @Command(comment = "设置强化等级")
/*     */   public String strengthen(Player player, int level) {
/* 101 */     Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
/* 102 */     for (Character chars : characters.values()) {
/* 103 */       chars.getBo().saveStrengthenAll(level);
/* 104 */       chars.onAttrChanged();
/*     */     } 
/* 106 */     return "ok";
/*     */   }
/*     */   
/*     */   @Command(comment = "设置宝石等级")
/*     */   public String gem(Player player, int level) {
/* 111 */     Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
/* 112 */     for (Character chars : characters.values()) {
/* 113 */       chars.getBo().saveGemAll(level);
/* 114 */       chars.onAttrChanged();
/*     */     } 
/* 116 */     return "ok";
/*     */   }
/*     */   
/*     */   @Command(comment = "设置星级")
/*     */   public String star(Player player, int level) {
/* 121 */     Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
/* 122 */     for (Character chars : characters.values()) {
/* 123 */       chars.getBo().saveStarAll(level);
/* 124 */       chars.onAttrChanged();
/*     */     } 
/* 126 */     return "ok";
/*     */   }
/*     */   
/*     */   @Command(comment = "设置炼化等级")
/*     */   public String artifice(Player player, int level) {
/* 131 */     Map<Integer, Character> characters = ((CharFeature)player.getFeature(CharFeature.class)).getAll();
/* 132 */     for (Character chars : characters.values()) {
/* 133 */       chars.getBo().saveArtificeAll(level);
/* 134 */       chars.onAttrChanged();
/*     */     } 
/* 136 */     return "ok";
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/gmcmd/cmds/CmdTeam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */