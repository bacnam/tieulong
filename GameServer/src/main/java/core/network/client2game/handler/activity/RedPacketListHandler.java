package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.RedPacket;
import business.global.redpacket.RedPacketMgr;
import business.player.Player;
import business.player.feature.features.PlayerRecord;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.RedPacketInfo;

import java.io.IOException;
import java.util.List;

public class RedPacketListHandler
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        RedPacket packet = (RedPacket) ActivityMgr.getActivity(RedPacket.class);
        if (packet.getStatus() == ActivityStatus.Close) {
            throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[]{packet.getType()});
        }
        int pickTimes = ((PlayerRecord) player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.RedPacket);
        int maxTimes = RedPacketMgr.getInstance().getMaxTime();
        request.response(new Response(pickTimes, maxTimes, packet.getPacketList(player)));
    }

    private static class Response {
        int pickTimes;
        int maxPickTimes;
        List<RedPacketInfo> packetList;

        public Response(int pickTimes, int maxPickTimes, List<RedPacketInfo> packetList) {
            this.pickTimes = pickTimes;
            this.maxPickTimes = maxPickTimes;
            this.packetList = packetList;
        }
    }
}

