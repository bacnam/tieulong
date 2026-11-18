package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefStrengthenInfo;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.List;

public class StrengthenAllUp extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        StrengthenAllUp.Request req = new Gson().fromJson(message, StrengthenAllUp.Request.class);
        Character character = player.getFeature(CharFeature.class).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", req.charId);
        } else if (character.getEquips().size() == 0) {
            throw new WSException(ErrorCode.Equip_NotEquip, "角色[%s]没穿装备", req.charId);
        } else {
            int Size = EquipPos.values().length - 1;
            int Num = 0;
            int totalMoney = 0;
            int totalMaterial = 0;

            for (int i = 0; i < Size * player.getLv(); i++) {
                int index = 1;
                if (character.getEquip(EquipPos.values()[index]) == null) {
                    index++;
                }

                for (int j = index; j < Size; j++) {
                    if (character.getEquip(EquipPos.values()[j + 1]) != null && character.getBo().getStrengthen(index) > character.getBo().getStrengthen(j + 1)
                        )
                     {
                        index = j + 1;
                    }
                }

                int Level = character.getBo().getStrengthen(index);
                if (Level >= player.getLv() || Level >= RefDataMgr.size(RefStrengthenInfo.class)) {
                    break;
                }

                RefStrengthenInfo ref = RefDataMgr.get(RefStrengthenInfo.class, Level + 1);
                int goldRequired = ref.Gold;
                int material = ref.Material;
                totalMoney += goldRequired;
                totalMaterial += material;
                if (player.getPlayerBO().getGold() < totalMoney || player.getPlayerBO().getStrengthenMaterial() < totalMaterial) {
                    totalMoney -= goldRequired;
                    totalMaterial -= material;
                    break;
                }

                character.getBo().setStrengthen(index, Level + 1);
                Num++;
            }

            PlayerCurrency playerCurrency = player.getFeature(PlayerCurrency.class);
            if (!playerCurrency.check(PrizeType.Gold, totalMoney)) {
                throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", player.getPlayerBO().getGold(), totalMoney);
            } else if (!playerCurrency.check(PrizeType.StrengthenMaterial, totalMaterial)) {
                throw new WSException(ErrorCode.NotEnough_StengthenMaterial, "玩家材料:%s<强化需要材料:%s", player.getPlayerBO().getStrengthenMaterial(), totalMaterial);
            } else {
                playerCurrency.consume(PrizeType.Gold, totalMoney, ItemFlow.StrengthenLevelUp);
                playerCurrency.consume(PrizeType.StrengthenMaterial, totalMaterial, ItemFlow.StrengthenLevelUp);
                character.getBo().saveAll();
                character.onAttrChanged();
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.Strengthen, Num);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.Strengthen_M1, Num);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.Strengthen_M2, Num);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.Strengthen_M3, Num);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.StrengthTotal, Num);
                StrengthenAllUp.SkillNotify notify = new StrengthenAllUp.SkillNotify(character.getCharId(), character.getBo().getStrengthenAll());
                request.response(notify);
            }
        }
    }

    public static class Request {
        int charId;
    }

    public static class SkillNotify {
        int charId;
        List<Integer> StrengthenLevel;

        public SkillNotify(int charId, List<Integer> StrengthenLevel) {
            this.charId = charId;
            this.StrengthenLevel = StrengthenLevel;
        }
    }
}
