package core.network.client2game.handler.chars;

import business.global.notice.NoticeMgr;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import business.player.feature.character.Equip;
import business.player.feature.character.EquipFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.Quality;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefEquip;
import core.config.refdata.ref.RefSmelt;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class RedEquipUp
extends PlayerHandler
{
public static class Request
{
int charId;
EquipPos pos;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
if (character == null) {
throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
}
Equip equip = character.getEquip(req.pos);
int fromLevel = 0;
if (equip == null || equip.getRef().getQuality() != Quality.Red) {
fromLevel = 0;
} else {
fromLevel = equip.getLevel();
} 

int toLevel = player.getLv();
if (toLevel < 10) {
toLevel = 1;
} else {
toLevel = toLevel / 10 * 10;
} 

if (fromLevel >= toLevel) {
throw new WSException(ErrorCode.Equip_LevelRequired, "升级后超过角色等级，无法升级");
}

toLevel = (fromLevel >= 100) ? (fromLevel + 10) : ((toLevel > 100) ? 100 : toLevel);

String equipPos = req.pos.toString();
String left = "Left";
String right = "Right";
if (equipPos.endsWith(left) || equipPos.endsWith(right)) {
equipPos = equipPos.replace(left, "");
equipPos = equipPos.replace(right, "");
} 

String key = String.valueOf(req.charId) + equipPos + toLevel;
RefEquip ref = (RefEquip)RefEquip.redEquip.get(key);
if (ref == null) {
throw new WSException(ErrorCode.Equip_NotFound, "装备不存在");
}

int material = 0;
RefSmelt toRefSmelt = (RefSmelt)RefDataMgr.get(RefSmelt.class, String.valueOf(Quality.Red.toString()) + toLevel);
if (fromLevel == 0) {
material = toRefSmelt.RedPiece;
} else {
RefSmelt fromRefSmelt = (RefSmelt)RefDataMgr.get(RefSmelt.class, String.valueOf(Quality.Red.toString()) + fromLevel);
material = toRefSmelt.RedPiece - fromRefSmelt.RedPiece;
} 

PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
if (!playerCurrency.check(PrizeType.RedPiece, material)) {
throw new WSException(ErrorCode.NotEnough_Money, "玩家红碎片:%s<升级所需碎片:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getRedPiece()), Integer.valueOf(material) });
}
EquipFeature equipFeature = (EquipFeature)player.getFeature(EquipFeature.class);

Equip newEquip = equipFeature.gainOneEquip(ref.id, ItemFlow.MakeRedEquip);
if (newEquip == null) {
throw new WSException(ErrorCode.Package_Full, "玩家背包已满");
}

playerCurrency.consume(PrizeType.RedPiece, material, ItemFlow.MakeRedEquip);

Equip preEquip = equipFeature.equipOn(newEquip.getSid(), req.pos, req.charId, character);

if (preEquip != null && preEquip.getRef().getQuality() == Quality.Red) {
equipFeature.consume(preEquip);
}
character.onAttrChanged();
EquipOn.EquipNotify notify = new EquipOn.EquipNotify(character.getCharId(), req.pos, newEquip.getSid());

NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.MakeRedEquip, new String[] { player.getName(), ref.Name });

request.response(notify);
}
}

