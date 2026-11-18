package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import business.player.feature.character.DressFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.DressType;
import com.zhonglian.server.common.enums.UnlockType;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefUnlockFunction;
import core.database.game.bo.DressBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class DressOn extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        DressOn.Request req = new Gson().fromJson(message, DressOn.Request.class);
        RefUnlockFunction refunlock = RefDataMgr.get(RefUnlockFunction.class, UnlockType.Dress);
        if (refunlock.UnlockLevel > player.getLv()) {
            throw new WSException(ErrorCode.NotEnough_UnlockCond, "解锁条件不足");
        } else {
            DressFeature feature = player.getFeature(DressFeature.class);
            Character character = player.getFeature(CharFeature.class).getCharacter(req.charId);
            if (character == null) {
                throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", req.charId);
            } else {
                if (req.sid != 0L) {
                    feature.Active(req.sid, req.type, req.charId, character);
                } else {
                    DressBO dressbo = character.getDress(req.type);
                    if (dressbo != null) {
                        character.unEquipDress(req.type);
                    }
                }

                character.onAttrChanged();
                DressOn.DressNotify notify = new DressOn.DressNotify(character.getCharId(), req.type, req.sid);
                player.getFeature(CharFeature.class).updateCharPower();
                request.response(notify);
            }
        }
    }

    public static class DressNotify {
        int charId;
        DressType type;
        long sid;

        public DressNotify(int charId, DressType type, long sid) {
            this.charId = charId;
            this.type = type;
            this.sid = sid;
        }
    }

    public static class Request {
        long sid;
        int charId;
        DressType type;
    }
}
