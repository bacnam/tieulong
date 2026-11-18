package business.player.feature;

import BaseCommon.CommLog;
import business.global.activity.ActivityMgr;
import business.global.activity.detail.AccumConsume;
import business.player.PlayerMgr;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.ConstEnum.ResOpType;
import com.zhonglian.server.logger.flow.ItemFlow;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefTeamExp;
import core.config.refdata.ref.RefVIP;
import core.database.game.bo.PlayerBO;
import core.logger.flow.FlowLogger;
import core.network.proto.Player;
import java.util.ArrayList;
import java.util.List;

public class PlayerCurrency extends Feature {
    public PlayerCurrency(business.player.Player data) {
        super(data);
    }

    @Override
    public void loadDB() {
    }

    public boolean check(PrizeType type, int count) {
        switch (type) {
            case Gold:
                if (this.player.getPlayerBO().getGold() >= count) {
                    return true;
                }

                return false;
            case Crystal:
                if (this.player.getPlayerBO().getCrystal() >= count) {
                    return true;
                }

                return false;
            case Exp:
                if (this.player.getPlayerBO().getExp() >= count) {
                    return true;
                }

                return false;
            case VipExp:
            case Item:
            case Equip:
            case ExpMaterial:
            case MarryRing:
            case DivorcePermit:
            case Warspirit:
            case Dress:
            case Title:
            default:
                CommLog.warn("类型{}未指定货币或商品，无法check", type, new Throwable());
                return false;
            case StrengthenMaterial:
                if (this.player.getPlayerBO().getStrengthenMaterial() >= count) {
                    return true;
                }

                return false;
            case GemMaterial:
                if (this.player.getPlayerBO().getGemMaterial() >= count) {
                    return true;
                }

                return false;
            case StarMaterial:
                if (this.player.getPlayerBO().getStarMaterial() >= count) {
                    return true;
                }

                return false;
            case ArenaToken:
                if (this.player.getPlayerBO().getArenaToken() >= count) {
                    return true;
                }

                return false;
            case MerMaterial:
                if (this.player.getPlayerBO().getMerMaterial() >= count) {
                    return true;
                }

                return false;
            case WingMaterial:
                if (this.player.getPlayerBO().getWingMaterial() >= count) {
                    return true;
                }

                return false;
            case EquipInstanceMaterial:
                if (this.player.getPlayerBO().getEquipInstanceMaterial() >= count) {
                    return true;
                }

                return false;
            case GemInstanceMaterial:
                if (this.player.getPlayerBO().getGemInstanceMaterial() >= count) {
                    return true;
                }

                return false;
            case MeridianInstanceMaterial:
                if (this.player.getPlayerBO().getMeridianInstanceMaterial() >= count) {
                    return true;
                }

                return false;
            case RedPiece:
                if (this.player.getPlayerBO().getRedPiece() >= count) {
                    return true;
                }

                return false;
            case ArtificeMaterial:
                if (this.player.getPlayerBO().getArtificeMaterial() >= count) {
                    return true;
                }

                return false;
            case WarspiritTalentMaterial:
                if (this.player.getPlayerBO().getWarspiritTalentMaterial() >= count) {
                    return true;
                }

                return false;
            case GuildDonate:
                if (this.player.getFeature(GuildMemberFeature.class).getDonate() >= count) {
                    return true;
                }

                return false;
            case Lottery:
                if (this.player.getPlayerBO().getLottery() >= count) {
                    return true;
                }

                return false;
            case DressMaterial:
                return this.player.getPlayerBO().getDressMaterial() >= count;
        }
    }

