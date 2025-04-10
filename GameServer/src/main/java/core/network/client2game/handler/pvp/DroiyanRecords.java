package core.network.client2game.handler.pvp;

import business.player.Player;
import business.player.feature.pvp.DroiyanFeature;
import com.zhonglian.server.common.enums.FightResult;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.DroiyanRecordBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DroiyanRecords
extends PlayerHandler
{
private static class Record
{
int time;
String name;
FightResult result;
int point;
int gold;
int exp;
int treasure;

public Record(DroiyanRecordBO bo) {
this.time = bo.getTime();
this.name = bo.getTargetName();
this.result = FightResult.values()[bo.getResult()];
this.point = bo.getPoint();
this.gold = bo.getGold();
this.exp = bo.getExp();
this.treasure = bo.getTreasure();
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
List<DroiyanRecordBO> list = ((DroiyanFeature)player.getFeature(DroiyanFeature.class)).getRecords();
List<Record> rtn = new ArrayList<>();
for (DroiyanRecordBO bo : list) {
rtn.add(new Record(bo));
}
request.response(rtn);
}
}

