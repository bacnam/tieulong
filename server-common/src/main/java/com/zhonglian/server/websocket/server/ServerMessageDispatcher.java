package com.zhonglian.server.websocket.server;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import com.zhonglian.server.websocket.IMessageDispatcher;
import com.zhonglian.server.websocket.def.MessageType;
import com.zhonglian.server.websocket.handler.MessageDispatcher;
import com.zhonglian.server.websocket.handler.MessageHeader;
import com.zhonglian.server.websocket.handler.requset.RequestHandler;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ServerMessageDispatcher<Session extends ServerSession> implements IMessageDispatcher<Session> {
    protected final Map<MessageType, MessageDispatcher<Session>> _messageDispatcher = new HashMap<>();

    @Override
    public abstract void init();

    public int handleRawMessage(Session session, ByteBuffer stream) {
        MessageHeader header = new MessageHeader();
        header.messageType = MessageType.values()[stream.get()];
        header.srcType = stream.get();
        header.srcId = stream.getLong();
        header.descType = stream.get();
        header.descId = stream.getLong();
        MessageDispatcher<Session> dispatcher = this._messageDispatcher.get(header.messageType);
        if (dispatcher == null) {
            CommLog.error("[WSBaseSocketListener] 协议类型错误 类型:{},消息頭部:{}", header.messageType, header);
            return -1;
        } else {
            try {
                byte[] event = new byte[stream.getShort()];
                stream.get(event);
                header.event = new String(event, "UTF-8").toLowerCase();
                header.sequence = stream.getShort();
                header.errcode = stream.getShort();
                byte[] msg = new byte[stream.getShort()];
                stream.get(msg);
                return dispatcher.dispatch(session, header, new String(msg, "UTF-8"));
            } catch (Exception var7) {
                CommLog.error("[WSBaseSocketListener] 协议处理协议信息处理时错误:", (Throwable)var7);
                return -2;
            }
        }
    }

    public void putDispatcher(MessageType messageType, MessageDispatcher<Session> dispatcher) {
        this._messageDispatcher.put(messageType, dispatcher);
    }

    public void addRequestHandler(RequestHandler handler) {
        this._messageDispatcher.get(MessageType.Request).addHandler(handler);
    }

    public void registerRequestHandlers(Class<? extends RequestHandler> clazz) {
        List<Class<?>> dealers = CommClass.getAllClassByInterface(clazz, clazz.getPackage().getName());
        int regCnt = 0;

        for (Class<?> cs : dealers) {
            RequestHandler dealer = null;

            try {
                dealer = (RequestHandler)CommClass.forName(cs.getName()).newInstance();
            } catch (Exception var8) {
                CommLog.error("ServerMessageDispatcher register handler occured error:{}", var8.getMessage(), var8);
            }

            if (dealer != null) {
                this.addRequestHandler(dealer);
                regCnt++;
            }
        }

        CommLog.info("ServerMessageDispatcher registerHandler count:{}", regCnt);
    }
}
