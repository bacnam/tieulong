package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.character.WarSpirit;
import business.player.feature.character.WarSpiritFeature;
import com.google.gson.Gson;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefWarSpirit;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.WarSpiritInfo;
import java.io.IOException;

public class WarSpiritActive extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        WarSpiritActive.Request req = new Gson().fromJson(message, WarSpiritActive.Request.class);
        RefWarSpirit ref = RefDataMgr.get(RefWarSpirit.class, req.spiritId);
        if (ref == null) {
            throw new WSException(ErrorCode.WarSpiritTalentFull, "战灵未找到");
        } else if (ref.ActiveId != 0 && !player.getFeature(PlayerItem.class).check(ref.ActiveId, ref.ActiveCount)) {
            throw new WSException(ErrorCode.NotEnough_Currency, "材料不足");
        } else {
            int id = player.getFeature(WarSpiritFeature.class).unlockWarSpirit(req.spiritId);
            player.getFeature(PlayerItem.class).consume(ref.ActiveId, ref.ActiveCount, ItemFlow.UnlockWarSpirit);
            WarSpirit WarSpirit = player.getFeature(WarSpiritFeature.class).getWarSpirit(id);
            request.response(new WarSpiritInfo(WarSpirit));
        }
    }

    public static class Request {
        int spiritId;
    }
}
