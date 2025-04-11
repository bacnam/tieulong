package core.network.client2game.handler.chars;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.RankWing;
import business.global.rank.RankManager;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import business.player.feature.player.TitleFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.Title;
import com.zhonglian.server.common.utils.Random;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefWing;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class WingLevelUp
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        Character character = ((CharFeature) player.getFeature(CharFeature.class)).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[]{Integer.valueOf(req.charId)});
        }

        if (player.getVipLevel() < RefDataMgr.getFactor("VipUnlockUpWing", 4)) {
            throw new WSException(ErrorCode.NotEnough_VIP, "vip等级不足");
        }

        int nowLevel = character.getBo().getWing();
        if (nowLevel >= RefDataMgr.size(RefWing.class) - 1) {
            throw new WSException(ErrorCode.Wing_LevelFull, "翅膀已满级");
        }

        RefWing now_ref = (RefWing) RefDataMgr.get(RefWing.class, Integer.valueOf(nowLevel));
        int Num = 0;
        int totalMoney = 0;
        int totalCrystal = 0;
        boolean useCrystal = false;
        int finalexp = character.getBo().getWingExp();
        int finallevel = character.getBo().getWing();

        PrizeType prizetype = null;
        ErrorCode error = null;

        if (req.way.equalsIgnoreCase("material")) {
            prizetype = PrizeType.WingMaterial;
            error = ErrorCode.NotEnough_WingMaterial;
            if (req.useCrystal == 1) {
                useCrystal = true;
            }
        } else if (req.way.equalsIgnoreCase("gold")) {
            prizetype = PrizeType.Gold;
            error = ErrorCode.NotEnough_Money;
        } else {
            throw new WSException(ErrorCode.Wing_NotChooseWay, "没有选择翅膀升阶方式");
        }

        while (true) {
            int tmp_nowLevel = finallevel;
            RefWing ref = (RefWing) RefDataMgr.get(RefWing.class, Integer.valueOf(tmp_nowLevel + 1));
            if (ref == null) {
                break;
            }
            if (now_ref.Level != ref.Level) {
                finallevel = ref.id;
                break;
            }
            int gainExp = 0;

            int need_count = 0;
            float crit = 0.0F;
            int exp = 0;

            if (req.way.equalsIgnoreCase("material")) {
                need_count = ref.Material;
                crit = ref.MaterialCrit;
                exp = ref.MaterialExp;
            } else if (req.way.equalsIgnoreCase("gold")) {
                need_count = ref.Gold;
                crit = ref.GoldCrit;
                exp = ref.GoldExp;
            } else {
                throw new WSException(ErrorCode.Wing_NotChooseWay, "没有选择翅膀升阶方式");
            }

            totalMoney += need_count;

            PlayerCurrency playerCurrency1 = (PlayerCurrency) player.getFeature(PlayerCurrency.class);
            if (!playerCurrency1.check(prizetype, totalMoney)) {
                if (useCrystal) {
                    totalMoney -= need_count;
                    totalCrystal += need_count * RefDataMgr.getFactor("WingPrice", 5);
                } else {
                    totalMoney -= need_count;

                    break;
                }
            }
            if (Random.nextInt(10000) < crit * 10000.0F) {
                gainExp = exp * 2;
            } else {
                gainExp = exp;
            }
            int nowExp = finalexp + gainExp;
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
            finalexp = nowExp;
            finallevel = tmp_nowLevel + levelUp;
            Num++;
        }
        PlayerCurrency playerCurrency = (PlayerCurrency) player.getFeature(PlayerCurrency.class);
        if (!playerCurrency.check(prizetype, totalMoney)) {
            throw new WSException(error, "玩家材料:<升级材料:%s", new Object[]{Integer.valueOf(totalMoney)});
        }
        if (!playerCurrency.check(PrizeType.Crystal, totalCrystal)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "元宝不足");
        }
        if (totalMoney > 0)
            playerCurrency.consume(prizetype, totalMoney, ItemFlow.WingLevelUp);
        if (totalCrystal > 0) {
            playerCurrency.consume(PrizeType.Crystal, totalCrystal, ItemFlow.WingLevelUp);
        }
        character.getBo().setWingExp(finalexp);
        character.getBo().setWing(finallevel);
        character.getBo().saveAll();

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

        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.WingUp, Integer.valueOf(Num));
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.WingUp_M1, Integer.valueOf(Num));
        ((AchievementFeature) player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.WingUp_M2, Integer.valueOf(Num));

        WingLevelNotify notify = new WingLevelNotify(req.charId, character.getBo().getWing(), character.getBo().getWingExp());
        request.response(notify);
    }

    public static class Request {
        int charId;
        String way;
        int useCrystal;
    }

    public static class WingLevelNotify {
        int charId;
        long wingLevel;
        long wingExp;

        public WingLevelNotify(int charId, long wingLevel, long wingExp) {
            this.charId = charId;
            this.wingLevel = wingLevel;
            this.wingExp = wingExp;
        }
    }
}

