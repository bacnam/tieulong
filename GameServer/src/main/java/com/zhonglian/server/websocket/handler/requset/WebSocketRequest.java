package com.zhonglian.server.websocket.handler.requset;

import BaseCommon.CommLog;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.handler.MessageHeader;
import com.zhonglian.server.websocket.server.ServerSession;

public class WebSocketRequest
{
private ServerSession session;
private MessageHeader header;

public WebSocketRequest(ServerSession session, MessageHeader header) {
this.session = session;
this.header = header;
}

public void response(Object protocol) {
if (protocol == null) {
protocol = "{}";
}
this.session.sendResponse(this.header, protocol);
}

public void response(String protocol) {
this.session.sendResponse(this.header, protocol);
}

public void response() {
this.session.sendResponse(this.header, "{}");
}

public void error(short errorcode, String format, Object... params) {
String message = null;
if (format == null) {
message = "null";
} else {
try {
message = String.format(format, params);
} catch (Exception e) {
CommLog.error("[WebSocketRequest]格式化错误字符串:{},时错误", e);
} 
} 
this.session.sendError(this.header, errorcode, message);
}

public void error(ErrorCode errorcode, String format, Object... params) {
error(errorcode.value(), format, params);
}

public ServerSession getSession() {
return this.session;
}

public int getRemoteServerID() {
return this.session.getRemoteServerID();
}

public MessageHeader getHeader() {
return this.header;
}
}

