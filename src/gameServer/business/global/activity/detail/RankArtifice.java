/*    */ package business.global.activity.detail;
/*    */ 
/*    */ import business.global.activity.RankActivity;
/*    */ import business.global.gmmail.MailCenter;
/*    */ import business.global.rank.RankManager;
/*    */ import business.global.rank.Record;
/*    */ import business.player.Player;
/*    */ import business.player.feature.character.CharFeature;
/*    */ import business.player.feature.character.Character;
/*    */ import com.zhonglian.server.common.enums.ActivityType;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.enums.RankType;
/*    */ import core.database.game.bo.ActivityBO;
/*    */ import core.database.game.bo.ActivityRecordBO;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RankArtifice
/*    */   extends RankActivity
/*    */ {
/*    */   public RankArtifice(ActivityBO bo) {
/* 25 */     super(bo);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnd() {
/* 30 */     for (Record record : RankManager.getInstance().getRankList(RankType.Artifice, RankManager.getInstance().getRankSize(RankType.Artifice))) {
/* 31 */       if (record == null)
/*    */         continue; 
/* 33 */       int rank = record.getRank();
/*    */       
/* 35 */       for (RankActivity.RankAward ref : this.rankrewardList) {
/* 36 */         if (!ref.rankrange.within(rank))
/*    */           continue; 
/* 38 */         MailCenter.getInstance().sendMail(record.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, ref.reward, new String[0]);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ActivityType getType() {
/* 46 */     return ActivityType.ArtificeRank;
/*    */   }
/*    */ 
/*    */   
/*    */   public ConstEnum.VIPGiftType getAwardType() {
/* 51 */     return ConstEnum.VIPGiftType.ArtificeRank;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ActivityRecordBO createPlayerActRecord(Player player) {
/* 62 */     ActivityRecordBO bo = new ActivityRecordBO();
/* 63 */     bo.setPid(player.getPid());
/* 64 */     bo.setAid(this.bo.getId());
/* 65 */     bo.setActivity(getType().toString());
/* 66 */     int artificeLevel = 0;
/* 67 */     for (Character charac : ((CharFeature)player.getFeature(CharFeature.class)).getAll().values()) {
/* 68 */       for (Iterator<Integer> iterator = charac.getBo().getArtificeAll().iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/* 69 */         artificeLevel += i; }
/*    */     
/*    */     } 
/* 72 */     bo.setExtInt(0, artificeLevel);
/* 73 */     bo.insert();
/* 74 */     return bo;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/RankArtifice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */