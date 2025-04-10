package core.network.client2game.handler.chars;

import business.global.rank.RankManager;
import business.player.Player;
import business.player.feature.PlayerBase;
import business.player.feature.PlayerCurrency;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefRebirth;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.List;

public class Rebirth
extends PlayerHandler
{
public static class Request
{
int charId;
}

private static class WingNotify {
int charId;
long rebirthLevel;
long rebirthExp;
long gainExp;

private WingNotify(int charId, long rebirthLevel, long rebirthExp, long gainExp) {
this.charId = charId;
this.rebirthLevel = rebirthLevel;
this.rebirthExp = rebirthExp;
this.gainExp = gainExp;
}
}

private static class CharRebirth
{
int charId;
int rebirthLevel;
int rebirthExp;

private CharRebirth(int charId, int rebirthLevel, int rebirthExp) {
this.charId = charId;
this.rebirthLevel = rebirthLevel;
this.rebirthExp = rebirthExp;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
if (character == null) {
throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
}

int nowLevel = character.getBo().getRebirth();
if (nowLevel >= RefDataMgr.size(RefRebirth.class) - 1) {
throw new WSException(ErrorCode.Rebirth_LevelFull, "转生已满级");
}

RefRebirth now_ref = (RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(nowLevel));
RefRebirth next_ref = (RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(nowLevel + 1));
int gainExp = 0;
if (now_ref.Level != next_ref.Level) {
for (Character tmpchar : ((CharFeature)player.getFeature(CharFeature.class)).getAll().values()) {
RefRebirth tmp_ref = (RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(tmpchar.getBo().getRebirth()));
int level = tmp_ref.Level;

if (tmp_ref.Star != ((List)RefRebirth.sameLevel.get(Integer.valueOf(level))).size()) {
throw new WSException(ErrorCode.Rebirth_NotEnough, "转生条件不足");
}
} 

for (Character char1 : ((CharFeature)player.getFeature(CharFeature.class)).getAll().values()) {
RefRebirth now_ref1 = (RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(char1.getBo().getRebirth()));
RefRebirth next_ref1 = (RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(char1.getBo().getRebirth() + 1));
int nowExp1 = char1.getBo().getRebirthExp();
rebirthLevelUp(now_ref1, next_ref1, char1, nowExp1, true);
player.pushProto("charRebirth", new CharRebirth(char1.getCharId(), char1.getBo().getRebirth(), char1.getBo().getRebirthExp(), null));
} 

player.getPlayerBO().saveLv(player.getLv() + RefDataMgr.getFactor("RebirthAddLevel", 10));
((PlayerBase)player.getFeature(PlayerBase.class)).onLevelUp(player.getPlayerBO().getLv());
player.pushProperties("lv", player.getPlayerBO().getLv());
} else {

PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
if (!playerCurrency.check(PrizeType.Exp, next_ref.CostTeamExp)) {
throw new WSException(ErrorCode.NotEnough_Exp, "玩家经验:%s<升级经验:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getExp()), Integer.valueOf(next_ref.CostTeamExp) });
}
playerCurrency.consume(PrizeType.Exp, next_ref.CostTeamExp, ItemFlow.Rebirth);
gainExp = next_ref.GainExp * next_ref.getCrit();
int nowExp = character.getBo().getRebirthExp() + gainExp;
rebirthLevelUp(now_ref, next_ref, character, nowExp, false);
} 

int newLevel = character.getBo().getRebirth();
if (newLevel != nowLevel) {
int rebirthLevel = 0;
for (Character charac : ((CharFeature)player.getFeature(CharFeature.class)).getAll().values()) {
rebirthLevel += charac.getBo().getRebirth();
}

character.onAttrChanged();
RankManager.getInstance().update(RankType.Level, player.getPid(), player.getLv(), new long[] { rebirthLevel });
} 

WingNotify notify = new WingNotify(req.charId, character.getBo().getRebirth(), character.getBo().getRebirthExp(), gainExp, null);
request.response(notify);
}

private void rebirthLevelUp(RefRebirth now_ref, RefRebirth next_ref, Character character, int nowExp, boolean isRebirth) {
int levelUp = 0;
for (int i = now_ref.id; ((RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(i))).Star <= ((List)RefRebirth.sameLevel.get(Integer.valueOf(now_ref.Level))).size(); i++) {
RefRebirth temp_now_ref = (RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(i));
RefRebirth temp_next_ref = (RefRebirth)RefDataMgr.get(RefRebirth.class, Integer.valueOf(i + 1));

if (temp_next_ref == null)
break; 
long needExp = temp_next_ref.Exp;

if (needExp > nowExp) {
break;
}
if (temp_now_ref.Level != temp_next_ref.Level && !isRebirth) {
break;
}
levelUp++;
nowExp = (int)(nowExp - needExp);
} 

character.getBo().saveRebirthExp(nowExp);
character.getBo().saveRebirth(character.getBo().getRebirth() + levelUp);
}
}

