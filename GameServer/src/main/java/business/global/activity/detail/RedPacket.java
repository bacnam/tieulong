package business.global.activity.detail;

import business.global.activity.Activity;
import business.global.redpacket.RedPacketMgr;
import business.player.Player;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.websocket.exception.WSException;
import core.database.game.bo.ActivityBO;
import core.network.proto.RedPacketInfo;
import core.network.proto.RedPacketPickInfo;
import java.util.List;

public class RedPacket
extends Activity
{
public RedPacket(ActivityBO bo) {
super(bo);
}

public void load(JsonObject json) throws WSException {}

public void onOpen() {}

public void onEnd() {}

public void onClosed() {}

public ActivityType getType() {
return ActivityType.RedPacket;
}

public List<RedPacketInfo> getPacketList(Player player) {
return RedPacketMgr.getInstance().getList(player);
}

public List<RedPacketPickInfo> getPickList(long id) {
return RedPacketMgr.getInstance().getPickInfo(id);
}

public int pick(long id, Player player) throws WSException {
return RedPacketMgr.getInstance().snatchPacket(id, player);
}

public void handle(int money, Player player) {
if (getStatus() != ActivityStatus.Open) {
return;
}
RedPacketMgr.getInstance().handleRecharge(money, player);
}
}

