package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.character.WarSpiritFeature;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefRebirth;
import core.config.refdata.ref.RefWarSpiritLv;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.List;

public class WarSpiritLv
extends PlayerHandler
{
private static class SpiritNotify
{
long warSpiritLv;
long warSpiritExp;
long gainExp;

private SpiritNotify(long rebirthLevel, long rebirthExp, long gainExp) {
this.warSpiritLv = rebirthLevel;
this.warSpiritExp = rebirthExp;
this.gainExp = gainExp;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
int nowLevel = player.getPlayerBO().getWarspiritLv();
if (nowLevel >= RefDataMgr.size(RefRebirth.class) - 1) {
throw new WSException(ErrorCode.Rebirth_LevelFull, "转生已满级");
}

RefWarSpiritLv now_ref = (RefWarSpiritLv)RefDataMgr.get(RefWarSpiritLv.class, Integer.valueOf(nowLevel));
RefWarSpiritLv next_ref = (RefWarSpiritLv)RefDataMgr.get(RefWarSpiritLv.class, Integer.valueOf(nowLevel + 1));
int gainExp = 0;

if (now_ref.Level != next_ref.Level) {
player.getPlayerBO().saveWarspiritLv(nowLevel + 1);
} else {
if (!((PlayerItem)player.getFeature(PlayerItem.class)).checkAndConsume(next_ref.UniformId, next_ref.UniformCount, ItemFlow.WarSpiritLv)) {
throw new WSException(ErrorCode.NotEnough_WarSpiritLv, "玩家材料不够");
}

gainExp = next_ref.GainExp * next_ref.getCrit();
int nowExp = player.getPlayerBO().getWarspiritExp() + gainExp;
rebirthLevelUp(player, now_ref, next_ref, nowExp);
} 

((WarSpiritFeature)player.getFeature(WarSpiritFeature.class)).updatePower();

SpiritNotify notify = new SpiritNotify(player.getPlayerBO().getWarspiritLv(), player.getPlayerBO().getWarspiritExp(), gainExp, null);
request.response(notify);
}

private void rebirthLevelUp(Player player, RefWarSpiritLv now_ref, RefWarSpiritLv next_ref, int nowExp) {
int levelUp = 0;
for (int i = now_ref.id; ((RefWarSpiritLv)RefDataMgr.get(RefWarSpiritLv.class, Integer.valueOf(i))).Star <= ((List)RefWarSpiritLv.sameLevel.get(Integer.valueOf(now_ref.Level))).size(); i++) {
RefWarSpiritLv temp_now_ref = (RefWarSpiritLv)RefDataMgr.get(RefWarSpiritLv.class, Integer.valueOf(i));
RefWarSpiritLv temp_next_ref = (RefWarSpiritLv)RefDataMgr.get(RefWarSpiritLv.class, Integer.valueOf(i + 1));

if (temp_next_ref == null)
break; 
long needExp = temp_next_ref.Exp;

if (needExp > nowExp) {
break;
}
if (temp_now_ref.Level != temp_next_ref.Level) {
break;
}
levelUp++;
nowExp = (int)(nowExp - needExp);
} 

player.getPlayerBO().saveWarspiritExp(nowExp);
player.getPlayerBO().saveWarspiritLv(player.getPlayerBO().getWarspiritLv() + levelUp);
}
}

