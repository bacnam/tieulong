package core.network.http.handler;

import business.global.gmmail.MailCenter;
import business.global.gmmail.TimerMailMgr;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.item.Reward;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.http.annotation.RequestMapping;
import com.zhonglian.server.http.server.HttpRequest;
import com.zhonglian.server.http.server.HttpResponse;
import com.zhonglian.server.http.server.HttpUtils;
import java.util.Iterator;
import java.util.List;

public class MailRequest
{
@RequestMapping(uri = "/game/mail/send")
public void send(HttpRequest request, HttpResponse response) throws Exception {
JsonObject postParam = HttpUtils.abstractGMParams(request.getRequestBody());
int globalmail = HttpUtils.getInt(postParam, "globalmail");
if (globalmail != 0 && globalmail != 1) {
response.error(30103, "非法的操作类型:%s", new Object[] { Integer.valueOf(globalmail) });
return;
} 
if (globalmail == 0) {
sendToPointPlayer(postParam);
} else if (globalmail == 1) {
sendToAllPlayer(postParam);
} 

JsonObject rtn = new JsonObject();
rtn.addProperty("state", Integer.valueOf(1000));
rtn.addProperty("msg", "success");
response.response(rtn.toString());
}

private void sendToPointPlayer(JsonObject postParam) throws Exception {
List<Long> cids = HttpUtils.getLongList(postParam, "pid");

String sendName = HttpUtils.getString(postParam, "name");

String title = HttpUtils.getString(postParam, "title");

String content = HttpUtils.getString(postParam, "content");

Reward reward = new Reward(HttpUtils.getJsonArray(postParam, "itemlist"));

for (Iterator<Long> iterator = cids.iterator(); iterator.hasNext(); ) { long cid = ((Long)iterator.next()).longValue();
Player player = PlayerMgr.getInstance().getPlayer(cid);
if (player != null) {
MailCenter.getInstance().sendMail(cid, sendName, title, content, reward.uniformItemIds(), reward.uniformItemCounts());
} }

}

private void sendToAllPlayer(JsonObject postParam) throws Exception {
String sendName = HttpUtils.getString(postParam, "name");

String title = HttpUtils.getString(postParam, "title");

String content = HttpUtils.getString(postParam, "content");

Reward reward = new Reward(HttpUtils.getJsonArray(postParam, "itemlist"));

MailCenter.getInstance().sendGlobalMail(sendName, title, content, 0, 0, reward.uniformItemIds(), reward.uniformItemCounts());
}

@RequestMapping(uri = "/game/mail/timerMailList")
public void timerMailList(HttpRequest request, HttpResponse response) throws Exception {
HttpUtils.abstractGMParams(request.getRequestBody());

JsonObject rtn = new JsonObject();
rtn.addProperty("state", Integer.valueOf(1000));
rtn.add("mails", (JsonElement)TimerMailMgr.getInstance().timerMailList());
response.response(rtn.toString());
}

@RequestMapping(uri = "/game/mail/timersend")
public void timerSend(HttpRequest request, HttpResponse response) throws Exception {
JsonObject postParam = HttpUtils.abstractGMParams(request.getRequestBody());
int beginTime = HttpUtils.getTime(postParam, "begintime");
if (beginTime == 0) {
response.error(30103, "非法的开始时间:%s", new Object[] { Integer.valueOf(beginTime) });
return;
} 
int cycles = HttpUtils.getInt(postParam, "cycles");
if (cycles < 1) {
response.error(30103, "非法的循环次数:%s", new Object[] { Integer.valueOf(cycles) });
return;
} 
TimerMailMgr.getInstance().addTimerMail(postParam);

JsonObject rtn = new JsonObject();
rtn.addProperty("state", Integer.valueOf(1000));
rtn.addProperty("msg", "success");
response.response(rtn.toString());
}

@RequestMapping(uri = "/game/mail/timerdel")
public void timerDel(HttpRequest request, HttpResponse response) throws Exception {
JsonObject postParam = HttpUtils.abstractGMParams(request.getRequestBody());
long mailId = HttpUtils.getLong(postParam, "mailid");
if (mailId < 0L) {
response.error(30103, "非法的邮件Id:%s", new Object[] { Long.valueOf(mailId) });
return;
} 
TimerMailMgr.getInstance().delTimerMail(mailId);

JsonObject rtn = new JsonObject();
rtn.addProperty("state", Integer.valueOf(1000));
rtn.addProperty("msg", "success");
response.response(rtn.toString());
}
}

