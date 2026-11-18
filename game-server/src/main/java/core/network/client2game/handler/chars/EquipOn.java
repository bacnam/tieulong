package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import business.player.feature.character.Equip;
import business.player.feature.character.EquipFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class EquipOn extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        EquipOn.Request req = new Gson().fromJson(message, EquipOn.Request.class);
        EquipFeature feature = player.getFeature(EquipFeature.class);
        Character character = player.getFeature(CharFeature.class).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", req.charId);
        } else {
            if (req.equipSid != 0L) {
                feature.equipOn(req.equipSid, req.pos, req.charId, character);
            } else {
                Equip preEquip = character.getEquip(req.pos);
                if (preEquip != null) {
                    character.unEquip(req.pos);
                    preEquip.saveOwner(null, req.pos);
                }
            }

            character.onAttrChanged();
            EquipOn.EquipNotify notify = new EquipOn.EquipNotify(character.getCharId(), req.pos, req.equipSid);
            request.response(notify);
        }
    }

    public static class EquipNotify {
        int charId;
        EquipPos pos;
        long equipSid;

        public EquipNotify(int charId, EquipPos pos, long equipSid) {
            this.charId = charId;
            this.pos = pos;
            this.equipSid = equipSid;
        }
    }

    public static class Request {
        long equipSid;
        int charId;
        EquipPos pos;
    }
}
