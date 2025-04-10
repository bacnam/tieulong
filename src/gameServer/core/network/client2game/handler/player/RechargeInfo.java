package core.network.client2game.handler.player;

import business.global.recharge.RechargeMgr;
import business.player.Player;
import business.player.feature.features.RechargeFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.PlayerRechargeRecordBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RechargeInfo
extends PlayerHandler
{
private static RechargeMgr rechargeManager = null;

private static class Recharged {
private String goodsID;
private int lastBuyTime;
private int buyCount;

private Recharged() {}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
if (rechargeManager == null) {
rechargeManager = RechargeMgr.getInstance();
}
Collection<PlayerRechargeRecordBO> recharge = ((RechargeFeature)player.getFeature(RechargeFeature.class)).getRecharged();
List<Recharged> rtn = new ArrayList<>();
for (PlayerRechargeRecordBO recordBO : recharge) {

if (rechargeManager.getResetSign(recordBO.getGoodsID()).equals(recordBO.getResetSign())) {
Recharged recharged = new Recharged(null);
recharged.goodsID = recordBO.getGoodsID();
recharged.buyCount = recordBO.getBuyCount();
recharged.lastBuyTime = recordBO.getLastBuyTime();
rtn.add(recharged);
} 
} 
request.response(rtn);
}
}

