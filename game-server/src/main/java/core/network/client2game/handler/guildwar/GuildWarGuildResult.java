package core.network.client2game.handler.guildwar;

import business.global.guild.GuildWarMgr;
import business.player.Player;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.GuildwarResultBO;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.GuildWarFightProtol;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuildWarGuildResult extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildWarGuildResult.Request req = new Gson().fromJson(message, GuildWarGuildResult.Request.class);
        List<GuildWarFightProtol.ResultRecord> result = new ArrayList<>();
        if (GuildWarMgr.getInstance().guildAllResulet.get(req.sid) != null) {
            for (GuildwarResultBO bo : GuildWarMgr.getInstance().guildAllResulet.get(req.sid)) {
                result.add(new GuildWarFightProtol.ResultRecord(bo));
            }
        }

        request.response(result);
    }

    public static class Request {
        long sid;
    }
}
