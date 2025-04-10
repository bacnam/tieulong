package business.player.feature;

import BaseCommon.CommLog;
import business.global.activity.ActivityMgr;
import business.global.activity.detail.AccumConsume;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.logger.flow.ItemFlow;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefTeamExp;
import core.config.refdata.ref.RefVIP;
import core.database.game.bo.PlayerBO;
import core.logger.flow.FlowLogger;
import core.network.proto.Player;
import java.util.ArrayList;
import java.util.List;

public class PlayerCurrency
extends Feature
{
public PlayerCurrency(Player data) {
super(data);
}

public void loadDB() {}

public boolean check(PrizeType type, int count) {
switch (type) {
case Crystal:
return (this.player.getPlayerBO().getCrystal() >= count);
case Gold:
return (this.player.getPlayerBO().getGold() >= count);
case Exp:
return (this.player.getPlayerBO().getExp() >= count);
case StrengthenMaterial:
return (this.player.getPlayerBO().getStrengthenMaterial() >= count);
case GemMaterial:
return (this.player.getPlayerBO().getGemMaterial() >= count);
case StarMaterial:
return (this.player.getPlayerBO().getStarMaterial() >= count);
case MerMaterial:
return (this.player.getPlayerBO().getMerMaterial() >= count);
case WingMaterial:
return (this.player.getPlayerBO().getWingMaterial() >= count);
case null:
return (this.player.getPlayerBO().getArenaToken() >= count);
case EquipInstanceMaterial:
return (this.player.getPlayerBO().getEquipInstanceMaterial() >= count);
case GemInstanceMaterial:
return (this.player.getPlayerBO().getGemInstanceMaterial() >= count);
case MeridianInstanceMaterial:
return (this.player.getPlayerBO().getMeridianInstanceMaterial() >= count);
case RedPiece:
return (this.player.getPlayerBO().getRedPiece() >= count);
case ArtificeMaterial:
return (this.player.getPlayerBO().getArtificeMaterial() >= count);
case WarspiritTalentMaterial:
return (this.player.getPlayerBO().getWarspiritTalentMaterial() >= count);
case GuildDonate:
return (((GuildMemberFeature)this.player.getFeature(GuildMemberFeature.class)).getDonate() >= count);
case Lottery:
return (this.player.getPlayerBO().getLottery() >= count);
case DressMaterial:
return (this.player.getPlayerBO().getDressMaterial() >= count);
} 
CommLog.warn("类型{}未指定货币或商品，无法check", type, new Throwable());
return false;
}

public void consume(PrizeType type, int count, ItemFlow reason) {
if (count <= 0) {
CommLog.error("PlayerCurrency.consume 只能正参, type:{}, count:{}, reason:{}", new Object[] { type, Integer.valueOf(count), reason });
return;
} 
switch (type) {
case Crystal:
consumeCrystal(count, reason);
return;
case Gold:
consumeMoney(count, reason);
return;
case Exp:
consumeExp(count, reason);
return;
case StrengthenMaterial:
consumeStrengthenMaterial(count, reason);
return;
case GemMaterial:
consumeGemMaterial(count, reason);
return;
case StarMaterial:
consumeStarMaterial(count, reason);
return;
case MerMaterial:
consumeMerMaterial(count, reason);
return;
case WingMaterial:
consumeWingMaterial(count, reason);
return;
case null:
consumeArenaToken(count, reason);
return;
case EquipInstanceMaterial:
consumeEquipInstanceMaterial(count, reason);
return;
case GemInstanceMaterial:
consumeGemInstanceMaterial(count, reason);
return;
case MeridianInstanceMaterial:
consumeMeridianInstanceMaterial(count, reason);
return;
case RedPiece:
consumeRedPiece(count, reason);
return;
case ArtificeMaterial:
consumeArtificeMaterial(count, reason);
return;
case WarspiritTalentMaterial:
consumeWarspiritTalentMaterial(count, reason);
return;
case GuildDonate:
consumeGuildDonate(count, reason);
return;
case Lottery:
consumeLottery(count, reason);
return;
case DressMaterial:
consumeDressMaterial(count, reason);
return;
case ExpMaterial:
consumeExpMaterial(count, reason);
return;
} 
CommLog.warn("类型{}未指定货币或商品，无法consume。", type, new Throwable());
}

public boolean checkAndConsume(PrizeType prizeType, int count, ItemFlow reason) {
if (!check(prizeType, count)) {
return false;
}
consume(prizeType, count, reason);
return true;
}

public int gain(PrizeType type, int count, ItemFlow reason) {
int gain, i;
if (count <= 0) {
CommLog.error("PlayerCurrency.gain 禁止传负参, type:{}, count:{}, reason:{}", new Object[] { type, Integer.valueOf(count), reason });
return 0;
} 
switch (type) {
case Crystal:
return gainCrystal(count, reason);
case Gold:
return gainGold(count, reason);
case VipExp:
return gainVipExp(count, reason);
case Exp:
return gainExp(count, reason);
case StrengthenMaterial:
return gainStrengthenMaterial(count, reason);
case GemMaterial:
return gainGemMaterial(count, reason);
case StarMaterial:
return gainStarMaterial(count, reason);
case MerMaterial:
return gainMerMaterial(count, reason);
case WingMaterial:
return gainWingMaterial(count, reason);
case null:
return gainArenaToken(count, reason);
case EquipInstanceMaterial:
return gainEquipInstanceMaterial(count, reason);
case GemInstanceMaterial:
return gainGemInstanceMaterial(count, reason);
case MeridianInstanceMaterial:
return gainMeridianInstanceMaterial(count, reason);
case RedPiece:
return gainRedPiece(count, reason);
case ArtificeMaterial:
return gainArtificeMaterial(count, reason);
case WarspiritTalentMaterial:
return gainWarspiritTalentMaterial(count, reason);
case GuildDonate:
return gainGuildDonate(count, reason);
case Lottery:
return gainLottery(count, reason);
case DressMaterial:
return gainDressMaterial(count, reason);
case ExpMaterial:
gain = gainExpMaterial(count, reason);
consume(type, gain, reason);
for (i = 0; i < count; i++) {
gain(PrizeType.Exp, RefDataMgr.getFactor("ExpMaterialForExp", 4000000), reason);
}
return gain;
} 
CommLog.warn("类型{}未指定货币或商品，无法 获得。", type, new Throwable());
return 0;
}

private void consumeCrystal(int crystal, ItemFlow reason) {
if (crystal <= 0) {
return;
}
lock();
int before = this.player.getPlayerBO().getCrystal();
int finalCrystal = Math.max(0, before - crystal);

this.player.getPlayerBO().saveCrystal(finalCrystal);
unlock();

((AccumConsume)ActivityMgr.getActivity(AccumConsume.class)).handlePlayerChange(this.player, crystal);

ActivityMgr.updateWorldRank(this.player, crystal, RankType.WorldConsume);

crystalLog(reason.value(), -crystal, finalCrystal, before, ConstEnum.ResOpType.Lose);
this.player.pushProperties("crystal", this.player.getPlayerBO().getCrystal());
}

private int gainCrystal(int crystal, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getCrystal();
int finalCrystal = Math.min(RefDataMgr.getFactor("Hero_Max_Crystal", 999999999), before + crystal);

this.player.getPlayerBO().saveCrystal(finalCrystal);
unlock();
this.player.pushProperties("crystal", this.player.getPlayerBO().getCrystal());

crystalLog(reason.value(), crystal, finalCrystal, before, ConstEnum.ResOpType.Gain);

return finalCrystal - before;
}

private void consumeMoney(int money, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getGold();
int finalMoney = Math.max(0, before - money);

this.player.getPlayerBO().saveGold(finalMoney);
unlock();
this.player.pushProperties("gold", this.player.getPlayerBO().getGold());
goldLog(reason.value(), -money, finalMoney, before, ConstEnum.ResOpType.Lose);
}

private int gainGold(int money, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getGold();
int finalMoney = Math.min(RefDataMgr.getFactor("Hero_Max_Gold", 999999999), before + money);

this.player.getPlayerBO().saveGold(finalMoney);
unlock();
this.player.pushProperties("gold", this.player.getPlayerBO().getGold());

goldLog(reason.value(), money, finalMoney, before, ConstEnum.ResOpType.Gain);
return money;
}

private void consumeStrengthenMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getStrengthenMaterial();
int finalMaterial = Math.max(0, before - material);

this.player.getPlayerBO().saveStrengthenMaterial(finalMaterial);
unlock();
this.player.pushProperties("strengthenMaterial", this.player.getPlayerBO().getStrengthenMaterial());
strengthenMaterialLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
}