    public void consume(PrizeType type, int count, ItemFlow reason) {
        if (count <= 0) {
            CommLog.error("PlayerCurrency.consume 只能正参, type:{}, count:{}, reason:{}", type, count, reason);
        } else {
            switch (type) {
                case Gold:
                    this.consumeMoney(count, reason);
                    return;
                case Crystal:
                    this.consumeCrystal(count, reason);
                    return;
                case Exp:
                    this.consumeExp(count, reason);
                    return;
                case VipExp:
                case Item:
                case Equip:
                case MarryRing:
                case DivorcePermit:
                case Warspirit:
                case Dress:
                case Title:
                default:
                    CommLog.warn("类型{}未指定货币或商品，无法consume。", type, new Throwable());
                    return;
                case StrengthenMaterial:
                    this.consumeStrengthenMaterial(count, reason);
                    return;
                case GemMaterial:
                    this.consumeGemMaterial(count, reason);
                    return;
                case StarMaterial:
                    this.consumeStarMaterial(count, reason);
                    return;
                case ArenaToken:
                    this.consumeArenaToken(count, reason);
                    return;
                case MerMaterial:
                    this.consumeMerMaterial(count, reason);
                    return;
                case WingMaterial:
                    this.consumeWingMaterial(count, reason);
                    return;
                case EquipInstanceMaterial:
                    this.consumeEquipInstanceMaterial(count, reason);
                    return;
                case GemInstanceMaterial:
                    this.consumeGemInstanceMaterial(count, reason);
                    return;
                case MeridianInstanceMaterial:
                    this.consumeMeridianInstanceMaterial(count, reason);
                    return;
                case RedPiece:
                    this.consumeRedPiece(count, reason);
                    return;
                case ArtificeMaterial:
                    this.consumeArtificeMaterial(count, reason);
                    return;
                case WarspiritTalentMaterial:
                    this.consumeWarspiritTalentMaterial(count, reason);
                    return;
                case GuildDonate:
                    this.consumeGuildDonate(count, reason);
                    return;
                case Lottery:
                    this.consumeLottery(count, reason);
                    return;
                case ExpMaterial:
                    this.consumeExpMaterial(count, reason);
                    return;
                case DressMaterial:
                    this.consumeDressMaterial(count, reason);
            }
        }
    }

    public boolean checkAndConsume(PrizeType prizeType, int count, ItemFlow reason) {
        if (!this.check(prizeType, count)) {
            return false;
        } else {
            this.consume(prizeType, count, reason);
            return true;
        }
    }

    public int gain(PrizeType type, int count, ItemFlow reason) {
        if (count <= 0) {
            CommLog.error("PlayerCurrency.gain 禁止传负参, type:{}, count:{}, reason:{}", type, count, reason);
            return 0;
        } else {
            switch (type) {
                case Gold:
                    return this.gainGold(count, reason);
                case Crystal:
                    return this.gainCrystal(count, reason);
                case Exp:
                    return this.gainExp(count, reason);
                case VipExp:
                    return this.gainVipExp(count, reason);
                case Item:
                case Equip:
                case MarryRing:
                case DivorcePermit:
                case Warspirit:
                case Dress:
                case Title:
                default:
                    CommLog.warn("类型{}未指定货币或商品，无法 获得。", type, new Throwable());
                    return 0;
                case StrengthenMaterial:
                    return this.gainStrengthenMaterial(count, reason);
                case GemMaterial:
                    return this.gainGemMaterial(count, reason);
                case StarMaterial:
                    return this.gainStarMaterial(count, reason);
                case ArenaToken:
                    return this.gainArenaToken(count, reason);
                case MerMaterial:
                    return this.gainMerMaterial(count, reason);
                case WingMaterial:
                    return this.gainWingMaterial(count, reason);
                case EquipInstanceMaterial:
                    return this.gainEquipInstanceMaterial(count, reason);
                case GemInstanceMaterial:
                    return this.gainGemInstanceMaterial(count, reason);
                case MeridianInstanceMaterial:
                    return this.gainMeridianInstanceMaterial(count, reason);
                case RedPiece:
                    return this.gainRedPiece(count, reason);
                case ArtificeMaterial:
                    return this.gainArtificeMaterial(count, reason);
                case WarspiritTalentMaterial:
                    return this.gainWarspiritTalentMaterial(count, reason);
                case GuildDonate:
                    return this.gainGuildDonate(count, reason);
                case Lottery:
                    return this.gainLottery(count, reason);
                case ExpMaterial:
                    int gain = this.gainExpMaterial(count, reason);
                    this.consume(type, gain, reason);

                    for (int i = 0; i < count; i++) {
                        this.gain(PrizeType.Exp, RefDataMgr.getFactor("ExpMaterialForExp", 4000000), reason);
                    }

                    return gain;
                case DressMaterial:
                    return this.gainDressMaterial(count, reason);
            }
        }
    }

