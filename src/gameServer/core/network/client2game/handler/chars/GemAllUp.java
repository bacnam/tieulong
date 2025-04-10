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
import core.config.refdata.ref.RefGem;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.List;

public class GemAllUp
extends PlayerHandler
{
public static class Request
{
int charId;
}

public static class SkillNotify {
int charId;
List<Integer> GemLevel;

public SkillNotify(int charId, List<Integer> GemLevel) {
this.charId = charId;
this.GemLevel = GemLevel;
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
int totalMaterial = 0;
for (int i = 0; i < Size * RefDataMgr.size(RefGem.class); i++) {
int index = 1;
for (int j = index; j < Size; j++) {
if (character.getBo().getGem(index) > character.getBo().getGem(j + 1)) {
index = j + 1;
}
} 

int Level = character.getBo().getGem(index);

if (Level >= RefDataMgr.size(RefGem.class)) {
break;
}

RefGem ref = (RefGem)RefDataMgr.get(RefGem.class, Integer.valueOf(Level + 1));

int material = ref.Material;

totalMaterial += material;

if (player.getPlayerBO().getGemMaterial() < totalMaterial) {
totalMaterial -= material;

break;
} 

character.getBo().setGem(index, Level + 1);
Num++;
} 

PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);

if (!playerCurrency.check(PrizeType.GemMaterial, totalMaterial)) {
throw new WSException(ErrorCode.NotEnough_GemMaterial, "玩家材料:%s<强化需要材料:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getGemMaterial()), Integer.valueOf(totalMaterial) });
}

playerCurrency.consume(PrizeType.GemMaterial, totalMaterial, ItemFlow.GemLevelUp);
character.getBo().saveAll();

character.onAttrChanged();

((AchievementFeature)player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.GemMax, Integer.valueOf(character.getBo().getGem(1)));
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.GemMax_M1, Integer.valueOf(Num));
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.GemMax_M2, Integer.valueOf(Num));

SkillNotify notify = new SkillNotify(character.getCharId(), character.getBo().getGemAll());
request.response(notify);
}
}

