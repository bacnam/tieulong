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
import java.util.List;

public class SkillAllUp extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        SkillAllUp.Request req = new Gson().fromJson(message, SkillAllUp.Request.class);
        Character character = player.getFeature(CharFeature.class).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", req.charId);
        } else {
            int skillSize = character.getBo().getSkillSize();
            int skillNum = 0;
            int totalMoney = 0;

            for (int i = 0; i < skillSize * player.getLv(); i++) {
                int index = 0;

                for (int j = 0; j < skillSize - 1; j++) {
                    RefCharacter refCharacter = RefDataMgr.get(RefCharacter.class, req.charId);
                    RefSkill refSkill = RefDataMgr.get(RefSkill.class, refCharacter.SkillList.get(j + 1));
                    if (refSkill == null) {
                        throw new WSException(ErrorCode.Skill_NotFound, "技能没找到");
                    }

                    if (refSkill.Require <= player.getLv() && character.getBo().getSkill(index) > character.getBo().getSkill(j + 1)) {
                        index = j + 1;
                    }
                }

                int skillLevel = character.getBo().getSkill(index);
                if (skillLevel + 1 >= player.getLv() || skillLevel + 1 >= RefDataMgr.getFactor("MaxSkillLevel", 100)) {
                    break;
                }

                RefCharacter refCharacterx = RefDataMgr.get(RefCharacter.class, req.charId);
                RefSkill refSkillx = RefDataMgr.get(RefSkill.class, refCharacterx.SkillList.get(index));
                if (refSkillx == null) {
                    throw new WSException(ErrorCode.Skill_NotFound, "技能没找到");
                }

                int goldRequired = refSkillx.GoldAdd * (skillLevel + 1 - 1) + refSkillx.Gold;
                totalMoney += goldRequired;
                if (player.getPlayerBO().getGold() < totalMoney) {
                    totalMoney -= goldRequired;
                    break;
                }

                character.getBo().setSkill(index, skillLevel + 1);
                skillNum++;
            }

            PlayerCurrency playerCurrency = player.getFeature(PlayerCurrency.class);
            if (!playerCurrency.check(PrizeType.Gold, totalMoney)) {
                throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", player.getPlayerBO().getGold(), totalMoney);
            } else {
                playerCurrency.consume(PrizeType.Gold, totalMoney, ItemFlow.SkillLevelUp);
                character.getBo().saveAll();
                character.onAttrChanged();
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.SkillUp, skillNum);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.SkillUp_M1, skillNum);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.SkillUp_M2, skillNum);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.SkillUp_M3, skillNum);
                player.getFeature(AchievementFeature.class).updateInc(AchievementType.SkillTotal, skillNum);
                SkillAllUp.SkillNotify notify = new SkillAllUp.SkillNotify(character.getCharId(), character.getBo().getSkillAll());
                request.response(notify);
            }
        }
    }

    public static class Request {
        int charId;
    }

    public static class SkillNotify {
        int charId;
        List<Integer> skill;

        public SkillNotify(int charId, List<Integer> skill) {
            this.charId = charId;
            this.skill = skill;
        }
    }
}
