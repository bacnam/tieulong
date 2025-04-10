package core.network.proto;

import core.database.game.bo.NewtitleBO;

public class TitleInfo {
int titleId;
int level;
boolean isUsing;

public TitleInfo(NewtitleBO bo) {
this.titleId = bo.getTitleId();
this.level = bo.getLevel();
this.isUsing = bo.getIsUsing();
}

public int getTitleId() {
return this.titleId;
}

public void setTitleId(int titleId) {
this.titleId = titleId;
}

public int getLevel() {
return this.level;
}

public void setLevel(int level) {
this.level = level;
}

public boolean isUsing() {
return this.isUsing;
}

public void setUsing(boolean isUsing) {
this.isUsing = isUsing;
}
}

