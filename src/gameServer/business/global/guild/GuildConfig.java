/*     */ package business.global.guild;
/*     */ 
/*     */ import business.player.Player;
/*     */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*     */ import com.zhonglian.server.common.enums.UnlockType;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefGuildSkillLevel;
/*     */ import core.config.refdata.ref.RefUnlockFunction;
/*     */ import core.config.refdata.ref.RefVIP;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuildConfig
/*     */ {
/*     */   public static int JoinCD() {
/*  18 */     return RefDataMgr.getFactor("Guild_JoinCD", 10800);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int FuncCD() {
/*  25 */     return RefDataMgr.getFactor("Guild_FuncCD", 172800);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int UnlockLevel() {
/*  32 */     return ((RefUnlockFunction)RefDataMgr.get(RefUnlockFunction.class, UnlockType.Guild)).UnlockLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int BoardCD() {
/*  39 */     return RefDataMgr.getFactor("GuildMessageInterval", 5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int MaxBoardMessageCount() {
/*  46 */     return RefDataMgr.getFactor("Guild_MaxBoardMessageCount", 30);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int MaxLogCount() {
/*  53 */     return RefDataMgr.getFactor("Guild_MaxLogCount", 100);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String VicePresidentNotice() {
/*  60 */     return RefDataMgr.getGeneral("Guild_VicePresidentNotice", "我任命{0}为副帮主，请大家多多支持TA！");
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
/*     */   public static int JoinGuildMailID() {
/*  74 */     return RefDataMgr.getFactor("Guild_JoninGuildMailID", 500001);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int KickoutGuildMailID() {
/*  81 */     return RefDataMgr.getFactor("Guild_JoninGuildMailID", 500002);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int ChangeJobGuildMailID() {
/*  88 */     return RefDataMgr.getFactor("Guild_JoninGuildMailID", 500003);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RefGuildSkillLevel getGuildSkillLevel(int skillId, int level) {
/*  99 */     for (RefGuildSkillLevel ref : RefDataMgr.getAll(RefGuildSkillLevel.class).values()) {
/* 100 */       if (ref.SkillID == skillId && ref.SkillLevel == level) {
/* 101 */         return ref;
/*     */       }
/*     */     } 
/* 104 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getSkillMaxLevel(int skillId, int level) {
/* 115 */     int i = 0;
/* 116 */     for (RefGuildSkillLevel ref : RefDataMgr.getAll(RefGuildSkillLevel.class).values()) {
/* 117 */       if (ref.NeedGuildLevel <= level && ref.SkillID == skillId) {
/* 118 */         i++;
/*     */       }
/*     */     } 
/* 121 */     return i - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NumberRange getGuildBossOpenTime() {
/* 132 */     int from = RefDataMgr.getFactor("GuildBossBeginTime", 10);
/* 133 */     int to = RefDataMgr.getFactor("GuildBossEndTime", 22);
/* 134 */     return new NumberRange(from, to);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getGuildBossMaxTime() {
/* 145 */     return RefDataMgr.getFactor("GuildBossChallengeTimes", 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getGuildBossMaxBuyTime(Player player) {
/* 156 */     return ((RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(player.getVipLevel()))).GuildChallengeBuyTimes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getGuildJoinListSize() {
/* 167 */     return RefDataMgr.getFactor("GuildJoinListSize", 20);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getGuildJoinTimeLimit() {
/* 178 */     return RefDataMgr.getFactor("GuildJoinTimeLimit", 10800);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/guild/GuildConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */