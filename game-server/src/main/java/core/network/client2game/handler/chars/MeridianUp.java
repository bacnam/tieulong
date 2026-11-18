package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefMeridian;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class MeridianUp extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        MeridianUp.Request req = new Gson().fromJson(message, MeridianUp.Request.class);
        Character character = player.getFeature(CharFeature.class).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", req.charId);
        } else {
            int nowLevel = character.getBo().getMeridian();
            if (nowLevel >= RefDataMgr.size(RefMeridian.class) - 1) {
                throw new WSException(ErrorCode.Strengthen_LevelFull, "经脉已满级");
            } else {
                RefMeridian ref = RefDataMgr.get(RefMeridian.class, nowLevel + 1);
                PlayerCurrency playerCurrency = player.getFeature(PlayerCurrency.class);
                if (!playerCurrency.check(PrizeType.MerMaterial, ref.Material)) {
                    throw new WSException(ErrorCode.NotEnough_MerMaterial, "玩家材料:%s<升级材料:%s", player.getPlayerBO().getMerMaterial(), ref.Material);
                } else {
                    playerCurrency.consume(PrizeType.MerMaterial, ref.Material, ItemFlow.MerLevelUp);
                    character.getBo().saveMeridian(nowLevel + 1);
                    character.onAttrChanged();
                    player.getFeature(AchievementFeature.class).updateInc(AchievementType.MeridianTotal);
                    player.getFeature(AchievementFeature.class).updateInc(AchievementType.MeridianTotal_M1);
                    player.getFeature(AchievementFeature.class).updateInc(AchievementType.MeridianTotal_M2);
                    player.getFeature(AchievementFeature.class).updateInc(AchievementType.MeridianTotal_M3);
                    MeridianUp.MeridianNotify notify = new MeridianUp.MeridianNotify(req.charId, character.getBo().getMeridian());
                    request.response(notify);
                }
            }
        }
    }

    public static class MeridianNotify {
        int charId;
        long MeridianLevel;

        public MeridianNotify(int charId, long MeridianLevel) {
            this.charId = charId;
            this.MeridianLevel = MeridianLevel;
        }
    }

    public static class Request {
        int charId;
    }
}
