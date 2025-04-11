package org.apache.thrift;

import org.apache.thrift.transport.TTransport;

public class TProcessorFactory {

    private final TProcessor processor_;

    public TProcessorFactory(TProcessor processor) {
        processor_ = processor;
    }

    public TProcessor getProcessor(TTransport trans) {
        return processor_;
    }
}
