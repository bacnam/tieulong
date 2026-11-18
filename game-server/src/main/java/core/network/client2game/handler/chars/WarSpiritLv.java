package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.character.WarSpiritFeature;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefRebirth;
import core.config.refdata.ref.RefWarSpiritLv;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class WarSpiritLv extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        int nowLevel = player.getPlayerBO().getWarspiritLv();
        if (nowLevel >= RefDataMgr.size(RefRebirth.class) - 1) {
            throw new WSException(ErrorCode.Rebirth_LevelFull, "转生已满级");
        } else {
            RefWarSpiritLv now_ref = RefDataMgr.get(RefWarSpiritLv.class, nowLevel);
            RefWarSpiritLv next_ref = RefDataMgr.get(RefWarSpiritLv.class, nowLevel + 1);
            int gainExp = 0;
            if (now_ref.Level != next_ref.Level) {
                player.getPlayerBO().saveWarspiritLv(nowLevel + 1);
            } else {
                if (!player.getFeature(PlayerItem.class).checkAndConsume(next_ref.UniformId, next_ref.UniformCount, ItemFlow.WarSpiritLv)) {
                    throw new WSException(ErrorCode.NotEnough_WarSpiritLv, "玩家材料不够");
                }

                gainExp = next_ref.GainExp * next_ref.getCrit();
                int nowExp = player.getPlayerBO().getWarspiritExp() + gainExp;
                this.rebirthLevelUp(player, now_ref, next_ref, nowExp);
            }

            player.getFeature(WarSpiritFeature.class).updatePower();
            WarSpiritLv.SpiritNotify notify = new WarSpiritLv.SpiritNotify(
                player.getPlayerBO().getWarspiritLv(), player.getPlayerBO().getWarspiritExp(), gainExp);
            request.response(notify);
        }
    }

    private void rebirthLevelUp(Player player, RefWarSpiritLv now_ref, RefWarSpiritLv next_ref, int nowExp) {
        int levelUp = 0;

        for (int i = now_ref.id; RefDataMgr.get(RefWarSpiritLv.class, i).Star <= RefWarSpiritLv.sameLevel.get(now_ref.Level).size(); i++) {
            RefWarSpiritLv temp_now_ref = RefDataMgr.get(RefWarSpiritLv.class, i);
            RefWarSpiritLv temp_next_ref = RefDataMgr.get(RefWarSpiritLv.class, i + 1);
            if (temp_next_ref == null) {
                break;
            }

            long needExp = temp_next_ref.Exp;
            if (needExp > nowExp || temp_now_ref.Level != temp_next_ref.Level) {
                break;
            }

            levelUp++;
            nowExp = (int)(nowExp - needExp);
        }

        player.getPlayerBO().saveWarspiritExp(nowExp);
        player.getPlayerBO().saveWarspiritLv(player.getPlayerBO().getWarspiritLv() + levelUp);
    }

    private static class SpiritNotify {
        long warSpiritLv;
        long warSpiritExp;
        long gainExp;

        private SpiritNotify(long rebirthLevel, long rebirthExp, long gainExp) {
            this.warSpiritLv = rebirthLevel;
            this.warSpiritExp = rebirthExp;
            this.gainExp = gainExp;
        }
    }
}
