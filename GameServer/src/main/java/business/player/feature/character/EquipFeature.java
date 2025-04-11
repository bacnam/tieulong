package business.player.feature.character;

import business.player.Player;
import business.player.feature.Feature;
import business.player.item.IItemFilter;
import business.player.item.IUniItemContainer;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.utils.CommMath;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefEquip;
import core.database.game.bo.EquipBO;
import core.network.client2game.handler.chars.EquipOn;
import core.network.proto.EquipMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipFeature
        extends Feature
        implements IUniItemContainer<Equip> {
    public final Map<Long, Equip> equipMap = new HashMap<>();

    public EquipFeature(Player owner) {
        super(owner);
    }

    public PrizeType getType() {
        return PrizeType.Equip;
    }

    public boolean check(int itemId, int count, IItemFilter<Equip> filter) {
        throw new UnsupportedOperationException();
    }

    public Equip consume(int itemId, int count, ItemFlow reason, IItemFilter<Equip> filter) {
        throw new UnsupportedOperationException();
    }

    public int gain(int equipId, int count, ItemFlow reason) {
        RefEquip ref = (RefEquip) RefDataMgr.get(RefEquip.class, Integer.valueOf(equipId));
        if (ref == null) {
            return 0;
        }
        int gained = 0;
        for (int i = 0; i < count; i++) {
            if (getRemain() <= 0 && (reason == ItemFlow.Offline || reason == ItemFlow.Dungeon_Win)) {
                return gained;
            }

            EquipBO bo = new EquipBO();
            bo.setPid(this.player.getPid());
            bo.setEquipId(ref.id);
            for (int attridx = 0; attridx < ref.AttrValueList.size(); attridx++) {
                bo.setAttr(attridx, ((Integer) ref.AttrValueList.get(attridx)).intValue() * CommMath.randomInt(500, 1500) / 10000);
            }
            bo.setGainTime(CommTime.nowSecond());
            bo.insert();
            gain(new Equip(this.player, bo));
            gained++;
        }
        return gained;
    }

    public Equip gainOneEquip(int equipId, ItemFlow reason) {
        RefEquip ref = (RefEquip) RefDataMgr.get(RefEquip.class, Integer.valueOf(equipId));
        if (ref == null) {
            return null;
        }
        if (getRemain() <= 0) {
            return null;
        }

        EquipBO bo = new EquipBO();
        bo.setPid(this.player.getPid());
        bo.setEquipId(ref.id);
        for (int attridx = 0; attridx < ref.AttrValueList.size(); attridx++) {
            bo.setAttr(attridx, ((Integer) ref.AttrValueList.get(attridx)).intValue() * CommMath.randomInt(500, 1500) / 10000);
        }
        bo.setGainTime(CommTime.nowSecond());
        bo.insert();
        Equip equip = new Equip(this.player, bo);
        gain(equip);
        return equip;
    }

    public void loadDB() {
        List<EquipBO> boList = BM.getBM(EquipBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
        for (EquipBO bo : boList) {
            this.equipMap.put(Long.valueOf(bo.getId()), new Equip(this.player, bo));
        }
    }

    public List<Equip> getAllEquips() {
        return new ArrayList<>(this.equipMap.values());
    }

    public Equip getEquip(long sid) {
        return this.equipMap.get(Long.valueOf(sid));
    }

    public void gain(Equip equip) {
        this.equipMap.put(Long.valueOf(equip.getSid()), equip);

        this.player.pushProto("addEquip", new EquipMessage(equip.getBo()));
    }

    public void consume(Equip equip) {
        this.equipMap.remove(Long.valueOf(equip.getSid()));
        equip.getBo().del();
        this.player.pushProto("delEquip", new EquipMessage(equip.getBo()));
    }

    public int getRemain() {
        int totalSize = RefDataMgr.getFactor("PlayerEquipSize", 200);
        int ext_package = this.player.getPlayerBO().getExtPackage();
        totalSize += ext_package;
        for (Character Char : ((CharFeature) this.player.getFeature(CharFeature.class)).getAll().values()) {
            totalSize += Char.getEquips().size();
        }
        return Math.max(0, totalSize - this.equipMap.size());
    }

    public Equip equipOn(long equipSid, EquipPos pos, int charId, Character character) throws WSException {
        Equip equip = getEquip(equipSid);
        if (equip == null) {
            throw new WSException(ErrorCode.Equip_NotFound, "装备[%s]不存在", new Object[]{Long.valueOf(equipSid)});
        }
        if (this.player.getLv() < equip.getLevel()) {
            throw new WSException(ErrorCode.Equip_LevelRequired, "装备[%s]装备需要玩家%s级", new Object[]{Integer.valueOf(equip.getEquipId()), Integer.valueOf(equip.getLevel())});
        }

        if (!equip.getRef().couldEquipOn(pos)) {
            throw new WSException(ErrorCode.Equip_WrongPos, "装备[%s]不能装备在%s上", new Object[]{Integer.valueOf(equip.getEquipId()), pos});
        }
        if ((equip.getRef()).CharID != charId) {
            throw new WSException(ErrorCode.Equip_NotBelong, "装备[%s]不能装备在角色%s上", new Object[]{Integer.valueOf(equip.getEquipId()), pos});
        }

        Character preOwner = equip.getOwner();
        if (preOwner != null) {
            preOwner.unEquip(equip.getPos());
            this.player.pushProto("equipon", new EquipOn.EquipNotify(preOwner.getCharId(), pos, 0L));
        }

        Equip preEquip = character.getEquip(pos);
        if (preEquip != null) {
            character.unEquip(pos);
            preEquip.saveOwner(null, EquipPos.None);
            this.player.pushProto("equipon", new EquipOn.EquipNotify(0, EquipPos.None, preEquip.getSid()));
        }
        character.equip(equip, pos);
        equip.saveOwner(character, pos);
        EquipOn.EquipNotify notify = new EquipOn.EquipNotify(character.getCharId(), pos, equipSid);
        this.player.pushProto("equipon", notify);

        return preEquip;
    }
}

