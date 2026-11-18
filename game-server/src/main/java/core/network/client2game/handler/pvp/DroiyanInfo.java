package core.network.client2game.handler.pvp;

import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import business.player.feature.pvp.DroiyanFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.DroiyanBO;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DroiyanInfo extends PlayerHandler {
    private static PlayerMgr mananer;

    @Override
    public void handle(business.player.Player player, WebSocketRequest request, String message) throws WSException, IOException {
        DroiyanFeature feature = player.getFeature(DroiyanFeature.class);
        if (mananer == null) {
            mananer = PlayerMgr.getInstance();
        }

        feature.checkDroiyan();
        List<Player.Summary> fihgters = new ArrayList<>();

        for (long pid : feature.getBo().getFightersAll()) {
            if (pid != 0L) {
                business.player.Player tar = mananer.getPlayer(pid);
                Player.Summary summary = tar.getFeature(PlayerBase.class).summary();
                if (tar.getFeature(DroiyanFeature.class).haveTreature()) {
                    summary.name = "神秘玩家";
                }

                fihgters.add(summary);
            }
        }

        request.response(new DroiyanInfo.Response(feature.getBo(), feature.getHpMap(), fihgters));
    }

    private static class Response {
        int point;
        int red;
        int lastSearchTime;
        List<Player.Summary> fihgters;
        Map<Integer, Integer> hp;

        public Response(DroiyanBO bo, Map<Integer, Integer> hp, List<Player.Summary> fihgters) {
            this.point = bo.getPoint();
            this.red = bo.getRed();
            this.lastSearchTime = bo.getLastSearchTime();
            this.fihgters = fihgters;
            this.hp = hp;
        }
    }
}
