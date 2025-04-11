package com.zhonglian.server.websocket;

import BaseCommon.CommLog;
import com.zhonglian.server.websocket.def.MessageType;
import com.zhonglian.server.websocket.handler.MessageHeader;

import java.util.LinkedList;
import java.util.Queue;

public class PkgCacheMgr {
    private final int MaxCachedCnt = 30;

    private Queue<PkgRecv> recvs = new LinkedList<>();

    public synchronized void cacheSent(MessageHeader header, String body) {
        String key = String.format("%s@%s", new Object[]{header.event, Short.valueOf(header.sequence)});
        for (PkgRecv recv : this.recvs) {
            if (recv.getKey().equals(key)) {
                recv.setResponse(header, body);
                return;
            }
        }
    }

    public synchronized PkgRecv fetchSentOrRegist(MessageHeader header) {
        if (header.messageType == MessageType.Notify) {
            return null;
        }
        String key = String.format("%s@%s", new Object[]{header.event, Short.valueOf(header.sequence)});
        for (PkgRecv recv : this.recvs) {
            if (recv.getKey().equals(key)) {
                return recv;
            }
        }

        this.recvs.add(new PkgRecv(key, header));

        if (this.recvs.size() > 30) {
            ((PkgRecv) this.recvs.poll()).onRemove();
        }
        return null;
    }

    public class PkgRecv {
        private String key;
        private MessageHeader header;
        private String body;

        public PkgRecv(String key, MessageHeader header) {
            this.key = key;
            this.header = header;
        }

        public void setResponse(MessageHeader header, String body) {
            this.header = header;
            this.body = body;
        }

        public String getKey() {
            return this.key;
        }

        public void onRemove() {
            if (this.body == null) {
                CommLog.error("PkgRecv not sendBack : {}", this.key);
            }
        }

        public MessageHeader getHeader() {
            return this.header;
        }

        public String getBody() {
            return this.body;
        }

        public String toString() {
            return this.key;
        }
    }
}

