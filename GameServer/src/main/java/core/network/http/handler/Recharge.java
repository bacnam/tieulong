package core.network.http.handler;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import business.global.recharge.RechargeMgr;
import business.player.Player;
import business.player.PlayerMgr;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.http.annotation.RequestMapping;
import com.zhonglian.server.http.server.HttpRequest;
import com.zhonglian.server.http.server.HttpResponse;
import com.zhonglian.server.http.server.HttpUtils;
import com.zhonglian.server.http.server.RequestException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefRecharge;
import core.database.game.bo.RechargeOrderBO;

public class Recharge {
    private Object Lock = new Object();

    @RequestMapping(uri = "/game/recharge/notify")
    public void recharge(HttpRequest request, HttpResponse response) throws Exception {
        CommLog.info("收到充值回调：", request.getRequestBody());
        synchronized (this.Lock) {
            JsonObject params = HttpUtils.abstractGMParams(request.getRequestBody(), "cYUPYnZgCiy0zUBg9KRRXM7H4GUsRKpI");
            int ordertype = HttpUtils.getInt(params, "ordertype");
            if (ordertype != 1) {
                response.error(30011, "暂不支持退单功能", new Object[0]);
                return;
            }
            long cid = HttpUtils.getLong(params, "cid");
            Player player = PlayerMgr.getInstance().getPlayer(cid);
            if (player == null) {
                response.error(30011, "该服务器上无该玩家[%s]", new Object[]{Long.valueOf(cid)});

                return;
            }
            SyncTaskManager.task(() -> {
                try {
                    String cporderid = HttpUtils.getString(paramJsonObject, "cporderid");

                    RechargeOrderBO bo = (RechargeOrderBO) BM.getBM(RechargeOrderBO.class).findOne("cporderid", cporderid);

                    if (bo != null && bo.getStatus().equals(RechargeMgr.OrderStatus.Delivered.toString())) {
                        paramHttpResponse.error(1000, "已经处理过该订单[%s]", new Object[]{cporderid});

                        return;
                    }
                    if (bo == null) {
                        bo = new RechargeOrderBO();
                        bo.setCporderid(cporderid);
                        bo.setCarrier(HttpUtils.getString(paramJsonObject, "carrier"));
                        bo.setPlatform(HttpUtils.getString(paramJsonObject, "platform"));
                        bo.setAdfrom(HttpUtils.getString(paramJsonObject, "adfrom"));
                        bo.setAdfrom2(HttpUtils.getString(paramJsonObject, "adfrom2"));
                        bo.setGameid(HttpUtils.getString(paramJsonObject, "gameid"));
                        bo.setServerID(HttpUtils.getString(paramJsonObject, "server_id"));
                        bo.setAppID(HttpUtils.getString(paramJsonObject, "appid"));
                        bo.setOpenId(HttpUtils.getString(paramJsonObject, "open_id"));
                        bo.setPid(paramLong);
                        bo.setAdfromOrderid(HttpUtils.getString(paramJsonObject, "adfrom_orderid"));
                        bo.setQuantity(HttpUtils.getInt(paramJsonObject, "quantity"));
                        bo.setProductid(HttpUtils.getString(paramJsonObject, "productid"));
                        bo.setOrderTime(CommTime.nowSecond());
                        bo.setStatus(RechargeMgr.OrderStatus.Paid.toString());
                        bo.insert();
                        RechargeMgr.getInstance().rechargeLog(paramPlayer, bo);
                    }
                    if (paramPlayer.isOnline()) {
                        RechargeMgr.getInstance().sendPrize(bo);
                    } else {
                        RechargeMgr.getInstance().cacheOrder(bo);
                    }
                    JsonObject rtn = new JsonObject();
                    rtn.addProperty("state", Integer.valueOf(1000));
                    rtn.addProperty("msg", "success");
                    rtn.addProperty("cporderid", cporderid);
                    rtn.addProperty("balance", Integer.valueOf(paramPlayer.getPlayerBO().getCrystal()));
                    rtn.addProperty("uid", paramPlayer.getPlayerBO().getOpenId());
                    paramHttpResponse.response(rtn.toString());
                } catch (RequestException e) {
                    Throwable cause = e.getCause();
                    if (cause != null && cause instanceof RequestException) {
                        RequestException re = (RequestException) cause;
                        paramHttpResponse.error(re.getCode(), re.getMessage(), new Object[0]);
                    } else {
                        paramHttpResponse.error(300001, "服务器发生未知错误，错误信息：%s", new Object[]{e.getMessage()});
                    }
                }
            });
        }
    }

    @RequestMapping(uri = "/game/recharge/reset")
    public void reset(HttpRequest request, HttpResponse response) throws Exception {
        JsonObject params = HttpUtils.abstractGMParams(request.getRequestBody());
        String goodsid = HttpUtils.getString(params, "goodsid");

        RefRecharge ref = (RefRecharge) RefDataMgr.get(RefRecharge.class, goodsid);
        if (ref == null) {
            response.error(30011, "并无商品[%s]", new Object[]{goodsid});
            return;
        }
        RechargeMgr.getInstance().reset(goodsid);
        JsonObject rtn = new JsonObject();
        rtn.addProperty("state", Integer.valueOf(1000));
        rtn.addProperty("msg", "success");
        response.response(rtn.toString());
    }
}

