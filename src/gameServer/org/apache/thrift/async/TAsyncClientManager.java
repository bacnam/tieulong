
package org.apache.thrift.async;

import java.io.IOException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeoutException;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TAsyncClientManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(TAsyncClientManager.class.getName());

  private final SelectThread selectThread;
  private final ConcurrentLinkedQueue<TAsyncMethodCall> pendingCalls = new ConcurrentLinkedQueue<TAsyncMethodCall>();

  public TAsyncClientManager() throws IOException {
    this.selectThread = new SelectThread();
    selectThread.start();
  }

  public void call(TAsyncMethodCall method) throws TException {
    method.prepareMethodCall();
    pendingCalls.add(method);
    selectThread.getSelector().wakeup();
  }

  public void stop() {
    selectThread.finish();
  }

  private class SelectThread extends Thread {

    private static final long SELECT_TIME = 5;

    private final Selector selector;
    private volatile boolean running;
    private final Set<TAsyncMethodCall> timeoutWatchSet = new HashSet<TAsyncMethodCall>();

    public SelectThread() throws IOException {
      this.selector = SelectorProvider.provider().openSelector();
      this.running = true;

      setDaemon(true);
    }

    public Selector getSelector() {
      return selector;
    }

    public void finish() {
      running = false;
      selector.wakeup();
    }

    public void run() {
      while (running) {
        try {
          try {
            selector.select(SELECT_TIME);
          } catch (IOException e) {
            LOGGER.error("Caught IOException in TAsyncClientManager!", e);
          }

          transitionMethods();
          timeoutIdleMethods();
          startPendingMethods();
        } catch (Throwable throwable) {
          LOGGER.error("Ignoring uncaught exception in SelectThread", throwable);
        }
      }
    }

    private void transitionMethods() {
      try {
        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
        while (keys.hasNext()) {
          SelectionKey key = keys.next();
          keys.remove();
          if (!key.isValid()) {

            continue;
          }
          TAsyncMethodCall methodCall = (TAsyncMethodCall)key.attachment();
          methodCall.transition(key);

          if (methodCall.isFinished() || methodCall.getClient().hasError()) {
            timeoutWatchSet.remove(methodCall);
          }
        }
      } catch (ClosedSelectorException e) {
        LOGGER.error("Caught ClosedSelectorException in TAsyncClientManager!", e);
      }
    }

    private void timeoutIdleMethods() {
      Iterator<TAsyncMethodCall> iterator = timeoutWatchSet.iterator();
      while (iterator.hasNext()) {
        TAsyncMethodCall methodCall = iterator.next();
        long clientTimeout = methodCall.getClient().getTimeout();
        long timeElapsed = System.currentTimeMillis() - methodCall.getLastTransitionTime();

        if (timeElapsed > clientTimeout) {
          iterator.remove();
          methodCall.onError(new TimeoutException("Operation " +
              methodCall.getClass() + " timed out after " + timeElapsed +
              " milliseconds."));
        }
      }
    }

    private void startPendingMethods() {
      TAsyncMethodCall methodCall;
      while ((methodCall = pendingCalls.poll()) != null) {

        try {
          methodCall.start(selector);

          TAsyncClient client = methodCall.getClient();
          if (client.hasTimeout() && !client.hasError()) {
            timeoutWatchSet.add(methodCall);
          }
        } catch (Throwable e) {
          LOGGER.warn("Caught throwable in TAsyncClientManager!", e);
          methodCall.onError(e);
        }
      }
    }
  }
}
