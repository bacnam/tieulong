package com.zhonglian.server.http.server;

import com.google.gson.JsonObject;
import com.zhonglian.server.common.utils.secure.MD5;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

public class GMParam
extends TreeMap<String, Object>
{
private static final long serialVersionUID = 4770492078712306761L;
private String key = null;

public GMParam() {
this.key = "svGVs2JLvp00lW2IfmIKcpB38Zbgmhnv";
}

public GMParam(String key) {
this.key = key;
}

public HttpEntity toEntity() throws Exception {
JsonObject json = new JsonObject();
StringBuilder signsrc = new StringBuilder();
for (Map.Entry<String, Object> pair : entrySet()) {
String value = pair.getValue().toString();
json.addProperty(pair.getKey(), value);
value = URLEncoder.encode(value, "utf-8");
signsrc.append(pair.getKey()).append("=").append(value).append("&");
} 
signsrc = signsrc.append(this.key);
json.addProperty("sign", MD5.md5(signsrc.toString()));
return (HttpEntity)new StringEntity(json.toString());
}

public String toUrlParam() throws Exception {
StringBuilder params = new StringBuilder("?");
StringBuilder signsrc = new StringBuilder();
for (Map.Entry<String, Object> pair : entrySet()) {
String value = pair.getValue().toString();
value = URLEncoder.encode(value, "utf-8");
signsrc.append(pair.getKey()).append("=").append(value).append("&");
params.append(pair.getKey()).append("=").append(value).append("&");
} 
signsrc = signsrc.append(this.key);
params.append("sign").append("=").append(MD5.md5(signsrc.toString()));
return params.toString();
}
}

