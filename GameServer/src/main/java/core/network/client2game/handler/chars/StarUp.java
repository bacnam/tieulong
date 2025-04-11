package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefStarInfo;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class StarUp
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        Character character = ((CharFeature) player.getFeature(CharFeature.class)).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[]{Integer.valueOf(req.charId)});
        }

        int StarLevel = character.getBo().getStar(req.pos.ordinal());
        if (StarLevel >= RefDataMgr.size(RefStarInfo.class)) {
            throw new WSException(ErrorCode.Star_LevelFull, "位置[%s]升星已满级", new Object[]{req.pos});
        }

        RefStarInfo ref = (RefStarInfo) RefDataMgr.get(RefStarInfo.class, Integer.valueOf(StarLevel + 1));

        PlayerCurrency playerCurrency = (PlayerCurrency) player.getFeature(PlayerCurrency.class);
        if (!playerCurrency.check(PrizeType.Gold, ref.Gold)) {
            throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", new Object[]{Integer.valueOf(player.getPlayerBO().getGold()), Integer.valueOf(ref.Gold)});
        }

        if (!playerCurrency.check(PrizeType.StarMaterial, ref.Material)) {
            throw new WSException(ErrorCode.NotEnough_StarMaterial, "玩家材料:%s<强化需要材料:%s", new Object[]{Integer.valueOf(player.getPlayerBO().getStarMaterial()), Integer.valueOf(ref.Material)});
        }

        playerCurrency.consume(PrizeType.Gold, ref.Gold, ItemFlow.StarLevelUp);

        playerCurrency.consume(PrizeType.StarMaterial, ref.Material, ItemFlow.StarLevelUp);

        character.getBo().saveStar(req.pos.ordinal(), character.getBo().getStar(req.pos.ordinal()) + 1);

        character.onAttrChanged();

        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.StarUp);

        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.StarMax, Integer.valueOf(character.getBo().getStar(req.pos.ordinal())));
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.StarMax_M1, Integer.valueOf(character.getBo().getStar(req.pos.ordinal())));
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.StarMax_M2, Integer.valueOf(character.getBo().getStar(req.pos.ordinal())));

        StarLevelNotify notify = new StarLevelNotify(character.getCharId(), req.pos, character.getBo().getStar(req.pos.ordinal()));
        request.response(notify);
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

