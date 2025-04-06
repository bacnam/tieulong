/*     */ package com.zhonglian.server.logger.flow;
/*     */ 
/*     */ public enum ItemFlow
/*     */ {
/*   5 */   Unkwon(0),
/*   6 */   Command(1001),
/*   7 */   Simulate(1002),
/*     */   
/*   9 */   PlayerCreate(2001),
/*  10 */   Recharge(2002),
/*  11 */   Mail(2003),
/*  12 */   Achievement(2004),
/*  13 */   Offline(2005),
/*  14 */   UnlockChar(2006),
/*     */   
/*  16 */   StrengthenLevelUp(2101),
/*  17 */   GemLevelUp(2102),
/*  18 */   StarLevelUp(2103),
/*  19 */   MerLevelUp(2104),
/*  20 */   WingLevelUp(2105),
/*  21 */   SkillLevelUp(2106),
/*  22 */   WingActive(2107),
/*  23 */   MakeRedEquip(2110),
/*  24 */   ArtificeEquip(2111),
/*  25 */   LingBao(2121),
/*     */   
/*  27 */   Smelt(2200),
/*  28 */   StealGold(2201),
/*     */   
/*  30 */   ActiveDress(2302),
/*     */   
/*  32 */   MarryApply(2401),
/*  33 */   MarryAllReward(2402),
/*  34 */   MarryReward(2403),
/*  35 */   MarryDivorce(2404),
/*  36 */   MarrySign(2405),
/*     */   
/*  38 */   TitleUpgrade(2501),
/*  39 */   TitleActive(2502),
/*     */   
/*  41 */   AutoFightWorldboss(2601),
/*     */   
/*  43 */   Dungeon_Win(3001),
/*  44 */   Dungeon_BossWin(3002),
/*  45 */   Dungeon_Skip(3003),
/*  46 */   Dungeon_Rebirth(3004),
/*     */   
/*  48 */   WarSpiritLv(3010),
/*  49 */   WarSpiritTalent(3011),
/*  50 */   UnlockWarSpirit(3012),
/*  51 */   WarSpiritStar(3013),
/*     */   
/*  53 */   Guild_SkillUpgrade(3101),
/*  54 */   Guild_Create(3102),
/*  55 */   Guild_Sacrifice(3103),
/*  56 */   Guild_GuildSacrifice(3104),
/*  57 */   Guild_OpenBoss(3205),
/*  58 */   Guild_BuyTimes(3206),
/*  59 */   Guild_Boss(3207),
/*  60 */   GuildWar_Person(3301),
/*  61 */   GuildWar_Rebirth(3302),
/*     */   
/*  63 */   Longnv_Rebirth(3401),
/*  64 */   Longnv_Win(3402),
/*     */   
/*  66 */   ArenaFight(4001),
/*  67 */   ArenaClearFightCD(4002),
/*  68 */   ArenaAddChallenge(4003),
/*  69 */   DroiyanFight(4101),
/*  70 */   DroiyanClearRed(4102),
/*  71 */   DroiyanTreasure(4103),
/*  72 */   DroiyanSearch(4104),
/*  73 */   WorldBoss_DamageReward(4501),
/*  74 */   WorldBoss_KillReward(4502),
/*  75 */   WorldBoss_Inspiring(4503),
/*  76 */   WorldBoss_Resurrection(4504),
/*  77 */   InstanceWin(4601),
/*     */   
/*  79 */   UseGenericItem(5001),
/*     */   
/*  81 */   Instance(6001),
/*  82 */   AddInstanceChallenge(6002),
/*     */   
/*  84 */   RefreshStore(7001),
/*  85 */   BuyItem(7002),
/*  86 */   FindTreasure(7101),
/*     */   
/*  88 */   BuyPackage(7201),
/*     */   
/*  90 */   TaskActiveGain(7202),
/*     */   
/*  92 */   Activity_SignInDaily(7203),
/*     */   
/*  94 */   Activity_SignInSeven(7204),
/*  95 */   Rebirth(7204),
/*  96 */   RankReward(7205),
/*  97 */   Activity_DailyRecharge(7206),
/*  98 */   Activity_AccumRecharge(7207),
/*  99 */   VIP_PrivilegeGift(7210),
/* 100 */   ActivityInstance(7211),
/* 101 */   Worship(7212),
/* 102 */   Lottery(7213),
/* 103 */   Activity_WeeklyVipAward(7214),
/* 104 */   DrawPrize(7214),
/* 105 */   ReSign(7215),
/* 106 */   AccumRechargeDay(7216),
/* 107 */   LevelReward(7217),
/* 108 */   RedPacket(7218),
/* 109 */   FortuneWheel(7219);
/*     */   
/*     */   private int value;
/*     */ 
/*     */   
/*     */   ItemFlow(int value) {
/* 115 */     this.value = value;
/*     */   }
/*     */   
/*     */   public int value() {
/* 119 */     return this.value; } public static ItemFlow itemFlow(int value) {
/*     */     byte b;
/*     */     int i;
/*     */     ItemFlow[] arrayOfItemFlow;
/* 123 */     for (i = (arrayOfItemFlow = values()).length, b = 0; b < i; ) { ItemFlow flow = arrayOfItemFlow[b];
/* 124 */       if (flow.value == value)
/* 125 */         return flow; 
/*     */       b++; }
/*     */     
/* 128 */     return Unkwon;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/logger/flow/ItemFlow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */