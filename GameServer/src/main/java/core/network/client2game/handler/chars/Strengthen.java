package core.network.client2game.handler.chars;

import business.global.notice.NoticeMgr;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefStrengthenInfo;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class Strengthen
extends PlayerHandler
{
public static class Request
{
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

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
if (character == null) {
throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
}

int StrengthenLevel = character.getBo().getStrengthen(req.pos.ordinal());
if (StrengthenLevel >= RefDataMgr.size(RefStrengthenInfo.class)) {
throw new WSException(ErrorCode.Strengthen_LevelFull, "位置[%s]强化已满级", new Object[] { req.pos });
}

if (StrengthenLevel >= player.getLv()) {
throw new WSException(ErrorCode.Strengthen_LevelFull, "位置[%s]强化不能超过人物等级", new Object[] { req.pos });
}

RefStrengthenInfo ref = (RefStrengthenInfo)RefDataMgr.get(RefStrengthenInfo.class, Integer.valueOf(StrengthenLevel + 1));

PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
if (!playerCurrency.check(PrizeType.Gold, ref.Gold)) {
throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getGold()), Integer.valueOf(ref.Gold) });
}

if (!playerCurrency.check(PrizeType.StrengthenMaterial, ref.Material)) {
throw new WSException(ErrorCode.NotEnough_StengthenMaterial, "玩家材料:%s<强化需要材料:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getStrengthenMaterial()), Integer.valueOf(ref.Material) });
}

playerCurrency.consume(PrizeType.Gold, ref.Gold, ItemFlow.StrengthenLevelUp);

playerCurrency.consume(PrizeType.StrengthenMaterial, ref.Material, ItemFlow.StrengthenLevelUp);

character.getBo().saveStrengthen(req.pos.ordinal(), character.getBo().getStrengthen(req.pos.ordinal()) + 1);

if (character.getBo().getStrengthen(req.pos.ordinal()) == RefDataMgr.size(RefStrengthenInfo.class)) {
NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.Strengthen, new String[] { player.getName() });
}

character.onAttrChanged();

((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Strengthen);
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Strengthen_M1);
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Strengthen_M2);
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Strengthen_M3);

((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.StrengthTotal);

StrengthenNotify notify = new StrengthenNotify(character.getCharId(), req.pos, character.getBo().getStrengthen(req.pos.ordinal()));
request.response(notify);
}
}

