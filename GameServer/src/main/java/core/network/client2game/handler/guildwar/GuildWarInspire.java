package core.network.client2game.handler.guildwar;

import business.global.guild.Guild;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.ref.RefCrystalPrice;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class GuildWarInspire
extends PlayerHandler
{
public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
Guild guild = guildMember.getGuild();
if (guild == null) {
throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
}

PlayerRecord record = (PlayerRecord)player.getFeature(PlayerRecord.class);
int times = record.getValue(ConstEnum.DailyRefresh.GuildwarInspire);
RefCrystalPrice.getPrize(times);

int crystalCost = (RefCrystalPrice.getPrize(times)).GuildwarInspire;

PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
if (!playerCurrency.check(PrizeType.Crystal, crystalCost)) {
throw new WSException(ErrorCode.NotEnough_Crystal, "玩家元宝:%s<鼓励需要元宝:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getCrystal()), Integer.valueOf(crystalCost) });
}

playerCurrency.consume(PrizeType.Crystal, crystalCost, ItemFlow.WorldBoss_Inspiring);
record.addValue(ConstEnum.DailyRefresh.GuildwarInspire);
guildMember.bo.saveGuildwarInspire(guildMember.bo.getGuildwarInspire() + 1);
request.response(Integer.valueOf(guildMember.bo.getGuildwarInspire()));
}
}

