package core.network.http.handler;

import com.zhonglian.server.http.annotation.RequestMapping;
import com.zhonglian.server.http.server.HttpRequest;
import com.zhonglian.server.http.server.HttpResponse;
import java.io.File;

public class TestPHPRequest
{
@RequestMapping(uri = "/game/test/index")
public void index(HttpRequest request, HttpResponse response) {
response.response(new File("conf/httpserver/test.html"));
}
}

