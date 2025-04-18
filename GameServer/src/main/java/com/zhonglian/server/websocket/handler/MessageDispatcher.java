package com.zhonglian.server.websocket.handler;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import com.zhonglian.server.websocket.def.TerminalType;
import com.zhonglian.server.websocket.server.ServerSession;

public abstract class MessageDispatcher<Session extends ServerSession>
{
protected int thisServerId;
protected int thisServerType;

public MessageDispatcher(TerminalType thisServerType, int thisServerId) {
this.thisServerType = thisServerType.ordinal();
this.thisServerId = thisServerId;
}

public int dispatch(Session session, MessageHeader header, String message) {
if (header == null) {
CommLog.error("[MessageHandler]解析协议头部信息时发生错误,返回头部信息为空。");
return -1;
} 
SyncTaskManager.task(() -> {
try {
if (this.thisServerId == paramMessageHeader.descId || this.thisServerType == paramMessageHeader.descType) {
handle((Session)paramServerSession, paramMessageHeader, paramString);
} else {
forward((Session)paramServerSession, paramMessageHeader, paramString);
} 

paramServerSession.onReceived(paramMessageHeader, paramString);
} catch (Exception e) {
String handleName = String.valueOf(getClass().getSimpleName()) + " with [" + paramMessageHeader.event + "]";
CommLog.error("exception in NetHandlerContainerAdaptor: op:{}, sub:{}, error:", new Object[] { paramMessageHeader.event, handleName, e });
} 
});
return 0;
}

public abstract void handle(Session paramSession, MessageHeader paramMessageHeader, String paramString);

public abstract void forward(Session paramSession, MessageHeader paramMessageHeader, String paramString);

public abstract void addHandler(IBaseHandler paramIBaseHandler);
}

