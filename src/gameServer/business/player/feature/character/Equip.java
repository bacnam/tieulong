/*    */ package business.player.feature.character;
/*    */ 
/*    */ import business.player.Player;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import com.zhonglian.server.common.enums.EquipPos;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefEquip;
/*    */ import core.database.game.bo.EquipBO;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Equip
/*    */ {
/*    */   private EquipBO bo;
/*    */   private RefEquip ref;
/*    */   private Character owner;
/*    */   private Player player;
/*    */   private Integer basepower;
/*    */   
/*    */   Equip(Player player, EquipBO bo) {
/* 24 */     this.player = player;
/* 25 */     this.bo = bo;
/* 26 */     this.ref = (RefEquip)RefDataMgr.get(RefEquip.class, Integer.valueOf(bo.getEquipId()));
/*    */   }
/*    */   
/*    */   public Player getPlayer() {
/* 30 */     return this.player;
/*    */   }
/*    */   
/*    */   public EquipBO getBo() {
/* 34 */     return this.bo;
/*    */   }
/*    */   
/*    */   public void setOwner(Character character, EquipPos pos) {
/* 38 */     this.owner = character;
/* 39 */     if (character != null) {
/* 40 */       character.equip(this, pos);
/*    */     }
/*    */   }
/*    */   
/*    */   public Character getOwner() {
/* 45 */     return this.owner;
/*    */   }
/*    */   
/*    */   public EquipPos getPos() {
/* 49 */     if (this.owner == null) {
/* 50 */       return EquipPos.None;
/*    */     }
/* 52 */     return EquipPos.values()[this.bo.getPos()];
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEquipId() {
/* 57 */     return this.bo.getEquipId();
/*    */   }
/*    */   
/*    */   public long getSid() {
/* 61 */     return this.bo.getId();
/*    */   }
/*    */   
/*    */   public int getLevel() {
/* 65 */     return this.ref.Level;
/*    */   }
/*    */   
/*    */   public RefEquip getRef() {
/* 69 */     return this.ref;
/*    */   }
/*    */   
/*    */   public int getBasePower() {
/* 73 */     if (this.basepower != null) {
/* 74 */       return this.basepower.intValue();
/*    */     }
/* 76 */     return updateBasePower();
/*    */   }
/*    */   
/*    */   private int updateBasePower() {
/* 80 */     Map<Attribute, Integer> attrs = new HashMap<>();
/* 81 */     for (int i = 0; i < this.ref.AttrTypeList.size(); i++) {
/* 82 */       attrs.put(this.ref.AttrTypeList.get(i), Integer.valueOf(((Integer)this.ref.AttrValueList.get(i)).intValue() + this.bo.getAttr(i)));
/*    */     }
/* 84 */     return (this.basepower = Integer.valueOf(PowerUtils.getPower(attrs))).intValue();
/*    */   }
/*    */   
/*    */   public void onAttrChanged() {
/* 88 */     this.owner.onAttrChanged();
/*    */   }
/*    */   
/*    */   public void saveOwner(Character character, EquipPos pos) {
/* 92 */     this.owner = character;
/* 93 */     this.bo.setCharId((character == null) ? 0 : character.getCharId());
/* 94 */     this.bo.setPos(pos.ordinal());
/* 95 */     this.bo.saveAll();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/character/Equip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */