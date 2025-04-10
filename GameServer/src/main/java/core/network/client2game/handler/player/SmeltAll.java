package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.character.Character;
import business.player.feature.character.Equip;
import business.player.feature.character.EquipFeature;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.Quality;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefEquip;
import core.config.refdata.ref.RefSmelt;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SmeltAll
extends PlayerHandler
{
public static class Request
{
boolean isKeep;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
EquipFeature equipFeature = (EquipFeature)player.getFeature(EquipFeature.class);

Map<Integer, Map<EquipPos, Equip>> map = new HashMap<>();

equipFeature.getAllEquips().forEach(equip -> {
RefEquip ref = equip.getRef();

EquipPos weakest = null;

int power = equip.getBasePower();

for (EquipPos pos : ref.validEquipPos()) {
if (paramMap.get(Integer.valueOf(ref.CharID)) == null) {
Map<EquipPos, Equip> map1 = new HashMap<>();
map1.put(pos, equip);
paramMap.put(Integer.valueOf(ref.CharID), map1);
return;
} 
Equip cur = (Equip)((Map)paramMap.get(Integer.valueOf(ref.CharID))).get(pos);
if (cur == null) {
weakest = pos;
break;
} 
if (cur.getBasePower() < power) {
weakest = pos;
power = cur.getBasePower();
} 
} 
if (weakest != null) {
((Map<EquipPos, Equip>)paramMap.get(Integer.valueOf(ref.CharID))).put(weakest, equip);
}
});
int StrengthenMaterial = 0;
int Gold = 0;
int RedPiece = 0;
for (Equip equip : equipFeature.getAllEquips()) {
Character onwer = equip.getOwner();

RefEquip ref = equip.getRef();

if (onwer != null) {
continue;
}

if (req.isKeep) {

boolean flag = false;
for (EquipPos pos : ref.validEquipPos()) {
if (((Map)map.get(Integer.valueOf(ref.CharID))).get(pos) == equip) {
flag = true;
break;
} 
} 
if (flag) {
continue;
}
} 

RefSmelt refsmelt = (RefSmelt)RefDataMgr.get(RefSmelt.class, String.valueOf(ref.getQuality().toString()) + equip.getLevel());

StrengthenMaterial += refsmelt.Strengthen;
Gold += refsmelt.Gold;
if (ref.getQuality() == Quality.Red) {
RedPiece += refsmelt.RedPiece;
}
equipFeature.consume(equip);
} 
PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
if (StrengthenMaterial != 0)
playerCurrency.gain(PrizeType.StrengthenMaterial, StrengthenMaterial, ItemFlow.Smelt); 
if (Gold != 0)
playerCurrency.gain(PrizeType.Gold, Gold, ItemFlow.Smelt); 
if (RedPiece != 0)
playerCurrency.gain(PrizeType.RedPiece, RedPiece, ItemFlow.Smelt); 
Reward reward = new Reward();
reward.add(PrizeType.StrengthenMaterial, StrengthenMaterial);
reward.add(PrizeType.Gold, Gold);
reward.add(PrizeType.RedPiece, RedPiece);

if (reward.size() > 0) {
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Smelt);
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Smelt_M1);
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Smelt_M2);
((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.Smelt_M3);
} 

request.response(reward);
}
}

