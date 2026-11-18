package core.network.client2game.handler.guild;

import business.global.chat.ChatMgr;
import business.global.guild.Guild;
import business.global.notice.NoticeMgr;
import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.guild.GuildMemberFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.enums.ChatType;
import com.zhonglian.server.common.enums.ConstEnum.UniverseMessageType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefGuildBoss;
import core.config.refdata.ref.RefGuildJobInfo;
import core.database.game.bo.GuildBossBO;
import core.database.game.bo.GuildBossChallengeBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.List;

public class GuildBossOpen extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildBossOpen.Request req = new Gson().fromJson(message, GuildBossOpen.Request.class);
        GuildMemberFeature guildMember = player.getFeature(GuildMemberFeature.class);
        Guild guild = guildMember.getGuild();
        RefGuildJobInfo job = guildMember.getJobRef();
        if (!job.OpenInstance) {
            throw new WSException(ErrorCode.Guild_PermissionDenied, "玩家[%s]职务[%s]没有开启帮派BOSS的权限", player.getPid(), job.id);
        } else if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", player.getPid());
        } else {
            GuildBossBO boss = guild.getboss(req.bossId);
            if (boss != null) {
                throw new WSException(ErrorCode.GuildBoss_AlreadyOpen, "帮派boss已开启");
            } else if (!guild.isInOpenHour()) {
                throw new WSException(ErrorCode.GuildBoss_NotOpen, "不在时间段内");
            } else {
                RefGuildBoss ref = RefDataMgr.get(RefGuildBoss.class, req.bossId);
                if (guild.getLevel() < ref.NeedGuildLevel) {
                    throw new WSException(ErrorCode.GuildBoss_NotenoughLv, "帮派等级不够");
                } else if (guild.getBo().getGuildbossLevel() < ref.id - 1) {
                    throw new WSException(ErrorCode.GuildBoss_Lock, "帮派副本为解锁");
                } else if (!player.getFeature(PlayerItem.class).checkAndConsume(ref.UniformId, ref.UniformCount, ItemFlow.Guild_OpenBoss)) {
                    throw new WSException(ErrorCode.GuildBoss_NotEnoughCoin, "帮派副本资源不足");
                } else {
                    guild.openBoss(ref);
                    NoticeMgr.getInstance().sendMarquee(UniverseMessageType.OpenGuildBoss, guild.getName(), player.getName());
                    ChatMgr.getInstance().addChat(player, "帮派副本已开启，请各位成员赶紧前往战斗", ChatType.CHATTYPE_GUILD, 0L);
                    request.response(guild.getboss(req.bossId));
                }
            }
        }
    }

    private static class GuildBoss {
        List<GuildBossBO> bosslist;
        GuildBossChallengeBO challengBO;
        NumberRange openTime;
        int nexttime;

        private GuildBoss(List<GuildBossBO> bosslist, GuildBossChallengeBO challengBO, NumberRange openTime, int nexttime) {
            this.bosslist = bosslist;
            this.challengBO = challengBO;
            this.openTime = openTime;
            this.nexttime = nexttime;
        }
    }

    public static class Request {
        int bossId;
    }
}
