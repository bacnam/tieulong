package core.network.client2game.handler.player;

import BaseCommon.CommLog;
import business.global.gmmail.MailCenter;
import business.player.Player;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zhonglian.server.http.server.GMParam;
import com.zhonglian.server.http.server.HttpUtils;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class GiftCode extends PlayerHandler {
    public void handle(final Player player, final WebSocketRequest request, String message) throws WSException, IOException {
        Request req = new Gson().fromJson(message, Request.class);

        GMParam params = new GMParam();
        params.put("code", req.code);
        params.put("uid", player.getPid());
        params.put("cid", player.getPid());
        params.put("lv", player.getLv());
        params.put("vipLv", player.getVipLevel());

        String baseurl = System.getProperty("downConfUrl");
        String url = baseurl + "/gm/checkGiftCode"; // Ví dụ endpoint

        HttpUtils.RequestGM(url, params, new HttpUtils.Callback() {
            @Override
            public void completed(String response) {
                try {
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    if (json.get("state").getAsInt() != 1000) {
                        request.response(new GiftCode.Response(json.get("state").getAsInt(), json.get("message").getAsString()));
                        return;
                    }

                    JsonObject gift = json.get("gifid").getAsJsonObject();
                    String sender = gift.get("mailSender").getAsString();
                    String title = gift.get("mailTitle").getAsString();
                    String content = gift.get("mailContext").getAsString();
                    StringBuilder itemids = new StringBuilder();
                    StringBuilder counts = new StringBuilder();

                    for (JsonElement j : gift.get("itemlist").getAsJsonArray()) {
                        JsonObject item = j.getAsJsonObject();
                        itemids.append(item.get("uniformId").getAsInt()).append(";");
                        counts.append(item.get("count").getAsInt()).append(";");
                    }

                    if (itemids.toString().endsWith(";")) itemids.deleteCharAt(itemids.length() - 1);
                    if (counts.toString().endsWith(";")) counts.deleteCharAt(counts.length() - 1);

                    MailCenter.getInstance().sendMail(
                            player.getPid(), sender, title, content,
                            itemids.toString(), counts.toString()
                    );

                    request.response(new GiftCode.Response(1000, "ok"));
                } catch (Exception e) {
                    request.response(new GiftCode.Response(500, "内部异常"));
                    CommLog.error("兑换码失败，处理响应时异常", e);
                }
            }

            @Override
            public void failed(Exception exception) {
                request.error(ErrorCode.RechargeOrderFailed, "兑换码使用失败", new Object[0]);
            }
        });
    }


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
}

