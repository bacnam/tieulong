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

    @RequestMapping(
        uri = "/game/recharge/notify"
    )
    public void recharge(HttpRequest request, HttpResponse response) throws Exception {
        CommLog.info("收到充值回调：", request.getRequestBody());
        synchronized (this.Lock) {
            JsonObject params = HttpUtils.abstractGMParams(request.getRequestBody(), "cYUPYnZgCiy0zUBg9KRRXM7H4GUsRKpI");
            int ordertype = HttpUtils.getInt(params, "ordertype");
            if (ordertype != 1) {
                response.error(30011, "暂不支持退单功能");
            } else {
                long cid = HttpUtils.getLong(params, "cid");
                Player player = PlayerMgr.getInstance().getPlayer(cid);
                if (player == null) {
                    response.error(30011, "该服务器上无该玩家[%s]", cid);
                } else {
                    SyncTaskManager.task(() -> {
                        try {
                            String cporderid = HttpUtils.getString(params, "cporderid");
                            RechargeOrderBO bo = BM.getBM(RechargeOrderBO.class).findOne("cporderid", cporderid);
                            if (bo != null && bo.getStatus().equals(RechargeMgr.OrderStatus.Delivered.toString())) {
                                response.error(1000, "已经处理过该订单[%s]", cporderid);
                                return;
                            }

                            if (bo == null) {
                                bo = new RechargeOrderBO();
                                bo.setCporderid(cporderid);
                                bo.setCarrier(HttpUtils.getString(params, "carrier"));
                                bo.setPlatform(HttpUtils.getString(params, "platform"));
                                bo.setAdfrom(HttpUtils.getString(params, "adfrom"));
                                bo.setAdfrom2(HttpUtils.getString(params, "adfrom2"));
                                bo.setGameid(HttpUtils.getString(params, "gameid"));
                                bo.setServerID(HttpUtils.getString(params, "server_id"));
                                bo.setAppID(HttpUtils.getString(params, "appid"));
                                bo.setOpenId(HttpUtils.getString(params, "open_id"));
                                bo.setPid(cid);
                                bo.setAdfromOrderid(HttpUtils.getString(params, "adfrom_orderid"));
                                bo.setQuantity(HttpUtils.getInt(params, "quantity"));
                                bo.setProductid(HttpUtils.getString(params, "productid"));
                                bo.setOrderTime(CommTime.nowSecond());
                                bo.setStatus(RechargeMgr.OrderStatus.Paid.toString());
                                bo.insert();
                                RechargeMgr.getInstance().rechargeLog(player, bo);
                            }

                            if (player.isOnline()) {
                                RechargeMgr.getInstance().sendPrize(bo);
                            } else {
                                RechargeMgr.getInstance().cacheOrder(bo);
                            }

                            JsonObject rtn = new JsonObject();
                            rtn.addProperty("state", 1000);
                            rtn.addProperty("msg", "success");
                            rtn.addProperty("cporderid", cporderid);
                            rtn.addProperty("balance", player.getPlayerBO().getCrystal());
                            rtn.addProperty("uid", player.getPlayerBO().getOpenId());
                            response.response(rtn.toString());
                        } catch (RequestException var8x) {
                            Throwable cause = var8x.getCause();
                            if (cause != null && cause instanceof RequestException) {
                                RequestException re = (RequestException)cause;
                                response.error(re.getCode(), re.getMessage());
                            } else {
                                response.error(300001, "服务器发生未知错误，错误信息：%s", var8x.getMessage());
                            }
                        }
                    });
                }
            }
        }
    }

    @RequestMapping(
        uri = "/game/recharge/reset"
    )
    public void reset(HttpRequest request, HttpResponse response) throws Exception {
        JsonObject params = HttpUtils.abstractGMParams(request.getRequestBody());
        String goodsid = HttpUtils.getString(params, "goodsid");
        RefRecharge ref = RefDataMgr.get(RefRecharge.class, goodsid);
        if (ref == null) {
            response.error(30011, "并无商品[%s]", goodsid);
        } else {
            RechargeMgr.getInstance().reset(goodsid);
            JsonObject rtn = new JsonObject();
            rtn.addProperty("state", 1000);
            rtn.addProperty("msg", "success");
            response.response(rtn.toString());
        }
    }
}
