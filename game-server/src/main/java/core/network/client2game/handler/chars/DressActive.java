package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.character.DressFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.UnlockType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDress;
import core.config.refdata.ref.RefUnlockFunction;
import core.database.game.bo.DressBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class DressActive extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        DressActive.Request req = new Gson().fromJson(message, DressActive.Request.class);
        RefUnlockFunction refunlock = RefDataMgr.get(RefUnlockFunction.class, UnlockType.Dress);
        if (refunlock.UnlockLevel > player.getLv()) {
            throw new WSException(ErrorCode.NotEnough_UnlockCond, "解锁条件不足");
        } else {
            DressFeature feature = player.getFeature(DressFeature.class);
            DressBO bo = feature.getDressByDressId(req.dressId);
            if (bo != null) {
                throw new WSException(ErrorCode.Dress_AlreadyActive, "已激活");
            } else {
                RefDress ref = RefDataMgr.get(RefDress.class, req.dressId);
                if (!player.getFeature(PlayerItem.class).checkAndConsume(ref.Material, ref.Count, ItemFlow.ActiveDress)) {
                    throw new WSException(ErrorCode.NotEnough_Crystal, "材料不足");
                } else {
                    feature.gainAndEquip(req.dressId, 1, ItemFlow.ActiveDress);
                    request.response();
                }
            }
        }
    }

    public static class Request {
        int dressId;
    }
}
