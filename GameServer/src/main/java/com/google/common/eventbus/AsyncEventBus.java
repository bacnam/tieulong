package com.google.common.eventbus;

import com.google.common.annotations.Beta;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

@Beta
public class AsyncEventBus
        extends EventBus {
    private final Executor executor;
    private final ConcurrentLinkedQueue<EventBus.EventWithHandler> eventsToDispatch = new ConcurrentLinkedQueue<EventBus.EventWithHandler>();

    public AsyncEventBus(String identifier, Executor executor) {
        super(identifier);
        this.executor = executor;
    }

    public AsyncEventBus(Executor executor) {
        this.executor = executor;
    }

    protected void enqueueEvent(Object event, EventHandler handler) {
        this.eventsToDispatch.offer(new EventBus.EventWithHandler(event, handler));
    }

    protected void dispatchQueuedEvents() {
        while (true) {
            EventBus.EventWithHandler eventWithHandler = this.eventsToDispatch.poll();
            if (eventWithHandler == null) {
                break;
            }

            dispatch(eventWithHandler.event, eventWithHandler.handler);
        }
    }

    protected void dispatch(final Object event, final EventHandler handler) {
        this.executor.execute(new Runnable() {
            public void run() {
                AsyncEventBus.this.dispatch(event, handler);
            }
        });
    }
}