private int gainStrengthenMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getStrengthenMaterial();
int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_StrenghenMaterial", 999999999), before + material);

this.player.getPlayerBO().saveStrengthenMaterial(finalMaterial);
unlock();
this.player.pushProperties("strengthenMaterial", this.player.getPlayerBO().getStrengthenMaterial());
strengthenMaterialLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
return material;
}

private void consumeGemMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getGemMaterial();
int finalMaterial = Math.max(0, before - material);

this.player.getPlayerBO().saveGemMaterial(finalMaterial);
unlock();
this.player.pushProperties("gemMaterial", this.player.getPlayerBO().getGemMaterial());
gemMaterialLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
}

private int gainGemMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getGemMaterial();
int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_GemMaterial", 999999999), before + material);

this.player.getPlayerBO().saveGemMaterial(finalMaterial);
unlock();
this.player.pushProperties("gemMaterial", this.player.getPlayerBO().getGemMaterial());
gemMaterialLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
return material;
}

private void consumeDressMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getDressMaterial();
int finalMaterial = Math.max(0, before - material);

this.player.getPlayerBO().saveDressMaterial(finalMaterial);
unlock();
this.player.pushProperties("dressMaterial", this.player.getPlayerBO().getDressMaterial());
}

private int gainDressMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getDressMaterial();
int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_GemMaterial", 999999999), before + material);

