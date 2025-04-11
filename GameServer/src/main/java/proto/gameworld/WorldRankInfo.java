package proto.gameworld;

import proto.gamezone.Player;

import java.util.List;

public class WorldRankInfo {
    public int rank;

    public long value;

    public List<RankInfo> rankList;

    public List<RankAward> rewardList;

    public static class RankInfo extends Player.PlayerInfo {
        public int rank;

        public long value;
    }
}

