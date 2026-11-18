package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.character.WarSpirit;
import business.player.feature.character.WarSpiritFeature;
import business.player.item.UniformItem;
import com.google.gson.Gson;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefWarSpirit;
import core.config.refdata.ref.RefWarSpiritStar;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WarSpiritStar extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        WarSpiritStar.Request req = new Gson().fromJson(message, WarSpiritStar.Request.class);
        WarSpirit warspirit = player.getFeature(WarSpiritFeature.class).getWarSpirit(req.spiritId);
        if (warspirit == null) {
            throw new WSException(ErrorCode.Char_NotFound, "战灵[%s]不存在或未解锁", req.spiritId);
        } else {
            int star = warspirit.getBo().getStar();
            if (star >= RefWarSpiritStar.spiritMap.get(req.spiritId).size() - 1) {
                throw new WSException(ErrorCode.WarSpiritStarFull, "战灵[%s]星级已满", req.spiritId);
            } else {
                RefWarSpirit ref = RefDataMgr.get(RefWarSpirit.class, warspirit.getSpiritId());
                int costId = ref.StarMaterial;
                RefWarSpiritStar refstar = RefWarSpiritStar.spiritMap.get(req.spiritId).get(star + 1);
                if (refstar.NeedLv > player.getPlayerBO().getWarspiritLv()) {
                    throw new WSException(
                        ErrorCode.WarSpiritLevelRequire, "战灵[%s]等级[%s]不足[%s]", req.spiritId, player.getPlayerBO().getWarspiritLv(), refstar.NeedLv
                    );
                } else {
                    int costCount = refstar.WarspiritNum;
                    UniformItem costItem = new UniformItem(costId, costCount);
                    UniformItem extraItem = new UniformItem(refstar.ExtraId, refstar.ExtraCount);
                    List<UniformItem> list = new ArrayList<>();
                    list.add(costItem);
                    list.add(extraItem);
                    if (!player.getFeature(PlayerItem.class).check(list)) {
                        throw new WSException(ErrorCode.NotEnough_Currency, "材料不足");
                    } else {
                        player.getFeature(PlayerItem.class).consume(list, ItemFlow.WarSpiritStar);
                        warspirit.getBo().saveStar(star + 1);
                        warspirit.onAttrChanged();
                        request.response(new WarSpiritStar.Response(req.spiritId, warspirit.getBo().getStar()));
                    }
                }
            }
        }
    }

    public static class Request {
        int spiritId;
    }

    private static class Response {
        int spiritId;
        int star;

        private Response(int spiritId, int star) {
            this.spiritId = spiritId;
            this.star = star;
        }
    }
}
