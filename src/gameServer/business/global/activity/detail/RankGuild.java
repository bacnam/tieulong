/*    */ package business.global.activity.detail;
/*    */ 
/*    */ import business.global.activity.RankActivity;
/*    */ import business.global.gmmail.MailCenter;
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildMgr;
/*    */ import business.global.rank.RankManager;
/*    */ import business.global.rank.Record;
/*    */ import business.player.Player;
/*    */ import business.player.feature.guild.GuildMemberFeature;
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
/*    */ public class RankGuild
/*    */   extends RankActivity
/*    */ {
/*    */   public RankGuild(ActivityBO bo) {
/* 26 */     super(bo);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnd() {
/* 31 */     for (Record record : RankManager.getInstance().getRankList(RankType.Guild, RankManager.getInstance().getRankSize(RankType.Guild))) {
/* 32 */       if (record == null)
/*    */         continue; 
/* 34 */       int rank = record.getRank();
/* 35 */       for (RankActivity.RankAward ref : this.rankrewardList) {
/* 36 */         if (!ref.rankrange.within(rank))
/*    */           continue; 
/* 38 */         Guild guild = GuildMgr.getInstance().getGuild(record.getPid());
/* 39 */         if (guild != null) {
/* 40 */           for (Iterator<Long> iterator = guild.getMembers().iterator(); iterator.hasNext(); ) { long pid = ((Long)iterator.next()).longValue();
/* 41 */             MailCenter.getInstance().sendMail(pid, this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, ref.reward, new String[0]); }
/*    */         
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ActivityType getType() {
/* 51 */     return ActivityType.GuildRank;
/*    */   }
/*    */ 
/*    */   
/*    */   public ConstEnum.VIPGiftType getAwardType() {
/* 56 */     return ConstEnum.VIPGiftType.GuildRank;
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
/* 67 */     ActivityRecordBO bo = new ActivityRecordBO();
/* 68 */     bo.setPid(player.getPid());
/* 69 */     bo.setAid(this.bo.getId());
/* 70 */     bo.setActivity(getType().toString());
/* 71 */     Guild guild = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuild();
/* 72 */     if (guild != null) {
/* 73 */       int power = guild.getPower();
/* 74 */       bo.setExtInt(0, power);
/*    */     } 
/* 76 */     bo.insert();
/* 77 */     return bo;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/RankGuild.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */