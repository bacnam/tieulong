package business.player.feature;

import BaseCommon.CommLog;
import business.player.Player;
import business.player.PlayerMgr;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.utils.CommTime;
import core.database.game.bo.AccountBO;
import core.logger.flow.FlowLogger;

public class AccountFeature extends Feature {
public AccountBO bo;

public AccountFeature(Player owner) {
super(owner);
}

public void loadDB() {
this.bo = (AccountBO)BM.getBM(AccountBO.class).findOne("open_id", this.player.getOpenId());
if (this.bo == null) {
this.bo = new AccountBO();
this.bo.setOpenId(this.player.getOpenId());
this.bo.setUsername(this.player.getName());
this.bo.setCreateTime(this.player.getPlayerBO().getCreateTime());
this.bo.insert();
} 
}

enum LoginState {
NONE, CREATE, LOGIN, LOGOUT, CHANGE;
}

public AccountBO getAccount() {
return this.bo;
}

public void updateCreateRole() {
AccountBO bo = getAccount();

JsonObject playerJson = PlayerMgr.getInstance().getPlayerJson(bo.getOpenId());
try {
if (playerJson.get("channelId") != null)
bo.setAdFrom(playerJson.get("channelId").getAsString()); 
} catch (Exception e) {
CommLog.warn("发生错误:{}", e.toString());
} finally {
bo.saveAll();
} 

FlowLogger.createRoleLog(bo.getOpenId(), this.player.getPid(), CommTime.nowSecond(), bo.getAdFrom(), this.player.getSid());
}
}

