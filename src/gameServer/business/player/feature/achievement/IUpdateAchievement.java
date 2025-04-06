package business.player.feature.achievement;

import core.config.refdata.ref.RefAchievement;
import core.database.game.bo.AchievementBO;

public interface IUpdateAchievement {
  void update(AchievementBO paramAchievementBO, RefAchievement paramRefAchievement, Integer... paramVarArgs);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/achievement/IUpdateAchievement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */