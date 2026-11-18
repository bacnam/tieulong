package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.global.guild.GuildMgr;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.character.CharFeature;
import business.player.feature.guild.GuildMemberFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class GuildCreate extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildCreate.Request req = new Gson().fromJson(message, GuildCreate.Request.class);
        GuildMemberFeature guildMember = player.getFeature(GuildMemberFeature.class);
        Guild oldGuild = guildMember.getGuild();
        if (oldGuild != null) {
            throw new WSException(ErrorCode.Guild_AlreadyInGuild, "[%s]玩家已经在帮会[%s]，不能再创建帮会", player.getPid(), oldGuild.getName());
        } else {
            GuildMgr.CheckNameValid(req.name, player.getPid());
            int createcost = RefDataMgr.getFactor("CreateGuildCost", 500);
            PlayerCurrency currency = player.getFeature(PlayerCurrency.class);
            if (!currency.check(PrizeType.Crystal, createcost)) {
                throw new WSException(ErrorCode.NotEnough_Crystal, "玩家[%s]不能创建帮会,其钻石[%s]不足[%s]", player.getPid(), player.getPlayerBO().getCrystal(), createcost);
            } else {
                currency.consume(PrizeType.Crystal, createcost, ItemFlow.Guild_Create);
                GuildMgr.getInstance().createGuild(req.name, 0, player);
                GuildMgr.getInstance().removeApply(player.getPid());
                player.getFeature(CharFeature.class).updateCharPower();
                player.pushProto("guildSkill", player.getFeature(GuildMemberFeature.class).getGuildSkillList());
                request.response(new GuildInfoHandler.GuildBaseInfo(guildMember.getGuild().guildInfo(), guildMember.memberInfo(), guildMember.getJoinCD()));
            }
        }
    }

    public static class Request {
        String name;
    }
}
