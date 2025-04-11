package core.network.client2game.handler.pvp;

import business.global.fight.BossFight;
import business.global.fight.FightManager;
import business.player.Player;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.FightResult;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Fight;

import java.io.IOException;

public class DungeonBossWin
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Fight.End fightend = (Fight.End) (new Gson()).fromJson(message, Fight.End.class);
        BossFight fight = (BossFight) FightManager.getInstance().popFight(fightend.fightId);
        if (fight == null) {
            throw new WSException(ErrorCode.Arena_FightNotFound, "玩家[%s]提交的战斗[%s]不存在", new Object[]{Long.valueOf(player.getPid()), Integer.valueOf(fightend.fightId)});
        }
        fight.initAttr();
        fight.check(fightend.checks);
        Reward reward = (Reward) fight.settle(FightResult.Win);
        request.response(reward);
    }
}

