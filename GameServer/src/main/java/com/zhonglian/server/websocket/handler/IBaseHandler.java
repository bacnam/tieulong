package com.zhonglian.server.websocket.handler;

import BaseCommon.CommLog;

public abstract class IBaseHandler
{
private String event = "";

public IBaseHandler() {
try {
this.event = getClass().getName().replaceAll("^.*\\.handler\\.", "").toLowerCase();
} catch (Exception e) {
CommLog.error("error handler name for {}", getClass().getName());
} 
}

public IBaseHandler(String opName) {
this.event = opName;
}

public String getEvent() {
return this.event;
}
}

