package core.network.client2game.handler.player;

import business.gmcmd.cmd.CommandMgr;
import business.player.Player;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import com.zhonglian.server.websocket.handler.response.ResponseHandler;
import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
import core.network.client2game.handler.PlayerHandler;
import core.network.game2world.WorldConnector;
import core.network.game2zone.ZoneConnector;
import java.io.IOException;
import proto.common.GmCommand.G_GmCommand;

public class Command extends PlayerHandler {
    @Override
    public void handle(Player player, final WebSocketRequest request, String message) throws WSException, IOException {
        Command.Request req = new Gson().fromJson(message, Command.Request.class);
        String ip = player.getClientSession().remoteIP();
        if (!ip.startsWith("192.168.") && !ip.startsWith("127.0.0.") && player.getPlayerBO().getGmLevel() == 0) {
            throw new WSException(ErrorCode.Player_AccessDenied, "玩家不是GM账号，权限不足");
        } else {
            if (req.cmd.matches("^(z|Z)\\s.*")) {
                String cmd = req.cmd.replaceAll("^(z|Z)\\s", "");
                ZoneConnector.request("base.gmcommand", new G_GmCommand(player.getPid(), cmd), new ResponseHandler() {
                    @Override
                    public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
                        request.response(body);
                    }

                    @Override
                    public void handleError(WebSocketResponse ssresponse, short statusCode, String messagex) {
                        request.error(statusCode, messagex);
                    }
                });
            } else if (req.cmd.matches("^(w|W)\\s.*")) {
                String cmd = req.cmd.replaceAll("^(w|W)\\s", "");
                WorldConnector.request("base.GmCommand", new G_GmCommand(player.getPid(), cmd), new ResponseHandler() {
                    @Override
                    public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
                        request.response(body);
                    }

                    @Override
                    public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
                        request.error(statusCode, message);
                    }
                });
            } else {
                Command.Result result = new Command.Result();
                result.rslt = CommandMgr.getInstance().run(player, req.cmd);
                request.response(result);
            }
        }
    }

    private static class Request {
        String cmd;
    }

    private static class Result {
        String rslt;

        private Result() {
        }
    }
}
