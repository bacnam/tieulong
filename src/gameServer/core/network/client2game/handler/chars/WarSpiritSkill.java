package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.character.WarSpirit;
import business.player.feature.character.WarSpiritFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefSkill;
import core.config.refdata.ref.RefWarSpirit;
import core.config.refdata.ref.RefWarSpiritStar;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.Map;

public class WarSpiritSkill
extends PlayerHandler
{
public static class Request {
int spiritId;
}

public static class SkillNotify {
int spiritId;
long skillLevel;

public SkillNotify(int spiritId, long skillLevel) {
this.spiritId = spiritId;
this.skillLevel = skillLevel;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
WarSpirit warSpirit = ((WarSpiritFeature)player.getFeature(WarSpiritFeature.class)).getWarSpirit(req.spiritId);
if (warSpirit == null) {
throw new WSException(ErrorCode.Char_NotFound, "战灵[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.spiritId) });
}
int skillLevel = warSpirit.getBo().getSkill();

if (skillLevel + 1 >= RefDataMgr.getFactor("MaxSkillLevel", 100)) {
throw new WSException(ErrorCode.Skill_LevelFull, "技能等级[%s]已满", new Object[] { Integer.valueOf(skillLevel) });
}
int star = warSpirit.getBo().getStar();
RefWarSpiritStar refstar = (RefWarSpiritStar)((Map)RefWarSpiritStar.spiritMap.get(Integer.valueOf(req.spiritId))).get(Integer.valueOf(star));

if (skillLevel + 1 >= refstar.SkillLevel) {
throw new WSException(ErrorCode.Skill_LevelFull, "当前星级下技能等级[%s]已满", new Object[] { Integer.valueOf(skillLevel) });
}

RefWarSpirit refspirit = (RefWarSpirit)RefDataMgr.get(RefWarSpirit.class, Integer.valueOf(req.spiritId));
RefSkill refSkill = (RefSkill)RefDataMgr.get(RefSkill.class, refspirit.SkillList.get(0));
if (refSkill == null) {
throw new WSException(ErrorCode.Skill_NotFound, "技能没找到");
}

PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
int goldRequired = refSkill.GoldAdd * (skillLevel + 1 - 1) + refSkill.Gold;
if (!playerCurrency.check(PrizeType.Gold, goldRequired)) {
throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getGold()), Integer.valueOf(goldRequired) });
}

playerCurrency.consume(PrizeType.Gold, goldRequired, ItemFlow.SkillLevelUp);

warSpirit.getBo().saveSkill(skillLevel + 1);

warSpirit.onAttrChanged();

SkillNotify notify = new SkillNotify(warSpirit.getSpiritId(), warSpirit.getBo().getSkill());
request.response(notify);
}
}

