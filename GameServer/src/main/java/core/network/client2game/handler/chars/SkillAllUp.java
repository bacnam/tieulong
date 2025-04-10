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
import java.util.List;

public class SkillAllUp
extends PlayerHandler
{
public static class Request
{
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

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
if (character == null) {
throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
}

int skillSize = character.getBo().getSkillSize();
int skillNum = 0;
int totalMoney = 0;
for (int i = 0; i < skillSize * player.getLv(); i++) {
int index = 0;
for (int j = 0; j < skillSize - 1; j++) {
RefCharacter refCharacter1 = (RefCharacter)RefDataMgr.get(RefCharacter.class, Integer.valueOf(req.charId));
RefSkill refSkill1 = (RefSkill)RefDataMgr.get(RefSkill.class, refCharacter1.SkillList.get(j + 1));
if (refSkill1 == null) {
throw new WSException(ErrorCode.Skill_NotFound, "技能没找到");
}
if (refSkill1.Require <= player.getLv())
{
if (character.getBo().getSkill(index) > character.getBo().getSkill(j + 1))
index = j + 1; 
}
} 
int skillLevel = character.getBo().getSkill(index);

if (skillLevel + 1 >= player.getLv()) {
break;
}

if (skillLevel + 1 >= RefDataMgr.getFactor("MaxSkillLevel", 100)) {
break;
}

RefCharacter refCharacter = (RefCharacter)RefDataMgr.get(RefCharacter.class, Integer.valueOf(req.charId));
RefSkill refSkill = (RefSkill)RefDataMgr.get(RefSkill.class, refCharacter.SkillList.get(index));
if (refSkill == null) {
throw new WSException(ErrorCode.Skill_NotFound, "技能没找到");
}

int goldRequired = refSkill.GoldAdd * (skillLevel + 1 - 1) + refSkill.Gold;

totalMoney += goldRequired;

if (player.getPlayerBO().getGold() < totalMoney) {
totalMoney -= goldRequired;

break;
} 

character.getBo().setSkill(index, skillLevel + 1);
skillNum++;
} 

PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
if (!playerCurrency.check(PrizeType.Gold, totalMoney)) {
throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getGold()), Integer.valueOf(totalMoney) });
}

playerCurrency.consume(PrizeType.Gold, totalMoney, ItemFlow.SkillLevelUp);
character.getBo().saveAll();

character.onAttrChanged();

((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillUp, Integer.valueOf(skillNum));
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillUp_M1, Integer.valueOf(skillNum));
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillUp_M2, Integer.valueOf(skillNum));
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillUp_M3, Integer.valueOf(skillNum));

((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.SkillTotal, Integer.valueOf(skillNum));

SkillNotify notify = new SkillNotify(character.getCharId(), character.getBo().getSkillAll());
request.response(notify);
}
}

