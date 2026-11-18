package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.treasure.FindTreasureFeature;
import com.zhonglian.server.common.enums.ConstEnum.FindTreasureType;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class FindTreasureInfo extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        FindTreasureFeature feature = player.getFeature(FindTreasureFeature.class);
        request.response(new FindTreasureInfo.Response(feature.getLeftTimes(FindTreasureType.single), feature.getLeftTimes(FindTreasureType.Ten)));
    }

    private static class Response {
        int leftTimes;
        int leftTentimes;

        private Response(int leftTimes, int leftTentimes) {
            this.leftTimes = leftTimes;
            this.leftTentimes = leftTentimes;
        }
    }
}
