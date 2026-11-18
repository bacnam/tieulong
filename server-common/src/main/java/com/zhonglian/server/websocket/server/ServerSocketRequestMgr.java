package com.zhonglian.server.websocket.server;

import BaseThread.BaseMutexObject;
import com.zhonglian.server.websocket.handler.response.ResponseHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerSocketRequestMgr {
    private static short MAX_REQUEST_LENGTH = 16383;
    private final ServerSession session;
    private String opcode;
    private short sequence = 0;
    private BaseMutexObject seqMutex = new BaseMutexObject();
    private Map<Short, ServerSocketRequest> requests = new HashMap<>();

    public ServerSocketRequestMgr(ServerSession session, String operation) {
        this.session = session;
        this.opcode = operation;
        this.seqMutex.reduceMutexLevel(10);
    }

    public ServerSocketRequest popRequest(short sequence) {
        ServerSocketRequest request = null;

        try {
            this.seqMutex.lock();
            request = this.requests.remove(sequence);
        } finally {
            this.seqMutex.unlock();
        }

        return request;
    }

    public ServerSocketRequest genRequest(ResponseHandler handler) {
        if (this.requests.size() >= MAX_REQUEST_LENGTH) {
            return null;
        } else {
            ServerSocketRequest request = null;
            this.seqMutex.lock();

            try {
                do {
                    this.sequence++;
                    this.sequence = this.sequence >= MAX_REQUEST_LENGTH ? 0 : this.sequence;
                } while (this.requests.get(this.sequence) != null);

                request = new ServerSocketRequest(this.session, this.opcode, this.sequence, handler);
                this.requests.put(this.sequence, request);
                return request;
            } finally {
                this.seqMutex.unlock();
            }
        }
    }

    public void checkTimeoutRequest() {
        List<Short> timeoutList = new ArrayList<>();
        this.seqMutex.lock();
        List<ServerSocketRequest> requestLists = new ArrayList<>(this.requests.values());
        this.seqMutex.unlock();

        for (ServerSocketRequest request : requestLists) {
            if (request.isTimeout()) {
                timeoutList.add(request.getSequence());
            }
        }

        for (Short req : timeoutList) {
            ServerSocketRequest requestx = this.popRequest(req);
            if (requestx != null) {
                requestx.expired();
            }
        }
    }
}
