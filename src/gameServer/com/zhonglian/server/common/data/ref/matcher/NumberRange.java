package com.zhonglian.server.common.data.ref.matcher;

import BaseCommon.CommLog;
import com.zhonglian.server.common.utils.CommMath;

public class NumberRange
{
int from;
int to;

public NumberRange() {
this.from = 0;
this.to = 0;
}

public NumberRange(int f, int t) {
this.from = f;
this.to = t;
}

public boolean within(int r) {
return (r >= this.from && (r <= this.to || this.to == -1));
}

public static NumberRange parse(String data) {
NumberRange range = new NumberRange();
if (data == null || "".equals(data)) {
range.from = 0;
range.to = 9999;
} else {
range.parseFrom(data);
} 
return range;
}

public int getLow() {
return this.from;
}

public int getTop() {
return this.to;
}

public boolean isNarrow() {
return (this.from == this.to);
}

public void parseFrom(String data) {
if (data == null) {
CommLog.error("NumberRange failed to parse string 'null'");
return;
} 
String[] split = data.split("~");
for (int i = 0; i < split.length; i++) {
if (split[i].equals(">")) {
split[i] = String.valueOf(2147483647);
}
} 

try {
if (split.length == 1) {
this.from = this.to = Integer.parseInt(data);
} else if (split.length == 2) {
int a = Integer.parseInt(split[0]);
int b = Integer.parseInt(split[1]);
this.from = Math.min(a, b);
this.to = Math.max(a, b);
} 
} catch (Exception e) {
CommLog.error("NumberRange failed to parse string '" + data + "'");
CommLog.error(e.getMessage(), e);
} 
}

public int random() {
return CommMath.randomInt(this.from, this.to);
}

public String toString() {
return "[ " + this.from + " ~ " + this.to + " ]";
}
}

