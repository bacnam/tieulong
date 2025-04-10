package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefGuildJobInfo;
import core.config.refdata.ref.RefGuildLevel;
import core.database.game.bo.GuildApplyBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class GuildDealApplyAll
extends PlayerHandler
{
public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
Guild guild = guildMember.getGuild();
if (guild == null) {
throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
}
RefGuildJobInfo job = guildMember.getJobRef();
if (!job.DealRequest) {
throw new WSException(ErrorCode.Guild_PermissionDenied, "玩家[%s]职务[%s]没有处理申请[DealRequest]的权限", new Object[] { Long.valueOf(player.getPid()), job.id });
}
for (GuildApplyBO bo : guild.getApplies()) {
RefGuildLevel refGuildLevel = (RefGuildLevel)RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(guild.getLevel()));
if (guild.getMemberCnt() >= refGuildLevel.MaxMemberAmount) {
break;
}
Player newMember = PlayerMgr.getInstance().getPlayer(bo.getPid());
guild.takeinMember(newMember);
} 
request.response();
}
}

