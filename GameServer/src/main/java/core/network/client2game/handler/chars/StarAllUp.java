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
import core.config.refdata.ref.RefStarInfo;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.List;

public class StarAllUp
extends PlayerHandler
{
public static class Request
{
int charId;
}

public static class SkillNotify {
int charId;
List<Integer> StarLevel;

public SkillNotify(int charId, List<Integer> StarLevel) {
this.charId = charId;
this.StarLevel = StarLevel;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
if (character == null) {
throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
}

int Size = (EquipPos.values()).length - 1;
int Num = 0;
int totalMoney = 0;
int totalMaterial = 0;
for (int i = 0; i < Size * RefDataMgr.size(RefStarInfo.class); i++) {
int index = 1;
for (int j = index; j < Size; j++) {

if (character.getBo().getStar(index) > character.getBo().getStar(j + 1)) {
index = j + 1;
}
} 

int Level = character.getBo().getStar(index);

if (Level >= RefDataMgr.size(RefStarInfo.class)) {
break;
}

RefStarInfo ref = (RefStarInfo)RefDataMgr.get(RefStarInfo.class, Integer.valueOf(Level + 1));

int goldRequired = ref.Gold;
int material = ref.Material;

totalMoney += goldRequired;
totalMaterial += material;

if (player.getPlayerBO().getGold() < totalMoney || player.getPlayerBO().getStarMaterial() < totalMaterial) {
totalMoney -= goldRequired;
totalMaterial -= material;

break;
} 

character.getBo().setStar(index, Level + 1);
Num++;
} 

PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
if (!playerCurrency.check(PrizeType.Gold, totalMoney)) {
throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getGold()), Integer.valueOf(totalMoney) });
}

if (!playerCurrency.check(PrizeType.StarMaterial, totalMaterial)) {
throw new WSException(ErrorCode.NotEnough_StarMaterial, "玩家材料:%s<强化需要材料:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getStarMaterial()), Integer.valueOf(totalMaterial) });
}

playerCurrency.consume(PrizeType.Gold, totalMoney, ItemFlow.StarLevelUp);

playerCurrency.consume(PrizeType.StarMaterial, totalMaterial, ItemFlow.StarLevelUp);
character.getBo().saveAll();

character.onAttrChanged();

((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.StarUp, Integer.valueOf(Num));

((AchievementFeature)player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.StarMax, Integer.valueOf(character.getBo().getStar(1)));
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.StarMax_M1, Integer.valueOf(Num));
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.StarMax_M2, Integer.valueOf(Num));

SkillNotify notify = new SkillNotify(character.getCharId(), character.getBo().getStarAll());
request.response(notify);
}
}

