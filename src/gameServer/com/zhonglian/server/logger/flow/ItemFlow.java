package com.zhonglian.server.logger.flow;

public enum ItemFlow
{
Unkwon(0),
Command(1001),
Simulate(1002),

PlayerCreate(2001),
Recharge(2002),
Mail(2003),
Achievement(2004),
Offline(2005),
UnlockChar(2006),

StrengthenLevelUp(2101),
GemLevelUp(2102),
StarLevelUp(2103),
MerLevelUp(2104),
WingLevelUp(2105),
SkillLevelUp(2106),
WingActive(2107),
MakeRedEquip(2110),
ArtificeEquip(2111),
LingBao(2121),

Smelt(2200),
StealGold(2201),

ActiveDress(2302),

MarryApply(2401),
MarryAllReward(2402),
MarryReward(2403),
MarryDivorce(2404),
MarrySign(2405),

TitleUpgrade(2501),
TitleActive(2502),

AutoFightWorldboss(2601),

Dungeon_Win(3001),
Dungeon_BossWin(3002),
Dungeon_Skip(3003),
Dungeon_Rebirth(3004),

WarSpiritLv(3010),
WarSpiritTalent(3011),
UnlockWarSpirit(3012),
WarSpiritStar(3013),

Guild_SkillUpgrade(3101),
Guild_Create(3102),
Guild_Sacrifice(3103),
Guild_GuildSacrifice(3104),
Guild_OpenBoss(3205),
Guild_BuyTimes(3206),
Guild_Boss(3207),
GuildWar_Person(3301),
GuildWar_Rebirth(3302),

Longnv_Rebirth(3401),
Longnv_Win(3402),

ArenaFight(4001),
ArenaClearFightCD(4002),
ArenaAddChallenge(4003),
DroiyanFight(4101),
DroiyanClearRed(4102),
DroiyanTreasure(4103),
DroiyanSearch(4104),
WorldBoss_DamageReward(4501),
WorldBoss_KillReward(4502),
WorldBoss_Inspiring(4503),
WorldBoss_Resurrection(4504),
InstanceWin(4601),

UseGenericItem(5001),

Instance(6001),
AddInstanceChallenge(6002),

RefreshStore(7001),
BuyItem(7002),
FindTreasure(7101),

BuyPackage(7201),

TaskActiveGain(7202),

Activity_SignInDaily(7203),

Activity_SignInSeven(7204),
Rebirth(7204),
RankReward(7205),
Activity_DailyRecharge(7206),
Activity_AccumRecharge(7207),
VIP_PrivilegeGift(7210),
ActivityInstance(7211),
Worship(7212),
Lottery(7213),
Activity_WeeklyVipAward(7214),
DrawPrize(7214),
ReSign(7215),
AccumRechargeDay(7216),
LevelReward(7217),
RedPacket(7218),
FortuneWheel(7219);

private int value;

ItemFlow(int value) {
this.value = value;
}

public int value() {
return this.value; } public static ItemFlow itemFlow(int value) {
byte b;
int i;
ItemFlow[] arrayOfItemFlow;
for (i = (arrayOfItemFlow = values()).length, b = 0; b < i; ) { ItemFlow flow = arrayOfItemFlow[b];
if (flow.value == value)
return flow; 
b++; }

return Unkwon;
}
}

