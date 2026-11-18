package core.network.client2game.handler.chars;

import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.character.WarSpirit;
import business.player.feature.character.WarSpiritFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefSkill;
import core.config.refdata.ref.RefWarSpirit;
import core.config.refdata.ref.RefWarSpiritStar;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class WarSpiritSkill extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        WarSpiritSkill.Request req = new Gson().fromJson(message, WarSpiritSkill.Request.class);
        WarSpirit warSpirit = player.getFeature(WarSpiritFeature.class).getWarSpirit(req.spiritId);
        if (warSpirit == null) {
            throw new WSException(ErrorCode.Char_NotFound, "战灵[%s]不存在或未解锁", req.spiritId);
        } else {
            int skillLevel = warSpirit.getBo().getSkill();
            if (skillLevel + 1 >= RefDataMgr.getFactor("MaxSkillLevel", 100)) {
                throw new WSException(ErrorCode.Skill_LevelFull, "技能等级[%s]已满", skillLevel);
            } else {
                int star = warSpirit.getBo().getStar();
                RefWarSpiritStar refstar = RefWarSpiritStar.spiritMap.get(req.spiritId).get(star);
                if (skillLevel + 1 >= refstar.SkillLevel) {
                    throw new WSException(ErrorCode.Skill_LevelFull, "当前星级下技能等级[%s]已满", skillLevel);
                } else {
                    RefWarSpirit refspirit = RefDataMgr.get(RefWarSpirit.class, req.spiritId);
                    RefSkill refSkill = RefDataMgr.get(RefSkill.class, refspirit.SkillList.get(0));
                    if (refSkill == null) {
                        throw new WSException(ErrorCode.Skill_NotFound, "技能没找到");
                    } else {
                        PlayerCurrency playerCurrency = player.getFeature(PlayerCurrency.class);
                        int goldRequired = refSkill.GoldAdd * (skillLevel + 1 - 1) + refSkill.Gold;
                        if (!playerCurrency.check(PrizeType.Gold, goldRequired)) {
                            throw new WSException(ErrorCode.NotEnough_Money, "玩家金币:%s<升级金币:%s", player.getPlayerBO().getGold(), goldRequired);
                        } else {
                            playerCurrency.consume(PrizeType.Gold, goldRequired, ItemFlow.SkillLevelUp);
                            warSpirit.getBo().saveSkill(skillLevel + 1);
                            warSpirit.onAttrChanged();
                            WarSpiritSkill.SkillNotify notify = new WarSpiritSkill.SkillNotify(warSpirit.getSpiritId(), warSpirit.getBo().getSkill());
                            request.response(notify);
                        }
                    }
                }
            }
        }
    }

    public static class Request {
        int spiritId;
    }

    public static class SkillNotify {
        int spiritId;
        long skillLevel;

        public SkillNotify(int spiritId, long skillLevel) {
            this.spiritId = spiritId;
            this.skillLevel = skillLevel;
        }
    }
}
