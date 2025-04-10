package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.global.guild.GuildMgr;
import business.global.rank.RankManager;
import business.global.rank.Record;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.guild.GuildMemberFeature;
import business.player.feature.pvp.WorshipFeature;
import com.zhonglian.server.common.enums.GuildJob;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuildRank
extends PlayerHandler
{
private static class Response
{
int worshipTimes;
int rank;
String name;
List<GuildRank.GuildRankInfo> rankList;

public Response(int worshipTimes, int rank, String name, List<Record> records) {
this.worshipTimes = worshipTimes;
this.rank = rank;
this.name = name;
this.rankList = new ArrayList<>();
for (int i = 0; i < records.size(); i++) {
Record r = records.get(i);
if (r != null)
{

this.rankList.add(new GuildRank.GuildRankInfo(r)); } 
} 
}
}

public static class GuildRankInfo {
int rank;
long value;
String guildName;
long presidentId;
String presidentName;
int presidentLv;
int presidentVip;

public GuildRankInfo(Record record) {
Guild guild = GuildMgr.getInstance().getGuild(record.getPid());
this.rank = record.getRank();
this.value = record.getValue();
this.guildName = guild.getName();
Player player = PlayerMgr.getInstance().getPlayer(((Long)guild.getMember(GuildJob.President).get(0)).longValue());
this.presidentId = player.getPid();
this.presidentName = player.getName();
this.presidentLv = player.getLv();
this.presidentVip = player.getVipLevel();
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
List<Record> records = RankManager.getInstance().getRankList(RankType.Guild, 10);
int rank = 0;
Guild guild = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuild();
String name = "";
if (guild != null) {
name = guild.getName();
rank = RankManager.getInstance().getRank(RankType.Guild, guild.getGuildId());
} 
int times = ((WorshipFeature)player.getFeature(WorshipFeature.class)).getTimes(RankType.Guild.ordinal());
request.response(new Response(times, rank, name, records));
}
}

