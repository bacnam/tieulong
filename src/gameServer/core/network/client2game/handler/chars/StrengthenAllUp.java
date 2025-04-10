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
import core.config.refdata.ref.RefStrengthenInfo;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.List;

public class StrengthenAllUp
extends PlayerHandler
{
public static class Request
{
int charId;
}

public static class SkillNotify {
int charId;
List<Integer> StrengthenLevel;

public SkillNotify(int charId, List<Integer> StrengthenLevel) {
this.charId = charId;
this.StrengthenLevel = StrengthenLevel;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
if (character == null) {
throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
}

if (character.getEquips().size() == 0) {
throw new WSException(ErrorCode.Equip_NotEquip, "角色[%s]没穿装备", new Object[] { Integer.valueOf(req.charId) });
}

int Size = (EquipPos.values()).length - 1;
int Num = 0;
int totalMoney = 0;
int totalMaterial = 0;

for (int i = 0; i < Size * player.getLv(); i++) {
int index = 1;
if (character.getEquip(EquipPos.values()[index]) == null) {
index++;
}
for (int j = index; j < Size; j++) {

if (character.getEquip(EquipPos.values()[j + 1]) != null && character.getBo().getStrengthen(index) > character.getBo().getStrengthen(j + 1)) {
index = j + 1;
}
} 

int Level = character.getBo().getStrengthen(index);

if (Level >= player.getLv()) {
break;
}

if (Level >= RefDataMgr.size(RefStrengthenInfo.class)) {
break;
}

RefStrengthenInfo ref = (RefStrengthenInfo)RefDataMgr.get(RefStrengthenInfo.class, Integer.valueOf(Level + 1));

int goldRequired = ref.Gold;
int material = ref.Material;

totalMoney += goldRequired;
totalMaterial += material;

if (player.getPlayerBO().getGold() < totalMoney || player.getPlayerBO().getStrengthenMaterial() < totalMaterial) {
totalMoney -= goldRequired;
totalMaterial -= material;

break;
} 

character.getBo().setStrengthen(index, Level + 1);
Num++;
} 

PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
if (!playerCurrency.check(PrizeType.Gold, totalMoney)) {
throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getGold()), Integer.valueOf(totalMoney) });
}

if (!playerCurrency.check(PrizeType.StrengthenMaterial, totalMaterial)) {
throw new WSException(ErrorCode.NotEnough_StengthenMaterial, "玩家材料:%s<强化需要材料:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getStrengthenMaterial()), Integer.valueOf(totalMaterial) });
}

playerCurrency.consume(PrizeType.Gold, totalMoney, ItemFlow.StrengthenLevelUp);

playerCurrency.consume(PrizeType.StrengthenMaterial, totalMaterial, ItemFlow.StrengthenLevelUp);
character.getBo().saveAll();

character.onAttrChanged();

((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Strengthen, Integer.valueOf(Num));
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Strengthen_M1, Integer.valueOf(Num));
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Strengthen_M2, Integer.valueOf(Num));
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Strengthen_M3, Integer.valueOf(Num));

((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.StrengthTotal, Integer.valueOf(Num));

SkillNotify notify = new SkillNotify(character.getCharId(), character.getBo().getStrengthenAll());
request.response(notify);
}
}

