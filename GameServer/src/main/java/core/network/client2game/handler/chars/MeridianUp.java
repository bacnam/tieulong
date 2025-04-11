package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefMeridian;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class MeridianUp
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        Character character = ((CharFeature) player.getFeature(CharFeature.class)).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[]{Integer.valueOf(req.charId)});
        }

        int nowLevel = character.getBo().getMeridian();
        if (nowLevel >= RefDataMgr.size(RefMeridian.class) - 1) {
            throw new WSException(ErrorCode.Strengthen_LevelFull, "经脉已满级");
        }
        RefMeridian ref = (RefMeridian) RefDataMgr.get(RefMeridian.class, Integer.valueOf(nowLevel + 1));

        PlayerCurrency playerCurrency = (PlayerCurrency) player.getFeature(PlayerCurrency.class);
        if (!playerCurrency.check(PrizeType.MerMaterial, ref.Material)) {
            throw new WSException(ErrorCode.NotEnough_MerMaterial, "玩家材料:%s<升级材料:%s", new Object[]{Integer.valueOf(player.getPlayerBO().getMerMaterial()), Integer.valueOf(ref.Material)});
        }

        playerCurrency.consume(PrizeType.MerMaterial, ref.Material, ItemFlow.MerLevelUp);

        character.getBo().saveMeridian(nowLevel + 1);

        character.onAttrChanged();

        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.MeridianTotal);
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.MeridianTotal_M1);
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.MeridianTotal_M2);
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.MeridianTotal_M3);

        MeridianNotify notify = new MeridianNotify(req.charId, character.getBo().getMeridian());
        request.response(notify);
    }

    public static class Request {
        int charId;
    }

    public static class MeridianNotify {
        int charId;
        long MeridianLevel;

        public MeridianNotify(int charId, long MeridianLevel) {
            this.charId = charId;
            this.MeridianLevel = MeridianLevel;
        }
    }
}

