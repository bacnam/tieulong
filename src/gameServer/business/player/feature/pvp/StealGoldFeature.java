/*     */ package business.player.feature.pvp;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Random;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefVIP;
/*     */ import core.database.game.bo.StealGoldBO;
/*     */ import core.database.game.bo.StealGoldNewsBO;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class StealGoldFeature extends Feature {
/*     */   public static final int MAX_FIGHTERS = 4;
/*     */   private StealGoldBO bo;
/*     */   
/*     */   public StealGoldFeature(Player player) {
/*  21 */     super(player);
/*     */ 
/*     */ 
/*     */     
/*  25 */     this.news = new ArrayList<>();
/*     */     
/*  27 */     this.fighters = new ArrayList<>();
/*  28 */     this.extmoney = new ArrayList<>();
/*     */   }
/*     */   private List<StealGoldNewsBO> news; private List<Long> fighters; private List<Integer> extmoney;
/*     */   public void loadDB() {
/*  32 */     this.bo = (StealGoldBO)BM.getBM(StealGoldBO.class).findOne("pid", Long.valueOf(getPid()));
/*  33 */     this.news = BM.getBM(StealGoldNewsBO.class).findAll("pid", Long.valueOf(getPid()));
/*  34 */     if (this.bo == null) {
/*  35 */       this.bo = new StealGoldBO();
/*  36 */       this.bo.setPid(getPid());
/*  37 */       this.bo.insert();
/*     */     } 
/*  39 */     this.fighters = this.bo.getFightersAll();
/*     */   }
/*     */   
/*     */   public int addTimes() {
/*  43 */     this.bo.saveTimes(this.bo.getTimes() + 1);
/*  44 */     return this.bo.getTimes();
/*     */   }
/*     */   
/*     */   public int addTimes(int value) {
/*  48 */     this.bo.saveTimes(this.bo.getTimes() + value);
/*  49 */     return this.bo.getTimes();
/*     */   }
/*     */   
/*     */   public int getTimes() {
/*  53 */     return this.bo.getTimes();
/*     */   }
/*     */   
/*     */   public boolean checkTimes() {
/*  57 */     return (((RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).StealGold >= this.bo.getTimes());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Long> getList() {
/*  62 */     for (Iterator<Long> iterator = this.fighters.iterator(); iterator.hasNext(); ) { long i = ((Long)iterator.next()).longValue();
/*  63 */       if (i == 0L) {
/*  64 */         search();
/*     */         
/*     */         break;
/*     */       }  }
/*     */     
/*  69 */     return this.fighters;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Integer> getMoneyList() {
/*  74 */     return this.extmoney;
/*     */   }
/*     */   
/*     */   public void search() {
/*  78 */     List<Long> enemies = ((DroiyanFeature)this.player.getFeature(DroiyanFeature.class)).getEnemies();
/*  79 */     List<Long> players = ((DroiyanFeature)this.player.getFeature(DroiyanFeature.class)).ranOpponent(this.player.getPid(), 5);
/*  80 */     List<Long> ranplayers = new ArrayList<>();
/*  81 */     ranplayers.addAll(enemies);
/*  82 */     ranplayers.removeAll(players);
/*  83 */     ranplayers.addAll(players);
/*  84 */     for (int i = 0; i < 4; i++) {
/*  85 */       this.fighters.set(i, ranplayers.get(i));
/*  86 */       if (this.extmoney.size() != 4) {
/*  87 */         this.extmoney.add(Integer.valueOf(Random.nextInt(200, -100)));
/*     */       } else {
/*  89 */         this.extmoney.set(i, Integer.valueOf(Random.nextInt(200, -100)));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addNews(long stealpid, int money) {
/*  95 */     StealGoldNewsBO newbo = new StealGoldNewsBO();
/*  96 */     newbo.setPid(this.player.getPid());
/*  97 */     newbo.setAtkid(stealpid);
/*  98 */     newbo.setMoney(money);
/*  99 */     newbo.setTime(CommTime.nowSecond());
/* 100 */     newbo.insert();
/* 101 */     this.news.add(newbo);
/*     */   }
/*     */   
/*     */   public List<StealGoldNewsBO> getNews() {
/* 105 */     return this.news;
/*     */   }
/*     */   
/*     */   public void dailyRefresh() {
/*     */     try {
/* 110 */       search();
/* 111 */       this.bo.saveTimes(0);
/* 112 */     } catch (Exception e) {
/*     */       
/* 114 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/pvp/StealGoldFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */