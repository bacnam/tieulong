package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import business.player.feature.character.Equip;
import business.player.feature.character.EquipFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OneKeyEquip extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        OneKeyEquip.Request req = new Gson().fromJson(message, OneKeyEquip.Request.class);
        Character character = player.getFeature(CharFeature.class).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", req.charId);
        } else {
            Map<EquipPos, Equip> bestMap = new HashMap<>();
            EquipFeature feature = player.getFeature(EquipFeature.class);
            feature.getAllEquips().forEach(equip -> {
                Character onwer = equip.getOwner();
                if (equip.getRef().CharID == req.charId) {
                    if (onwer == null || onwer == character) {
                        if (player.getLv() >= equip.getLevel()) {
                            List<EquipPos> poslist = equip.getRef().validEquipPos();
                            EquipPos weakest = null;
                            int power = equip.getBasePower();

                            for (EquipPos pos : poslist) {
                                Equip cur = bestMap.get(pos);
                                if (cur == null) {
                                    weakest = pos;
                                    break;
                                }

                                if (cur.getBasePower() < power) {
                                    weakest = pos;
                                    power = cur.getBasePower();
                                }
                            }

                            if (weakest != null) {
                                bestMap.put(weakest, equip);
                            }
                        }
                    }
                }
            });
            bestMap.forEach((pos, equip) -> {
                if (equip.getOwner() != null) {
                    equip.getOwner().unEquip(equip.getPos());
                }

                Equip preEquip = character.getEquip(pos);
                if (preEquip != null) {
                    character.unEquip(pos);
                }

                character.equip(equip, pos);
                equip.saveOwner(character, pos);
                player.pushProto("equipon", new EquipOn.EquipNotify(character.getCharId(), pos, equip.getSid()));
            });
            character.onAttrChanged();
            player.getFeature(AchievementFeature.class).updateInc(AchievementType.OneKeyEquip_M1);
            player.getFeature(AchievementFeature.class).updateInc(AchievementType.OneKeyEquip_M2);
            player.getFeature(AchievementFeature.class).updateInc(AchievementType.OneKeyEquip_M3);
            player.getFeature(AchievementFeature.class).updateInc(AchievementType.OneKeyEquip_M4);
            player.getFeature(AchievementFeature.class).updateInc(AchievementType.OneKeyEquip_M5);
            request.response(new Object());
        }
    }

    class Request {
        int charId;
    }
}
