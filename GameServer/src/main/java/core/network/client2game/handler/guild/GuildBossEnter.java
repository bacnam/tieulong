package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.player.Player;
import business.player.feature.PlayerBase;
import business.player.feature.guild.GuildMemberFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.GuildBossBO;
import core.database.game.bo.GuildBossChallengeBO;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuildBossEnter
extends PlayerHandler
{
public static class Request
{
int bossId;
}

private static class EnterInfo
{
List<Player.FightInfo> players;
GuildBossBO boss;
GuildBossChallengeBO challenge;

private EnterInfo(List<Player.FightInfo> fullInfo, GuildBossBO boss, GuildBossChallengeBO challenge) {
this.players = fullInfo;
this.boss = boss;
this.challenge = challenge;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
Guild guild = guildMember.getGuild();
if (guild == null) {
throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
}

GuildBossChallengeBO challengBo = guildMember.beginFightGuildBoss(req.bossId);
GuildBossBO boss = guild.getboss(req.bossId);
List<Player> players = guild.getPlayerList(boss);
List<Player.FightInfo> fullInfoList = new ArrayList<>();
for (Player tmpPlay : players) {
if (tmpPlay != null && tmpPlay != player) {
fullInfoList.add(((PlayerBase)tmpPlay.getFeature(PlayerBase.class)).fightInfo());
}
} 
request.response(new EnterInfo(fullInfoList, boss, challengBo, null));
}
}

