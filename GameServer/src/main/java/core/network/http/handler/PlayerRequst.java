package core.network.http.handler;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.http.annotation.RequestMapping;
import com.zhonglian.server.http.server.HttpRequest;
import com.zhonglian.server.http.server.HttpResponse;
import com.zhonglian.server.http.server.HttpUtils;
import java.util.List;

public class PlayerRequst
{
public Gson gson = new Gson();

@RequestMapping(uri = "/game/player/info")
public void playerList(HttpRequest request, HttpResponse response) throws Exception {
JsonObject postParam = HttpUtils.abstractGMParams(request.getRequestBody());

String openid = HttpUtils.getString(postParam, "open_id");

int serverid = HttpUtils.getInt(postParam, "server_id");

List<Player> player = PlayerMgr.getInstance().searchPlayer(openid, serverid);
if (player == null) {
response.error(30103, "找不到该玩家:%s", new Object[] { openid });
return;
} 
JsonArray roles = new JsonArray();
for (Player p : player) {

JsonObject roleObj = new JsonObject();
roleObj.addProperty("server", p.getSid());
roleObj.addProperty("id", p.getPid());
roleObj.addProperty("name", p.getName());
roleObj.addProperty("level", p.getLv());
roleObj.addProperty("vip", p.getVipLevel());
roleObj.addProperty("crystal", p.getPlayerBO().getCrystal());
roles.add((JsonElement)roleObj);
} 

JsonObject rtn = new JsonObject();
rtn.addProperty("state", Integer.valueOf(1000));
rtn.add("roleinfo", (JsonElement)roles);

response.response(this.gson.toJson((JsonElement)rtn));
}

@RequestMapping(uri = "/game/player/bannedLogin")
public void bannedPlayerLogin(HttpRequest request, HttpResponse response) throws Exception {
JsonObject postParam = HttpUtils.abstractGMParams(request.getRequestBody());

List<Long> cidList = HttpUtils.getLongList(postParam, "pid");

int duration = HttpUtils.getInt(postParam, "duration");

List<Long> failCids = Lists.newArrayList();

cidList.forEach(cid -> {
Player player = PlayerMgr.getInstance().getPlayer(cid.longValue());

if (player != null) {
((PlayerBase)player.getFeature(PlayerBase.class)).bannedLogin(paramInt);
} else {
paramList.add(cid);
} 
});
cidList.removeAll(failCids);

JsonObject rtn = new JsonObject();
rtn.addProperty("state", Integer.valueOf(1000));
rtn.addProperty("msg", "成功禁登:" + cidList.toString() + ",找不到玩家:" + failCids.toString());

response.response(this.gson.toJson((JsonElement)rtn));
}

@RequestMapping(uri = "/game/player/bannedChat")
public void bannedPlayerChat(HttpRequest request, HttpResponse response) throws Exception {
JsonObject postParam = HttpUtils.abstractGMParams(request.getRequestBody());

List<Long> cidList = HttpUtils.getLongList(postParam, "pid");

int duration = HttpUtils.getInt(postParam, "duration");
if (duration > 0) {
duration = CommTime.nowSecond() + duration;
}

int expiredTime = duration;

List<Long> failCids = Lists.newArrayList();

cidList.forEach(cid -> {
Player player = PlayerMgr.getInstance().getPlayer(cid.longValue());

if (player != null) {
player.getPlayerBO().saveBannedChatExpiredTime(paramInt);
} else {
paramList.add(cid);
} 
});
cidList.removeAll(failCids);

JsonObject rtn = new JsonObject();
rtn.addProperty("state", Integer.valueOf(1000));
rtn.addProperty("msg", "成功禁言:" + cidList.toString() + ",找不到玩家:" + failCids.toString());

response.response(this.gson.toJson((JsonElement)rtn));
}
}

