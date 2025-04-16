package core.network.client2game.handler.player;

import BaseCommon.CommLog;
import business.global.recharge.RechargeMgr;
import business.player.Player;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zhonglian.server.common.utils.secure.MD5;
import com.zhonglian.server.http.client.IResponseHandler;
import com.zhonglian.server.http.server.GMParam;
import com.zhonglian.server.http.server.HttpUtils;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefRecharge;
import core.network.client2game.handler.PlayerHandler;
import core.server.ServerConfig;

import java.io.IOException;

public class RechargeOrder
        extends PlayerHandler {
    private static RechargeMgr rechargeManager = null;

    public void handle(final Player player, final WebSocketRequest request, String message) throws WSException, IOException {
        if (rechargeManager == null) {
            rechargeManager = RechargeMgr.getInstance();
        }
        Request req = (Request) (new Gson()).fromJson(message, Request.class);

        final RefRecharge ref = (RefRecharge) RefDataMgr.get(RefRecharge.class, req.goodsId);
        if (ref == null) {
            throw new WSException(ErrorCode.RechargeOrderFailed, "产品%s不存在", new Object[]{req.goodsId});
        }

        GMParam params = new GMParam();
        params.put("carrier", "aiaiu");
        params.put("adfrom", "aiaiu");
        params.put("adfrom2", "aiaiu");
        params.put("app_id", "10294");
        params.put("amount", Integer.valueOf(ref.Price));
        params.put("quantity", Integer.valueOf(1));
        params.put("crystal", Integer.valueOf(ref.Price));
        params.put("productid", req.goodsId);
        params.put("uid", player.getOpenId());
        params.put("cid", Long.valueOf(player.getPid()));

        String createorderurl = System.getProperty("RechargeCreateOrderAddr");
        HttpUtils.RequestGM(createorderurl, params, new IResponseHandler() {
            @Override
            public void compeleted(String response) {
                try {
                    JsonObject resJson = JsonParser.parseString(response).getAsJsonObject();
                    if (resJson.get("code").getAsInt() != 1000) {
                        CommLog.error("充值回调结果：{}", response);
                        request.error(ErrorCode.RechargeOrderFailed, "充值创建订单失败1", new Object[0]);
                        return;
                    }

                    PayData data = new PayData();
                    data.open_id = player.getOpenId();
                    data.access_token = player.getClientSession().getAccessToken();
                    data.bill_no = resJson.get("bill_no").getAsString();
                    data.goods_name = ref.Title;
                    data.total_fee = ref.Price;
                    data.ext = data.bill_no;
                    String signsrc = data.getSignSrc();
                    data.sign = MD5.md5(signsrc);

                    CommLog.info("sign src: {}, \nsign:{}", signsrc, data.sign);
                    request.response(data);

                } catch (Exception e) {
                    request.error(ErrorCode.RechargeOrderFailed, "充值创建订单失败2", new Object[]{e});
                    CommLog.error("订单充值失败,error", e);
                }
            }

            @Override
            public void failed(Exception exception) {
                request.error(ErrorCode.RechargeOrderFailed, "充值创建订单失败3", new Object[0]);
            }
        });


    }

    private static class Request {
        String goodsId;
    }

    private static class PayData {
        String open_id;
        String access_token;
        String goods_name;
        String bill_no;
        int total_fee;
        String ext;
        String sign;

        private PayData() {
        }

        public String getSignSrc() {
            return "access_token=" + this.access_token +
                    "&bill_no=" + this.bill_no +
                    "&ext=" + this.ext +
                    "&goods_name=" + this.goods_name +
                    "&open_id=" + this.open_id +
                    "&secret_key=" + ServerConfig.AAY_SecretKey() +
                    "&total_fee=" + this.total_fee;
        }
    }
}

