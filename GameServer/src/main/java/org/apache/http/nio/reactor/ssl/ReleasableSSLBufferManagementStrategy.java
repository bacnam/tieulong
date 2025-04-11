package org.apache.http.nio.reactor.ssl;

import org.apache.http.util.Args;

import java.nio.ByteBuffer;

public class ReleasableSSLBufferManagementStrategy
        implements SSLBufferManagementStrategy {
    public SSLBuffer constructBuffer(int size) {
        return new InternalBuffer(size);
    }

    private static final class InternalBuffer
            implements SSLBuffer {
        private final int length;
        private ByteBuffer wrapped;

        public InternalBuffer(int size) {
            Args.positive(size, "size");
            this.length = size;
        }

        public ByteBuffer acquire() {
            if (this.wrapped != null) {
                return this.wrapped;
            }
            this.wrapped = ByteBuffer.allocate(this.length);
            return this.wrapped;
        }

        public void release() {
            this.wrapped = null;
        }

        public boolean isAcquired() {
            return (this.wrapped != null);
        }

        public boolean hasData() {
            return (this.wrapped != null && this.wrapped.position() > 0);
        }
    }
}

