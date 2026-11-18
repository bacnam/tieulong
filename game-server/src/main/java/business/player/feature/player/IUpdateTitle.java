package business.player.feature.player;

import core.config.refdata.ref.RefTitle;
import core.database.game.bo.TitleBO;

public interface IUpdateTitle {
    void update(TitleBO var1, RefTitle var2, Integer... var3);
}
