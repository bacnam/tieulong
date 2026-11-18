package com.zhonglian.server.common.enums;

public class ConstEnum {
    public static enum AchieveClassify {
        Serial,
        Single;
    }

    public static enum AchieveReset {
        None,
        EveryWeek,
        EveryDay;
    }

    public static enum BoxMatcherType {
        Specific,
        PerN,
        Any;
    }

    public static enum BuffType {
    }

    public static enum DailyRefresh {
        None,
        ArenaChallenge,
        ArenaResetFightCD,
        ArenaResetRefreshCD,
        DroiyanSearch,
        EquipInstanceBuyTimes,
        MeridianInstanceBuyTimes,
        GemInstanceBuyTimes,
        ArenaBuyChallengeTimes,
        PackageBuyTimes,
        StealGold,
        GuildWarRebirth,
        GuildSacrifice,
        LoversSend,
        GuildwarInspire,
        DungeonRebirth,
        LongnvDonate,
        LongnvCrystal,
        LongnvRebirth,
        AutoFightWorldboss,
        OnlineSecond,
        OpenTreasure,
        DailyRecharge,
        RedPacket;
    }

    public static enum DiscountType {
        None,
        Item,
        ItemBox,
        CardChip,
        Card,
        Equip,
        EquipChip,
        Rune,
        Gem,
        Signet,
        Pet,
        PetChip;
    }

    public static enum FindTreasureType {
        single,
        Ten;
    }

    public static enum GoodsUnLockType {
        None,
        GuildLevel,
        TowerStar,
        ArenaRank,
        TeamLevel;
    }

    public static enum KickOutType {
        None,
        KickOut_PlayerBanned,
        KickOut_OtherDevicceLogin;
    }

    public static enum LevelType {
        None,
        NORMAL,
        BOSS,
        FEEDER,
        BOX;
    }

    public static enum LotteryType {
        None,
        normal,
        rich;
    }

    public static enum MailType {
        None,
        Message,
        Prize,
        Custom,
        CustomReward;
    }

    public static enum MarryType {
        None,
        Single,
        Married;
    }

    public static enum RankRewardType {
        None,
        WingRank,
        DungeonRank,
        LevelRank,
        PowerRank,
        DroiyanRank,
        ArenaRank,
        GumuRank,
        TianLongRank,
        XiaoyaoRank,
        ArtificeRank,
        GuildRank,
        WorldRecharge,
        WorldConsume,
        WorldTreasure;
    }

    public static enum ResOpType {
        None,
        Gain,
        Lose;
    }

    public static enum ResdisType {
        None,
        gain,
        lose;
    }

    public static enum SexType {
        None,
        Man,
        Women;
    }

    public static enum TaskClassify {
        MainTask,
        Achievement,
        DailyTask,
        ActivityTask,
        Competitive,
        TJCompetitive,
        ZoneArena;
    }

    public static enum UniverseMessageFormat {
        None,
        CommonAnounce,
        Marquee;
    }

    public static enum UniverseMessageType {
        None,
        CommonAnounce,
        BeginGame,
        FirstCharge,
        KillWorldBoss,
        Strengthen,
        Wing,
        UnlockChar,
        Gift,
        DroiyanRedName,
        DropTreasure,
        RobTreasure,
        UseTreasure,
        FindTreasure,
        MakeRedEquip,
        DressActive,
        OpenGuildBoss,
        VipLogin,
        PowerLogin,
        Artifice,
        Richman,
        DroiyanTop,
        ArenaFirstChange,
        NewMarry,
        LongnvUnderAttack,
        LongnvWin;
    }

    public static enum VIPGiftType {
        None,
        Daily,
        Weekly,
        LevelRank,
        WingRank,
        DroiyanRank,
        DungeonRank,
        PowerRank,
        ArtificeRank,
        GuildRank;
    }
}
