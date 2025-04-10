package core.network.client2game.handler.chars;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.RankArtifice;
import business.global.notice.NoticeMgr;
import business.global.rank.RankManager;
import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.utils.Random;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefArtifice;
import core.config.refdata.ref.RefCharacter;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.Iterator;

public class Artifice
extends PlayerHandler
{
public static class Request {
int charId;
EquipPos pos;
}

public static class ArtificeNotify {
int charId;
EquipPos pos;
long Level;

public ArtificeNotify(int charId, EquipPos pos, long Level) {
this.charId = charId;
this.pos = pos;
this.Level = Level;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
if (character == null) {
throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
}

int level = character.getBo().getArtifice(req.pos.ordinal());
if (level >= RefDataMgr.getAll(RefArtifice.class).size() - 1) {
throw new WSException(ErrorCode.Artifice_Full, "装备炼化等级已满");
}

RefArtifice ref = (RefArtifice)RefDataMgr.get(RefArtifice.class, Integer.valueOf(level + 1));

boolean check = ((PlayerItem)player.getFeature(PlayerItem.class)).checkAndConsume(ref.UniformId, ref.UniformCount, ItemFlow.ArtificeEquip);
if (!check) {
throw new WSException(ErrorCode.Artifice_NotEnough, "炼化石不足");
}

int rate = 0;
int MaxLevel = character.getBo().getArtificeMax(req.pos.ordinal());

if (level < MaxLevel) {
rate = 10000;
}
else if (ref.Timemin != 0 && character.getBo().getArtificeTimes(req.pos.ordinal()) <= ref.Timemin) {
rate = 0;
} else if (ref.TimeMax != 0 && character.getBo().getArtificeTimes(req.pos.ordinal()) >= ref.TimeMax) {
rate = 100;
} else {
rate = ref.Rate;
} 

if (Random.nextInt(10000) < rate) {
character.getBo().saveArtifice(req.pos.ordinal(), level + 1);
if (level + 1 > MaxLevel) {
character.getBo().saveArtificeMax(req.pos.ordinal(), level + 1);
character.getBo().saveArtificeTimes(req.pos.ordinal(), 0);
}

} else {

character.getBo().saveArtifice(req.pos.ordinal(), Math.max(0, level - ref.Star));
character.getBo().saveArtificeTimes(req.pos.ordinal(), character.getBo().getArtificeTimes(req.pos.ordinal()) + 1);
} 

character.onAttrChanged();

int artificeLevel = 0;
for (Character charac : ((CharFeature)player.getFeature(CharFeature.class)).getAll().values()) {
for (Iterator<Integer> iterator = charac.getBo().getArtificeAll().iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
artificeLevel += i; }

} 
RankManager.getInstance().update(RankType.Artifice, player.getPid(), artificeLevel);

((RankArtifice)ActivityMgr.getActivity(RankArtifice.class)).UpdateMaxRequire_cost(player, artificeLevel);

if (character.getBo().getArtifice(req.pos.ordinal()) >= RefDataMgr.getAll(RefArtifice.class).size() - 1) {
RefCharacter refc = (RefCharacter)RefDataMgr.get(RefCharacter.class, Integer.valueOf(character.getCharId()));

String posname = "";
switch (req.pos) {
case Head:
posname = "头盔";
break;
case Neck:
posname = "项链";
break;
case null:
posname = "衣服";
break;
case Weapon:
posname = "武器";
break;
case BraceletLeft:
posname = "左手镯";
break;
case BraceletRight:
posname = "右手镯";
break;
case RingLeft:
posname = "左戒指";
break;
case RingRight:
posname = "右戒指";
break;
} 

NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.Artifice, new String[] { player.getName(), refc.Name, posname });
} 

ArtificeNotify notify = new ArtificeNotify(character.getCharId(), req.pos, character.getBo().getArtifice(req.pos.ordinal()));
request.response(notify);
}
}