    private void consumeCrystal(int crystal, ItemFlow reason) {
        if (crystal > 0) {
            this.lock();
            int before = this.player.getPlayerBO().getCrystal();
            int finalCrystal = Math.max(0, before - crystal);
            this.player.getPlayerBO().saveCrystal(finalCrystal);
            this.unlock();
            ActivityMgr.getActivity(AccumConsume.class).handlePlayerChange(this.player, crystal);
            ActivityMgr.updateWorldRank(this.player, crystal, RankType.WorldConsume);
            this.crystalLog(reason.value(), -crystal, finalCrystal, before, ResOpType.Lose);
            this.player.pushProperties("crystal", this.player.getPlayerBO().getCrystal());
        }
    }

    private int gainCrystal(int crystal, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getCrystal();
        int finalCrystal = Math.min(RefDataMgr.getFactor("Hero_Max_Crystal", 999999999), before + crystal);
        this.player.getPlayerBO().saveCrystal(finalCrystal);
        this.unlock();
        this.player.pushProperties("crystal", this.player.getPlayerBO().getCrystal());
        this.crystalLog(reason.value(), crystal, finalCrystal, before, ResOpType.Gain);
        return finalCrystal - before;
    }

    private void consumeMoney(int money, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getGold();
        int finalMoney = Math.max(0, before - money);
        this.player.getPlayerBO().saveGold(finalMoney);
        this.unlock();
        this.player.pushProperties("gold", this.player.getPlayerBO().getGold());
        this.goldLog(reason.value(), -money, finalMoney, before, ResOpType.Lose);
    }

    private int gainGold(int money, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getGold();
        int finalMoney = Math.min(RefDataMgr.getFactor("Hero_Max_Gold", 999999999), before + money);
        this.player.getPlayerBO().saveGold(finalMoney);
        this.unlock();
        this.player.pushProperties("gold", this.player.getPlayerBO().getGold());
        this.goldLog(reason.value(), money, finalMoney, before, ResOpType.Gain);
        return money;
    }

