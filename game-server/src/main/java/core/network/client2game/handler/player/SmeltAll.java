package core.network.client2game.handler.player;

import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.character.Character;
import business.player.feature.character.Equip;
import business.player.feature.character.EquipFeature;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.Quality;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefEquip;
import core.config.refdata.ref.RefSmelt;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SmeltAll extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        SmeltAll.Request req = new Gson().fromJson(message, SmeltAll.Request.class);
        EquipFeature equipFeature = player.getFeature(EquipFeature.class);
        Map<Integer, Map<EquipPos, Equip>> map = new HashMap<>();
        equipFeature.getAllEquips().forEach(equip -> {
            RefEquip refx = equip.getRef();
            EquipPos weakest = null;
            int power = equip.getBasePower();

            for (EquipPos posx : refx.validEquipPos()) {
                if (map.get(refx.CharID) == null) {
                    Map<EquipPos, Equip> map1 = new HashMap<>();
                    map1.put(posx, equip);
                    map.put(refx.CharID, map1);
                    return;
                }

                Equip cur = map.get(refx.CharID).get(posx);
                if (cur == null) {
                    weakest = posx;
                    break;
                }

                if (cur.getBasePower() < power) {
                    weakest = posx;
                    power = cur.getBasePower();
                }
            }

            if (weakest != null) {
                map.get(refx.CharID).put(weakest, equip);
            }
        });
        int StrengthenMaterial = 0;
        int Gold = 0;
        int RedPiece = 0;
        Iterator reward = equipFeature.getAllEquips().iterator();

        while (true) {
            Equip equip;
            RefEquip ref;
            while (true) {
                if (!reward.hasNext()) {
                    PlayerCurrency playerCurrency = player.getFeature(PlayerCurrency.class);
                    if (StrengthenMaterial != 0) {
                        playerCurrency.gain(PrizeType.StrengthenMaterial, StrengthenMaterial, ItemFlow.Smelt);
                    }

                    if (Gold != 0) {
                        playerCurrency.gain(PrizeType.Gold, Gold, ItemFlow.Smelt);
                    }

                    if (RedPiece != 0) {
                        playerCurrency.gain(PrizeType.RedPiece, RedPiece, ItemFlow.Smelt);
                    }

                    Reward rewardx = new Reward();
                    rewardx.add(PrizeType.StrengthenMaterial, StrengthenMaterial);
                    rewardx.add(PrizeType.Gold, Gold);
                    rewardx.add(PrizeType.RedPiece, RedPiece);
                    if (rewardx.size() > 0) {
                        player.getFeature(AchievementFeature.class).updateInc(AchievementType.Smelt);
                        player.getFeature(AchievementFeature.class).updateInc(AchievementType.Smelt_M1);
                        player.getFeature(AchievementFeature.class).updateInc(AchievementType.Smelt_M2);
                        player.getFeature(AchievementFeature.class).updateInc(AchievementType.Smelt_M3);
                    }

                    request.response(rewardx);
                    return;
                }

                equip = (Equip)reward.next();
                Character onwer = equip.getOwner();
                ref = equip.getRef();
                if (onwer == null) {
                    if (!req.isKeep) {
                        break;
                    }

                    boolean flag = false;

                    for (EquipPos pos : ref.validEquipPos()) {
                        if (map.get(ref.CharID).get(pos) == equip) {
                            flag = true;
                            break;
                        }
                    }

                    if (!flag) {
                        break;
                    }
                }
            }

            RefSmelt refsmelt = RefDataMgr.get(RefSmelt.class, ref.getQuality().toString() + equip.getLevel());
            StrengthenMaterial += refsmelt.Strengthen;
            Gold += refsmelt.Gold;
            if (ref.getQuality() == Quality.Red) {
                RedPiece += refsmelt.RedPiece;
            }

            equipFeature.consume(equip);
        }
    }

    public static class Request {
        boolean isKeep;
    }
}
