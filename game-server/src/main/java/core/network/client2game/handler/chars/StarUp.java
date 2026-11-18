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
import core.config.refdata.ref.RefStarInfo;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class StarUp extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        StarUp.Request req = new Gson().fromJson(message, StarUp.Request.class);
        Character character = player.getFeature(CharFeature.class).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", req.charId);
        } else {
            int StarLevel = character.getBo().getStar(req.pos.ordinal());
            if (StarLevel >= RefDataMgr.size(RefStarInfo.class)) {
                throw new WSException(ErrorCode.Star_LevelFull, "位置[%s]升星已满级", req.pos);
            } else {
                RefStarInfo ref = RefDataMgr.get(RefStarInfo.class, StarLevel + 1);
                PlayerCurrency playerCurrency = player.getFeature(PlayerCurrency.class);
                if (!playerCurrency.check(PrizeType.Gold, ref.Gold)) {
                    throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", player.getPlayerBO().getGold(), ref.Gold);
                } else if (!playerCurrency.check(PrizeType.StarMaterial, ref.Material)) {
                    throw new WSException(ErrorCode.NotEnough_StarMaterial, "玩家材料:%s<强化需要材料:%s", player.getPlayerBO().getStarMaterial(), ref.Material);
                } else {
                    playerCurrency.consume(PrizeType.Gold, ref.Gold, ItemFlow.StarLevelUp);
                    playerCurrency.consume(PrizeType.StarMaterial, ref.Material, ItemFlow.StarLevelUp);
                    character.getBo().saveStar(req.pos.ordinal(), character.getBo().getStar(req.pos.ordinal()) + 1);
                    character.onAttrChanged();
                    player.getFeature(AchievementFeature.class).updateInc(AchievementType.StarUp);
                    player.getFeature(AchievementFeature.class).updateMax(AchievementType.StarMax, character.getBo().getStar(req.pos.ordinal()));
                    player.getFeature(AchievementFeature.class).updateInc(AchievementType.StarMax_M1, character.getBo().getStar(req.pos.ordinal()));
                    player.getFeature(AchievementFeature.class).updateInc(AchievementType.StarMax_M2, character.getBo().getStar(req.pos.ordinal()));
                    StarUp.StarLevelNotify notify = new StarUp.StarLevelNotify(character.getCharId(), req.pos, character.getBo().getStar(req.pos.ordinal()));
                    request.response(notify);
                }
            }
        }
    }

    public static class Request {
        int charId;
        EquipPos pos;
    }

    public static class StarLevelNotify {
        int charId;
        EquipPos pos;
        long StarLevel;

        public StarLevelNotify(int charId, EquipPos pos, long StarLevel) {
            this.charId = charId;
            this.pos = pos;
            this.StarLevel = StarLevel;
        }
    }
}
