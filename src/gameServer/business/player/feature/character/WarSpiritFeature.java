/*     */ package business.player.feature.character;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import business.player.item.IItemFilter;
/*     */ import business.player.item.IUniItemContainer;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.enums.UnlockType;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefPlayerInit;
/*     */ import core.config.refdata.ref.RefUnlockFunction;
/*     */ import core.database.game.bo.WarSpiritBO;
/*     */ import core.network.proto.WarSpiritInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class WarSpiritFeature extends Feature implements IUniItemContainer<Equip> {
/*     */   private Map<Integer, WarSpirit> warSpirits;
/*     */   private WarSpirit warSpiritNow;
/*     */   
/*     */   public WarSpiritFeature(Player owner) {
/*  28 */     super(owner);
/*     */ 
/*     */     
/*  31 */     this.warSpirits = new HashMap<>();
/*     */     
/*  33 */     this.warSpiritNow = null;
/*     */   }
/*     */   public WarSpirit getWarSpiritNow() {
/*  36 */     return this.warSpiritNow;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWarSpiritNow(WarSpirit warSpiritNow) throws WSException {
/*  41 */     UnlockType type = UnlockType.valueOf("WarSpirit");
/*  42 */     RefUnlockFunction.checkUnlock(this.player, type);
/*  43 */     if (this.warSpiritNow != null && this.warSpiritNow != warSpiritNow) {
/*  44 */       this.warSpiritNow.getBo().saveIsSelected(false);
/*     */     }
/*  46 */     this.warSpiritNow = warSpiritNow;
/*  47 */     this.warSpiritNow.getBo().saveIsSelected(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadDB() {
/*  52 */     List<WarSpiritBO> bos = BM.getBM(WarSpiritBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
/*  53 */     for (WarSpiritBO bo : bos) {
/*  54 */       WarSpirit spirit = new WarSpirit(this.player, bo);
/*  55 */       this.warSpirits.put(Integer.valueOf(bo.getSpiritId()), spirit);
/*  56 */       if (bo.getIsSelected()) {
/*  57 */         this.warSpiritNow = spirit;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public WarSpirit getWarSpirit(int id) {
/*  63 */     return this.warSpirits.get(Integer.valueOf(id));
/*     */   }
/*     */   
/*     */   public int unlockWarSpirit(int selected) throws WSException {
/*  67 */     if (this.warSpirits.containsKey(Integer.valueOf(selected))) {
/*  68 */       throw new WSException(ErrorCode.Player_AlreadyExist, "该战灵[%s]已解锁", new Object[] { Integer.valueOf(selected) });
/*     */     }
/*  70 */     WarSpiritBO bo = new WarSpiritBO();
/*  71 */     bo.setPid(this.player.getPid());
/*  72 */     bo.setSpiritId(selected);
/*  73 */     bo.insert();
/*  74 */     WarSpirit sp = new WarSpirit(this.player, bo);
/*  75 */     this.warSpirits.put(Integer.valueOf(sp.getSpiritId()), sp);
/*     */     
/*  77 */     if (this.player.getPlayerBO().getWarspiritLv() <= 0) {
/*  78 */       this.player.getPlayerBO().saveWarspiritLv(((RefPlayerInit)RefDataMgr.get(RefPlayerInit.class, Integer.valueOf(0))).WarspiritLv);
/*  79 */       this.player.pushProto("initWarspiritLv", Integer.valueOf(this.player.getPlayerBO().getWarspiritLv()));
/*     */     } 
/*     */     
/*  82 */     this.player.pushProto("newWarSpirit", new WarSpiritInfo(sp));
/*  83 */     return bo.getSpiritId();
/*     */   }
/*     */   
/*     */   public List<WarSpiritInfo> getAllInfo() {
/*  87 */     List<WarSpiritInfo> list = new ArrayList<>();
/*  88 */     this.warSpirits.values().forEach(spirit -> paramList.add(new WarSpiritInfo(spirit)));
/*     */ 
/*     */     
/*  91 */     return list;
/*     */   }
/*     */   
/*     */   public int getPower() {
/*  95 */     if (this.warSpiritNow != null) {
/*  96 */       return this.warSpiritNow.getPower();
/*     */     }
/*  98 */     return 0;
/*     */   }
/*     */   
/*     */   public void updatePower() {
/* 102 */     if (this.warSpiritNow != null) {
/* 103 */       this.warSpiritNow.onAttrChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public PrizeType getType() {
/* 109 */     return PrizeType.Warspirit;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean check(int id, int count, IItemFilter<Equip> filter) {
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Equip consume(int id, int count, ItemFlow reason, IItemFilter<Equip> filter) {
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int gain(int id, int count, ItemFlow reason) {
/* 126 */     return 0;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/character/WarSpiritFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */