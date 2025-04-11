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
import core.config.refdata.ref.RefGem;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class GemUp
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        Character character = ((CharFeature) player.getFeature(CharFeature.class)).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[]{Integer.valueOf(req.charId)});
        }

        int GemLevel = character.getBo().getGem(req.pos.ordinal());
        if (GemLevel >= RefDataMgr.size(RefGem.class)) {
            throw new WSException(ErrorCode.Gem_LevelFull, "位置[%s]宝石已满级", new Object[]{req.pos});
        }

        RefGem ref = (RefGem) RefDataMgr.get(RefGem.class, Integer.valueOf(GemLevel + 1));
        PlayerCurrency playerCurrency = (PlayerCurrency) player.getFeature(PlayerCurrency.class);

        if (!playerCurrency.check(PrizeType.GemMaterial, ref.Material)) {
            throw new WSException(ErrorCode.NotEnough_GemMaterial, "玩家材料:%s<宝石升级需要材料:%s", new Object[]{Integer.valueOf(player.getPlayerBO().getGemMaterial()), Integer.valueOf(ref.Material)});
        }

        playerCurrency.consume(PrizeType.GemMaterial, ref.Material, ItemFlow.GemLevelUp);

        character.getBo().saveGem(req.pos.ordinal(), character.getBo().getGem(req.pos.ordinal()) + 1);

        character.onAttrChanged();

        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.GemMax, Integer.valueOf(character.getBo().getGem(req.pos.ordinal())));
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.GemMax_M1);
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.GemMax_M2);

        GemupNotify notify = new GemupNotify(character.getCharId(), req.pos, character.getBo().getGem(req.pos.ordinal()));
        request.response(notify);
    }

    public static class Request {
        int charId;
        EquipPos pos;
    }

    public static class GemupNotify {
        int charId;
        EquipPos pos;
        long GemLevel;

        public GemupNotify(int charId, EquipPos pos, long GemLevel) {
            this.charId = charId;
            this.pos = pos;
            this.GemLevel = GemLevel;
        }
    }
}

