package core.network.client2game.handler.longnv;

import business.global.guild.Guild;
import business.player.Player;
import business.player.feature.features.PlayerRecord;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.LongnvSacrificeType;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class LongnvSacrificeInfo
extends PlayerHandler
{
public static class Request
{
LongnvSacrificeType type;
}

private static class Sacrifice {
int level;
int exp;
int donateTimes;
int crystalTimes;

private Sacrifice(int level, int exp, int donateTimes, int crystalTimes) {
this.level = level;
this.exp = exp;
this.donateTimes = donateTimes;
this.crystalTimes = crystalTimes;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
Guild guild = guildMember.getGuild();

if (guild == null) {
throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
}
int level = guild.bo.getLnlevel();
int exp = guild.bo.getLnexp();
int donateTimes = ((PlayerRecord)player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.LongnvDonate);
int crystalTimes = ((PlayerRecord)player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.LongnvCrystal);

request.response(new Sacrifice(level, exp, donateTimes, crystalTimes, null));
}
}

