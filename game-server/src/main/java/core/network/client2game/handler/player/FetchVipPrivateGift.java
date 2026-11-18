package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.record.VipRecord;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefReward;
import core.config.refdata.ref.RefVIP;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.List;

public class FetchVipPrivateGift extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        FetchVipPrivateGift.Request req = new Gson().fromJson(message, FetchVipPrivateGift.Request.class);
        int reqVipLevel = req.level;
        if (reqVipLevel < 0) {
            throw new WSException(ErrorCode.InvalidParam, "非法的参数viplevel=%s", reqVipLevel);
        } else {
            RefVIP refVip = RefDataMgr.get(RefVIP.class, reqVipLevel);
            if (refVip == null) {
                throw new WSException(ErrorCode.InvalidParam, "找不到vip:%s", reqVipLevel);
            } else if (player.getVipLevel() < reqVipLevel) {
                throw new WSException(ErrorCode.NotEnough_VIP, "vip等级不足");
            } else {
                int lastFetchTime = player.getFeature(VipRecord.class).getLastFetchPrivateTime(reqVipLevel);
                if (lastFetchTime > 0) {
                    throw new WSException(ErrorCode.Vipinfo_FetchedAlready, "已经领取了%s的vip礼包", reqVipLevel);
                } else {
                    Reward reward = RefDataMgr.get(RefReward.class, refVip.PrivateGiftID).genReward();
                    player.getFeature(PlayerItem.class).gain(reward, ItemFlow.VIP_PrivilegeGift);
                    player.getFeature(VipRecord.class).setLastFetchPrivateTime(reqVipLevel);
                    request.response(new FetchVipPrivateGift.FetchInfo(reward, player.getFeature(VipRecord.class).getFetchGiftStatusList()));
                }
            }
        }
    }

    private static class FetchInfo {
        Reward reward;
        List<Boolean> list;

        private FetchInfo(Reward reward, List<Boolean> list) {
            this.reward = reward;
            this.list = list;
        }
    }

    private class Request {
        int level;
    }
}