this.player.getPlayerBO().saveDressMaterial(finalMaterial);
unlock();
this.player.pushProperties("dressMaterial", this.player.getPlayerBO().getDressMaterial());
return material;
}

private void consumeArtificeMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getArtificeMaterial();
int finalMaterial = Math.max(0, before - material);

this.player.getPlayerBO().saveArtificeMaterial(finalMaterial);
unlock();
this.player.pushProperties("artificeMaterial", this.player.getPlayerBO().getArtificeMaterial());
artificeMaterialChargeLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
}

private int gainArtificeMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getArtificeMaterial();
int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_ArtificeMaterial", 999999999), before + material);

this.player.getPlayerBO().saveArtificeMaterial(finalMaterial);
unlock();
this.player.pushProperties("artificeMaterial", this.player.getPlayerBO().getArtificeMaterial());
artificeMaterialChargeLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
return material;
}

private void consumeStarMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getStarMaterial();
int finalMaterial = Math.max(0, before - material);

this.player.getPlayerBO().saveStarMaterial(finalMaterial);
unlock();
this.player.pushProperties("starMaterial", this.player.getPlayerBO().getStarMaterial());
starMaterialLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
}

private int gainStarMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getStarMaterial();
int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_StarMaterial", 999999999), before + material);

this.player.getPlayerBO().saveStarMaterial(finalMaterial);
unlock();
this.player.pushProperties("starMaterial", this.player.getPlayerBO().getStarMaterial());
starMaterialLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
return material;
}

private void consumeMerMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getMerMaterial();
int finalMaterial = Math.max(0, before - material);

this.player.getPlayerBO().saveMerMaterial(finalMaterial);
unlock();
this.player.pushProperties("merMaterial", this.player.getPlayerBO().getMerMaterial());
merMaterialLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
}

private int gainMerMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getMerMaterial();
int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_MerMaterial", 999999999), before + material);

this.player.getPlayerBO().saveMerMaterial(finalMaterial);
unlock();
this.player.pushProperties("merMaterial", this.player.getPlayerBO().getMerMaterial());
merMaterialLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
return material;
}

private void consumeWingMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getWingMaterial();
int finalMaterial = Math.max(0, before - material);

this.player.getPlayerBO().saveWingMaterial(finalMaterial);
unlock();
this.player.pushProperties("wingMaterial", this.player.getPlayerBO().getWingMaterial());
wingMaterialLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
}

private int gainWingMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getWingMaterial();
int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_WingMaterial", 999999999), before + material);

this.player.getPlayerBO().saveWingMaterial(finalMaterial);
unlock();
this.player.pushProperties("wingMaterial", this.player.getPlayerBO().getWingMaterial());
wingMaterialLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
return material;
}

private void consumeArenaToken(int count, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getArenaToken();
int final0 = Math.max(0, before - count);

this.player.getPlayerBO().saveArenaToken(final0);
unlock();
this.player.pushProperties("arenaToken", this.player.getPlayerBO().getArenaToken());
arenaTokenLog(reason.value(), -count, final0, before, ConstEnum.ResOpType.Lose);
}

private int gainArenaToken(int count, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getArenaToken();
int final0 = Math.min(RefDataMgr.getFactor("Hero_Max_ArenaToken", 999999999), before + count);

this.player.getPlayerBO().saveArenaToken(final0);
unlock();
this.player.pushProperties("arenaToken", this.player.getPlayerBO().getArenaToken());
arenaTokenLog(reason.value(), count, final0, before, ConstEnum.ResOpType.Gain);
return count;
}

private void consumeEquipInstanceMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getEquipInstanceMaterial();
int finalMaterial = Math.max(0, before - material);

