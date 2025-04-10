package core.network.client2game.handler.longnv;

import business.global.guild.Guild;
import business.player.Player;
import business.player.feature.guild.GuildMemberFeature;
import business.player.item.Reward;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class LongnvPickReward
extends PlayerHandler
{
private static class Response
{
Reward reward;
int pickTimes;

private Response(Reward reward, int pickTimes) {
this.reward = reward;
this.pickTimes = pickTimes;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
Guild guild = guildMember.getGuild();

if (guild == null) {
throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
}
Reward reward = guild.getOrCreateLongnv().pickReward(player);

request.response(new Response(reward, guildMember.bo.getLongnvPickReward(), null));
}
}

