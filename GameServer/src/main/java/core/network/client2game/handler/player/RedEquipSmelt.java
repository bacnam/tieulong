package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.character.Equip;
import business.player.feature.character.EquipFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.Quality;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefSmelt;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class RedEquipSmelt
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        EquipFeature feature = (EquipFeature) player.getFeature(EquipFeature.class);
        Equip equip = feature.getEquip(req.equipSid);
        if (equip == null) {
            throw new WSException(ErrorCode.Equip_NotFound, "装备[%s]不存在", new Object[]{Long.valueOf(req.equipSid)});
        }
        if (equip.getRef().getQuality() != Quality.Red) {
            throw new WSException(ErrorCode.Equip_NotRed, "装备[%s]不是神装", new Object[]{Long.valueOf(req.equipSid)});
        }
        if (equip.getOwner() != null) {
            throw new WSException(ErrorCode.Equip_Equiped, "装备在身上不能熔炼", new Object[]{Long.valueOf(req.equipSid)});
        }
        feature.consume(equip);
        RefSmelt refsmelt = (RefSmelt) RefDataMgr.get(RefSmelt.class, String.valueOf(equip.getRef().getQuality().toString()) + equip.getLevel());
        PlayerCurrency playerCurrency = (PlayerCurrency) player.getFeature(PlayerCurrency.class);
        playerCurrency.gain(PrizeType.RedPiece, refsmelt.RedPiece, ItemFlow.Smelt);
        request.response();
    }

    public static class Request {
        long equipSid;
    }
}

