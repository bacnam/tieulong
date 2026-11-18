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
import core.config.refdata.ref.RefGem;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.List;

public class GemAllUp extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GemAllUp.Request req = new Gson().fromJson(message, GemAllUp.Request.class);
        Character character = player.getFeature(CharFeature.class).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", req.charId);
        } else {
            int Size = EquipPos.values().length - 1;
            int Num = 0;
            int totalMaterial = 0;

            for (int i = 0; i < Size * RefDataMgr.size(RefGem.class); i++) {
                int index = 1;

                for (int j = index; j < Size; j++) {
                    if (character.getBo().getGem(index) > character.getBo().getGem(j + 1)) {
                        index = j + 1;
                    }
                }

                int Level = character.getBo().getGem(index);
                if (Level >= RefDataMgr.size(RefGem.class)) {
                    break;
                }

                RefGem ref = RefDataMgr.get(RefGem.class, Level + 1);
                int material = ref.Material;
                totalMaterial += material;
                if (player.getPlayerBO().getGemMaterial() < totalMaterial) {
                    totalMaterial -= material;
                    break;
                }

                character.getBo().setGem(index, Level + 1);
                Num++;
            }

            PlayerCurrency playerCurrency = player.getFeature(PlayerCurrency.class);
            if (!playerCurrency.check(PrizeType.GemMaterial, totalMaterial)) {
                throw new WSException(ErrorCode.NotEnough_GemMaterial, "玩家材料:%s<强化需要材料:%s", player.getPlayerBO().getGemMaterial(), totalMaterial);
            } else {
                playerCurrency.consume(PrizeType.GemMaterial, totalMaterial, ItemFlow.GemLevelUp);
                character.getBo().saveAll();
                character.onAttrChanged();
                player.getFeature(AchievementFeature.class).updateMax(AchievementType.GemMax, character.getBo().getGem(1));
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.GemMax_M1, Num);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.GemMax_M2, Num);
                GemAllUp.SkillNotify notify = new GemAllUp.SkillNotify(character.getCharId(), character.getBo().getGemAll());
                request.response(notify);
            }
        }
    }

    public static class Request {
        int charId;
    }

    public static class SkillNotify {
        int charId;
        List<Integer> GemLevel;

        public SkillNotify(int charId, List<Integer> GemLevel) {
            this.charId = charId;
            this.GemLevel = GemLevel;
        }
    }
}
