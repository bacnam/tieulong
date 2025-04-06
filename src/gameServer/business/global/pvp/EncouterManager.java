/*    */ package business.global.pvp;
/*    */ 
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import java.util.ArrayList;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class EncouterManager
/*    */ {
/*    */   public enum NewsType
/*    */   {
/* 14 */     None,
/* 15 */     Drop,
/* 16 */     Rob,
/* 17 */     Open;
/*    */   }
/*    */ 
/*    */   
/*    */   public static class EncouterNews
/*    */   {
/*    */     public EncouterManager.NewsType type;
/*    */     public String player;
/*    */     public int treasureId;
/*    */     public int time;
/*    */   }
/* 28 */   private static EncouterManager instacne = null;
/*    */   
/*    */   public static EncouterManager getInstance() {
/* 31 */     if (instacne == null) {
/* 32 */       instacne = new EncouterManager();
/*    */     }
/* 34 */     return instacne;
/*    */   }
/*    */   private LinkedList<EncouterNews> news;
/*    */   private EncouterManager() {
/* 38 */     this.news = new LinkedList<>();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addNews(NewsType type, String player, int treasureId) {
/* 44 */     EncouterNews news = new EncouterNews();
/* 45 */     news.type = type;
/* 46 */     news.player = player;
/* 47 */     news.treasureId = treasureId;
/* 48 */     news.time = CommTime.nowSecond();
/*    */     
/* 50 */     this.news.add(news);
/* 51 */     if (this.news.size() > RefDataMgr.getFactor("Encouter_NewsSize", 30)) {
/* 52 */       this.news.poll();
/*    */     }
/*    */   }
/*    */   
/*    */   public List<EncouterNews> getEncouterNews() {
/* 57 */     return new ArrayList<>(this.news);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/pvp/EncouterManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */