package com.zhonglian.server.common.enums;

public class ConstEnum {
    public enum BuffType {

    }

    public enum AchieveClassify {
        Serial,
        Single;
    }

    public enum FindTreasureType {
        single,
        Ten;
    }

    public enum AchieveReset {
        None,
        EveryWeek,
        EveryDay;
    }

    public enum BoxMatcherType {
        Specific,
        PerN,
        Any;
    }

    public enum DailyRefresh {
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

    public enum DiscountType {
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

    public enum GoodsUnLockType {
        None,
        GuildLevel,
        TowerStar,
        ArenaRank,
        TeamLevel;
    }

    public enum KickOutType {
        None,
        KickOut_PlayerBanned,
        KickOut_OtherDevicceLogin;
    }

    public enum LevelType {
        None,
        NORMAL,
        BOSS,
        FEEDER,
        BOX;
    }

    public enum MailType {
        None,
        Message,
        Prize,
        Custom,
        CustomReward;
    }

    public enum TaskClassify {
        MainTask,
        Achievement,
        DailyTask,
        ActivityTask,
        Competitive,
        TJCompetitive,
        ZoneArena;
    }

    public enum UniverseMessageFormat {
        None,
        CommonAnounce,
        Marquee;
    }

    public enum UniverseMessageType {
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

    public enum VIPGiftType {
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

    public enum RankRewardType {
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

    public enum ResOpType {
        None,
        Gain,
        Lose;
    }

    public enum ResdisType {
        None,
        gain,
        lose;
    }

    public enum LotteryType {
        None,
        normal,
        rich;
    }

    public enum SexType {
        None,
        Man,
        Women;
    }

    public enum MarryType {
        None,
        Single,
        Married;
    }
}