    private void consumeStrengthenMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getStrengthenMaterial();
        int finalMaterial = Math.max(0, before - material);
        this.player.getPlayerBO().saveStrengthenMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("strengthenMaterial", this.player.getPlayerBO().getStrengthenMaterial());
        this.strengthenMaterialLog(reason.value(), -material, finalMaterial, before, ResOpType.Lose);
    }

    private int gainStrengthenMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getStrengthenMaterial();
        int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_StrenghenMaterial", 999999999), before + material);
        this.player.getPlayerBO().saveStrengthenMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("strengthenMaterial", this.player.getPlayerBO().getStrengthenMaterial());
        this.strengthenMaterialLog(reason.value(), material, finalMaterial, before, ResOpType.Gain);
        return material;
    }

    private void consumeGemMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getGemMaterial();
        int finalMaterial = Math.max(0, before - material);
        this.player.getPlayerBO().saveGemMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("gemMaterial", this.player.getPlayerBO().getGemMaterial());
        this.gemMaterialLog(reason.value(), -material, finalMaterial, before, ResOpType.Lose);
    }

    private int gainGemMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getGemMaterial();
        int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_GemMaterial", 999999999), before + material);
        this.player.getPlayerBO().saveGemMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("gemMaterial", this.player.getPlayerBO().getGemMaterial());
        this.gemMaterialLog(reason.value(), material, finalMaterial, before, ResOpType.Gain);
        return material;
    }

    private void consumeDressMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getDressMaterial();
        int finalMaterial = Math.max(0, before - material);
        this.player.getPlayerBO().saveDressMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("dressMaterial", this.player.getPlayerBO().getDressMaterial());
    }

    private int gainDressMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getDressMaterial();
        int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_GemMaterial", 999999999), before + material);
        this.player.getPlayerBO().saveDressMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("dressMaterial", this.player.getPlayerBO().getDressMaterial());
        return material;
    }

    private void consumeArtificeMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getArtificeMaterial();
        int finalMaterial = Math.max(0, before - material);
        this.player.getPlayerBO().saveArtificeMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("artificeMaterial", this.player.getPlayerBO().getArtificeMaterial());
        this.artificeMaterialChargeLog(reason.value(), -material, finalMaterial, before, ResOpType.Lose);
    }

    private int gainArtificeMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getArtificeMaterial();
        int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_ArtificeMaterial", 999999999), before + material);
        this.player.getPlayerBO().saveArtificeMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("artificeMaterial", this.player.getPlayerBO().getArtificeMaterial());
        this.artificeMaterialChargeLog(reason.value(), material, finalMaterial, before, ResOpType.Gain);
        return material;
    }

    private void consumeStarMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getStarMaterial();
        int finalMaterial = Math.max(0, before - material);
        this.player.getPlayerBO().saveStarMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("starMaterial", this.player.getPlayerBO().getStarMaterial());
        this.starMaterialLog(reason.value(), -material, finalMaterial, before, ResOpType.Lose);
    }

    private int gainStarMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getStarMaterial();
        int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_StarMaterial", 999999999), before + material);
        this.player.getPlayerBO().saveStarMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("starMaterial", this.player.getPlayerBO().getStarMaterial());
        this.starMaterialLog(reason.value(), material, finalMaterial, before, ResOpType.Gain);
        return material;
    }

    private void consumeMerMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getMerMaterial();
        int finalMaterial = Math.max(0, before - material);
        this.player.getPlayerBO().saveMerMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("merMaterial", this.player.getPlayerBO().getMerMaterial());
        this.merMaterialLog(reason.value(), -material, finalMaterial, before, ResOpType.Lose);
    }

    private int gainMerMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getMerMaterial();
        int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_MerMaterial", 999999999), before + material);
        this.player.getPlayerBO().saveMerMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("merMaterial", this.player.getPlayerBO().getMerMaterial());
        this.merMaterialLog(reason.value(), material, finalMaterial, before, ResOpType.Gain);
        return material;
    }

    private void consumeWingMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getWingMaterial();
        int finalMaterial = Math.max(0, before - material);
        this.player.getPlayerBO().saveWingMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("wingMaterial", this.player.getPlayerBO().getWingMaterial());
        this.wingMaterialLog(reason.value(), -material, finalMaterial, before, ResOpType.Lose);
    }

    private int gainWingMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getWingMaterial();
        int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_WingMaterial", 999999999), before + material);
        this.player.getPlayerBO().saveWingMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("wingMaterial", this.player.getPlayerBO().getWingMaterial());
        this.wingMaterialLog(reason.value(), material, finalMaterial, before, ResOpType.Gain);
        return material;
    }

    private void consumeArenaToken(int count, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getArenaToken();
        int final0 = Math.max(0, before - count);
        this.player.getPlayerBO().saveArenaToken(final0);
        this.unlock();
        this.player.pushProperties("arenaToken", this.player.getPlayerBO().getArenaToken());
        this.arenaTokenLog(reason.value(), -count, final0, before, ResOpType.Lose);
    }

    private int gainArenaToken(int count, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getArenaToken();
        int final0 = Math.min(RefDataMgr.getFactor("Hero_Max_ArenaToken", 999999999), before + count);
        this.player.getPlayerBO().saveArenaToken(final0);
        this.unlock();
        this.player.pushProperties("arenaToken", this.player.getPlayerBO().getArenaToken());
        this.arenaTokenLog(reason.value(), count, final0, before, ResOpType.Gain);
        return count;
    }

    private void consumeEquipInstanceMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getEquipInstanceMaterial();
        int finalMaterial = Math.max(0, before - material);
        this.player.getPlayerBO().saveEquipInstanceMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("equipInstanceMaterial", this.player.getPlayerBO().getEquipInstanceMaterial());
        this.EquipInstanceMaterialLog(reason.value(), -material, finalMaterial, before, ResOpType.Lose);
    }

    private int gainEquipInstanceMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getEquipInstanceMaterial();
        int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_EquipInstanceMaterial", 999999999), before + material);
        this.player.getPlayerBO().saveEquipInstanceMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("equipInstanceMaterial", this.player.getPlayerBO().getEquipInstanceMaterial());
        this.EquipInstanceMaterialLog(reason.value(), material, finalMaterial, before, ResOpType.Gain);
        return material;
    }

    private void consumeGemInstanceMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getGemInstanceMaterial();
        int finalMaterial = Math.max(0, before - material);
        this.player.getPlayerBO().saveGemInstanceMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("gemInstanceMaterial", this.player.getPlayerBO().getGemInstanceMaterial());
        this.GemInstanceMaterialLog(reason.value(), -material, finalMaterial, before, ResOpType.Lose);
    }

    private int gainGemInstanceMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getGemInstanceMaterial();
        int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_GemInstanceMaterial", 999999999), before + material);
        this.player.getPlayerBO().saveGemInstanceMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("gemInstanceMaterial", this.player.getPlayerBO().getGemInstanceMaterial());
        this.GemInstanceMaterialLog(reason.value(), material, finalMaterial, before, ResOpType.Gain);
        return material;
    }

    private void consumeMeridianInstanceMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getMeridianInstanceMaterial();
        int finalMaterial = Math.max(0, before - material);
        this.player.getPlayerBO().saveMeridianInstanceMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("meridianInstanceMaterial", this.player.getPlayerBO().getMeridianInstanceMaterial());
        this.meridianInstanceMaterialLog(reason.value(), -material, finalMaterial, before, ResOpType.Lose);
    }

    private int gainMeridianInstanceMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getMeridianInstanceMaterial();
        int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_MeridianInstanceMaterial", 999999999), before + material);
        this.player.getPlayerBO().saveMeridianInstanceMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("meridianInstanceMaterial", this.player.getPlayerBO().getMeridianInstanceMaterial());
        this.meridianInstanceMaterialLog(reason.value(), material, finalMaterial, before, ResOpType.Gain);
        return material;
    }

    private void consumeRedPiece(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getRedPiece();
        int finalMaterial = Math.max(0, before - material);
        this.player.getPlayerBO().saveRedPiece(finalMaterial);
        this.unlock();
        this.player.pushProperties("redPiece", this.player.getPlayerBO().getRedPiece());
        this.redPieceLog(reason.value(), -material, finalMaterial, before, ResOpType.Lose);
    }

    private int gainRedPiece(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getRedPiece();
        int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_RedPiece", 999999999), before + material);
        this.player.getPlayerBO().saveRedPiece(finalMaterial);
        this.unlock();
        this.player.pushProperties("redPiece", this.player.getPlayerBO().getRedPiece());
        this.redPieceLog(reason.value(), material, finalMaterial, before, ResOpType.Gain);
        return material;
    }

    private void consumeWarspiritTalentMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getWarspiritTalentMaterial();
        int finalMaterial = Math.max(0, before - material);
        this.player.getPlayerBO().saveWarspiritTalentMaterial(finalMaterial);
        this.unlock();
        this.warspiritTalentMaterialChargeLog(reason.value(), -material, finalMaterial, before, ResOpType.Lose);
        this.player.pushProperties("warspiritTalentMaterial", this.player.getPlayerBO().getWarspiritTalentMaterial());
    }

    private int gainWarspiritTalentMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getWarspiritTalentMaterial();
        int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_WarspiritTalentMaterial", 999999999), before + material);
        this.player.getPlayerBO().saveWarspiritTalentMaterial(finalMaterial);
        this.unlock();
        this.player.pushProperties("warspiritTalentMaterial", this.player.getPlayerBO().getWarspiritTalentMaterial());
        this.warspiritTalentMaterialChargeLog(reason.value(), material, finalMaterial, before, ResOpType.Gain);
        return material;
    }

    private void consumeGuildDonate(int donate, ItemFlow reason) {
        this.player.getFeature(GuildMemberFeature.class).consumeDonate(donate, reason);
    }

    private int gainGuildDonate(int donate, ItemFlow reason) {
        return this.player.getFeature(GuildMemberFeature.class).gainDonate(donate, reason);
    }

    private int gainVipExp(int addExp, ItemFlow reason) {
        this.lock();
        int oldLvl = this.player.getPlayerBO().getVipLevel();
        int oldExp = this.player.getPlayerBO().getVipExp();
        int nowexp = oldExp + addExp;
        if (nowexp < 0) {
            nowexp = 0;
        }

        int nowLvl = oldLvl;

        while (true) {
            RefVIP VIP = RefDataMgr.get(RefVIP.class, nowLvl + 1);
            if (VIP == null || nowexp < VIP.CurNeedExp) {
                PlayerBO bo = this.player.getPlayerBO();
                bo.saveVipExp(nowexp);
                if (nowLvl != oldLvl) {
                    bo.saveVipLevel(nowLvl);
                    this.player.getFeature(PlayerBase.class).onVipLevelUp(nowLvl, oldLvl);
                    PlayerMgr.getInstance().tryNotify(this.player);
                }

                this.unlock();
                if (nowLvl != oldLvl || nowexp != oldExp) {
                    this.player.pushProperties("vipExp", this.player.getPlayerBO().getVipExp(), "vipLv", this.player.getPlayerBO().getVipLevel());
                }

                return addExp;
            }

            nowLvl++;
            nowexp -= VIP.CurNeedExp;
        }
    }

    private int gainExp(int addExp, ItemFlow reason) {
        if (addExp <= 0) {
            return 0;
        } else {
            this.lock();
            int oldLvl = this.player.getPlayerBO().getLv();
            int oldExp = this.player.getPlayerBO().getExp();
            int nowexp = oldExp + addExp;
            if (nowexp < 0) {
                nowexp = 0;
            }

            nowexp = Math.min(nowexp, RefDataMgr.getFactor("HeroMaxExp", 999999999));
            int nowLvl = oldLvl;

            while (true) {
                int MAX_TEAMLV = RefDataMgr.getFactor("HeroMaxLevel", 80);
                if (nowLvl >= MAX_TEAMLV) {
                    break;
                }

                RefTeamExp teamExp = RefDataMgr.get(RefTeamExp.class, nowLvl);
                if (teamExp == null) {
                    break;
                }

                int levelUpExp = teamExp.UpExp;
                if (nowexp < levelUpExp) {
                    break;
                }

                nowLvl++;
                nowexp -= levelUpExp;
            }

            List<Player.Property> properties = new ArrayList<>();
            if (oldExp != nowexp) {
                this.player.getPlayerBO().saveExp(nowexp);
                properties.add(new Player.Property("exp", this.player.getPlayerBO().getExp()));
            }

            if (oldLvl != nowLvl) {
                this.player.getPlayerBO().saveLv(nowLvl);
                properties.add(new Player.Property("lv", this.player.getPlayerBO().getLv()));
                this.player.getFeature(PlayerBase.class).onLevelUp(nowLvl);
            }

            this.unlock();
            if (properties.size() > 0) {
                this.player.pushProperties(properties);
            }

            this.expLog(reason.value(), addExp, nowexp, oldExp, ResOpType.Gain);
            return addExp;
        }
    }

    private void consumeExp(int exp, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getExp();
        int finalMaterial = Math.max(0, before - exp);
        this.player.getPlayerBO().saveExp(finalMaterial);
        this.unlock();
        this.player.pushProperties("exp", this.player.getPlayerBO().getExp());
        this.expLog(reason.value(), -exp, finalMaterial, before, ResOpType.Lose);
    }

    public boolean updateMaxGlobalMailID(long mailID) {
        boolean ret = false;
        this.lock();
        if (this.player.getPlayerBO().getGmMailCheckId() < mailID) {
            this.player.getPlayerBO().saveGmMailCheckId(mailID);
            ret = true;
        }

        this.unlock();
        return ret;
    }

    private void consumeLottery(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getLottery();
        int finalMaterial = Math.max(0, before - material);
        this.player.getPlayerBO().saveLottery(finalMaterial);
        this.unlock();
        this.player.pushProperties("lottery", this.player.getPlayerBO().getLottery());
        this.lotteryChargeLog(reason.value(), -material, finalMaterial, before, ResOpType.Lose);
    }

    private int gainLottery(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getLottery();
        int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_Lottery", 999999999), before + material);
        this.player.getPlayerBO().saveLottery(finalMaterial);
        this.unlock();
        this.player.pushProperties("lottery", this.player.getPlayerBO().getLottery());
        this.lotteryChargeLog(reason.value(), material, finalMaterial, before, ResOpType.Gain);
        return material;
    }

    private void consumeExpMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getExpMaterial();
        int finalMaterial = Math.max(0, before - material);
        this.player.getPlayerBO().saveExpMaterial(finalMaterial);
        this.unlock();
    }

    private int gainExpMaterial(int material, ItemFlow reason) {
        this.lock();
        int before = this.player.getPlayerBO().getExpMaterial();
        int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_RedPiece", 999999999), before + material);
        this.player.getPlayerBO().saveExpMaterial(finalMaterial);
        this.unlock();
        return material;
    }

    public void arenaTokenLog(int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.arenaTokenChargeLog(
            this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason, crystal, finalCrystal, before, opType.ordinal()
        );
    }

    public void crystalLog(int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.crystalChargeLog(
            this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason, crystal, finalCrystal, before, opType.ordinal()
        );
    }

    public void EquipInstanceMaterialLog(int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.EquipInstanceMaterialChargeLog(
            this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason, crystal, finalCrystal, before, opType.ordinal()
        );
    }

    public void expLog(int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.expChargeLog(this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason, crystal, finalCrystal, before, opType.ordinal());
    }

    public void GemInstanceMaterialLog(int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.GemInstanceMaterialChargeLog(
            this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason, crystal, finalCrystal, before, opType.ordinal()
        );
    }

    public void gemMaterialLog(int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.gemMaterialChargeLog(
            this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason, crystal, finalCrystal, before, opType.ordinal()
        );
    }

    public void goldLog(int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.goldChargeLog(this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason, crystal, finalCrystal, before, opType.ordinal());
    }

    public void meridianInstanceMaterialLog(int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.meridianInstanceMaterialChargeLog(
            this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason, crystal, finalCrystal, before, opType.ordinal()
        );
    }

    public void merMaterialLog(int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.merMaterialChargeLog(
            this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason, crystal, finalCrystal, before, opType.ordinal()
        );
    }

    public void redPieceLog(int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.redPieceChargeLog(
            this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason, crystal, finalCrystal, before, opType.ordinal()
        );
    }

    public void starMaterialLog(int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.starMaterialChargeLog(
            this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason, crystal, finalCrystal, before, opType.ordinal()
        );
    }

    public void strengthenMaterialLog(int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.strengthenMaterialChargeLog(
            this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason, crystal, finalCrystal, before, opType.ordinal()
        );
    }

    public void wingMaterialLog(int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.wingMaterialChargeLog(
            this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason, crystal, finalCrystal, before, opType.ordinal()
        );
    }

    public void warspiritTalentMaterialChargeLog(int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.warspiritTalentMaterialChargeLog(
            this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason, crystal, finalCrystal, before, opType.ordinal()
        );
    }

    public void artificeMaterialChargeLog(int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.artificeMaterialChargeLog(
            this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason, crystal, finalCrystal, before, opType.ordinal()
        );
    }

    public void lotteryChargeLog(int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.lotteryChargeLog(
            this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason, crystal, finalCrystal, before, opType.ordinal()
        );
    }
}
