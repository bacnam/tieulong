package core.network.http.handler;

import business.gmcmd.cmd.CommandMgr;
import business.player.Player;
import business.player.PlayerMgr;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.http.annotation.RequestMapping;
import com.zhonglian.server.http.server.HttpRequest;
import com.zhonglian.server.http.server.HttpResponse;
import com.zhonglian.server.http.server.HttpUtils;
import core.database.game.bo.ServerConfigBO;
import core.server.BshRunner;
import core.server.DynamicConfig;
import java.util.Map;

public class Control
{
@RequestMapping(uri = "/game/control/cmd")
public void script(HttpRequest request, HttpResponse response) throws Exception {
JsonObject json = HttpUtils.abstractGMParams(request.getRequestBody());
String scrpit = HttpUtils.getString(json, "cmd");
String mode = HttpUtils.getString(json, "mode");
boolean async = HttpUtils.getBool(json, "async", false);
if (async) {
(new Thread(() -> runScript(paramString1, paramString2, paramHttpResponse))).start();
} else {
runScript(mode, scrpit, response);
} 
}

public void runScript(String mode, String script, HttpResponse response) {
Object rslt = null;
if (mode.equalsIgnoreCase("script")) {
rslt = (new BshRunner()).eval(script);
} else {
try {
String[] args = script.split(" ", 2);
Player p = PlayerMgr.getInstance().getPlayer(Long.parseLong(args[0]));
rslt = CommandMgr.getInstance().run(p, args[1]);
} catch (Exception e) {
JsonObject jsonObject = new JsonObject();
jsonObject.addProperty("state", Integer.valueOf(30114));
jsonObject.addProperty("msg", "执行命令[" + script + "]错误，异常：" + e.toString());
response.response(jsonObject.toString());
return;
} 
} 
String msg = (rslt == null) ? "执行成功" : rslt.toString();

JsonObject rtn = new JsonObject();
rtn.addProperty("state", Integer.valueOf(1000));
rtn.addProperty("msg", msg);
response.response(rtn.toString());
}

@RequestMapping(uri = "/game/control/set")
public void set(HttpRequest request, HttpResponse response) throws Exception {
JsonObject json = HttpUtils.abstractGMParams(request.getRequestBody());
String key = HttpUtils.getString(json, "key");
String value = HttpUtils.getString(json, "value");

DynamicConfig.set(key, value);

JsonObject rtn = new JsonObject();
rtn.addProperty("state", Integer.valueOf(1000));
rtn.addProperty("msg", "set [" + key + "] to [" + value + "]");
response.response(rtn.toString());
}

@RequestMapping(uri = "/game/control/setlist")
public void setlist(HttpRequest request, HttpResponse response) throws Exception {
HttpUtils.abstractGMParams(request.getRequestBody());
Map<String, ServerConfigBO> settings = DynamicConfig.getAllConfig();

JsonObject jSettings = new JsonObject();
for (ServerConfigBO config : settings.values()) {
jSettings.addProperty(config.getKey(), config.getValue());
}

String msg = "";
JsonObject rtn = new JsonObject();
rtn.addProperty("state", Integer.valueOf(1000));
rtn.addProperty("msg", msg);
rtn.add("settings", (JsonElement)jSettings);
response.response(rtn.toString());
}
}

