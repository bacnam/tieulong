package core.network.http.handler;

import com.zhonglian.server.http.annotation.RequestMapping;
import com.zhonglian.server.http.server.HttpRequest;
import com.zhonglian.server.http.server.HttpResponse;
import core.server.ServerConfig;
import java.io.File;

public class LoginRequest
{
@RequestMapping(uri = "/game/loginkey/index")
public void index(HttpRequest request, HttpResponse response) {
response.response(new File("conf/httpserver/login.html"));
}

@RequestMapping(uri = "/game/loginkey/refresh")
public void create(HttpRequest request, HttpResponse response) {
response.response("{\"loginKey\":" + ServerConfig.refreshLoginKey() + "}");
}

@RequestMapping(uri = "/game/loginkey/get")
public void get(HttpRequest request, HttpResponse response) {
response.response("{\"loginKey\":" + ServerConfig.getLoginKey() + "}");
}
}

