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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/proto/gameworld/WorldRankInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */