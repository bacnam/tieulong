package org.apache.mina.filter.stream;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractStreamWriteFilter<T>
        extends IoFilterAdapter {
    public static final int DEFAULT_STREAM_BUFFER_SIZE = 4096;
    protected final AttributeKey CURRENT_STREAM = new AttributeKey(getClass(), "stream");

    protected final AttributeKey WRITE_REQUEST_QUEUE = new AttributeKey(getClass(), "queue");

    protected final AttributeKey CURRENT_WRITE_REQUEST = new AttributeKey(getClass(), "writeRequest");

    private int writeBufferSize = 4096;

    public void onPreAdd(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
        Class<? extends IoFilterAdapter> clazz = (Class) getClass();
        if (parent.contains(clazz)) {
            throw new IllegalStateException("Only one " + clazz.getName() + " is permitted.");
        }
    }

    public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
        if (session.getAttribute(this.CURRENT_STREAM) != null) {
            Queue<WriteRequest> queue = getWriteRequestQueue(session);
            if (queue == null) {
                queue = new ConcurrentLinkedQueue<WriteRequest>();
                session.setAttribute(this.WRITE_REQUEST_QUEUE, queue);
            }
            queue.add(writeRequest);

            return;
        }
        Object message = writeRequest.getMessage();

        if (getMessageClass().isInstance(message)) {

            T stream = getMessageClass().cast(message);

            IoBuffer buffer = getNextBuffer(stream);
            if (buffer == null) {

                writeRequest.getFuture().setWritten();
                nextFilter.messageSent(session, writeRequest);
            } else {
                session.setAttribute(this.CURRENT_STREAM, message);
                session.setAttribute(this.CURRENT_WRITE_REQUEST, writeRequest);

                nextFilter.filterWrite(session, (WriteRequest) new DefaultWriteRequest(buffer));
            }
        } else {

            nextFilter.filterWrite(session, writeRequest);
        }
    }

    protected abstract Class<T> getMessageClass();

    private Queue<WriteRequest> getWriteRequestQueue(IoSession session) {
        return (Queue<WriteRequest>) session.getAttribute(this.WRITE_REQUEST_QUEUE);
    }

    private Queue<WriteRequest> removeWriteRequestQueue(IoSession session) {
        return (Queue<WriteRequest>) session.removeAttribute(this.WRITE_REQUEST_QUEUE);
    }

    public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
        T stream = getMessageClass().cast(session.getAttribute(this.CURRENT_STREAM));

        if (stream == null) {
            nextFilter.messageSent(session, writeRequest);
        } else {
            IoBuffer buffer = getNextBuffer(stream);

            if (buffer == null) {

                session.removeAttribute(this.CURRENT_STREAM);
                WriteRequest currentWriteRequest = (WriteRequest) session.removeAttribute(this.CURRENT_WRITE_REQUEST);

                Queue<WriteRequest> queue = removeWriteRequestQueue(session);
                if (queue != null) {
                    WriteRequest wr = queue.poll();
                    while (wr != null) {
                        filterWrite(nextFilter, session, wr);
                        wr = queue.poll();
                    }
                }

                currentWriteRequest.getFuture().setWritten();
                nextFilter.messageSent(session, currentWriteRequest);
            } else {
                nextFilter.filterWrite(session, (WriteRequest) new DefaultWriteRequest(buffer));
            }
        }
    }

    public int getWriteBufferSize() {
        return this.writeBufferSize;
    }

    public void setWriteBufferSize(int writeBufferSize) {
        if (writeBufferSize < 1) {
            throw new IllegalArgumentException("writeBufferSize must be at least 1");
        }
        this.writeBufferSize = writeBufferSize;
    }

    protected abstract IoBuffer getNextBuffer(T paramT) throws IOException;
}

