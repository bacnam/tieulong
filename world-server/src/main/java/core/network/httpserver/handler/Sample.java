package core.network.httpserver.handler;

import BaseCommon.CommLog;
import com.zhonglian.server.common.utils.secure.MD5;
import com.zhonglian.server.http.annotation.RequestMapping;
import com.zhonglian.server.http.server.HttpMethod;
import com.zhonglian.server.http.server.HttpRequest;
import com.zhonglian.server.http.server.HttpResponse;
import com.zhonglian.server.http.server.HttpUtils;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class Sample {
    private Set<String> orders = new HashSet<>();

    @RequestMapping(
        uri = "/game/recharge_notify",
        method = {HttpMethod.GET}
    )
    public void recharge(HttpRequest request, HttpResponse response) {
        Map<String, String> postParam = HttpUtils.abstractHttpParams(request.getRequestBody());
        String sign = postParam.remove("sign");
        if (sign == null) {
            response.error(30001, "param [sign] miss");
        } else {
            String sign_Src = this.getParamStr(postParam) + "nhUeW6FY29MOXrHjx7nkUAPMdfJ8Sosq";
            String md5 = MD5.md5(sign_Src);
            if (!md5.equals(sign)) {
                CommLog.info("src:[{}], md5:[{}], sign:[{}]", sign_Src, md5, sign);
                response.error(30002, "wrong sign value");
            } else {
                String orderid = postParam.get("cporderid");
                if (orderid == null) {
                    response.error(30101, "param [order] miss");
                } else if (this.orders.contains(orderid)) {
                    response.error(30102, "order duplicate");
                } else {
                    CommLog.info("recharge:{}", postParam.toString());
                    this.orders.add(orderid);
                    String rtn = String.format("{\"state\":1000,\"msg\":\"success\",\"cporderid\":\"%s\"}", orderid);
                    response.response(rtn);
                }
            }
        }
    }

    private String getParamStr(Map<String, String> param) {
        StringBuilder sb = new StringBuilder();

        for (Entry<String, String> pair : param.entrySet()) {
            sb.append(pair.getKey()).append("=").append(pair.getValue()).append("&");
        }

        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
