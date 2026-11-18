package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.RedPacket;
import business.global.redpacket.RedPacketMgr;
import business.player.Player;
import business.player.feature.features.PlayerRecord;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.RedPacketInfo;
import java.io.IOException;

public class RedPacketPickHandler extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        RedPacketPickHandler.Request req = new Gson().fromJson(message, RedPacketPickHandler.Request.class);
        RedPacket packet = ActivityMgr.getActivity(RedPacket.class);
        if (packet.getStatus() == ActivityStatus.Close) {
            throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", packet.getType());
        } else {
            int gain = packet.pick(req.id, player);
            int pickTimes = player.getFeature(PlayerRecord.class).getValue(DailyRefresh.RedPacket);
            request.response(new RedPacketPickHandler.Response(pickTimes, gain, RedPacketMgr.getInstance().getPacket(req.id, player)));
        }
    }

    class Request {
        long id;
    }

    private static class Response {
        int pickTimes;
        int gain;
        RedPacketInfo packetInfo;

        public Response(int pickTimes, int gain, RedPacketInfo packetInfo) {
            this.pickTimes = pickTimes;
            this.gain = gain;
            this.packetInfo = packetInfo;
        }
    }
}
