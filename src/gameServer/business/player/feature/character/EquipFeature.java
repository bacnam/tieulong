/*     */ package business.player.feature.character;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import business.player.item.IItemFilter;
/*     */ import business.player.item.IUniItemContainer;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.EquipPos;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.utils.CommMath;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefEquip;
/*     */ import core.database.game.bo.EquipBO;
/*     */ import core.network.client2game.handler.chars.EquipOn;
/*     */ import core.network.proto.EquipMessage;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class EquipFeature
/*     */   extends Feature
/*     */   implements IUniItemContainer<Equip> {
/*  28 */   public final Map<Long, Equip> equipMap = new HashMap<>();
/*     */   
/*     */   public EquipFeature(Player owner) {
/*  31 */     super(owner);
/*     */   }
/*     */ 
/*     */   
/*     */   public PrizeType getType() {
/*  36 */     return PrizeType.Equip;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean check(int itemId, int count, IItemFilter<Equip> filter) {
/*  41 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Equip consume(int itemId, int count, ItemFlow reason, IItemFilter<Equip> filter) {
/*  46 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int gain(int equipId, int count, ItemFlow reason) {
/*  51 */     RefEquip ref = (RefEquip)RefDataMgr.get(RefEquip.class, Integer.valueOf(equipId));
/*  52 */     if (ref == null) {
/*  53 */       return 0;
/*     */     }
/*  55 */     int gained = 0;
/*  56 */     for (int i = 0; i < count; i++) {
/*  57 */       if (getRemain() <= 0 && (reason == ItemFlow.Offline || reason == ItemFlow.Dungeon_Win)) {
/*  58 */         return gained;
/*     */       }
/*     */       
/*  61 */       EquipBO bo = new EquipBO();
/*  62 */       bo.setPid(this.player.getPid());
/*  63 */       bo.setEquipId(ref.id);
/*  64 */       for (int attridx = 0; attridx < ref.AttrValueList.size(); attridx++) {
/*  65 */         bo.setAttr(attridx, ((Integer)ref.AttrValueList.get(attridx)).intValue() * CommMath.randomInt(500, 1500) / 10000);
/*     */       }
/*  67 */       bo.setGainTime(CommTime.nowSecond());
/*  68 */       bo.insert();
/*  69 */       gain(new Equip(this.player, bo));
/*  70 */       gained++;
/*     */     } 
/*  72 */     return gained;
/*     */   }
/*     */   
/*     */   public Equip gainOneEquip(int equipId, ItemFlow reason) {
/*  76 */     RefEquip ref = (RefEquip)RefDataMgr.get(RefEquip.class, Integer.valueOf(equipId));
/*  77 */     if (ref == null) {
/*  78 */       return null;
/*     */     }
/*  80 */     if (getRemain() <= 0) {
/*  81 */       return null;
/*     */     }
/*     */     
/*  84 */     EquipBO bo = new EquipBO();
/*  85 */     bo.setPid(this.player.getPid());
/*  86 */     bo.setEquipId(ref.id);
/*  87 */     for (int attridx = 0; attridx < ref.AttrValueList.size(); attridx++) {
/*  88 */       bo.setAttr(attridx, ((Integer)ref.AttrValueList.get(attridx)).intValue() * CommMath.randomInt(500, 1500) / 10000);
/*     */     }
/*  90 */     bo.setGainTime(CommTime.nowSecond());
/*  91 */     bo.insert();
/*  92 */     Equip equip = new Equip(this.player, bo);
/*  93 */     gain(equip);
/*  94 */     return equip;
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadDB() {
/*  99 */     List<EquipBO> boList = BM.getBM(EquipBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
/* 100 */     for (EquipBO bo : boList) {
/* 101 */       this.equipMap.put(Long.valueOf(bo.getId()), new Equip(this.player, bo));
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Equip> getAllEquips() {
/* 106 */     return new ArrayList<>(this.equipMap.values());
/*     */   }
/*     */   
/*     */   public Equip getEquip(long sid) {
/* 110 */     return this.equipMap.get(Long.valueOf(sid));
/*     */   }
/*     */ 
/*     */   
/*     */   public void gain(Equip equip) {
/* 115 */     this.equipMap.put(Long.valueOf(equip.getSid()), equip);
/*     */     
/* 117 */     this.player.pushProto("addEquip", new EquipMessage(equip.getBo()));
/*     */   }
/*     */   
/*     */   public void consume(Equip equip) {
/* 121 */     this.equipMap.remove(Long.valueOf(equip.getSid()));
/* 122 */     equip.getBo().del();
/* 123 */     this.player.pushProto("delEquip", new EquipMessage(equip.getBo()));
/*     */   }
/*     */   
/*     */   public int getRemain() {
/* 127 */     int totalSize = RefDataMgr.getFactor("PlayerEquipSize", 200);
/* 128 */     int ext_package = this.player.getPlayerBO().getExtPackage();
/* 129 */     totalSize += ext_package;
/* 130 */     for (Character Char : ((CharFeature)this.player.getFeature(CharFeature.class)).getAll().values()) {
/* 131 */       totalSize += Char.getEquips().size();
/*     */     }
/* 133 */     return Math.max(0, totalSize - this.equipMap.size());
/*     */   }
/*     */   
/*     */   public Equip equipOn(long equipSid, EquipPos pos, int charId, Character character) throws WSException {
/* 137 */     Equip equip = getEquip(equipSid);
/* 138 */     if (equip == null) {
/* 139 */       throw new WSException(ErrorCode.Equip_NotFound, "装备[%s]不存在", new Object[] { Long.valueOf(equipSid) });
/*     */     }
/* 141 */     if (this.player.getLv() < equip.getLevel()) {
/* 142 */       throw new WSException(ErrorCode.Equip_LevelRequired, "装备[%s]装备需要玩家%s级", new Object[] { Integer.valueOf(equip.getEquipId()), Integer.valueOf(equip.getLevel()) });
/*     */     }
/*     */     
/* 145 */     if (!equip.getRef().couldEquipOn(pos)) {
/* 146 */       throw new WSException(ErrorCode.Equip_WrongPos, "装备[%s]不能装备在%s上", new Object[] { Integer.valueOf(equip.getEquipId()), pos });
/*     */     }
/* 148 */     if ((equip.getRef()).CharID != charId) {
/* 149 */       throw new WSException(ErrorCode.Equip_NotBelong, "装备[%s]不能装备在角色%s上", new Object[] { Integer.valueOf(equip.getEquipId()), pos });
/*     */     }
/*     */     
/* 152 */     Character preOwner = equip.getOwner();
/* 153 */     if (preOwner != null) {
/* 154 */       preOwner.unEquip(equip.getPos());
/* 155 */       this.player.pushProto("equipon", new EquipOn.EquipNotify(preOwner.getCharId(), pos, 0L));
/*     */     } 
/*     */     
/* 158 */     Equip preEquip = character.getEquip(pos);
/* 159 */     if (preEquip != null) {
/* 160 */       character.unEquip(pos);
/* 161 */       preEquip.saveOwner(null, EquipPos.None);
/* 162 */       this.player.pushProto("equipon", new EquipOn.EquipNotify(0, EquipPos.None, preEquip.getSid()));
/*     */     } 
/* 164 */     character.equip(equip, pos);
/* 165 */     equip.saveOwner(character, pos);
/* 166 */     EquipOn.EquipNotify notify = new EquipOn.EquipNotify(character.getCharId(), pos, equipSid);
/* 167 */     this.player.pushProto("equipon", notify);
/*     */     
/* 169 */     return preEquip;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/character/EquipFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */