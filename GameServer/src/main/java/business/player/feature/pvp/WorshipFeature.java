package business.player.feature.pvp;

import business.player.Player;
import business.player.feature.Feature;
import com.zhonglian.server.common.db.BM;
import core.database.game.bo.WorshipBO;

public class WorshipFeature extends Feature {
private WorshipBO bo;

public WorshipFeature(Player player) {
super(player);
}

public void loadDB() {
this.bo = (WorshipBO)BM.getBM(WorshipBO.class).findOne("pid", Long.valueOf(getPid()));
}

public WorshipBO getOrCreate() {
WorshipBO bo = this.bo;
if (bo != null) {
return bo;
}
synchronized (this) {
bo = this.bo;
if (bo != null) {
return bo;
}
bo = new WorshipBO();
bo.setPid(this.player.getPid());
bo.insert();
this.bo = bo;
} 
return bo;
}

public int addTimes(int ranktype) {
WorshipBO bo = getOrCreate();
bo.saveWorshipTimes(ranktype, this.bo.getWorshipTimes(ranktype) + 1);
return getTimes(ranktype);
}

public int getTimes(int ranktype) {
WorshipBO bo = getOrCreate();
return bo.getWorshipTimes(ranktype);
}

public void beWorshiped(int ranktype) {
WorshipBO bo = getOrCreate();
bo.saveBeWorshipTimes(ranktype, this.bo.getBeWorshipTimes(ranktype) + 1);
}

public WorshipBO getBO() {
return getOrCreate();
}
}

