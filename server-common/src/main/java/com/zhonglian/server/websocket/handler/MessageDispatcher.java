package com.zhonglian.server.websocket.handler;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import com.zhonglian.server.websocket.def.TerminalType;
import com.zhonglian.server.websocket.server.ServerSession;

public abstract class MessageDispatcher<Session extends ServerSession> {
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
        } else {
            SyncTaskManager.task(() -> {
                try {
                    if (this.thisServerId != header.descId && this.thisServerType != header.descType) {
                        this.forward(session, header, message);
                    } else {
                        this.handle(session, header, message);
                    }

                    session.onReceived(header, message);
                } catch (Exception var6) {
                    String handleName = this.getClass().getSimpleName() + " with [" + header.event + "]";
                    CommLog.error("exception in NetHandlerContainerAdaptor: op:{}, sub:{}, error:", header.event, handleName, var6);
                }
            });
            return 0;
        }
    }

    public abstract void handle(Session var1, MessageHeader var2, String var3);

    public abstract void forward(Session var1, MessageHeader var2, String var3);

    public abstract void addHandler(IBaseHandler var1);
}
