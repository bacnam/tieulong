package core.network.http.handler;

import business.global.chat.ChatMgr;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ChatType;
import com.zhonglian.server.http.annotation.RequestMapping;
import com.zhonglian.server.http.server.HttpRequest;
import com.zhonglian.server.http.server.HttpResponse;
import com.zhonglian.server.http.server.HttpUtils;

public class ChatRequest
{
@RequestMapping(uri = "/game/chat/answer")
public void answer(HttpRequest request, HttpResponse response) throws Exception {
JsonObject chat = HttpUtils.abstractGMParams(request.getRequestBody());
String content = HttpUtils.getString(chat, "content");
long toCId = HttpUtils.getLong(chat, "receiveId");
ChatMgr.getInstance().addChat(null, content, ChatType.CHATTYPE_GM, toCId);
response.response("ok");
}
}

