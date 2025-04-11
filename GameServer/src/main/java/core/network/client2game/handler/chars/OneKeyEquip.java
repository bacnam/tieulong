package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import business.player.feature.character.Equip;
import business.player.feature.character.EquipFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OneKeyEquip
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        Character character = ((CharFeature) player.getFeature(CharFeature.class)).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[]{Integer.valueOf(req.charId)});
        }

        Map<EquipPos, Equip> bestMap = new HashMap<>();
        EquipFeature feature = (EquipFeature) player.getFeature(EquipFeature.class);
        feature.getAllEquips().forEach(equip -> {
            Character onwer = equip.getOwner();
            if ((equip.getRef()).CharID != paramRequest.charId) {
                return;
            }
            if (onwer != null && onwer != paramCharacter) {
                return;
            }
            if (paramPlayer.getLv() < equip.getLevel()) {
                return;
            }
            List<EquipPos> poslist = equip.getRef().validEquipPos();
            EquipPos weakest = null;
            int power = equip.getBasePower();
            for (EquipPos pos : poslist) {
                Equip cur = (Equip) paramMap.get(pos);
                if (cur == null) {
                    weakest = pos;
                    break;
                }
                if (cur.getBasePower() < power) {
                    weakest = pos;
                    power = cur.getBasePower();
                }
            }
            if (weakest != null)
                paramMap.put(weakest, equip);
        });
        bestMap.forEach((pos, equip) -> {
            if (equip.getOwner() != null) {
                equip.getOwner().unEquip(equip.getPos());
            }

            Equip preEquip = paramCharacter.getEquip(pos);

            if (preEquip != null) {
                paramCharacter.unEquip(pos);
            }
            paramCharacter.equip(equip, pos);
            equip.saveOwner(paramCharacter, pos);
            paramPlayer.pushProto("equipon", new EquipOn.EquipNotify(paramCharacter.getCharId(), pos, equip.getSid()));
        });
        character.onAttrChanged();

        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.OneKeyEquip_M1);
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.OneKeyEquip_M2);
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.OneKeyEquip_M3);
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.OneKeyEquip_M4);
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.OneKeyEquip_M5);

        request.response(new Object());
    }

    class Request {
        int charId;
    }
}

