package core.network.game2world.handler.base;

import business.gmcmd.cmd.CommandMgr;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.game2world.GRequestHandler;
import proto.common.GmCommand.G_GmCommand;
import proto.common.GmCommand.Z_GmCommand;

public class GmCommand extends GRequestHandler {
    @Override
    public void handle(WebSocketRequest request, String data) throws WSException {
        G_GmCommand req = new Gson().fromJson(data, G_GmCommand.class);
        String rslt = CommandMgr.getInstance().run(req.cid, req.cmd);
        request.response(new Z_GmCommand(rslt));
    }
}
