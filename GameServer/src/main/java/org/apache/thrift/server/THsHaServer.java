

package org.apache.thrift.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class THsHaServer extends TNonblockingServer {
  private static final Logger LOGGER =
    LoggerFactory.getLogger(THsHaServer.class.getName());

  private ExecutorService invoker;

  public THsHaServer( TProcessor processor,
                      TNonblockingServerTransport serverTransport) {
    this(processor, serverTransport, new Options());
  }

  public THsHaServer( TProcessor processor,
                      TNonblockingServerTransport serverTransport,
                      Options options) {
    this(new TProcessorFactory(processor), serverTransport, options);
  }

  public THsHaServer( TProcessorFactory processorFactory,
                      TNonblockingServerTransport serverTransport) {
    this(processorFactory, serverTransport, new Options());
  }

  public THsHaServer( TProcessorFactory processorFactory,
                      TNonblockingServerTransport serverTransport,
                      Options options) {
    this(processorFactory, serverTransport, new TFramedTransport.Factory(),
      new TBinaryProtocol.Factory(), options);
  }

  public THsHaServer( TProcessor processor,
                      TNonblockingServerTransport serverTransport,
                      TProtocolFactory protocolFactory) {
    this(processor, serverTransport, protocolFactory, new Options());
  }

  public THsHaServer( TProcessor processor,
                      TNonblockingServerTransport serverTransport,
                      TProtocolFactory protocolFactory,
                      Options options) {
    this(new TProcessorFactory(processor), serverTransport,
      new TFramedTransport.Factory(),
      protocolFactory, protocolFactory, 
      options);
  }

  public THsHaServer( TProcessor processor,
                      TNonblockingServerTransport serverTransport,
                      TFramedTransport.Factory transportFactory,
                      TProtocolFactory protocolFactory) {
    this(new TProcessorFactory(processor), serverTransport,
      transportFactory, protocolFactory);
  }

  public THsHaServer( TProcessorFactory processorFactory,
                      TNonblockingServerTransport serverTransport,
                      TFramedTransport.Factory transportFactory,
                      TProtocolFactory protocolFactory) {
    this(processorFactory, serverTransport,
      transportFactory,
      protocolFactory, protocolFactory, new Options());
  }

  public THsHaServer( TProcessorFactory processorFactory,
                      TNonblockingServerTransport serverTransport,
                      TFramedTransport.Factory transportFactory,
                      TProtocolFactory protocolFactory,
                      Options options) {
    this(processorFactory, serverTransport,
      transportFactory,
      protocolFactory, protocolFactory,
      options);
  }

  public THsHaServer( TProcessor processor,
                      TNonblockingServerTransport serverTransport,
                      TFramedTransport.Factory outputTransportFactory,
                      TProtocolFactory inputProtocolFactory,
                      TProtocolFactory outputProtocolFactory) {
    this(new TProcessorFactory(processor), serverTransport,
      outputTransportFactory,
      inputProtocolFactory, outputProtocolFactory);
  }

  public THsHaServer( TProcessorFactory processorFactory,
                      TNonblockingServerTransport serverTransport,
                      TFramedTransport.Factory outputTransportFactory,
                      TProtocolFactory inputProtocolFactory,
                      TProtocolFactory outputProtocolFactory)
  {
    this(processorFactory, serverTransport,
      outputTransportFactory,
      inputProtocolFactory, outputProtocolFactory, new Options());
  }

  public THsHaServer( TProcessorFactory processorFactory,
                      TNonblockingServerTransport serverTransport,
                      TFramedTransport.Factory outputTransportFactory,
                      TProtocolFactory inputProtocolFactory,
                      TProtocolFactory outputProtocolFactory,
                      Options options)
  {
    this(processorFactory, serverTransport,
      outputTransportFactory,
      inputProtocolFactory, outputProtocolFactory,
      createInvokerPool(options),
      options);
  }

  public THsHaServer( TProcessorFactory processorFactory,
                      TNonblockingServerTransport serverTransport,
                      TFramedTransport.Factory outputTransportFactory,
                      TProtocolFactory inputProtocolFactory,
                      TProtocolFactory outputProtocolFactory,
                      ExecutorService executor,
                      TNonblockingServer.Options options) {
    super(processorFactory, serverTransport,
      outputTransportFactory,
      inputProtocolFactory, outputProtocolFactory,
      options);

    invoker = executor;
  }

  @Override
  public void serve() {

    if (!startListening()) {
      return;
    }

    if (!startSelectorThread()) {
      return;
    }

    joinSelector();

    gracefullyShutdownInvokerPool();

    stopListening();

  }

  protected static ExecutorService createInvokerPool(Options options) {
    int workerThreads = options.workerThreads;
    int stopTimeoutVal = options.stopTimeoutVal;
    TimeUnit stopTimeoutUnit = options.stopTimeoutUnit;

    LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
    ExecutorService invoker = new ThreadPoolExecutor(workerThreads, workerThreads,
      stopTimeoutVal, stopTimeoutUnit, queue);

    return invoker;
  }

  protected void gracefullyShutdownInvokerPool() {

    invoker.shutdown();

    long timeoutMS = 10000;
    long now = System.currentTimeMillis();
    while (timeoutMS >= 0) {
      try {
        invoker.awaitTermination(timeoutMS, TimeUnit.MILLISECONDS);
        break;
      } catch (InterruptedException ix) {
        long newnow = System.currentTimeMillis();
        timeoutMS -= (newnow - now);
        now = newnow;
      }
    }
  }

  @Override
  protected boolean requestInvoke(FrameBuffer frameBuffer) {
    try {
      invoker.execute(new Invocation(frameBuffer));
      return true;
    } catch (RejectedExecutionException rx) {
      LOGGER.warn("ExecutorService rejected execution!", rx);
      return false;
    }
  }

  private class Invocation implements Runnable {

    private final FrameBuffer frameBuffer;

    public Invocation(final FrameBuffer frameBuffer) {
      this.frameBuffer = frameBuffer;
    }

    public void run() {
      frameBuffer.invoke();
    }
  }

  public static class Options extends TNonblockingServer.Options {
    public int workerThreads = 5;
    public int stopTimeoutVal = 60;
    public TimeUnit stopTimeoutUnit = TimeUnit.SECONDS;
  }
}
