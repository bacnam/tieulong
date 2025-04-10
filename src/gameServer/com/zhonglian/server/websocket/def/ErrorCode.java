package com.zhonglian.server.websocket.def;

public enum ErrorCode
{
Success(0),

Unknown(100),
InvalidParam(101),
Server_NotFound(102),
Server_AlreadyLogin(103),
Server_NotConnected(104),

Request_IncorrectSession(200),
Request_NullResponse(201),
Request_UnknownMesaggeType(202),
Request_RequestTimeout(203),
Request_ServerNotConnected(204),
Request_NotFoundHandler(205),

Login_AlreadyExistPlayer(300),
Login_OnlineLimit(301),
Login_PlayerBanned(302),
Login_ThirdAuthError(303),

Player_NotFound(400),
Player_AlreadyExist(401),
Player_NotLogin(402),
Player_AccessDenied(403),
RechargeOrderFailed(404),
Fight_CheckFailed(405),

Char_NotFound(501),
Lv_NotEnough(502),

Dungeon_WinCD(601),
Dungeon_NotBegin(602),
Dungeon_NotFound(603),
Dungeon_RebirthFull(604),

Equip_NotFound(701),
Equip_WrongPos(702),
Equip_LevelRequired(703),
Equip_NotBelong(704),
Equip_NotRed(705),
Equip_Equiped(706),
Equip_NotEquip(707),

Strengthen_LevelFull(801),
Strengthen_LevelOver(802),

Gem_LevelFull(810),

Star_LevelFull(820),

Wing_LevelFull(830),
Wing_NotChooseWay(831),
Wing_AlreadyActive(832),
Wing_NotFound(833),

Skill_LevelFull(840),
Skill_NotFound(841),

NotEnough_Currency(900),
NotEnough_Crystal(901),
NotEnough_Money(902),
NotEnough_StengthenMaterial(903),
NotEnough_GemMaterial(904),
NotEnough_StarMaterial(905),
NotEnough_MerMaterial(906),
NotEnough_WingMaterial(907),
NotEnough_Exp(908),
NotEnough_WarSpiritLv(909),
NotEnough_WarSpiritTalent(909),

Arena_WrongTarget(1001),
Arena_ChallengeTimesRequired(1002),
Arena_FightInCD(1003),
Arena_FightNotFound(1004),
Arena_RefreshInCD(1005),
Arena_ChallengeTimesFull(1006),
Arena_BuyTimesFull(1007),

StealGold_NotFound(1101),
StealGold_NotEnough(1102),

Dress_NotFound(1201),
Dress_OverTime(1202),
Dress_AlreadyActive(1203),

Marry_SexExit(1201),
Marry_SexNot(1202),
Marry_ApplyFull(1203),
Marry_ApplyFail(1204),
Marry_Already(1205),
Marry_DivorceAlready(1206),
Marry_SignAlready(1207),
Marry_NotYet(1208),
Marry_SignNotEnough(1208),
Marry_SignAlreadyPick(1209),
Marry_SignNotFound(1210),
Marry_LevelNotFound(1211),
Marry_LevelNotEnough(1212),

WorldBoss_NotChallengeTimes(1803),
WorldBoss_FightInCD(1804),
WorldBoss_IsDeath(1806),
WorldBoss_NotOpen(1811),
WorldBoss_InspireFull(1812),
WorldBoss_NotEnoughRank(1813),
WorldBoss_AlreadyAuto(1814),
WorldBoss_NotAuto(1814),

Droiyan_WrongTarget(2001),
Droiyan_FullDroiyan(2002),
Droiyan_TreasureNoFound(2003),
Droiyan_TreasureTimeLimit(2004),
Droiyan_TreasureOpenTimes(2005),

Instance_Locked(3001),
Instance_NotEounghTimes(3002),
Instance_NotEounghMaterial(3003),
Instance_Full(3004),
Instance_NotPassed(3004),

NotFound_Achievement(411),
NotFound_Title(421),
NotActive_Title(422),
AlreadyUse_Title(423),
NotUse_Title(424),
NotEnough_CompleteCount(626),
NotEnough_CompleteLevel(627),
NotEnough_UnlockCond(641),
Already_Worship(701),
NotEnoughFindTimes(702),

Store_RefreshFull(4001),
NotEnough_StoreRefreshCost(4002),
Goods_HasRefresh(4003),
Goods_Soldout(4004),
Goods_NotEnough(4005),
NotFound_RefGoodsInfo(4006),
Goods_PriceLess(4007),

Artifice_Full(5010),
Artifice_NotEnough(5011),

LingBao_Full(5101),
LingBao_NotEnough(5102),

WarSpiritTalentFull(5501),
WarSpiritNotFound(5502),
WarSpiritStarFull(5503),
WarSpiritLevelRequire(5504),

BuyPackage_full(5001),
Package_Full(707),

NotEnough_TaskActiveValue(5002),

Rebirth_LevelFull(7001),
Rebirth_NotEnough(7002),

ALREADY_FETCH(1306),

Signin_AlreadySigned(1501),
Activity_Close(1502),
Not_Enough(1503),
Already_Picked(1504),
NotFound_ActivityAwardId(1505),
DailyRecharge_CanNotReceive(1506),
DailyRecharge_HasReceive(1507),
AccumRecharge_CanNotReceive(1508),
AccumRecharge_HasReceive(1509),
Lottery_NotExist(1510),
Lottery_Error(1511),
NotFound_RefVipAward(1512),
VipAward_WeekNoNowVIPGift(1513),
VipAward_WeekVIPGiftSold(1514),
AccumRechargeDay_AlreadyPick(1515),
AccumRechargeDay_NotFound(1516),
AccumRechargeDay_NotEnough(1517),
SignIn_AlreadyPick(1518),
SignIn_OnlyToday(1519),
SignIn_NotFound(1520),

Packet_NotExist(1520),
Packet_Out(1521),
Packet_TimesOut(1522),
Packet_Picked(1523),

Wheel_NoTimes(1524),

Chat_CdTime(1602),
Chat_Banned(1150),

NotEnough_VIP(2001),
Vipinfo_FetchedAlready(2002),

Guild_ResearchIsLock(6001),
Guild_SkillResearchFull(6002),
Guild_NoResearchByGuildlvLow(6003),
Dirty_Word(6004),
Guild_AlreadyExist(6005),
Guild_UpgradeSkillIsLock(6006),
Guild_SkillUpgradeFull(6007),
NotEnough_GuildDonate(6008),
NotEnough_TeamLevel(6009),
Guild_AlreadyInGuild(6010),
NotFound_Guild(6011),
Guild_DenyAll(6012),
Guild_FullMember(6013),
Guild_InJoinCD(6014),
Guild_IndependentMan(6015),
Guild_PermissionDenied(6016),
Guild_NotMember(6017),
Guild_SacrificeAlready(6018),
NotEnough_GuildSacrificeCost(6019),
Guild_LevelMax(6020),
NotEnough_GuildExp(6021),
Guild_Already(6023),
Guild_SkillNotEnough(6024),
GuildBoss_AlreadyOpen(6101),
GuildBoss_NotenoughLv(6102),
GuildBoss_Lock(6103),
GuildBoss_NotEnoughCoin(6104),
GuildBoss_NotOpen(6105),
GuildBoss_IsDeath(6106),
GuildBoss_NotChallengeTimes(6107),
GuildBoss_NotChallengeBuyTimes(6108),
GuildBoss_NotEnoughTimes(6108),
GuildBoss_NotEnter(6109),

GuildWar_AlreadyOpen(6201),
GuildWar_NotFoundCenter(6202),
GuildWar_NotApply(6203),
GuildWar_NotOpen(6204),
GuildWar_AlreadyApply(6205),
GuildWar_NotFoundFight(6206),
GuildWar_NotAttend(6207),
GuildWar_RebirthCD(6208),
GuildWar_ApplyNotOpen(6209),
GuildWar_ApplyFull(6210),
GuildWar_NotPermit(6211),
GuildWar_LevelLimit(6212),
GuildWar_TimeLimit(6213),

Longnv_NotApply(6301),
Longnv_NotFoundFight(6302),
Longnv_NotPickTime(6303),
Longnv_NotWin(6304),
Longnv_AlreadyPick(6305),

Title_NotFound(6401),
Title_AlreadyFull(6402),
Title_AlreadyActive(6403),
Title_NotLevel(6404),

NotEnough_UseItem(7001);

private short value;

ErrorCode(int value) {
this.value = (short)value;
}

public short value() {
return this.value;
}
}

