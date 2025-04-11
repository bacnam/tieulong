package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.RedPacket;
import business.global.redpacket.RedPacketMgr;
import business.player.Player;
import business.player.feature.features.PlayerRecord;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.RedPacketInfo;

import java.io.IOException;

public class RedPacketPickHandler
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        RedPacket packet = (RedPacket) ActivityMgr.getActivity(RedPacket.class);
        if (packet.getStatus() == ActivityStatus.Close) {
            throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[]{packet.getType()});
        }
        int gain = packet.pick(req.id, player);
        int pickTimes = ((PlayerRecord) player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.RedPacket);
        request.response(new Response(pickTimes, gain, RedPacketMgr.getInstance().getPacket(req.id, player)));
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

    class Request {
        long id;
    }
}

