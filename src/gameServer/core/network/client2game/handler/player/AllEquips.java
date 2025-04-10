package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.character.Equip;
import business.player.feature.character.EquipFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.EquipMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllEquips
extends PlayerHandler
{
private int limit = 200;

private static class Response
{
int now;
int max;
List<EquipMessage> equips;

private Response(int now, int max, List<EquipMessage> equips) {
this.now = now;
this.max = max;
this.equips = equips;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
List<Equip> equips = ((EquipFeature)player.getFeature(EquipFeature.class)).getAllEquips();
int now = 1;
int Max = equips.size() / (this.limit + 1) + 1;
request.response(Integer.valueOf(Max));
List<EquipMessage> rtn = new ArrayList<>();
for (Equip bo : equips) {
if (rtn.size() >= this.limit) {
player.pushProto("allEquips", new Response(now, Max, rtn, null));
now++;
rtn = new ArrayList<>();
} 
rtn.add(new EquipMessage(bo.getBo()));
} 
if (rtn.size() != 0)
player.pushProto("allEquips", new Response(now, Max, rtn, null)); 
}
}

