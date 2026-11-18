package core.network.client2game.handler.pvp;

import business.global.fight.DroiyanFight;
import business.global.fight.FightFactory;
import business.global.fight.FightManager;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.pvp.DroiyanFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Fight;
import java.io.IOException;

public class DroiyanBeginFight extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        DroiyanBeginFight.Request req = new Gson().fromJson(message, DroiyanBeginFight.Request.class);
        if (!req.revenge) {
            if (!player.getFeature(DroiyanFeature.class).popDroiyan(req.targetPid)) {
                throw new WSException(ErrorCode.Droiyan_WrongTarget, "玩家[%s]没有遭遇[%s]无法攻击", player.getPid(), req.targetPid);
            }
        } else if (!player.getFeature(DroiyanFeature.class).popEnemy(req.targetPid)) {
            throw new WSException(ErrorCode.Droiyan_WrongTarget, "玩家[%s]没有仇人[%s]无法攻击", player.getPid(), req.targetPid);
        }

        DroiyanFeature atk = player.getFeature(DroiyanFeature.class);
        DroiyanFeature def = PlayerMgr.getInstance().getPlayer(req.targetPid).getFeature(DroiyanFeature.class);
        DroiyanFight fight = FightFactory.createFight(atk, def, req.revenge);
        int fightid = FightManager.getInstance().pushFight(fight);
        request.response(new Fight.Begin(fightid));
    }

    private static class Request {
        long targetPid;
        boolean revenge = false;
    }
}
