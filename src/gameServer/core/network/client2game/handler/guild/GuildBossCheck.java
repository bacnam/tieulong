package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.global.guild.GuildConfig;
import business.player.Player;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.GuildBossBO;
import core.database.game.bo.GuildBossChallengeBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.List;

public class GuildBossCheck
extends PlayerHandler
{
private static class GuildBoss
{
List<GuildBossBO> bosslist;
GuildBossChallengeBO challengBO;
NumberRange openTime;
int nextTime;
int maxLevel;
int openTimes;

private GuildBoss(List<GuildBossBO> bosslist, GuildBossChallengeBO challengBO, NumberRange openTime, int nextTime, int maxLevel, int openTimes) {
this.bosslist = bosslist;
this.challengBO = challengBO;
this.openTime = openTime;
this.nextTime = nextTime;
this.maxLevel = maxLevel;
this.openTimes = openTimes;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
Guild guild = guildMember.getGuild();
if (guild == null) {
throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
}
List<GuildBossBO> bosslist = guild.getbosslist();
GuildBossChallengeBO challengBO = guildMember.getOrCreateChalleng();
NumberRange openTime = GuildConfig.getGuildBossOpenTime();
int nexttime = Math.max(guildMember.nextRefreshTime() - CommTime.nowSecond(), 0);
int maxLevel = guild.getBo().getGuildbossLevel();

request.response(new GuildBoss(bosslist, challengBO, openTime, nexttime, maxLevel, guild.getBo().getGuildbossOpenNum(), null));
}
}

