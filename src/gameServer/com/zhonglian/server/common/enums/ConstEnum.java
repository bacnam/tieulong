/*     */ package com.zhonglian.server.common.enums;
/*     */ 
/*     */ public class ConstEnum
/*     */ {
/*     */   public enum BuffType
/*     */   {
/*     */   
/*     */   }
/*     */   
/*     */   public enum AchieveClassify {
/*  11 */     Serial,
/*  12 */     Single;
/*     */   }
/*     */   
/*     */   public enum FindTreasureType
/*     */   {
/*  17 */     single,
/*  18 */     Ten;
/*     */   }
/*     */   
/*     */   public enum AchieveReset
/*     */   {
/*  23 */     None,
/*  24 */     EveryWeek,
/*  25 */     EveryDay;
/*     */   }
/*     */   
/*     */   public enum BoxMatcherType
/*     */   {
/*  30 */     Specific,
/*  31 */     PerN,
/*  32 */     Any;
/*     */   }
/*     */   
/*     */   public enum DailyRefresh
/*     */   {
/*  37 */     None,
/*  38 */     ArenaChallenge,
/*  39 */     ArenaResetFightCD,
/*  40 */     ArenaResetRefreshCD,
/*  41 */     DroiyanSearch,
/*  42 */     EquipInstanceBuyTimes,
/*  43 */     MeridianInstanceBuyTimes,
/*  44 */     GemInstanceBuyTimes,
/*  45 */     ArenaBuyChallengeTimes,
/*  46 */     PackageBuyTimes,
/*  47 */     StealGold,
/*  48 */     GuildWarRebirth,
/*  49 */     GuildSacrifice,
/*  50 */     LoversSend,
/*  51 */     GuildwarInspire,
/*  52 */     DungeonRebirth,
/*  53 */     LongnvDonate,
/*  54 */     LongnvCrystal,
/*  55 */     LongnvRebirth,
/*  56 */     AutoFightWorldboss,
/*  57 */     OnlineSecond,
/*  58 */     OpenTreasure,
/*  59 */     DailyRecharge,
/*  60 */     RedPacket;
/*     */   }
/*     */ 
/*     */   
/*     */   public enum DiscountType
/*     */   {
/*  66 */     None,
/*  67 */     Item,
/*  68 */     ItemBox,
/*  69 */     CardChip,
/*  70 */     Card,
/*  71 */     Equip,
/*  72 */     EquipChip,
/*  73 */     Rune,
/*  74 */     Gem,
/*  75 */     Signet,
/*  76 */     Pet,
/*  77 */     PetChip;
/*     */   }
/*     */   
/*     */   public enum GoodsUnLockType
/*     */   {
/*  82 */     None,
/*  83 */     GuildLevel,
/*  84 */     TowerStar,
/*  85 */     ArenaRank,
/*  86 */     TeamLevel;
/*     */   }
/*     */   
/*     */   public enum KickOutType
/*     */   {
/*  91 */     None,
/*  92 */     KickOut_PlayerBanned,
/*  93 */     KickOut_OtherDevicceLogin;
/*     */   }
/*     */   
/*     */   public enum LevelType
/*     */   {
/*  98 */     None,
/*  99 */     NORMAL,
/* 100 */     BOSS,
/* 101 */     FEEDER,
/* 102 */     BOX;
/*     */   }
/*     */   
/*     */   public enum MailType
/*     */   {
/* 107 */     None,
/* 108 */     Message,
/* 109 */     Prize,
/* 110 */     Custom,
/* 111 */     CustomReward;
/*     */   }
/*     */   
/*     */   public enum TaskClassify
/*     */   {
/* 116 */     MainTask,
/* 117 */     Achievement,
/* 118 */     DailyTask,
/* 119 */     ActivityTask,
/* 120 */     Competitive,
/* 121 */     TJCompetitive,
/* 122 */     ZoneArena;
/*     */   }
/*     */   
/*     */   public enum UniverseMessageFormat
/*     */   {
/* 127 */     None,
/* 128 */     CommonAnounce,
/* 129 */     Marquee;
/*     */   }
/*     */   
/*     */   public enum UniverseMessageType
/*     */   {
/* 134 */     None,
/* 135 */     CommonAnounce,
/* 136 */     BeginGame,
/* 137 */     FirstCharge,
/* 138 */     KillWorldBoss,
/* 139 */     Strengthen,
/* 140 */     Wing,
/* 141 */     UnlockChar,
/* 142 */     Gift,
/* 143 */     DroiyanRedName,
/* 144 */     DropTreasure,
/* 145 */     RobTreasure,
/* 146 */     UseTreasure,
/* 147 */     FindTreasure,
/* 148 */     MakeRedEquip,
/* 149 */     DressActive,
/*     */ 
/*     */     
/* 152 */     OpenGuildBoss,
/* 153 */     VipLogin,
/* 154 */     PowerLogin,
/* 155 */     Artifice,
/* 156 */     Richman,
/* 157 */     DroiyanTop,
/*     */     
/* 159 */     ArenaFirstChange,
/*     */     
/* 161 */     NewMarry,
/* 162 */     LongnvUnderAttack,
/* 163 */     LongnvWin;
/*     */   }
/*     */   
/*     */   public enum VIPGiftType
/*     */   {
/* 168 */     None,
/* 169 */     Daily,
/* 170 */     Weekly,
/* 171 */     LevelRank,
/* 172 */     WingRank,
/* 173 */     DroiyanRank,
/* 174 */     DungeonRank,
/* 175 */     PowerRank,
/* 176 */     ArtificeRank,
/* 177 */     GuildRank;
/*     */   }
/*     */ 
/*     */   
/*     */   public enum RankRewardType
/*     */   {
/* 183 */     None,
/* 184 */     WingRank,
/* 185 */     DungeonRank,
/* 186 */     LevelRank,
/* 187 */     PowerRank,
/* 188 */     DroiyanRank,
/* 189 */     ArenaRank,
/* 190 */     GumuRank,
/* 191 */     TianLongRank,
/* 192 */     XiaoyaoRank,
/* 193 */     ArtificeRank,
/* 194 */     GuildRank,
/* 195 */     WorldRecharge,
/* 196 */     WorldConsume,
/* 197 */     WorldTreasure;
/*     */   }
/*     */   
/*     */   public enum ResOpType
/*     */   {
/* 202 */     None,
/* 203 */     Gain,
/* 204 */     Lose;
/*     */   }
/*     */   
/*     */   public enum ResdisType
/*     */   {
/* 209 */     None,
/* 210 */     gain,
/* 211 */     lose;
/*     */   }
/*     */   
/*     */   public enum LotteryType
/*     */   {
/* 216 */     None,
/* 217 */     normal,
/* 218 */     rich;
/*     */   }
/*     */   
/*     */   public enum SexType
/*     */   {
/* 223 */     None,
/* 224 */     Man,
/* 225 */     Women;
/*     */   }
/*     */   
/*     */   public enum MarryType
/*     */   {
/* 230 */     None,
/* 231 */     Single,
/* 232 */     Married;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/enums/ConstEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */