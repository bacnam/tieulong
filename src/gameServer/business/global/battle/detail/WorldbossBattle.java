/*     */ package business.global.battle.detail;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import business.global.battle.Battle;
/*     */ import business.global.battle.Creature;
/*     */ import business.player.Player;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefWorldBoss;
/*     */ import core.server.ServerConfig;
/*     */ import java.io.File;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ 
/*     */ public class WorldbossBattle
/*     */   extends Battle {
/*     */   private int atk_x;
/*     */   private int atk_y;
/*     */   private int def_x;
/*     */   private int def_y;
/*  21 */   private int MaxTime = 0;
/*     */   private int tilewidth;
/*     */   private int tileheight;
/*     */   private String map;
/*  25 */   private long MaxHp = 0L;
/*     */   
/*     */   public WorldbossBattle(Player me, int monsterId) {
/*  28 */     super(me, monsterId);
/*     */   }
/*     */   
/*     */   public void init(int bossId) {
/*  32 */     RefWorldBoss ref = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(bossId));
/*  33 */     this.MaxHp = ref.MaxHP;
/*  34 */     this.map = ref.MapId;
/*  35 */     String mapPath = String.valueOf(ServerConfig.BattleMapPath()) + File.separator + 
/*  36 */       ref.MapId + ".tmx";
/*     */     try {
/*  38 */       DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*  39 */       Element root = dbf.newDocumentBuilder().parse(mapPath).getDocumentElement();
/*  40 */       NamedNodeMap attrnodes = root.getAttributes();
/*  41 */       this.tilewidth = Integer.valueOf(attrnodes.getNamedItem("tilewidth").getNodeValue()).intValue();
/*  42 */       this.tileheight = Integer.valueOf(attrnodes.getNamedItem("tileheight").getNodeValue()).intValue();
/*     */       
/*  44 */       String map = ((Element)root.getElementsByTagName("layer").item(0)).getElementsByTagName("data").item(0).getTextContent();
/*     */       
/*  46 */       String[] rows = map.split("\n");
/*  47 */       for (int i = 1; i < rows.length; i++) {
/*  48 */         String[] cols = rows[i].trim().split(",");
/*  49 */         for (int j = 0; j < cols.length; j++) {
/*  50 */           if (cols[j].equals("2")) {
/*  51 */             this.atk_x = j * this.tilewidth + this.tilewidth / 2;
/*  52 */             this.atk_y = (i - 1) * this.tileheight + this.tileheight / 2;
/*  53 */           } else if (cols[j].equals("3")) {
/*  54 */             this.def_x = j * this.tilewidth + this.tilewidth / 2;
/*  55 */             this.def_y = (i - 1) * this.tileheight + this.tileheight / 2;
/*     */           } 
/*     */         } 
/*     */       } 
/*  59 */     } catch (Exception e) {
/*  60 */       CommLog.error("初始化世界BOSS战斗出现异常", e);
/*     */     } 
/*  62 */     this.MaxTime = RefDataMgr.getFactor("WorldBossAttackCD", 30);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getMap() {
/*  67 */     return this.map;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int fightTime() {
/*  72 */     return this.MaxTime;
/*     */   }
/*     */   
/*     */   protected int tiledWidth() {
/*  76 */     return this.tilewidth;
/*     */   }
/*     */   
/*     */   protected int tiledHeight() {
/*  80 */     return this.tileheight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLost() {
/*  85 */     this.stopped = true;
/*  86 */     CommLog.info("def player win!");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onWin() {
/*  92 */     this.stopped = true;
/*  93 */     CommLog.info("atk player win!");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initLoc() {
/*  99 */     initLoc(this.opponents, this.def_x, this.def_y);
/* 100 */     initLoc(this.team, this.atk_x, this.atk_y);
/*     */   }
/*     */   
/*     */   public long getDamage() {
/* 104 */     Creature boss = this.opponents.get(0);
/* 105 */     return this.MaxHp - (long)boss.getHp();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/battle/detail/WorldbossBattle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */