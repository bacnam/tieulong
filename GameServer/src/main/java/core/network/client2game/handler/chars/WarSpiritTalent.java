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

public class WarSpiritTalent
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        int nowLevel = player.getPlayerBO().getWarspiritTalent();
        if (nowLevel >= RefDataMgr.size(RefWarSpiritTalent.class) - 1) {
            throw new WSException(ErrorCode.WarSpiritTalentFull, "天赋已满级");
        }
        RefWarSpiritTalent ref = (RefWarSpiritTalent) RefDataMgr.get(RefWarSpiritTalent.class, Integer.valueOf(nowLevel + 1));

        PlayerCurrency playerCurrency = (PlayerCurrency) player.getFeature(PlayerCurrency.class);
        if (!playerCurrency.check(PrizeType.WarspiritTalentMaterial, ref.Material)) {
            throw new WSException(ErrorCode.NotEnough_WarSpiritTalent, "玩家材料:%s<升级材料:%s", new Object[]{Integer.valueOf(player.getPlayerBO().getWarspiritTalentMaterial()), Integer.valueOf(ref.Material)});
        }

        playerCurrency.consume(PrizeType.WarspiritTalentMaterial, ref.Material, ItemFlow.WarSpiritTalent);

        player.getPlayerBO().saveWarspiritTalent(nowLevel + 1);

        ((WarSpiritFeature) player.getFeature(WarSpiritFeature.class)).updatePower();

        MeridianNotify notify = new MeridianNotify(player.getPlayerBO().getWarspiritTalent());
        request.response(notify);
    }

    public static class MeridianNotify {
        long warspiritTalent;

        public MeridianNotify(int warspiritTalent) {
            this.warspiritTalent = warspiritTalent;
        }
    }
}

