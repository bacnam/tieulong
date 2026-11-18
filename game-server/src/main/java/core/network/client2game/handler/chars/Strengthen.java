package core.network.client2game.handler.chars;

import business.global.notice.NoticeMgr;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.common.enums.ConstEnum.UniverseMessageType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefStrengthenInfo;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class Strengthen extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Strengthen.Request req = new Gson().fromJson(message, Strengthen.Request.class);
        Character character = player.getFeature(CharFeature.class).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", req.charId);
        } else {
            int StrengthenLevel = character.getBo().getStrengthen(req.pos.ordinal());
            if (StrengthenLevel >= RefDataMgr.size(RefStrengthenInfo.class)) {
                throw new WSException(ErrorCode.Strengthen_LevelFull, "位置[%s]强化已满级", req.pos);
            } else if (StrengthenLevel >= player.getLv()) {
                throw new WSException(ErrorCode.Strengthen_LevelFull, "位置[%s]强化不能超过人物等级", req.pos);
            } else {
                RefStrengthenInfo ref = RefDataMgr.get(RefStrengthenInfo.class, StrengthenLevel + 1);
                PlayerCurrency playerCurrency = player.getFeature(PlayerCurrency.class);
                if (!playerCurrency.check(PrizeType.Gold, ref.Gold)) {
                    throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", player.getPlayerBO().getGold(), ref.Gold);
                } else if (!playerCurrency.check(PrizeType.StrengthenMaterial, ref.Material)) {
                    throw new WSException(
                        ErrorCode.NotEnough_StengthenMaterial, "玩家材料:%s<强化需要材料:%s", player.getPlayerBO().getStrengthenMaterial(), ref.Material
                    );
                } else {
                    playerCurrency.consume(PrizeType.Gold, ref.Gold, ItemFlow.StrengthenLevelUp);
                    playerCurrency.consume(PrizeType.StrengthenMaterial, ref.Material, ItemFlow.StrengthenLevelUp);
                    character.getBo().saveStrengthen(req.pos.ordinal(), character.getBo().getStrengthen(req.pos.ordinal()) + 1);
                    if (character.getBo().getStrengthen(req.pos.ordinal()) == RefDataMgr.size(RefStrengthenInfo.class)) {
                        NoticeMgr.getInstance().sendMarquee(UniverseMessageType.Strengthen, player.getName());
                    }

                    character.onAttrChanged();
                    player.getFeature(AchievementFeature.class).updateInc(AchievementType.Strengthen);
                    player.getFeature(AchievementFeature.class).updateInc(AchievementType.Strengthen_M1);
                    player.getFeature(AchievementFeature.class).updateInc(AchievementType.Strengthen_M2);
                    player.getFeature(AchievementFeature.class).updateInc(AchievementType.Strengthen_M3);
                    player.getFeature(AchievementFeature.class).updateInc(AchievementType.StrengthTotal);
                    Strengthen.StrengthenNotify notify = new Strengthen.StrengthenNotify(
                        character.getCharId(), req.pos, character.getBo().getStrengthen(req.pos.ordinal())
                    );
                    request.response(notify);
                }
            }
        }
    }

    public static class Request {
        int charId;
        EquipPos pos;
    }

    public static class StrengthenNotify {
        int charId;
        EquipPos pos;
        long StrengthenLevel;

        public StrengthenNotify(int charId, EquipPos pos, long StrengthenLevel) {
            this.charId = charId;
            this.pos = pos;
            this.StrengthenLevel = StrengthenLevel;
        }
    }
}
