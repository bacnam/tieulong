package core.network.client2game.handler.chars;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.RankWing;
import business.global.rank.RankManager;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefWingActive;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class WingActive extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        WingActive.Request req = new Gson().fromJson(message, WingActive.Request.class);
        Character character = player.getFeature(CharFeature.class).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", req.charId);
        } else {
            int wingLevel = 0;

            for (Character charac : player.getFeature(CharFeature.class).getAll().values()) {
                wingLevel += charac.getBo().getWing();
            }

            if (wingLevel != 0) {
                throw new WSException(ErrorCode.Wing_AlreadyActive, "翅膀已激活");
            } else {
                RefWingActive ref = RefDataMgr.get(RefWingActive.class, req.refId);
                if (ref == null) {
                    throw new WSException(ErrorCode.Wing_NotFound, "没有这个档位");
                } else if (!player.getFeature(PlayerCurrency.class).checkAndConsume(PrizeType.Crystal, ref.Discount, ItemFlow.WingActive)) {
                    throw new WSException(ErrorCode.NotEnough_Crystal, "元宝不足");
                } else {
                    character.getBo().saveWing(ref.Level);
                    int wingLevelnow = 0;

                    for (Character charac : player.getFeature(CharFeature.class).getAll().values()) {
                        wingLevelnow += charac.getBo().getWing();
                    }

                    character.onAttrChanged();
                    RankManager.getInstance().update(RankType.WingLevel, player.getPid(), wingLevelnow);
                    ActivityMgr.getActivity(RankWing.class).UpdateMaxRequire_cost(player, wingLevelnow);
                    WingActive.WingNotify notify = new WingActive.WingNotify(req.charId, character.getBo().getWing(), character.getBo().getWingExp());
                    request.response(notify);
                }
            }
        }
    }

    public static class Request {
        int charId;
        int refId;
    }

    private static class WingNotify {
        int charId;
        long wingLevel;
        long wingExp;

        private WingNotify(int charId, long wingLevel, long wingExp) {
            this.charId = charId;
            this.wingLevel = wingLevel;
            this.wingExp = wingExp;
        }
    }
}
