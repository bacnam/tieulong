package business.global.arena;

import core.config.refdata.RefDataMgr;

public class ArenaConfig
{
public static int fightTime() {
return RefDataMgr.getFactor("Arena_FightTime", 90);
}

public static int opponentsSize() {
return RefDataMgr.getFactor("Arena_OpponentsSize", 4);
}

public static int opponentsOffset() {
return RefDataMgr.getFactor("Arena_OpponentsOffset", 5);
}

public static int showSize() {
return RefDataMgr.getFactor("Arena_ShowSize", 30);
}

public static int maxChallengeTimes() {
return RefDataMgr.getFactor("Arena_MaxChallengeTimes", 10);
}

public static int fightCD() {
return RefDataMgr.getFactor("Arena_FightCD", 60);
}

public static int winnerToken() {
return RefDataMgr.getFactor("Arena_WinnerToken", 150);
}

public static int winnerGold() {
return RefDataMgr.getFactor("Arena_WinnerGold", 3000);
}

public static int loserToken() {
return RefDataMgr.getFactor("Arena_LoserToken", 50);
}

public static int refreshCD() {
return RefDataMgr.getFactor("Arena_RefreshCD", 30);
}

public static int resetRefreshCDCost() {
return RefDataMgr.getFactor("Arena_resetRefreshCD", 10);
}

public static int resetFightCDCost() {
return RefDataMgr.getFactor("Arena_resetFightCD", 5);
}
}

