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
import core.config.refdata.ref.RefCharacter;
import core.config.refdata.ref.RefSkill;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class SkillUp extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        SkillUp.Request req = new Gson().fromJson(message, SkillUp.Request.class);
        Character character = player.getFeature(CharFeature.class).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", req.charId);
        } else {
            int skillLevel = character.getBo().getSkill(req.index);
            if (skillLevel + 1 >= player.getLv()) {
                throw new WSException(ErrorCode.Skill_LevelFull, "技能等级[%s]不能超过人物等级", skillLevel);
            } else if (skillLevel + 1 >= RefDataMgr.getFactor("MaxSkillLevel", 100)) {
                throw new WSException(ErrorCode.Skill_LevelFull, "技能等级[%s]已满", skillLevel);
            } else {
                RefCharacter refCharacter = RefDataMgr.get(RefCharacter.class, req.charId);
                RefSkill refSkill = RefDataMgr.get(RefSkill.class, refCharacter.SkillList.get(req.index));
                if (refSkill == null) {
                    throw new WSException(ErrorCode.Skill_NotFound, "技能没找到");
                } else {
                    PlayerCurrency playerCurrency = player.getFeature(PlayerCurrency.class);
                    int goldRequired = refSkill.GoldAdd * (skillLevel + 1 - 1) + refSkill.Gold;
                    if (!playerCurrency.check(PrizeType.Gold, goldRequired)) {
                        throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", player.getPlayerBO().getGold(), goldRequired);
                    } else {
                        playerCurrency.consume(PrizeType.Gold, goldRequired, ItemFlow.SkillLevelUp);
                        character.getBo().saveSkill(req.index, skillLevel + 1);
                        character.onAttrChanged();
                        player.getFeature(AchievementFeature.class).updateInc(AchievementType.SkillUp);
                        player.getFeature(AchievementFeature.class).updateInc(AchievementType.SkillUp_M1);
                        player.getFeature(AchievementFeature.class).updateInc(AchievementType.SkillUp_M2);
                        player.getFeature(AchievementFeature.class).updateInc(AchievementType.SkillUp_M3);
                        player.getFeature(AchievementFeature.class).updateInc(AchievementType.SkillTotal);
                        SkillUp.SkillNotify notify = new SkillUp.SkillNotify(character.getCharId(), character.getBo().getSkill(req.index), req.index);
                        request.response(notify);
                    }
                }
            }
        }
    }

    public static class Request {
        int charId;
        int index;
    }

    public static class SkillNotify {
        int charId;
        int index;
        long skillLevel;

        public SkillNotify(int charId, long skillLevel, int index) {
            this.charId = charId;
            this.skillLevel = skillLevel;
            this.index = index;
        }
    }
}
