package proto.gameworld;

import java.util.List;
import proto.gamezone.Player;

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

