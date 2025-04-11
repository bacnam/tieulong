package org.apache.mina.core.session;

import org.apache.mina.core.file.FileRegion;
import org.apache.mina.core.filterchain.DefaultIoFilterChain;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.service.*;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestQueue;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

public class DummySession
        extends AbstractIoSession {
    private static final TransportMetadata TRANSPORT_METADATA = (TransportMetadata) new DefaultTransportMetadata("mina", "dummy", false, false, SocketAddress.class, IoSessionConfig.class, new Class[]{Object.class});

    private static final SocketAddress ANONYMOUS_ADDRESS = new SocketAddress() {
        private static final long serialVersionUID = -496112902353454179L;

        public String toString() {
            return "?";
        }
    };
    private final IoFilterChain filterChain = (IoFilterChain) new DefaultIoFilterChain(this);
    private final IoProcessor<IoSession> processor;
    private volatile IoService service;
    private volatile IoSessionConfig config = new AbstractIoSessionConfig() {
        protected void doSetAll(IoSessionConfig config) {
        }
    };
    private volatile IoHandler handler = (IoHandler) new IoHandlerAdapter();

    private volatile SocketAddress localAddress = ANONYMOUS_ADDRESS;

    private volatile SocketAddress remoteAddress = ANONYMOUS_ADDRESS;

    private volatile TransportMetadata transportMetadata = TRANSPORT_METADATA;

    public DummySession() {
        super((IoService) new AbstractIoAcceptor(new AbstractIoSessionConfig() {
            protected void doSetAll(IoSessionConfig config) {
            }
        }, new Executor() {

            public void execute(Runnable command) {
            }
        }) {

            protected Set<SocketAddress> bindInternal(List<? extends SocketAddress> localAddresses) throws Exception {
                throw new UnsupportedOperationException();
            }

            protected void unbind0(List<? extends SocketAddress> localAddresses) throws Exception {
                throw new UnsupportedOperationException();
            }

            public IoSession newSession(SocketAddress remoteAddress, SocketAddress localAddress) {
                throw new UnsupportedOperationException();
            }

            public TransportMetadata getTransportMetadata() {
                return DummySession.TRANSPORT_METADATA;
            }

            protected void dispose0() throws Exception {
            }

            public IoSessionConfig getSessionConfig() {
                return this.sessionConfig;
            }
        });

        this.processor = new IoProcessor<IoSession>() {
            public void add(IoSession session) {
            }

            public void flush(IoSession session) {
                DummySession s = (DummySession) session;
                WriteRequest req = s.getWriteRequestQueue().poll(session);

                if (req != null) {
                    Object m = req.getMessage();
                    if (m instanceof FileRegion) {
                        FileRegion file = (FileRegion) m;
                        try {
                            file.getFileChannel().position(file.getPosition() + file.getRemainingBytes());
                            file.update(file.getRemainingBytes());
                        } catch (IOException e) {
                            s.getFilterChain().fireExceptionCaught(e);
                        }
                    }
                    DummySession.this.getFilterChain().fireMessageSent(req);
                }
            }

            public void write(IoSession session, WriteRequest writeRequest) {
                WriteRequestQueue writeRequestQueue = session.getWriteRequestQueue();

                writeRequestQueue.offer(session, writeRequest);

                if (!session.isWriteSuspended()) {
                    flush(session);
                }
            }

            public void remove(IoSession session) {
                if (!session.getCloseFuture().isClosed()) {
                    session.getFilterChain().fireSessionClosed();
                }
            }

            public void updateTrafficControl(IoSession session) {
            }

            public void dispose() {
            }

            public boolean isDisposed() {
                return false;
            }

            public boolean isDisposing() {
                return false;
            }
        };

        this.service = super.getService();

        try {
            IoSessionDataStructureFactory factory = new DefaultIoSessionDataStructureFactory();
            setAttributeMap(factory.getAttributeMap(this));
            setWriteRequestQueue(factory.getWriteRequestQueue(this));
        } catch (Exception e) {
            throw new InternalError();
        }
    }

    public IoSessionConfig getConfig() {
        return this.config;
    }

    public void setConfig(IoSessionConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("config");
        }

        this.config = config;
    }

    public IoFilterChain getFilterChain() {
        return this.filterChain;
    }

    public IoHandler getHandler() {
        return this.handler;
    }

    public void setHandler(IoHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("handler");
        }

        this.handler = handler;
    }

    public SocketAddress getLocalAddress() {
        return this.localAddress;
    }

    public void setLocalAddress(SocketAddress localAddress) {
        if (localAddress == null) {
            throw new IllegalArgumentException("localAddress");
        }

        this.localAddress = localAddress;
    }

    public SocketAddress getRemoteAddress() {
        return this.remoteAddress;
    }

    public void setRemoteAddress(SocketAddress remoteAddress) {
        if (remoteAddress == null) {
            throw new IllegalArgumentException("remoteAddress");
        }

        this.remoteAddress = remoteAddress;
    }

    public IoService getService() {
        return this.service;
    }

    public void setService(IoService service) {
        if (service == null) {
            throw new IllegalArgumentException("service");
        }

        this.service = service;
    }

    public final IoProcessor<IoSession> getProcessor() {
        return this.processor;
    }

    public TransportMetadata getTransportMetadata() {
        return this.transportMetadata;
    }

    public void setTransportMetadata(TransportMetadata transportMetadata) {
        if (transportMetadata == null) {
            throw new IllegalArgumentException("transportMetadata");
        }

        this.transportMetadata = transportMetadata;
    }

    public void setScheduledWriteBytes(int byteCount) {
        super.setScheduledWriteBytes(byteCount);
    }

    public void setScheduledWriteMessages(int messages) {
        super.setScheduledWriteMessages(messages);
    }

    public void updateThroughput(boolean force) {
        updateThroughput(System.currentTimeMillis(), force);
    }
}

