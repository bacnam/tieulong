package core.network.client2game.handler.base;

import BaseCommon.CommLog;
import business.player.Player;
import business.player.PlayerMgr;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zhonglian.server.common.utils.secure.MD5;
import com.zhonglian.server.http.client.HttpAsyncClient;
import com.zhonglian.server.http.client.IResponseHandler;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.ClientSession;
import core.network.client2game.handler.BaseHandler;
import core.server.DynamicConfig;
import core.server.ServerConfig;
import core.server.ServerIDUtils;
import java.io.IOException;

public class Login
extends BaseHandler
{
private static class Request
{
public String open_id;
public String access_token;
public String serverid;
public String client_version;
public boolean testLogin;
public String channelId;
}

public void handle(final WebSocketRequest request, String data) throws IOException {
final Request req = (Request)(new Gson()).fromJson(data, Request.class);

int onlineCount = PlayerMgr.getInstance().getOnlinePlayers().size();
if (onlineCount >= DynamicConfig.get("OnlineLimit", 2000)) {
request.error(ErrorCode.Login_OnlineLimit, "在线人数超过上限", new Object[0]);

return;
} 
String sidString = req.serverid;
final int sidInt = ServerIDUtils.getSidBySids(sidString);

if (req.testLogin) {
login(request, req, sidInt, new JsonObject());
} else {
String param = "access_token=" + req.access_token + "&open_id=" + req.open_id;
String sign = MD5.md5(String.valueOf(param) + "&secret_key=" + ServerConfig.AAY_SecretKey());
final String url = "http:

ClientSession session = (ClientSession)request.getSession();
session.setAccessToken(req.access_token);

HttpAsyncClient.startHttpGet(url, new IResponseHandler()
{
public void failed(Exception e)
{
request.error(ErrorCode.Login_ThirdAuthError, "和爱爱游服务器通信发生异常:%s", new Object[] { e.getMessage() });
}

public void compeleted(String res) {
try {
JsonObject resJson = (new JsonParser()).parse(res).getAsJsonObject();
if (resJson.get("code").getAsInt() != 200) {
int code = resJson.get("code").getAsInt();
String msg = resJson.get("msg").getAsString();
CommLog.error("request:{}", url);
request.error(ErrorCode.Login_ThirdAuthError, "第三方验证失败, code:%s, msg:%s", new Object[] { Integer.valueOf(code), msg });
return;
} 
Login.this.login(request, req, sidInt, resJson.get("data").getAsJsonObject());
} catch (Exception e) {
CommLog.error("解析爱爱游返回数据失败", e);
request.error(ErrorCode.Unknown, "解析爱爱游返回数据失败", new Object[0]);
} 
}
});
} 
}

private void login(WebSocketRequest request, Request req, int sidInt, JsonObject data) {
try {
ClientSession session = (ClientSession)request.getSession();
session.setValid(true);
session.setOpenId(req.open_id);
session.setPlayerSid(sidInt);
Player player = PlayerMgr.getInstance().getPlayer(session.getOpenId(), session.getPlayerSid());
data.addProperty("channelId", req.channelId);
if (player != null) {
int bantime = player.getBanTime();
if (bantime != 0) {
request.error(ErrorCode.Login_PlayerBanned, getBanText(bantime, player.getPid()), new Object[0]);

return;
} 
PlayerMgr.getInstance().connectPlayer(session, player);
} else {
PlayerMgr.getInstance().cachePlayerJson(req.open_id, data);
} 

request.response(data);
} catch (Exception e) {
CommLog.error("登录服务器失败", e);
request.error(ErrorCode.Unknown, "登录服务器失败", new Object[0]);
} 
}

private String getBanText(int bantime, long id) {
if (bantime == -1)
return "您被永久封号，请联系客服解封。\nid:" + id; 
if (bantime > 86400)
return "您被封号" + (bantime / 86400) + "天，请联系客服解封。\nid:" + id; 
if (bantime > 3600)
return "您被封号" + (bantime / 3600) + "小时，请联系客服解封。\nid:" + id; 
if (bantime > 60) {
return "您被封号" + (bantime / 60) + "分钟，请联系客服解封。\nid:" + id;
}
return "您被封号" + bantime + "秒，请联系客服解封。\nid:" + id;
}
}

