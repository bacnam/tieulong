package com.notnoop.apns;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notnoop.apns.internal.Utilities;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class PayloadBuilder
{
private static final ObjectMapper mapper = new ObjectMapper();

private final Map<String, Object> root;

private final Map<String, Object> aps;

private final Map<String, Object> customAlert;

PayloadBuilder() {
this.root = new HashMap<String, Object>();
this.aps = new HashMap<String, Object>();
this.customAlert = new HashMap<String, Object>();
}

public PayloadBuilder alertBody(String alert) {
this.customAlert.put("body", alert);
return this;
}

public PayloadBuilder sound(String sound) {
if (sound != null) {
this.aps.put("sound", sound);
} else {
this.aps.remove("sound");
} 
return this;
}

public PayloadBuilder badge(int badge) {
this.aps.put("badge", Integer.valueOf(badge));
return this;
}

public PayloadBuilder clearBadge() {
return badge(0);
}

public PayloadBuilder actionKey(String actionKey) {
this.customAlert.put("action-loc-key", actionKey);
return this;
}

public PayloadBuilder noActionButton() {
return actionKey(null);
}

public PayloadBuilder forNewsstand() {
this.aps.put("content-available", Integer.valueOf(1));
return this;
}

public PayloadBuilder localizedKey(String key) {
this.customAlert.put("loc-key", key);
return this;
}

public PayloadBuilder localizedArguments(Collection<String> arguments) {
this.customAlert.put("loc-args", arguments);
return this;
}

public PayloadBuilder localizedArguments(String... arguments) {
return localizedArguments(Arrays.asList(arguments));
}

public PayloadBuilder launchImage(String launchImage) {
this.customAlert.put("launch-image", launchImage);
return this;
}

public PayloadBuilder customField(String key, Object value) {
this.root.put(key, value);
return this;
}

public PayloadBuilder mdm(String s) {
return customField("mdm", s);
}

public PayloadBuilder customFields(Map<String, ? extends Object> values) {
this.root.putAll(values);
return this;
}

public int length() {
return (copy().buildBytes()).length;
}

public boolean isTooLong() {
return (length() > 256);
}

public PayloadBuilder resizeAlertBody(int payloadLength) {
return resizeAlertBody(payloadLength, "");
}

public PayloadBuilder resizeAlertBody(int payloadLength, String postfix) {
int currLength = length();
if (currLength <= payloadLength) {
return this;
}

String body = (String)this.customAlert.get("body");

int acceptableSize = (Utilities.toUTF8Bytes(body)).length - currLength - payloadLength + (Utilities.toUTF8Bytes(postfix)).length;

body = Utilities.truncateWhenUTF8(body, acceptableSize) + postfix;

this.customAlert.put("body", body);

currLength = length();

if (currLength > payloadLength)
{

this.customAlert.remove("body");
}

return this;
}

public PayloadBuilder shrinkBody() {
return shrinkBody("");
}

public PayloadBuilder shrinkBody(String postfix) {
return resizeAlertBody(256, postfix);
}

public String build() {
if (!this.root.containsKey("mdm")) {
insertCustomAlert();
this.root.put("aps", this.aps);
} 
try {
return mapper.writeValueAsString(this.root);
} catch (Exception e) {
throw new RuntimeException(e);
} 
}

private void insertCustomAlert() {
switch (this.customAlert.size()) {
case 0:
this.aps.remove("alert");
return;
case 1:
if (this.customAlert.containsKey("body")) {
this.aps.put("alert", this.customAlert.get("body"));
return;
} 
break;
} 

this.aps.put("alert", this.customAlert);
}

public byte[] buildBytes() {
return Utilities.toUTF8Bytes(build());
}

public String toString() {
return build();
}

private PayloadBuilder(Map<String, Object> root, Map<String, Object> aps, Map<String, Object> customAlert) {
this.root = new HashMap<String, Object>(root);
this.aps = new HashMap<String, Object>(aps);
this.customAlert = new HashMap<String, Object>(customAlert);
}

public PayloadBuilder copy() {
return new PayloadBuilder(this.root, this.aps, this.customAlert);
}

public static PayloadBuilder newPayload() {
return new PayloadBuilder();
}
}

