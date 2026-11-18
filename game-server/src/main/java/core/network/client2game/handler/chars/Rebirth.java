package core.network.client2game.handler.chars;

import business.global.rank.RankManager;
import business.player.Player;
import business.player.feature.PlayerBase;
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
import core.config.refdata.ref.RefRebirth;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class Rebirth extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Rebirth.Request req = new Gson().fromJson(message, Rebirth.Request.class);
        Character character = player.getFeature(CharFeature.class).getCharacter(req.charId);
        if (character == null) {
            throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", req.charId);
        } else {
            int nowLevel = character.getBo().getRebirth();
            if (nowLevel >= RefDataMgr.size(RefRebirth.class) - 1) {
                throw new WSException(ErrorCode.Rebirth_LevelFull, "转生已满级");
            } else {
                RefRebirth now_ref = RefDataMgr.get(RefRebirth.class, nowLevel);
                RefRebirth next_ref = RefDataMgr.get(RefRebirth.class, nowLevel + 1);
                int gainExp = 0;
                if (now_ref.Level != next_ref.Level) {
                    for (Character tmpchar : player.getFeature(CharFeature.class).getAll().values()) {
                        RefRebirth tmp_ref = RefDataMgr.get(RefRebirth.class, tmpchar.getBo().getRebirth());
                        int level = tmp_ref.Level;
                        if (tmp_ref.Star != RefRebirth.sameLevel.get(level).size()) {
                            throw new WSException(ErrorCode.Rebirth_NotEnough, "转生条件不足");
                        }
                    }

                    for (Character char1 : player.getFeature(CharFeature.class).getAll().values()) {
                        RefRebirth now_ref1 = RefDataMgr.get(RefRebirth.class, char1.getBo().getRebirth());
                        RefRebirth next_ref1 = RefDataMgr.get(RefRebirth.class, char1.getBo().getRebirth() + 1);
                        int nowExp1 = char1.getBo().getRebirthExp();
                        this.rebirthLevelUp(now_ref1, next_ref1, char1, nowExp1, true);
                        player.pushProto(
                            "charRebirth", new Rebirth.CharRebirth(char1.getCharId(), char1.getBo().getRebirth(), char1.getBo().getRebirthExp())
                        );
                    }

                    player.getPlayerBO().saveLv(player.getLv() + RefDataMgr.getFactor("RebirthAddLevel", 10));
                    player.getFeature(PlayerBase.class).onLevelUp(player.getPlayerBO().getLv());
                    player.pushProperties("lv", player.getPlayerBO().getLv());
                } else {
                    PlayerCurrency playerCurrency = player.getFeature(PlayerCurrency.class);
                    if (!playerCurrency.check(PrizeType.Exp, next_ref.CostTeamExp)) {
                        throw new WSException(ErrorCode.NotEnough_Exp, "玩家经验:%s<升级经验:%s", player.getPlayerBO().getExp(), next_ref.CostTeamExp);
                    }

                    playerCurrency.consume(PrizeType.Exp, next_ref.CostTeamExp, ItemFlow.Rebirth);
                    gainExp = next_ref.GainExp * next_ref.getCrit();
                    int nowExp = character.getBo().getRebirthExp() + gainExp;
                    this.rebirthLevelUp(now_ref, next_ref, character, nowExp, false);
                }

                int newLevel = character.getBo().getRebirth();
                if (newLevel != nowLevel) {
                    int rebirthLevel = 0;

                    for (Character charac : player.getFeature(CharFeature.class).getAll().values()) {
                        rebirthLevel += charac.getBo().getRebirth();
                    }

                    character.onAttrChanged();
                    RankManager.getInstance().update(RankType.Level, player.getPid(), player.getLv(), rebirthLevel);
                }

                Rebirth.WingNotify notify = new Rebirth.WingNotify(req.charId, character.getBo().getRebirth(), character.getBo().getRebirthExp(), gainExp);
                request.response(notify);
            }
        }
    }

    private void rebirthLevelUp(RefRebirth now_ref, RefRebirth next_ref, Character character, int nowExp, boolean isRebirth) {
        int levelUp = 0;

        for (int i = now_ref.id; RefDataMgr.get(RefRebirth.class, i).Star <= RefRebirth.sameLevel.get(now_ref.Level).size(); i++) {
            RefRebirth temp_now_ref = RefDataMgr.get(RefRebirth.class, i);
            RefRebirth temp_next_ref = RefDataMgr.get(RefRebirth.class, i + 1);
            if (temp_next_ref == null) {
                break;
            }

            long needExp = temp_next_ref.Exp;
            if (needExp > nowExp || temp_now_ref.Level != temp_next_ref.Level && !isRebirth) {
                break;
            }

            levelUp++;
            nowExp = (int)(nowExp - needExp);
        }

        character.getBo().saveRebirthExp(nowExp);
        character.getBo().saveRebirth(character.getBo().getRebirth() + levelUp);
    }

    private static class CharRebirth {
        int charId;
        int rebirthLevel;
        int rebirthExp;

        private CharRebirth(int charId, int rebirthLevel, int rebirthExp) {
            this.charId = charId;
            this.rebirthLevel = rebirthLevel;
            this.rebirthExp = rebirthExp;
        }
    }

    public static class Request {
        int charId;
    }

    private static class WingNotify {
        int charId;
        long rebirthLevel;
        long rebirthExp;
        long gainExp;

        private WingNotify(int charId, long rebirthLevel, long rebirthExp, long gainExp) {
            this.charId = charId;
            this.rebirthLevel = rebirthLevel;
            this.rebirthExp = rebirthExp;
            this.gainExp = gainExp;
        }
    }
}
