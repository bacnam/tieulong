package com.zhonglian.server.websocket.server;

import BaseCommon.CommLog;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.BaseSession;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.def.MessageType;
import com.zhonglian.server.websocket.def.TerminalType;
import com.zhonglian.server.websocket.handler.MessageHeader;
import com.zhonglian.server.websocket.handler.response.ResponseHandler;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.mina.core.session.IoSession;

public abstract class ServerSession extends BaseSession {
    private Map<String, ServerSocketRequestMgr> requestMgrs = new HashMap<>();
    private int remoteServerID = -1;
    private TerminalType remoteServerType;
    private int localServerid;
    private TerminalType localServerType;

    public ServerSession(TerminalType localServerType, int localServerid, TerminalType remoteServerType, IoSession session, long sessionID) {
        super(session, sessionID);
        this.localServerType = localServerType;
        this.localServerid = localServerid;
        this.remoteServerType = remoteServerType;
    }

    public void setRemoteServerID(int serverID) {
        this.remoteServerID = serverID;
    }

    public int getRemoteServerID() {
        return this.remoteServerID;
    }

    public TerminalType getRemoteServerType() {
        return this.remoteServerType;
    }

    public int getLocalServerid() {
        return this.localServerid;
    }

    private ServerSocketRequestMgr getRequestMgr(String opcode) {
        opcode = opcode.toLowerCase();
        ServerSocketRequestMgr requestMgr = this.requestMgrs.get(opcode);
        if (requestMgr == null) {
            synchronized (this.requestMgrs) {
                requestMgr = this.requestMgrs.get(opcode);
                if (requestMgr == null) {
                    requestMgr = new ServerSocketRequestMgr(this, opcode);
                    this.requestMgrs.put(opcode, requestMgr);
                }
            }
        }

        return requestMgr;
    }

    public void request(TerminalType desctype, long descid, String opcode, Object protocol, ResponseHandler handler) {
        ServerSocketRequest request = this.getRequestMgr(opcode).genRequest(handler);
        if (request != null) {
            this.sendRequest(MessageType.Request, desctype, descid, opcode, request.getSequence(), protocol);
        } else {
            CommLog.error("[ServerSession]消息请求队列已满，发送失败!");
        }
    }

    public void request(String opcode, Object protocol, ResponseHandler handler) {
        this.request(this.remoteServerType, this.remoteServerID, opcode, protocol, handler);
    }

    public void notifyMessage(TerminalType serverType, long serverid, String opcode, Object protocol) {
        this.sendRequest(MessageType.Notify, serverType, serverid, opcode, (short)-1, protocol);
    }

    public void notifyMessage(String opcode, Object protocol) {
        this.notifyMessage(this.remoteServerType, this.remoteServerID, opcode, protocol);
    }

    public ServerSocketRequest popRequest(String opcode, short sequence) {
        return this.getRequestMgr(opcode).popRequest(sequence);
    }

    public void checkTimeoutRequest() {
        for (ServerSocketRequestMgr requestMgr : new ArrayList<>(this.requestMgrs.values())) {
            requestMgr.checkTimeoutRequest();
        }
    }

    public void sendRequest(MessageType messageType, TerminalType desctype, long descId, String opcode, short sequence, Object proto) {
        MessageHeader header = new MessageHeader();
        header.messageType = messageType;
        header.srcType = this.localServerType.value();
        header.srcId = this.localServerid;
        header.descType = (byte)desctype.ordinal();
        header.descId = descId;
        header.event = opcode;
        header.sequence = sequence;
        this.sendPacket(header, proto);
    }

    public void sendResponse(MessageHeader srcHeader, String body) {
        this.sendPacket(srcHeader.genResponseHeader(), body);
    }

    public void sendResponse(MessageHeader srcHeader, Object body) {
        this.sendPacket(srcHeader.genResponseHeader(), body);
    }

    public void sendError(MessageHeader srcHeader, ErrorCode errorCode, String message) {
        this.sendPacket(srcHeader.genResponseHeader(errorCode.value()), message);
    }

    public void sendError(MessageHeader srcHeader, short errorCode, String message) {
        this.sendPacket(srcHeader.genResponseHeader(errorCode), message);
    }

    public void forward(MessageHeader header, Object body) {
        this.sendPacket(header, body);
    }

    public void forward(MessageHeader header, String body) {
        this.sendPacket(header, body);
    }

    private void sendPacket(MessageHeader header, Object body) {
        this.sendPacket(header, new Gson().toJson(body));
    }

    @Deprecated
    public void sendPacket(MessageHeader header, String body) {
        try {
            byte[] event = header.event.getBytes("utf-8");
            byte[] bodybytes = body.getBytes("utf-8");
            if (bodybytes.length > 65535) {
                CommLog.error(
                    "发送协议过长无法发送. srctype:{}, srcid:{}, event:{}, sequance:{}, body:{}", header.srcType, header.srcId, header.event, header.sequence, body
                );
                return;
            }

            int length = 21 + event.length + 2 + 2 + 2 + bodybytes.length;
            ByteBuffer buff = ByteBuffer.allocate(length);
            buff.put((byte)header.messageType.ordinal());
            buff.put(header.srcType);
            buff.putLong(header.srcId);
            buff.put(header.descType);
            buff.putLong(header.descId);
            buff.putShort((short)event.length);
            buff.put(event);
            buff.putShort(header.sequence);
            buff.putShort(header.errcode);
            buff.putShort((short)bodybytes.length);
            buff.put(bodybytes);
            ((Buffer)buff).flip();
            this.session.write(buff);
            this.onSendPacket(header, body);
        } catch (Exception var7) {
            CommLog.error(
                "发送协议发送错误. srctype:{}, srcid:{}, event:{}, sequance:{}, body:{}", header.srcType, header.srcId, header.event, header.sequence, body, var7
            );
        }
    }

    protected void onSendPacket(MessageHeader header, String body) {
    }
}
