package business.player.feature.player;

import core.config.refdata.ref.RefTitle;
import core.database.game.bo.TitleBO;

public interface IUpdateTitle {
  void update(TitleBO paramTitleBO, RefTitle paramRefTitle, Integer... paramVarArgs);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/player/IUpdateTitle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */