package business.player.feature.achievement;

import core.config.refdata.ref.RefAchievement;
import core.database.game.bo.AchievementBO;

public interface IUpdateAchievement {
    void update(AchievementBO var1, RefAchievement var2, Integer... var3);
}
