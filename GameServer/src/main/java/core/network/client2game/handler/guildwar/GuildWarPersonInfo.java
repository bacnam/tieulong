package core.network.client2game.handler.guildwar;

import business.player.Player;
import business.player.feature.features.PlayerRecord;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class GuildWarPersonInfo
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        PlayerRecord record = (PlayerRecord) player.getFeature(PlayerRecord.class);
        int times = record.getValue(ConstEnum.DailyRefresh.GuildwarInspire);
        PersonInfo info = new PersonInfo(null);
        info.inspireTime = times;
        request.response(info);
    }

    private static class PersonInfo {
        int inspireTime;

        private PersonInfo() {
        }
    }
}