this.player.getPlayerBO().saveEquipInstanceMaterial(finalMaterial);
unlock();
this.player.pushProperties("equipInstanceMaterial", this.player.getPlayerBO().getEquipInstanceMaterial());
EquipInstanceMaterialLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
}

private int gainEquipInstanceMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getEquipInstanceMaterial();
int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_EquipInstanceMaterial", 999999999), before + material);

this.player.getPlayerBO().saveEquipInstanceMaterial(finalMaterial);
unlock();
this.player.pushProperties("equipInstanceMaterial", this.player.getPlayerBO().getEquipInstanceMaterial());
EquipInstanceMaterialLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
return material;
}

private void consumeGemInstanceMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getGemInstanceMaterial();
int finalMaterial = Math.max(0, before - material);

this.player.getPlayerBO().saveGemInstanceMaterial(finalMaterial);
unlock();
this.player.pushProperties("gemInstanceMaterial", this.player.getPlayerBO().getGemInstanceMaterial());
GemInstanceMaterialLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
}

private int gainGemInstanceMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getGemInstanceMaterial();
int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_GemInstanceMaterial", 999999999), before + material);

this.player.getPlayerBO().saveGemInstanceMaterial(finalMaterial);
unlock();
this.player.pushProperties("gemInstanceMaterial", this.player.getPlayerBO().getGemInstanceMaterial());
GemInstanceMaterialLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
return material;
}

private void consumeMeridianInstanceMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getMeridianInstanceMaterial();
int finalMaterial = Math.max(0, before - material);

this.player.getPlayerBO().saveMeridianInstanceMaterial(finalMaterial);
unlock();
this.player.pushProperties("meridianInstanceMaterial", this.player.getPlayerBO().getMeridianInstanceMaterial());
meridianInstanceMaterialLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
}

private int gainMeridianInstanceMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getMeridianInstanceMaterial();
int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_MeridianInstanceMaterial", 999999999), before + material);

this.player.getPlayerBO().saveMeridianInstanceMaterial(finalMaterial);
unlock();
this.player.pushProperties("meridianInstanceMaterial", this.player.getPlayerBO().getMeridianInstanceMaterial());
meridianInstanceMaterialLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
return material;
}

private void consumeRedPiece(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getRedPiece();
int finalMaterial = Math.max(0, before - material);

this.player.getPlayerBO().saveRedPiece(finalMaterial);
unlock();
this.player.pushProperties("redPiece", this.player.getPlayerBO().getRedPiece());
redPieceLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
}

private int gainRedPiece(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getRedPiece();
int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_RedPiece", 999999999), before + material);

this.player.getPlayerBO().saveRedPiece(finalMaterial);
unlock();
this.player.pushProperties("redPiece", this.player.getPlayerBO().getRedPiece());
redPieceLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
return material;
}

private void consumeWarspiritTalentMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getWarspiritTalentMaterial();
int finalMaterial = Math.max(0, before - material);

this.player.getPlayerBO().saveWarspiritTalentMaterial(finalMaterial);
unlock();
warspiritTalentMaterialChargeLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
this.player.pushProperties("warspiritTalentMaterial", this.player.getPlayerBO().getWarspiritTalentMaterial());
}

private int gainWarspiritTalentMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getWarspiritTalentMaterial();
int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_WarspiritTalentMaterial", 999999999), before + material);

this.player.getPlayerBO().saveWarspiritTalentMaterial(finalMaterial);
unlock();
this.player.pushProperties("warspiritTalentMaterial", this.player.getPlayerBO().getWarspiritTalentMaterial());
warspiritTalentMaterialChargeLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
return material;
}

private void consumeGuildDonate(int donate, ItemFlow reason) {
((GuildMemberFeature)this.player.getFeature(GuildMemberFeature.class)).consumeDonate(donate, reason);
}

private int gainGuildDonate(int donate, ItemFlow reason) {
return ((GuildMemberFeature)this.player.getFeature(GuildMemberFeature.class)).gainDonate(donate, reason);
}

private int gainVipExp(int addExp, ItemFlow reason) {
lock();

int oldLvl = this.player.getPlayerBO().getVipLevel();
int oldExp = this.player.getPlayerBO().getVipExp();

int nowexp = oldExp + addExp;
if (nowexp < 0) {
nowexp = 0;
}
int nowLvl = oldLvl;
while (true) {
RefVIP VIP = (RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(nowLvl + 1));

if (VIP == null) {
break;
}

if (nowexp >= VIP.CurNeedExp) {
nowLvl++;
nowexp -= VIP.CurNeedExp;

continue;
} 

break;
} 
PlayerBO bo = this.player.getPlayerBO();

bo.saveVipExp(nowexp);
if (nowLvl != oldLvl) {
bo.saveVipLevel(nowLvl);
((PlayerBase)this.player.getFeature(PlayerBase.class)).onVipLevelUp(nowLvl, oldLvl);

PlayerMgr.getInstance().tryNotify(this.player);
} 
unlock();

if (nowLvl != oldLvl || nowexp != oldExp) {
this.player.pushProperties("vipExp", this.player.getPlayerBO().getVipExp(), "vipLv", this.player.getPlayerBO().getVipLevel());
}

return addExp;
}

