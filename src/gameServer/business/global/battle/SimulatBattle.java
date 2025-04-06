/*     */ package business.global.battle;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import business.global.arena.ArenaConfig;
/*     */ import business.player.Player;
/*     */ import business.player.feature.guild.GuildMemberFeature;
/*     */ import com.zhonglian.server.common.enums.Attribute;
/*     */ import com.zhonglian.server.common.enums.FightResult;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefCrystalPrice;
/*     */ import core.database.game.bo.GuildMemberBO;
/*     */ import core.server.ServerConfig;
/*     */ import java.io.File;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ 
/*     */ 
/*     */ public class SimulatBattle
/*     */   extends Battle
/*     */ {
/*     */   private static int atk_x;
/*     */   private static int atk_y;
/*     */   private static int def_x;
/*     */   private static int def_y;
/*  28 */   private static int MaxTime = 0;
/*     */   private static int tilewidth;
/*     */   private static int tileheight;
/*  31 */   private static String map = "BattleBossMap03";
/*     */   
/*     */   static {
/*  34 */     String mapPath = String.valueOf(ServerConfig.BattleMapPath()) + File.separator + 
/*  35 */       RefDataMgr.getGeneral("BattleArenaMap", SimulatBattle.map) + ".tmx";
/*     */     try {
/*  37 */       DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*     */       
/*  39 */       Element root = dbf.newDocumentBuilder().parse(mapPath).getDocumentElement();
/*  40 */       NamedNodeMap attrnodes = root.getAttributes();
/*  41 */       tilewidth = Integer.valueOf(attrnodes.getNamedItem("tilewidth").getNodeValue()).intValue();
/*  42 */       tileheight = Integer.valueOf(attrnodes.getNamedItem("tileheight").getNodeValue()).intValue();
/*     */       
/*  44 */       String map = ((Element)root.getElementsByTagName("layer").item(0)).getElementsByTagName("data").item(0).getTextContent();
/*     */       
/*  46 */       String[] rows = map.split("\n");
/*  47 */       for (int i = 1; i < rows.length; i++) {
/*  48 */         String[] cols = rows[i].trim().split(",");
/*  49 */         for (int j = 0; j < cols.length; j++) {
/*  50 */           if (cols[j].equals("2")) {
/*  51 */             atk_x = j * tilewidth + tilewidth / 2;
/*  52 */             atk_y = (i - 1) * tileheight + tileheight / 2;
/*  53 */           } else if (cols[j].equals("3")) {
/*  54 */             def_x = j * tilewidth + tilewidth / 2;
/*  55 */             def_y = (i - 1) * tileheight + tileheight / 2;
/*     */           } 
/*     */         } 
/*     */       } 
/*  59 */     } catch (Exception e) {
/*  60 */       CommLog.error("初始化竞技场战斗出现异常", e);
/*     */     } 
/*  62 */     MaxTime = ArenaConfig.fightTime();
/*     */   }
/*     */   
/*     */   public SimulatBattle(Player me, Player opponent) {
/*  66 */     super(me, opponent);
/*     */   }
/*     */   
/*     */   public void initHp(List<Double> atkHp, List<Double> defHp) {
/*  70 */     if (atkHp != null && atkHp.size() >= 0) {
/*  71 */       int i = 0;
/*  72 */       for (Creature cre : this.team) {
/*  73 */         if (cre.RoleType == RoleType.Character) {
/*  74 */           cre.hp = ((Double)atkHp.get(i)).doubleValue();
/*  75 */           cre.initHp = ((Double)atkHp.get(i)).doubleValue();
/*  76 */           i++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  81 */     if (defHp != null && defHp.size() >= 0) {
/*  82 */       int i = 0;
/*  83 */       for (Creature cre : this.opponents) {
/*  84 */         if (cre.RoleType == RoleType.Character && 
/*  85 */           cre.RoleType == RoleType.Character) {
/*  86 */           cre.hp = ((Double)defHp.get(i)).doubleValue();
/*  87 */           cre.initHp = ((Double)defHp.get(i)).doubleValue();
/*  88 */           i++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void initInspire() {
/*  97 */     int meTimes = 0;
/*  98 */     int oppoTimes = 0;
/*     */     
/* 100 */     GuildMemberBO boMe = ((GuildMemberFeature)this.me.getFeature(GuildMemberFeature.class)).getBo();
/* 101 */     GuildMemberBO boOppo = ((GuildMemberFeature)this.opponent.getFeature(GuildMemberFeature.class)).getBo();
/* 102 */     if (boMe != null)
/* 103 */       meTimes = ((GuildMemberFeature)this.me.getFeature(GuildMemberFeature.class)).getBo().getGuildwarInspire(); 
/* 104 */     if (boOppo != null) {
/* 105 */       oppoTimes = ((GuildMemberFeature)this.opponent.getFeature(GuildMemberFeature.class)).getBo().getGuildwarInspire();
/*     */     }
/* 107 */     if (meTimes != 0) {
/* 108 */       for (Creature cre : this.team) {
/* 109 */         if (cre.RoleType == RoleType.Character) {
/* 110 */           Double base = cre.attrs.get(Attribute.ATK);
/* 111 */           int value = (RefCrystalPrice.getPrize(meTimes)).GuildwarInspireValue;
/* 112 */           if (base != null) {
/* 113 */             cre.attrs.put(Attribute.ATK, Double.valueOf(base.doubleValue() * (100 + value) / 100.0D));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 120 */     if (oppoTimes != 0) {
/* 121 */       for (Creature cre : this.opponents) {
/* 122 */         if (cre.RoleType == RoleType.Character) {
/* 123 */           Double base = cre.attrs.get(Attribute.ATK);
/* 124 */           int value = (RefCrystalPrice.getPrize(meTimes)).GuildwarInspireValue;
/* 125 */           if (base != null) {
/* 126 */             cre.attrs.put(Attribute.ATK, Double.valueOf(base.doubleValue() * (100 + value) / 100.0D));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getMap() {
/* 135 */     return map;
/*     */   }
/*     */   
/*     */   protected int fightTime() {
/* 139 */     return MaxTime;
/*     */   }
/*     */   
/*     */   protected int tiledWidth() {
/* 143 */     return tilewidth;
/*     */   }
/*     */   
/*     */   protected int tiledHeight() {
/* 147 */     return tileheight;
/*     */   }
/*     */   
/*     */   public void onLost() {
/* 151 */     this.stopped = true;
/* 152 */     CommLog.info("def player win!");
/*     */   }
/*     */   
/*     */   public void onWin() {
/* 156 */     this.stopped = true;
/* 157 */     CommLog.info("atk player win!");
/*     */   }
/*     */   
/*     */   public List<Double> getWinnerHp() {
/* 161 */     List<Double> list = new LinkedList<>();
/* 162 */     if (getResult() == FightResult.Win) {
/* 163 */       this.team.stream().filter(x -> (x.RoleType == RoleType.Character)).forEach(x -> paramList.add(Double.valueOf(x.hp)));
/*     */ 
/*     */     
/*     */     }
/* 167 */     else if (getResult() == FightResult.Lost) {
/* 168 */       this.opponents.stream().filter(x -> (x.RoleType == RoleType.Character)).forEach(x -> paramList.add(Double.valueOf(x.hp)));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 173 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initLoc() {
/* 179 */     initLoc(this.opponents, def_x, def_y);
/* 180 */     initLoc(this.team, atk_x, atk_y);
/*     */   }
/*     */   
/*     */   public FightResult getResult() {
/* 184 */     return this.result;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/battle/SimulatBattle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */