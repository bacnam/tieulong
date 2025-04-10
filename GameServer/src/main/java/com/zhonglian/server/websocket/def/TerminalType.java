package com.zhonglian.server.websocket.def;

public enum TerminalType {
None(0),
Client(1),
GameServer(2),
ZoneServer(3),
WorldServer(4);

private byte value;

TerminalType(int value) {
this.value = (byte)value;
}

public byte value() {
return this.value;
}
}

