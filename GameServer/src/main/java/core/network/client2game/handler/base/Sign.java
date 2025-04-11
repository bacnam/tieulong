package core.network.client2game.handler.base;

import com.google.gson.Gson;
import com.zhonglian.server.common.utils.secure.MD5;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.BaseHandler;
import core.server.ServerConfig;

import java.io.IOException;
import java.util.List;

public class Sign
        extends BaseHandler {
    public void handle(WebSocketRequest request, String data) throws IOException {
        Request req = (Request) (new Gson()).fromJson(data, Request.class);
        StringBuilder sb = new StringBuilder();
        for (Pair pair : req.params) {
            sb.append(pair.key).append('=').append(pair.value).append('&');
        }
        sb.append("secret_key").append('=').append(ServerConfig.AAY_SecretKey());

        Response response = new Response(null);
        response.sign = MD5.md5(sb.toString());
        request.response(response);
    }

    private static class Pair {
        String key;
        String value;
    }

    private static class Request {
        List<Sign.Pair> params;
    }

    private static class Response {
        String sign;

        private Response() {
        }
    }
}

