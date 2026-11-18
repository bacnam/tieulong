package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.character.WarSpiritFeature;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefWarSpiritTalent;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class WarSpiritTalent extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        int nowLevel = player.getPlayerBO().getWarspiritTalent();
        if (nowLevel >= RefDataMgr.size(RefWarSpiritTalent.class) - 1) {
            throw new WSException(ErrorCode.WarSpiritTalentFull, "天赋已满级");
        } else {
            RefWarSpiritTalent ref = RefDataMgr.get(RefWarSpiritTalent.class, nowLevel + 1);
            PlayerCurrency playerCurrency = player.getFeature(PlayerCurrency.class);
            if (!playerCurrency.check(PrizeType.WarspiritTalentMaterial, ref.Material)) {
                throw new WSException(ErrorCode.NotEnough_WarSpiritTalent, "玩家材料:%s<升级材料:%s", player.getPlayerBO().getWarspiritTalentMaterial(), ref.Material);
            } else {
                playerCurrency.consume(PrizeType.WarspiritTalentMaterial, ref.Material, ItemFlow.WarSpiritTalent);
                player.getPlayerBO().saveWarspiritTalent(nowLevel + 1);
                player.getFeature(WarSpiritFeature.class).updatePower();
                WarSpiritTalent.MeridianNotify notify = new WarSpiritTalent.MeridianNotify(player.getPlayerBO().getWarspiritTalent());
                request.response(notify);
            }
        }
    }

    public static class MeridianNotify {
        long warspiritTalent;

        public MeridianNotify(int warspiritTalent) {
            this.warspiritTalent = warspiritTalent;
        }
    }
}
