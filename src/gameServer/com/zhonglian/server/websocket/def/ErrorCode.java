/*     */ package com.zhonglian.server.websocket.def;
/*     */ 
/*     */ public enum ErrorCode
/*     */ {
/*   5 */   Success(0),
/*     */ 
/*     */   
/*   8 */   Unknown(100),
/*   9 */   InvalidParam(101),
/*  10 */   Server_NotFound(102),
/*  11 */   Server_AlreadyLogin(103),
/*  12 */   Server_NotConnected(104),
/*     */ 
/*     */   
/*  15 */   Request_IncorrectSession(200),
/*  16 */   Request_NullResponse(201),
/*  17 */   Request_UnknownMesaggeType(202),
/*  18 */   Request_RequestTimeout(203),
/*  19 */   Request_ServerNotConnected(204),
/*  20 */   Request_NotFoundHandler(205),
/*     */ 
/*     */   
/*  23 */   Login_AlreadyExistPlayer(300),
/*  24 */   Login_OnlineLimit(301),
/*  25 */   Login_PlayerBanned(302),
/*  26 */   Login_ThirdAuthError(303),
/*     */ 
/*     */   
/*  29 */   Player_NotFound(400),
/*  30 */   Player_AlreadyExist(401),
/*  31 */   Player_NotLogin(402),
/*  32 */   Player_AccessDenied(403),
/*  33 */   RechargeOrderFailed(404),
/*  34 */   Fight_CheckFailed(405),
/*     */ 
/*     */   
/*  37 */   Char_NotFound(501),
/*  38 */   Lv_NotEnough(502),
/*     */ 
/*     */   
/*  41 */   Dungeon_WinCD(601),
/*  42 */   Dungeon_NotBegin(602),
/*  43 */   Dungeon_NotFound(603),
/*  44 */   Dungeon_RebirthFull(604),
/*     */ 
/*     */   
/*  47 */   Equip_NotFound(701),
/*  48 */   Equip_WrongPos(702),
/*  49 */   Equip_LevelRequired(703),
/*  50 */   Equip_NotBelong(704),
/*  51 */   Equip_NotRed(705),
/*  52 */   Equip_Equiped(706),
/*  53 */   Equip_NotEquip(707),
/*     */ 
/*     */   
/*  56 */   Strengthen_LevelFull(801),
/*  57 */   Strengthen_LevelOver(802),
/*     */ 
/*     */   
/*  60 */   Gem_LevelFull(810),
/*     */ 
/*     */   
/*  63 */   Star_LevelFull(820),
/*     */   
/*  65 */   Wing_LevelFull(830),
/*  66 */   Wing_NotChooseWay(831),
/*  67 */   Wing_AlreadyActive(832),
/*  68 */   Wing_NotFound(833),
/*     */ 
/*     */   
/*  71 */   Skill_LevelFull(840),
/*  72 */   Skill_NotFound(841),
/*     */   
/*  74 */   NotEnough_Currency(900),
/*  75 */   NotEnough_Crystal(901),
/*  76 */   NotEnough_Money(902),
/*  77 */   NotEnough_StengthenMaterial(903),
/*  78 */   NotEnough_GemMaterial(904),
/*  79 */   NotEnough_StarMaterial(905),
/*  80 */   NotEnough_MerMaterial(906),
/*  81 */   NotEnough_WingMaterial(907),
/*  82 */   NotEnough_Exp(908),
/*  83 */   NotEnough_WarSpiritLv(909),
/*  84 */   NotEnough_WarSpiritTalent(909),
/*     */ 
/*     */   
/*  87 */   Arena_WrongTarget(1001),
/*  88 */   Arena_ChallengeTimesRequired(1002),
/*  89 */   Arena_FightInCD(1003),
/*  90 */   Arena_FightNotFound(1004),
/*  91 */   Arena_RefreshInCD(1005),
/*  92 */   Arena_ChallengeTimesFull(1006),
/*  93 */   Arena_BuyTimesFull(1007),
/*     */ 
/*     */   
/*  96 */   StealGold_NotFound(1101),
/*  97 */   StealGold_NotEnough(1102),
/*     */ 
/*     */   
/* 100 */   Dress_NotFound(1201),
/* 101 */   Dress_OverTime(1202),
/* 102 */   Dress_AlreadyActive(1203),
/*     */ 
/*     */   
/* 105 */   Marry_SexExit(1201),
/* 106 */   Marry_SexNot(1202),
/* 107 */   Marry_ApplyFull(1203),
/* 108 */   Marry_ApplyFail(1204),
/* 109 */   Marry_Already(1205),
/* 110 */   Marry_DivorceAlready(1206),
/* 111 */   Marry_SignAlready(1207),
/* 112 */   Marry_NotYet(1208),
/* 113 */   Marry_SignNotEnough(1208),
/* 114 */   Marry_SignAlreadyPick(1209),
/* 115 */   Marry_SignNotFound(1210),
/* 116 */   Marry_LevelNotFound(1211),
/* 117 */   Marry_LevelNotEnough(1212),
/*     */ 
/*     */   
/* 120 */   WorldBoss_NotChallengeTimes(1803),
/* 121 */   WorldBoss_FightInCD(1804),
/* 122 */   WorldBoss_IsDeath(1806),
/* 123 */   WorldBoss_NotOpen(1811),
/* 124 */   WorldBoss_InspireFull(1812),
/* 125 */   WorldBoss_NotEnoughRank(1813),
/* 126 */   WorldBoss_AlreadyAuto(1814),
/* 127 */   WorldBoss_NotAuto(1814),
/*     */ 
/*     */   
/* 130 */   Droiyan_WrongTarget(2001),
/* 131 */   Droiyan_FullDroiyan(2002),
/* 132 */   Droiyan_TreasureNoFound(2003),
/* 133 */   Droiyan_TreasureTimeLimit(2004),
/* 134 */   Droiyan_TreasureOpenTimes(2005),
/*     */ 
/*     */   
/* 137 */   Instance_Locked(3001),
/* 138 */   Instance_NotEounghTimes(3002),
/* 139 */   Instance_NotEounghMaterial(3003),
/* 140 */   Instance_Full(3004),
/* 141 */   Instance_NotPassed(3004),
/*     */ 
/*     */   
/* 144 */   NotFound_Achievement(411),
/* 145 */   NotFound_Title(421),
/* 146 */   NotActive_Title(422),
/* 147 */   AlreadyUse_Title(423),
/* 148 */   NotUse_Title(424),
/* 149 */   NotEnough_CompleteCount(626),
/* 150 */   NotEnough_CompleteLevel(627),
/* 151 */   NotEnough_UnlockCond(641),
/* 152 */   Already_Worship(701),
/* 153 */   NotEnoughFindTimes(702),
/*     */ 
/*     */   
/* 156 */   Store_RefreshFull(4001),
/* 157 */   NotEnough_StoreRefreshCost(4002),
/* 158 */   Goods_HasRefresh(4003),
/* 159 */   Goods_Soldout(4004),
/* 160 */   Goods_NotEnough(4005),
/* 161 */   NotFound_RefGoodsInfo(4006),
/* 162 */   Goods_PriceLess(4007),
/*     */ 
/*     */   
/* 165 */   Artifice_Full(5010),
/* 166 */   Artifice_NotEnough(5011),
/*     */ 
/*     */   
/* 169 */   LingBao_Full(5101),
/* 170 */   LingBao_NotEnough(5102),
/*     */ 
/*     */   
/* 173 */   WarSpiritTalentFull(5501),
/* 174 */   WarSpiritNotFound(5502),
/* 175 */   WarSpiritStarFull(5503),
/* 176 */   WarSpiritLevelRequire(5504),
/*     */ 
/*     */   
/* 179 */   BuyPackage_full(5001),
/* 180 */   Package_Full(707),
/*     */ 
/*     */   
/* 183 */   NotEnough_TaskActiveValue(5002),
/*     */ 
/*     */   
/* 186 */   Rebirth_LevelFull(7001),
/* 187 */   Rebirth_NotEnough(7002),
/*     */   
/* 189 */   ALREADY_FETCH(1306),
/*     */ 
/*     */   
/* 192 */   Signin_AlreadySigned(1501),
/* 193 */   Activity_Close(1502),
/* 194 */   Not_Enough(1503),
/* 195 */   Already_Picked(1504),
/* 196 */   NotFound_ActivityAwardId(1505),
/* 197 */   DailyRecharge_CanNotReceive(1506),
/* 198 */   DailyRecharge_HasReceive(1507),
/* 199 */   AccumRecharge_CanNotReceive(1508),
/* 200 */   AccumRecharge_HasReceive(1509),
/* 201 */   Lottery_NotExist(1510),
/* 202 */   Lottery_Error(1511),
/* 203 */   NotFound_RefVipAward(1512),
/* 204 */   VipAward_WeekNoNowVIPGift(1513),
/* 205 */   VipAward_WeekVIPGiftSold(1514),
/* 206 */   AccumRechargeDay_AlreadyPick(1515),
/* 207 */   AccumRechargeDay_NotFound(1516),
/* 208 */   AccumRechargeDay_NotEnough(1517),
/* 209 */   SignIn_AlreadyPick(1518),
/* 210 */   SignIn_OnlyToday(1519),
/* 211 */   SignIn_NotFound(1520),
/*     */   
/* 213 */   Packet_NotExist(1520),
/* 214 */   Packet_Out(1521),
/* 215 */   Packet_TimesOut(1522),
/* 216 */   Packet_Picked(1523),
/*     */   
/* 218 */   Wheel_NoTimes(1524),
/*     */ 
/*     */   
/* 221 */   Chat_CdTime(1602),
/* 222 */   Chat_Banned(1150),
/*     */ 
/*     */   
/* 225 */   NotEnough_VIP(2001),
/* 226 */   Vipinfo_FetchedAlready(2002),
/*     */ 
/*     */   
/* 229 */   Guild_ResearchIsLock(6001),
/* 230 */   Guild_SkillResearchFull(6002),
/* 231 */   Guild_NoResearchByGuildlvLow(6003),
/* 232 */   Dirty_Word(6004),
/* 233 */   Guild_AlreadyExist(6005),
/* 234 */   Guild_UpgradeSkillIsLock(6006),
/* 235 */   Guild_SkillUpgradeFull(6007),
/* 236 */   NotEnough_GuildDonate(6008),
/* 237 */   NotEnough_TeamLevel(6009),
/* 238 */   Guild_AlreadyInGuild(6010),
/* 239 */   NotFound_Guild(6011),
/* 240 */   Guild_DenyAll(6012),
/* 241 */   Guild_FullMember(6013),
/* 242 */   Guild_InJoinCD(6014),
/* 243 */   Guild_IndependentMan(6015),
/* 244 */   Guild_PermissionDenied(6016),
/* 245 */   Guild_NotMember(6017),
/* 246 */   Guild_SacrificeAlready(6018),
/* 247 */   NotEnough_GuildSacrificeCost(6019),
/* 248 */   Guild_LevelMax(6020),
/* 249 */   NotEnough_GuildExp(6021),
/* 250 */   Guild_Already(6023),
/* 251 */   Guild_SkillNotEnough(6024),
/* 252 */   GuildBoss_AlreadyOpen(6101),
/* 253 */   GuildBoss_NotenoughLv(6102),
/* 254 */   GuildBoss_Lock(6103),
/* 255 */   GuildBoss_NotEnoughCoin(6104),
/* 256 */   GuildBoss_NotOpen(6105),
/* 257 */   GuildBoss_IsDeath(6106),
/* 258 */   GuildBoss_NotChallengeTimes(6107),
/* 259 */   GuildBoss_NotChallengeBuyTimes(6108),
/* 260 */   GuildBoss_NotEnoughTimes(6108),
/* 261 */   GuildBoss_NotEnter(6109),
/*     */   
/* 263 */   GuildWar_AlreadyOpen(6201),
/* 264 */   GuildWar_NotFoundCenter(6202),
/* 265 */   GuildWar_NotApply(6203),
/* 266 */   GuildWar_NotOpen(6204),
/* 267 */   GuildWar_AlreadyApply(6205),
/* 268 */   GuildWar_NotFoundFight(6206),
/* 269 */   GuildWar_NotAttend(6207),
/* 270 */   GuildWar_RebirthCD(6208),
/* 271 */   GuildWar_ApplyNotOpen(6209),
/* 272 */   GuildWar_ApplyFull(6210),
/* 273 */   GuildWar_NotPermit(6211),
/* 274 */   GuildWar_LevelLimit(6212),
/* 275 */   GuildWar_TimeLimit(6213),
/*     */   
/* 277 */   Longnv_NotApply(6301),
/* 278 */   Longnv_NotFoundFight(6302),
/* 279 */   Longnv_NotPickTime(6303),
/* 280 */   Longnv_NotWin(6304),
/* 281 */   Longnv_AlreadyPick(6305),
/*     */   
/* 283 */   Title_NotFound(6401),
/* 284 */   Title_AlreadyFull(6402),
/* 285 */   Title_AlreadyActive(6403),
/* 286 */   Title_NotLevel(6404),
/*     */   
/* 288 */   NotEnough_UseItem(7001);
/*     */ 
/*     */   
/*     */   private short value;
/*     */ 
/*     */   
/*     */   ErrorCode(int value) {
/* 295 */     this.value = (short)value;
/*     */   }
/*     */   
/*     */   public short value() {
/* 299 */     return this.value;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/def/ErrorCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */