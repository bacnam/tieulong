/*     */ package business.player.feature.character;
/*     */ 
/*     */ import business.global.notice.NoticeMgr;
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import business.player.item.IItemFilter;
/*     */ import business.player.item.IUniItemContainer;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.DressType;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefDress;
/*     */ import core.database.game.bo.DressBO;
/*     */ import core.network.client2game.handler.chars.DressOn;
/*     */ import core.network.proto.DressInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class DressFeature
/*     */   extends Feature
/*     */   implements IUniItemContainer<DressBO>
/*     */ {
/*  30 */   public final Map<Long, DressBO> dressMap = new HashMap<>();
/*     */   
/*     */   public DressFeature(Player owner) {
/*  33 */     super(owner);
/*     */   }
/*     */ 
/*     */   
/*     */   public PrizeType getType() {
/*  38 */     return PrizeType.Dress;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean check(int itemId, int count, IItemFilter<DressBO> filter) {
/*  43 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public DressBO consume(int itemId, int count, ItemFlow reason, IItemFilter<DressBO> filter) {
/*  48 */     DressBO bo = getDressByDressId(itemId);
/*  49 */     if (bo != null) {
/*  50 */       removeDress(bo);
/*  51 */       this.player.pushProto("delDress", new DressInfo(bo));
/*     */     } 
/*  53 */     return bo;
/*     */   }
/*     */ 
/*     */   
/*     */   public int gain(int dressId, int count, ItemFlow reason) {
/*  58 */     RefDress ref = (RefDress)RefDataMgr.get(RefDress.class, Integer.valueOf(dressId));
/*  59 */     if (ref == null) {
/*  60 */       return 0;
/*     */     }
/*  62 */     int gained = 0;
/*  63 */     for (int i = 0; i < count; i++) {
/*     */       
/*  65 */       DressBO bo = new DressBO();
/*  66 */       bo.setPid(this.player.getPid());
/*  67 */       bo.setDressId(dressId);
/*  68 */       bo.setType(ref.Type.ordinal());
/*  69 */       bo.setActiveTime(CommTime.nowSecond());
/*  70 */       bo.insert();
/*  71 */       gain(bo, ref);
/*  72 */       gained++;
/*     */     } 
/*  74 */     return gained;
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadDB() {
/*  79 */     List<DressBO> boList = BM.getBM(DressBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
/*  80 */     for (DressBO bo : boList) {
/*  81 */       if (!onlycheck(bo)) {
/*  82 */         bo.del();
/*     */         continue;
/*     */       } 
/*  85 */       this.dressMap.put(Long.valueOf(bo.getId()), bo);
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<DressBO> getAllDress() {
/*  90 */     return new ArrayList<>(this.dressMap.values());
/*     */   }
/*     */   
/*     */   public List<DressInfo> getAllDressInfo() {
/*  94 */     List<DressInfo> list = new ArrayList<>();
/*  95 */     checkDressList(getAllDress());
/*  96 */     for (DressBO bo : getAllDress()) {
/*  97 */       list.add(new DressInfo(bo));
/*     */     }
/*  99 */     return list;
/*     */   }
/*     */   
/*     */   public DressBO getDress(long sid) {
/* 103 */     return this.dressMap.get(Long.valueOf(sid));
/*     */   }
/*     */   
/*     */   public DressBO getDressByDressId(int dressId) {
/* 107 */     for (DressBO bo : this.dressMap.values()) {
/* 108 */       if (bo.getDressId() == dressId && checkDress(bo)) {
/* 109 */         return bo;
/*     */       }
/*     */     } 
/*     */     
/* 113 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void gain(DressBO bo, RefDress ref) {
/* 118 */     this.dressMap.put(Long.valueOf(bo.getId()), bo);
/*     */     
/* 120 */     this.player.pushProto("addDress", new DressInfo(bo));
/*     */     
/* 122 */     NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.DressActive, new String[] { this.player.getName(), ref.Name });
/*     */   }
/*     */ 
/*     */   
/*     */   public int gainAndEquip(int dressId, int count, ItemFlow reason) throws WSException {
/* 127 */     RefDress ref = (RefDress)RefDataMgr.get(RefDress.class, Integer.valueOf(dressId));
/* 128 */     if (ref == null) {
/* 129 */       return 0;
/*     */     }
/* 131 */     int gained = 0;
/* 132 */     for (int i = 0; i < count; i++) {
/* 133 */       if (getDressByDressId(dressId) != null) {
/* 134 */         DressBO bo = getDressByDressId(dressId);
/* 135 */         if (bo.getEquipTime() != 0) {
/* 136 */           bo.saveEquipTime(bo.getEquipTime() + ref.TimeLimit);
/*     */           
/* 138 */           this.player.pushProto("addDress", new DressInfo(bo));
/*     */         } 
/*     */       } else {
/*     */         
/* 142 */         DressBO bo = new DressBO();
/* 143 */         bo.setPid(this.player.getPid());
/* 144 */         bo.setDressId(dressId);
/* 145 */         bo.setType(ref.Type.ordinal());
/* 146 */         bo.setActiveTime(CommTime.nowSecond());
/* 147 */         bo.insert();
/* 148 */         gainAndEquip(bo, ref);
/* 149 */         gained++;
/*     */       } 
/*     */     } 
/* 152 */     return gained;
/*     */   }
/*     */ 
/*     */   
/*     */   public void gainAndEquip(DressBO bo, RefDress ref) throws WSException {
/* 157 */     this.dressMap.put(Long.valueOf(bo.getId()), bo);
/*     */     
/* 159 */     this.player.pushProto("addDress", new DressInfo(bo));
/*     */     
/* 161 */     NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.DressActive, new String[] { this.player.getName(), ref.Name });
/*     */     
/* 163 */     Active(bo.getId(), ref.Type, ref.CharId, ((CharFeature)this.player.getFeature(CharFeature.class)).getCharacter(ref.CharId));
/*     */   }
/*     */   
/*     */   public void consume(DressBO bo) {
/* 167 */     this.dressMap.remove(Long.valueOf(bo.getId()));
/* 168 */     bo.del();
/* 169 */     this.player.pushProto("delDress", new DressInfo(bo));
/*     */   }
/*     */   
/*     */   public void Active(long equipSid, DressType type, int charId, Character character) throws WSException {
/* 173 */     DressBO dressbo = getDress(equipSid);
/* 174 */     if (dressbo == null) {
/* 175 */       throw new WSException(ErrorCode.Dress_NotFound, "时装[%s]不存在", new Object[] { Long.valueOf(equipSid) });
/*     */     }
/* 177 */     RefDress ref = (RefDress)RefDataMgr.get(RefDress.class, Integer.valueOf(dressbo.getDressId()));
/* 178 */     if (ref.CharId != 0 && ref.CharId != charId) {
/* 179 */       throw new WSException(ErrorCode.Equip_NotBelong, "时装[%s]不能装备在角色%s上", new Object[] { Integer.valueOf(dressbo.getDressId()), Integer.valueOf(charId) });
/*     */     }
/*     */     
/* 182 */     if (!checkDress(dressbo)) {
/* 183 */       throw new WSException(ErrorCode.Dress_OverTime, "时装超时");
/*     */     }
/*     */     
/* 186 */     Character preowner = ((CharFeature)this.player.getFeature(CharFeature.class)).getCharacter(dressbo.getCharId());
/* 187 */     if (preowner != null) {
/* 188 */       preowner.unEquipDress(type);
/* 189 */       this.player.pushProto("dresson", new DressOn.DressNotify(preowner.getCharId(), type, 0L));
/*     */     } 
/* 191 */     character.activeDress(type, dressbo);
/* 192 */     DressOn.DressNotify notify = new DressOn.DressNotify(character.getCharId(), type, equipSid);
/* 193 */     this.player.pushProto("dresson", notify);
/* 194 */     this.player.pushProto("dressinfo", new DressInfo(dressbo));
/* 195 */     ((CharFeature)this.player.getFeature(CharFeature.class)).updateCharPower();
/*     */   }
/*     */   
/*     */   public boolean checkDress(DressBO dressbo) {
/* 199 */     boolean check = onlycheck(dressbo);
/* 200 */     if (!check) {
/* 201 */       removeDress(dressbo);
/*     */     }
/* 203 */     return check;
/*     */   }
/*     */   
/*     */   public int checkDressList(List<DressBO> list) {
/* 207 */     List<DressBO> tmp_list = new ArrayList<>();
/* 208 */     for (DressBO bo : list) {
/* 209 */       if (!onlycheck(bo)) {
/* 210 */         tmp_list.add(bo);
/*     */       }
/*     */     } 
/* 213 */     for (DressBO bo : tmp_list) {
/* 214 */       removeDress(bo);
/*     */     }
/*     */     
/* 217 */     return tmp_list.size();
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeDress(DressBO bo) {
/* 222 */     this.dressMap.remove(Long.valueOf(bo.getId()));
/* 223 */     if (bo.getCharId() != 0) {
/* 224 */       Character character = ((CharFeature)this.player.getFeature(CharFeature.class)).getCharacter(bo.getCharId());
/* 225 */       character.removeDress(bo);
/*     */     } 
/* 227 */     bo.del();
/*     */   }
/*     */   
/*     */   private boolean onlycheck(DressBO dressbo) {
/* 231 */     RefDress ref = (RefDress)RefDataMgr.get(RefDress.class, Integer.valueOf(dressbo.getDressId()));
/* 232 */     if (ref == null) {
/* 233 */       return false;
/*     */     }
/* 235 */     if (ref.TimeLimit <= 0 || dressbo.getEquipTime() == 0) {
/* 236 */       return true;
/*     */     }
/*     */     
/* 239 */     return (CommTime.nowSecond() - dressbo.getEquipTime() < ref.TimeLimit);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/character/DressFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */