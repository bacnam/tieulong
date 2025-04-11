package core.network.client2game.handler.chars;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.RankWing;
import business.global.notice.NoticeMgr;
import business.global.rank.RankManager;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import business.player.feature.player.TitleFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.*;
import com.zhonglian.server.common.utils.Random;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCharacter;
import core.config.refdata.ref.RefWing;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class WingUp
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        Character character = ((CharFeature) player.getFeature(CharFeature.class)).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[]{Integer.valueOf(req.charId)});
        }

        int nowLevel = character.getBo().getWing();
        if (nowLevel >= RefDataMgr.size(RefWing.class) - 1) {
            throw new WSException(ErrorCode.Wing_LevelFull, "翅膀已满级");
        }
        RefWing ref = (RefWing) RefDataMgr.get(RefWing.class, Integer.valueOf(nowLevel + 1));
        RefWing now_ref = (RefWing) RefDataMgr.get(RefWing.class, Integer.valueOf(nowLevel));
        int gainExp = 0;

        if (now_ref.Level != ref.Level) {
            character.getBo().saveWing(nowLevel + 1);
            if (ref.Level >= RefDataMgr.getFactor("WingMarqueeLevel", 5)) {
                RefCharacter refc = (RefCharacter) RefDataMgr.get(RefCharacter.class, Integer.valueOf(character.getCharId()));
                NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.Wing, new String[]{player.getName(), refc.Name, (new StringBuilder(String.valueOf(ref.Level))).toString()});
            }
        } else {

            int need_count = 0;
            int player_count = 0;
            PrizeType prizetype = null;
            float crit = 0.0F;
            int exp = 0;
            ErrorCode error = null;

            if (req.way.equalsIgnoreCase("material")) {
                prizetype = PrizeType.WingMaterial;
                need_count = ref.Material;
                player_count = player.getPlayerBO().getWingMaterial();
                crit = ref.MaterialCrit;
                exp = ref.MaterialExp;
                error = ErrorCode.NotEnough_WingMaterial;
            } else if (req.way.equalsIgnoreCase("gold")) {
                prizetype = PrizeType.Gold;
                need_count = ref.Gold;
                player_count = player.getPlayerBO().getGold();
                crit = ref.GoldCrit;
                exp = ref.GoldExp;
                error = ErrorCode.NotEnough_Money;
            } else {
                throw new WSException(ErrorCode.Wing_NotChooseWay, "没有选择翅膀升阶方式");
            }

            PlayerCurrency playerCurrency = (PlayerCurrency) player.getFeature(PlayerCurrency.class);
            if (!playerCurrency.check(prizetype, need_count)) {
                throw new WSException(error, "玩家材料:%s<升级材料:%s", new Object[]{Integer.valueOf(player_count), Integer.valueOf(need_count)});
            }
            playerCurrency.consume(prizetype, need_count, ItemFlow.WingLevelUp);
            if (Random.nextInt(10000) < crit * 10000.0F) {
                gainExp = exp * 2;
            } else {
                gainExp = exp;
            }
            int nowExp = character.getBo().getWingExp() + gainExp;
            int levelUp = 0;
            for (int i = ref.id; i <= RefDataMgr.size(RefWing.class); i++) {
                RefWing temp_ref = (RefWing) RefDataMgr.get(RefWing.class, Integer.valueOf(i - 1));
                RefWing next_ref = (RefWing) RefDataMgr.get(RefWing.class, Integer.valueOf(i));

                if (next_ref == null)
                    break;
                long needExp = next_ref.Exp;

                if (needExp > nowExp) {
                    break;
                }
                if (temp_ref.Level != next_ref.Level) {
                    break;
                }
                levelUp++;
                nowExp = (int) (nowExp - needExp);
            }

            character.getBo().saveWingExp(nowExp);
            character.getBo().saveWing(nowLevel + levelUp);
        }

        int newLevel = character.getBo().getWing();
        if (newLevel != nowLevel) {
            int wingLevel = 0;
            for (Character charac : ((CharFeature) player.getFeature(CharFeature.class)).getAll().values()) {
                wingLevel += charac.getBo().getWing();
            }

            character.onAttrChanged();

            RankManager.getInstance().update(RankType.WingLevel, player.getPid(), wingLevel);

            ((RankWing) ActivityMgr.getActivity(RankWing.class)).UpdateMaxRequire_cost(player, wingLevel);

            ((TitleFeature) player.getFeature(TitleFeature.class)).updateMax(Title.WingLevel, Integer.valueOf(newLevel));
        }

        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.WingUp);
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.WingUp_M1);
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.WingUp_M2);

        WingNotify notify = new WingNotify(req.charId, character.getBo().getWing(), character.getBo().getWingExp(), gainExp);
        request.response(notify);
    }

    public static class Request {
        int charId;
        String way;
    }

    public static class WingNotify {
        int charId;
        long wingLevel;
        long wingExp;
        long gainExp;

        public WingNotify(int charId, long wingLevel, long wingExp, long gainExp) {
            this.charId = charId;
            this.wingLevel = wingLevel;
            this.wingExp = wingExp;
            this.gainExp = gainExp;
        }
    }
}