private int gainExp(int addExp, ItemFlow reason) {
if (addExp <= 0) {
return 0;
}

lock();

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
RefTeamExp teamExp = (RefTeamExp)RefDataMgr.get(RefTeamExp.class, Integer.valueOf(nowLvl));
if (teamExp == null) {
break;
}
int levelUpExp = teamExp.UpExp;

if (nowexp >= levelUpExp) {
nowLvl++;
nowexp -= levelUpExp;

continue;
} 
break;
} 
List<Player.Property> properties = new ArrayList<>();
if (oldExp != nowexp) {
this.player.getPlayerBO().saveExp(nowexp);
properties.add(new Player.Property("exp", this.player.getPlayerBO().getExp()));
} 
if (oldLvl != nowLvl) {
this.player.getPlayerBO().saveLv(nowLvl);
properties.add(new Player.Property("lv", this.player.getPlayerBO().getLv()));
((PlayerBase)this.player.getFeature(PlayerBase.class)).onLevelUp(nowLvl);
} 
unlock();
if (properties.size() > 0) {
this.player.pushProperties(properties);
}
expLog(reason.value(), addExp, nowexp, oldExp, ConstEnum.ResOpType.Gain);
return addExp;
}

private void consumeExp(int exp, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getExp();
int finalMaterial = Math.max(0, before - exp);

this.player.getPlayerBO().saveExp(finalMaterial);
unlock();
this.player.pushProperties("exp", this.player.getPlayerBO().getExp());
expLog(reason.value(), -exp, finalMaterial, before, ConstEnum.ResOpType.Lose);
}

public boolean updateMaxGlobalMailID(long mailID) {
boolean ret = false;
lock();
if (this.player.getPlayerBO().getGmMailCheckId() < mailID) {
this.player.getPlayerBO().saveGmMailCheckId(mailID);
ret = true;
} 
unlock();
return ret;
}

private void consumeLottery(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getLottery();
int finalMaterial = Math.max(0, before - material);

this.player.getPlayerBO().saveLottery(finalMaterial);
unlock();
this.player.pushProperties("lottery", this.player.getPlayerBO().getLottery());
lotteryChargeLog(reason.value(), -material, finalMaterial, before, ConstEnum.ResOpType.Lose);
}

private int gainLottery(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getLottery();
int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_Lottery", 999999999), before + material);

this.player.getPlayerBO().saveLottery(finalMaterial);
unlock();
this.player.pushProperties("lottery", this.player.getPlayerBO().getLottery());
lotteryChargeLog(reason.value(), material, finalMaterial, before, ConstEnum.ResOpType.Gain);
return material;
}

private void consumeExpMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getExpMaterial();
int finalMaterial = Math.max(0, before - material);

this.player.getPlayerBO().saveExpMaterial(finalMaterial);
unlock();
}

private int gainExpMaterial(int material, ItemFlow reason) {
lock();
int before = this.player.getPlayerBO().getExpMaterial();
int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_RedPiece", 999999999), before + material);

this.player.getPlayerBO().saveExpMaterial(finalMaterial);
unlock();

return material;
}

public void arenaTokenLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.arenaTokenChargeLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}

public void crystalLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.crystalChargeLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}

public void EquipInstanceMaterialLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.EquipInstanceMaterialChargeLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}

public void expLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.expChargeLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}

public void GemInstanceMaterialLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.GemInstanceMaterialChargeLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}

public void gemMaterialLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.gemMaterialChargeLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}

public void goldLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.goldChargeLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}

public void meridianInstanceMaterialLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.meridianInstanceMaterialChargeLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}

public void merMaterialLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.merMaterialChargeLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}

public void redPieceLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.redPieceChargeLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}

public void starMaterialLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.starMaterialChargeLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}

public void strengthenMaterialLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.strengthenMaterialChargeLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}

public void wingMaterialLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.wingMaterialChargeLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}

public void warspiritTalentMaterialChargeLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.warspiritTalentMaterialChargeLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}

public void artificeMaterialChargeLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.artificeMaterialChargeLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}

public void lotteryChargeLog(int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.lotteryChargeLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}
}

