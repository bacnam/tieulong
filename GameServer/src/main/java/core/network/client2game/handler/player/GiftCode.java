package core.network.client2game.handler.player;

import BaseCommon.CommLog;
import business.global.gmmail.MailCenter;
import business.player.Player;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zhonglian.server.http.client.IResponseHandler;
import com.zhonglian.server.http.server.GMParam;
import com.zhonglian.server.http.server.HttpUtils;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class GiftCode extends PlayerHandler {
private static class Request {
String code;
}

private static class Response {
int status;
String msg;

public Response(int status, String msg) {
this.status = status;
this.msg = msg;
}
}

public void handle(final Player player, final WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);

GMParam params = new GMParam();
params.put("code", req.code);
params.put("uid", Long.valueOf(player.getPid()));
params.put("cid", Long.valueOf(player.getPid()));
params.put("lv", Integer.valueOf(player.getLv()));
params.put("vipLv", Integer.valueOf(player.getVipLevel()));

String baseurl = System.getProperty("downConfUrl");
HttpUtils.RequestGM("http:
{
public void compeleted(String response) {
try {
JsonObject json = (new JsonParser()).parse(response).getAsJsonObject();
if (json.get("state").getAsInt() != 1000) {
request.response(new GiftCode.Response(json.get("state").getAsInt(), json.get("message").getAsString()));
return;
} 
json = json.get("gifid").getAsJsonObject();
String sender = json.get("mailSender").getAsString();
String title = json.get("mailTitle").getAsString();
String content = json.get("mailContext").getAsString();
StringBuilder itemids = new StringBuilder();
StringBuilder counts = new StringBuilder();
for (JsonElement j : json.get("itemlist").getAsJsonArray()) {
JsonObject item = j.getAsJsonObject();
itemids.append(item.get("uniformId").getAsInt()).append(";");
counts.append(item.get("count").getAsInt()).append(";");
} 
if (itemids.toString().endsWith(";")) {
itemids.deleteCharAt(itemids.length() - 1);
}
if (counts.toString().endsWith(";")) {
counts.deleteCharAt(counts.length() - 1);
}

MailCenter.getInstance().sendMail(player.getPid(), sender, title, content, itemids.toString(), counts.toString());

request.response(new GiftCode.Response(1000, "ok"));
} catch (Exception e) {
request.response(new GiftCode.Response(500, "内部异常"));
CommLog.error("订单充值失败,error", e);
} 
}

public void failed(Exception exception) {
request.error(ErrorCode.RechargeOrderFailed, "兑换码使用失败", new Object[0]);
}
});
}
}

