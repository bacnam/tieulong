

package org.apache.thrift.server;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TTransportFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TThreadPoolServer extends TServer {

  private static final Logger LOGGER = LoggerFactory.getLogger(TThreadPoolServer.class.getName());

  private ExecutorService executorService_;

  private volatile boolean stopped_;

  private Options options_;

  public static class Options {
    public int minWorkerThreads = 5;
    public int maxWorkerThreads = Integer.MAX_VALUE;
    public int stopTimeoutVal = 60;
    public TimeUnit stopTimeoutUnit = TimeUnit.SECONDS;
  }

  public TThreadPoolServer(TProcessor processor,
                           TServerTransport serverTransport) {
    this(processor, serverTransport,
         new TTransportFactory(), new TTransportFactory(),
         new TBinaryProtocol.Factory(), new TBinaryProtocol.Factory());
  }

  public TThreadPoolServer(TProcessorFactory processorFactory,
                           TServerTransport serverTransport) {
    this(processorFactory, serverTransport,
         new TTransportFactory(), new TTransportFactory(),
         new TBinaryProtocol.Factory(), new TBinaryProtocol.Factory());
  }

  public TThreadPoolServer(TProcessor processor,
                           TServerTransport serverTransport,
                           TProtocolFactory protocolFactory) {
    this(processor, serverTransport,
         new TTransportFactory(), new TTransportFactory(),
         protocolFactory, protocolFactory);
  }

  public TThreadPoolServer(TProcessor processor,
                           TServerTransport serverTransport,
                           TTransportFactory transportFactory,
                           TProtocolFactory protocolFactory) {
    this(processor, serverTransport,
         transportFactory, transportFactory,
         protocolFactory, protocolFactory);
  }

  public TThreadPoolServer(TProcessorFactory processorFactory,
                           TServerTransport serverTransport,
                           TTransportFactory transportFactory,
                           TProtocolFactory protocolFactory) {
    this(processorFactory, serverTransport,
         transportFactory, transportFactory,
         protocolFactory, protocolFactory);
  }

  public TThreadPoolServer(TProcessor processor,
                           TServerTransport serverTransport,
                           TTransportFactory inputTransportFactory,
                           TTransportFactory outputTransportFactory,
                           TProtocolFactory inputProtocolFactory,
                           TProtocolFactory outputProtocolFactory) {
    this(new TProcessorFactory(processor), serverTransport,
         inputTransportFactory, outputTransportFactory,
         inputProtocolFactory, outputProtocolFactory);
  }

  public TThreadPoolServer(TProcessorFactory processorFactory,
                           TServerTransport serverTransport,
                           TTransportFactory inputTransportFactory,
                           TTransportFactory outputTransportFactory,
                           TProtocolFactory inputProtocolFactory,
                           TProtocolFactory outputProtocolFactory) {
    super(processorFactory, serverTransport,
          inputTransportFactory, outputTransportFactory,
          inputProtocolFactory, outputProtocolFactory);
    options_ = new Options();
    executorService_ = Executors.newCachedThreadPool();
  }

  public TThreadPoolServer(TProcessor processor,
          TServerTransport serverTransport,
          TTransportFactory inputTransportFactory,
          TTransportFactory outputTransportFactory,
          TProtocolFactory inputProtocolFactory,
          TProtocolFactory outputProtocolFactory,
          Options options) {
    this(new TProcessorFactory(processor), serverTransport,
         inputTransportFactory, outputTransportFactory,
         inputProtocolFactory, outputProtocolFactory,
         options);
  }

  public TThreadPoolServer(TProcessorFactory processorFactory,
                           TServerTransport serverTransport,
                           TTransportFactory inputTransportFactory,
                           TTransportFactory outputTransportFactory,
                           TProtocolFactory inputProtocolFactory,
                           TProtocolFactory outputProtocolFactory,
                           Options options) {
    super(processorFactory, serverTransport,
          inputTransportFactory, outputTransportFactory,
          inputProtocolFactory, outputProtocolFactory);

    executorService_ = null;

    SynchronousQueue<Runnable> executorQueue =
      new SynchronousQueue<Runnable>();

    executorService_ = new ThreadPoolExecutor(options.minWorkerThreads,
                                              options.maxWorkerThreads,
                                              60,
                                              TimeUnit.SECONDS,
                                              executorQueue);

    options_ = options;
  }

  public void serve() {
    try {
      serverTransport_.listen();
    } catch (TTransportException ttx) {
      LOGGER.error("Error occurred during listening.", ttx);
      return;
    }

    stopped_ = false;
    while (!stopped_) {
      int failureCount = 0;
      try {
        TTransport client = serverTransport_.accept();
        WorkerProcess wp = new WorkerProcess(client);
        executorService_.execute(wp);
      } catch (TTransportException ttx) {
        if (!stopped_) {
          ++failureCount;
          LOGGER.warn("Transport error occurred during acceptance of message.", ttx);
        }
      }
    }

    executorService_.shutdown();

    long timeoutMS = options_.stopTimeoutUnit.toMillis(options_.stopTimeoutVal);
    long now = System.currentTimeMillis();
    while (timeoutMS >= 0) {
      try {
        executorService_.awaitTermination(timeoutMS, TimeUnit.MILLISECONDS);
        break;
      } catch (InterruptedException ix) {
        long newnow = System.currentTimeMillis();
        timeoutMS -= (newnow - now);
        now = newnow;
      }
    }
  }

  public void stop() {
    stopped_ = true;
    serverTransport_.interrupt();
  }

  private class WorkerProcess implements Runnable {

    private TTransport client_;

    private WorkerProcess(TTransport client) {
      client_ = client;
    }

    public void run() {
      TProcessor processor = null;
      TTransport inputTransport = null;
      TTransport outputTransport = null;
      TProtocol inputProtocol = null;
      TProtocol outputProtocol = null;
      try {
        processor = processorFactory_.getProcessor(client_);
        inputTransport = inputTransportFactory_.getTransport(client_);
        outputTransport = outputTransportFactory_.getTransport(client_);
        inputProtocol = inputProtocolFactory_.getProtocol(inputTransport);
        outputProtocol = outputProtocolFactory_.getProtocol(outputTransport);

        while (!stopped_ && processor.process(inputProtocol, outputProtocol)) {}
      } catch (TTransportException ttx) {

      } catch (TException tx) {
        LOGGER.error("Thrift error occurred during processing of message.", tx);
      } catch (Exception x) {
        LOGGER.error("Error occurred during processing of message.", x);
      }

      if (inputTransport != null) {
        inputTransport.close();
      }

      if (outputTransport != null) {
        outputTransport.close();
      }
    }
  }
}
