package core.network.client2game.handler;

import BaseCommon.CommLog;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.RequestHandler;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import java.io.IOException;

public abstract class BaseHandler
extends RequestHandler
{
public void handleMessage(WebSocketRequest request, String data) throws WSException, IOException {
if (!(request.getSession() instanceof core.network.client2game.ClientSession)) {
CommLog.warn("{} not handled.", (request.getHeader()).event);

return;
} 

try {
handle(request, data);
} catch (Throwable e) {
CommLog.error(String.valueOf(getClass().getName()) + " Exception: ", e);
request.error(ErrorCode.Unknown, e.toString(), new Object[0]);
} 
}

public abstract void handle(WebSocketRequest paramWebSocketRequest, String paramString) throws IOException;
}

