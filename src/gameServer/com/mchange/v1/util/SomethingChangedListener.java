package com.mchange.v1.util;

import java.util.EventListener;

public interface SomethingChangedListener extends EventListener {
  void somethingChanged(SomethingChangedEvent paramSomethingChangedEvent);
}

