package jsr166y;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public interface TransferQueue<E> extends BlockingQueue<E> {
  boolean tryTransfer(E paramE);

  void transfer(E paramE) throws InterruptedException;

  boolean tryTransfer(E paramE, long paramLong, TimeUnit paramTimeUnit) throws InterruptedException;

  boolean hasWaitingConsumer();

  int getWaitingConsumerCount();
}

