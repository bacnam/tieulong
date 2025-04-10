package core.network.client2game.handler.base;

import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.BaseHandler;
import java.io.IOException;

public class ServerInfo
extends BaseHandler
{
private static class Info {
private Info() {}

int nowTime = CommTime.nowSecond();
int timeZone = CommTime.timezone().getRawOffset();
String version = "0.0.0";
}

public void handle(WebSocketRequest request, String data) throws IOException {
request.response(new Info(null));
}
}

