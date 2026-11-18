package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.global.guild.GuildConfig;
import business.global.guild.GuildMgr;
import business.player.Player;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.GuildApplyBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuildJoinList extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        List<Guild> guilds = GuildMgr.getInstance().getAllGuild();
        GuildMgr guildMgr = GuildMgr.getInstance();
        List<GuildApplyBO> applys = GuildMgr.getInstance().getApplyByCid(player.getPid());
        List<GuildApplyBO> applysOverTime = new ArrayList<>();

        for (GuildApplyBO applyBO : applys) {
            if (CommTime.nowSecond() - applyBO.getApplyTime() > GuildConfig.getGuildJoinTimeLimit()) {
                applysOverTime.add(applyBO);
            }
        }

        for (GuildApplyBO applyBOx : applysOverTime) {
            guildMgr.removeApply(applyBOx.getPid(), applyBOx.getGuildId());
        }

        List<core.network.proto.Guild.JoinInfo> proto = new ArrayList<>();

        for (GuildApplyBO apply : applys) {
            Guild guild = guildMgr.getGuild(apply.getGuildId());
            if (guild != null) {
                core.network.proto.Guild.JoinInfo info = guild.joinInfo();
                info.setHasRequest(true);
                proto.add(info);
            }
        }

        int size = proto.size();
        Collections.shuffle(guilds);
        guilds.stream().filter(x -> {
            for (GuildApplyBO applyBOx : applys) {
                if (x.getGuildId() == applyBOx.getGuildId()) {
                    return false;
                }
            }

            return true;
        }).limit(GuildConfig.getGuildJoinListSize() - size).forEach(x -> {
            core.network.proto.Guild.JoinInfo infox = x.joinInfo();
            infox.setHasRequest(false);
            proto.add(infox);
        });
        request.response(proto);
    }
}
