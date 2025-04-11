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
import core.config.refdata.ref.RefCharacter;
import core.config.refdata.ref.RefSkill;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class SkillUp
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        Character character = ((CharFeature) player.getFeature(CharFeature.class)).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[]{Integer.valueOf(req.charId)});
        }
        int skillLevel = character.getBo().getSkill(req.index);

        if (skillLevel + 1 >= player.getLv()) {
            throw new WSException(ErrorCode.Skill_LevelFull, "技能等级[%s]不能超过人物等级", new Object[]{Integer.valueOf(skillLevel)});
        }

        if (skillLevel + 1 >= RefDataMgr.getFactor("MaxSkillLevel", 100)) {
            throw new WSException(ErrorCode.Skill_LevelFull, "技能等级[%s]已满", new Object[]{Integer.valueOf(skillLevel)});
        }

        RefCharacter refCharacter = (RefCharacter) RefDataMgr.get(RefCharacter.class, Integer.valueOf(req.charId));
        RefSkill refSkill = (RefSkill) RefDataMgr.get(RefSkill.class, refCharacter.SkillList.get(req.index));
        if (refSkill == null) {
            throw new WSException(ErrorCode.Skill_NotFound, "技能没找到");
        }

        PlayerCurrency playerCurrency = (PlayerCurrency) player.getFeature(PlayerCurrency.class);
        int goldRequired = refSkill.GoldAdd * (skillLevel + 1 - 1) + refSkill.Gold;
        if (!playerCurrency.check(PrizeType.Gold, goldRequired)) {
            throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", new Object[]{Integer.valueOf(player.getPlayerBO().getGold()), Integer.valueOf(goldRequired)});
        }

        playerCurrency.consume(PrizeType.Gold, goldRequired, ItemFlow.SkillLevelUp);

        character.getBo().saveSkill(req.index, skillLevel + 1);

        character.onAttrChanged();

        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillUp);
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillUp_M1);
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillUp_M2);
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillUp_M3);

        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillTotal);

        SkillNotify notify = new SkillNotify(character.getCharId(), character.getBo().getSkill(req.index), req.index);
        request.response(notify);
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

