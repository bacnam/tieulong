package com.zhonglian.server.websocket.handler;

import com.zhonglian.server.websocket.def.MessageType;

public class MessageHeader {
    public MessageType messageType;
    public byte srcType;
    public long srcId;
    public byte descType;
    public long descId;
    public String event;
    public short sequence;
    public short errcode;

    public MessageHeader genResponseHeader(short errorCode) {
        MessageHeader header = new MessageHeader();
        header.messageType = MessageType.Response;

        header.srcType = this.descType;
        header.srcId = this.descId;
        header.descType = this.srcType;
        header.descId = this.srcId;

        header.event = this.event;
        header.sequence = this.sequence;
        header.errcode = errorCode;
        return header;
    }

    public MessageHeader genResponseHeader() {
        return genResponseHeader((short) 0);
    }
}

